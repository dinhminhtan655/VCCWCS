package com.wcs.vcc.main.packingscan.pack;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atalay.bluetoothhelper.Model.BluetoothCallback;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickPackShipCartonsCompleteParameter;
import com.wcs.vcc.api.PickPackShipFinishItemParameter;
import com.wcs.vcc.api.PickPackShipPackScanParameter;
import com.wcs.vcc.api.PickPackShipPacksParameter;
import com.wcs.wcs.databinding.ActivityPacksBinding;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.packingscan.packdetails.PackDetailsActivity;
import com.wcs.vcc.main.vo.PrinterZebraSupport;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PacksActivity extends RingScanActivity implements BluetoothCallback {
    TextView tv_prev_barcode;
    EditText etTargetScanAsignCarton;
    TextView tvCartonLabel;
    TextView tvStoreNumberAssgin;
    TextView tvPrevBarcodeAssignCarton;
    View btCameraScanAssginCarton;
    EditText etInerCarton;
    Button btnAssignCartonToPallet;


    private ActivityPacksBinding binding;
    private PackAdapter adapter;
    private String username, androidId, barcode = "";
    private int cartonId;
    private boolean isCompleted;
    private String orderNumber;
    private int orderQuantity = 0, quantity = 0;
    private double orderNetWeight = 0, netWeight = 0,grossWeight = 0;
    private List<Pack> packList;
    private  String date;
    private  Integer storeNumber;

    private RadioButton raFixWeight;
    private RadioButton raRandomW;
    private RadioGroup rdGroupType;
    private EditText inerQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_packs);

        Utilities.showBackIcon(getSupportActionBar());
        setUpScan();

        username = LoginPref.getUsername(this);
        androidId = Utilities.getAndroidID(getApplicationContext());
        packList = new ArrayList<>();
        raFixWeight = this.findViewById(R.id.raFixWeight);
        raRandomW = this.findViewById(R.id.raRandoWeight);
        inerQty = this.findViewById(R.id.edQtyInner);
        rdGroupType = this.findViewById(R.id.rdGroupTypeProduct);
        adapter = new PackAdapter(new RecyclerViewItemListener<Pack>() {
            @Override
            public void onClick(Pack item, int position) {
                Intent intent = new Intent(PacksActivity.this, PackDetailsActivity.class);
                intent.putExtra("PRODUCT_ID", item.ProductID.toString());
                intent.putExtra("CARTON_ID", cartonId);
                intent.putExtra("PRODUCT_NAME", item.ProductName);
                intent.putExtra("PRODUCT_NUMBER", item.getProductNumber());
                startActivity(intent);
            }

            @Override
            public void onLongClick(final Pack item, int position) {
                finishPickPackShipItem(item.ProductNumber);
            }
        });

        cartonId = getIntent().getIntExtra("CARTON_ID", 1);
        int cartonNumber = getIntent().getIntExtra("CARTON_NUMBER", 1);
        isCompleted = getIntent().getBooleanExtra("COMPLETED", false);
        orderNumber = getIntent().getStringExtra("ORDER_NUMBER");
        date =   getIntent().getStringExtra("ORDER_DATE");
        storeNumber =  getIntent().getIntExtra("STORE_NUMBER",0);

        binding.rvPickPackShipPack.setAdapter(adapter);
        binding.setCartonId(cartonId);
        binding.setCartonNumber(cartonNumber);
        binding.setIsCompleted(isCompleted);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pickPackShipPacks();
    }

    private void pickPackShipPacks() {
        MyRetrofit.initRequest(this).pickPackShipPacks(new PickPackShipPacksParameter(cartonId)).enqueue(new Callback<List<Pack>>() {
            @Override
            public void onResponse(Response<List<Pack>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    packList = response.body();
                    adapter.replace(packList);

                    orderQuantity = 0;
                    quantity = 0;
                    orderNetWeight = 0;
                    netWeight = 0;
                    grossWeight = 0;

                    for (Pack pack : packList) {
                        orderQuantity += pack.OrderQuantity;
                        quantity += pack.Quantity;
                        orderNetWeight += pack.OrderNetWeight;
                        netWeight += pack.NetWeight;
                        grossWeight += pack.GrossWeight;
                    }

                    binding.setOrderQuantity(orderQuantity);
                    binding.setQuantity(quantity);
                    binding.setOrderNetWeight(NumberFormat.getInstance().format(orderNetWeight));
                    binding.setNetWeight(NumberFormat.getInstance().format(netWeight));
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onData(final String data) {
        super.onData(data);

        if (isCompleted) {
            showMessage("Thùng đã đóng, mở thùng để scan");
        }
        scanPickPackShipPack(data);
        barcode = data;
    }

    private void scanPickPackShipPack(String scanResult) {
        if(raFixWeight.isChecked()){
            MyRetrofit.initRequest(this).scanPickPackShipPackFixWeight(new PickPackShipPackScanParameter(username, androidId, scanResult, cartonId,Integer.parseInt(inerQty.getText().toString()))).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        showMessage(response.body());
                        pickPackShipPacks();
                    }
                }
                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
        else{
            MyRetrofit.initRequest(this).scanPickPackShipPack(new PickPackShipPackScanParameter(username, androidId, scanResult, cartonId,0)).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        showMessage(response.body());
                        pickPackShipPacks();
                    }
                }
                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

    }

    private void finishPickPackShipItem(String productNumber) {
        MyRetrofit.initRequest(this).finishPickPackShipItem(new PickPackShipFinishItemParameter(username, androidId, orderNumber, productNumber)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipPacks();
                    showMessage(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void completePickPackShipCartons(int cartonId, boolean isCompleted) {
        MyRetrofit.initRequest(this).completePickPackShipCartons(new PickPackShipCartonsCompleteParameter(username, androidId, cartonId, isCompleted)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void onXong(View view) {
        Utilities.basicDialog(this, "Bạn muốn đóng thùng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    printLabel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                completePickPackShipCartons(cartonId, true);

            }
        });
    }

    public void onNha(View view) {
        Utilities.basicDialog(this, "Bạn muốn mở thùng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                completePickPackShipCartons(cartonId, false);
            }
        });
    }

    void printLabel() throws Exception {
        try {
            String tmpHeader =
        /*
         Some basics of ZPL. Find more information here : http://www.zebra.com/content/dam/zebra/manuals/en-us/printer/zplii-pm-vol2-en.pdf

         ^XA indicates the beginning of a label
         ^PW sets the width of the label (in dots)
         ^MNN sets the printer in continuous mode (variable length receipts only make sense with variably sized labels)
         ^LL sets the length of the label (we calculate this value at the end of the routine)
         ^LH sets the reference axis for printing.
            You will notice we change this positioning of the 'Y' axis (length) as we build up the label. Once the positioning is changed, all new fields drawn on the label are rendered as if '0' is the new home position
         ^FO sets the origin of the field relative to Label Home ^LH
         ^A sets font information
         ^FD is a field description
         ^GB is graphic boxes (or lines)
         ^B sets barcode information
         ^XZ indicates the end of a label
         */
                    "^XA" +

                            "^PON^PW780^MNN^LL%d^LH0,0" + "\r\n" +

                            "^FO10,20" + "\r\n" + "^A0,N,43,100" + "\r\n" + "^FD%s^FS" + "\r\n" +

                            "^FO340,20" + "\r\n" + "^B3N,N,45,Y,N" + "\r\n" + "^FD%s^FS" + "\r\n" +

//                            "^FO10,140" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDDelivery Date:^FS" + "\r\n" +

                            "^FO10,90x" +
                            "" + "\r\n" + "^A0,N,43,100" + "\r\n" + "^FD%s^FS" + "\r\n" +

                            "^FO10,173" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDItem^FS" + "\r\n" +

                            "^FO480,173" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDQty^FS" + "\r\n" +

                            "^FO520,173" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDNetW^FS" + "\r\n" +

                            "^FO620,173" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDGross^FS" + "\r\n" +

                            "^FO0,220" + "\r\n" + "^GB550,5,5,B,0^FS";

            int headerHeight = 245;
            String body = String.format("^LH0,%d", headerHeight);

            int heightOfOneLine = 20;


            int totalQty = 0;
            float totalNetw = 0;
            float totalGross = 0;

            int i = 0;

            for (Pack p : packList) {
                String name = p.getProductNumber()+"-"+p.ProductName;
                int quantity = p.Quantity;
                double netw = p.NetWeight;
                double gross = p.GrossWeight;
                String lineItem = "^FO10,%d" + "\r\n" + "^A0,N,28,28,E:TT0003M_.TTF" + "\r\n" + "^CI28^CF0,28^FD%s^FS" + "\r\n"
                                + "^FO480,%d" + "\r\n" + "^A0,N,28,50," + "\r\n" + "E:TT0003M_.TTF^FD%s^FS" + "\r\n"
                                + "^FO520,%d" + "\r\n" + "^A0,N,28,50,E:TT0003M_.TTF" + "\r\n" + "^FD%s^FS" + "\r\n"
                                + "^FO620,%d" + "\r\n" + "^A0,N,28,50,E:TT0003M_.TTF" + "\r\n" + "^FD%s^FS";
                totalNetw += netw;
                totalQty += quantity;
                totalGross += gross;

                int totalHeight = i++ * heightOfOneLine;
                String name2 = "";
                if (name.length() >= 50) {
                    name2 = name.substring(0, 50);
                    name = name2;
                }

                //Log.e("sb", name2);

                body += String.format(lineItem, totalHeight,Utilities.removeAccent (name), totalHeight, quantity, totalHeight,netw , totalHeight, gross);
                i += 1;
            }

            long totalBodyHeight = 570;

            long footerStartPosition = headerHeight + totalBodyHeight;

            String footer = String.format("^LH0,%d" + "\r\n" +
                    "^FO0,1" + "\r\n" + "^GB550,5,5,B,0^FS" + "\r\n" +
                    "^FO0,25" + "\r\n" + "^A0,N,40,40" + "\r\n" + "^FDTotal^FS" + "\r\n" +
//                    "^FO175,15" + "\r\n" + "^A0,N,40,40" + "\r\n" + "^FD$%.2f^FS" + "\r\n" +
                    "^FO400,25" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD%d^FS" + "\r\n" +
                    "^FO450,25" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD%.2f^FS" + "\r\n" +
                    "^FO500,25" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD%.2f^FS" + "\r\n" +
                    "^XZ", footerStartPosition, totalQty, totalNetw, totalGross);

            long footerHeight = 100;
            long labelLength = headerHeight + totalBodyHeight + footerHeight;

//            Date date = new Date();
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateString =date;



            String header = String.format(tmpHeader, labelLength, packList.get(0).RouteCode+"-"+storeNumber ,
                    Utilities.addChar(String.valueOf(cartonId)) + cartonId, packList.get(0).CustomerClientName+"-"+dateString);
//            String header = String.format(tmpHeader, labelLength, StoreNum,  dateString);

            Log.e("car",  Utilities.addChar(String.valueOf(cartonId)) + cartonId);

            String wholeZplLabel = String.format("%s%s%s", header, body, footer);
            PrinterZebraSupport.initProvider(this, this);
            PrinterZebraSupport.printerConnection.write(wholeZplLabel.getBytes(StandardCharsets.UTF_8));
            PrinterZebraSupport.printerConnection.close();

            Toast.makeText(this, "Printing Text...", Toast.LENGTH_SHORT).show();

        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBegin() {

    }

    @Override
    public void onErrorImportant(String s) {

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onSuccessful() {

    }
}
