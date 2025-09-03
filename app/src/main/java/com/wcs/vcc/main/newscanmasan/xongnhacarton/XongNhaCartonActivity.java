package com.wcs.vcc.main.newscanmasan.xongnhacarton;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.atalay.bluetoothhelper.Model.BluetoothCallback;
import com.atalay.bluetoothhelper.Provider.BluetoothProvider;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickPackShipCartonsCompleteParameter;
import com.wcs.vcc.api.PickPackShipFinishItemParameter;
import com.wcs.vcc.api.PickPackShipPackScanParameter;
import com.wcs.vcc.api.PickPackShipPacksParameter;
import com.wcs.wcs.databinding.ActivityXongNhaCartonBinding;
import com.wcs.vcc.main.BarcodeFuncDef;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.ScanMasanActivity;
import com.wcs.vcc.main.packingscan.pack.Pack;
import com.wcs.vcc.main.packingscan.pack.PackAdapter;
import com.wcs.vcc.main.packingscan.packdetails.PackDetailsActivity;
import com.wcs.vcc.main.vo.PrinterZebraSupport;
import com.wcs.vcc.preferences.ItemCodePref;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.printer.ZebraPrinter;

import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class XongNhaCartonActivity extends RingScanActivity implements BluetoothCallback {
    View view;
    private ActivityXongNhaCartonBinding binding;
    private PackAdapter adapter;
    private String username, androidId, barcode = "", curBarcode = "";
    private int cartonId;
    private boolean isCompleted;
    private String orderNumber;

    private Dialog dialog;

    BluetoothProvider bluetoothProvider;

    Connection printerConnection = null;
    ZebraPrinter printer = null;

    List<Pack> packList2;
    List<Pack> packList;

    private String OrrderDate;
    private String StoreNum;

    private static Boolean IS_LOADATA = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_xong_nha_carton);
        Utilities.showBackIcon(getSupportActionBar());
        view = getWindow().getDecorView().getRootView();
        setUpScan();

        username = LoginPref.getUsername(this);
        androidId = Utilities.getAndroidID(getApplicationContext());
        curBarcode = ItemCodePref.LoadItemCode(XongNhaCartonActivity.this);
//        initProvider();

        packList = new ArrayList<>();
        adapter = new PackAdapter(new RecyclerViewItemListener<Pack>() {
            @Override
            public void onClick(Pack item, int position) {
                Intent intent = new Intent(XongNhaCartonActivity.this, PackDetailsActivity.class);
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
        StoreNum = getIntent().getStringExtra("STORE_NUM");
        OrrderDate = getIntent().getStringExtra("DATE");

        binding.rvPickPackShipPack.setAdapter(adapter);
        binding.setCartonId(cartonId);
        binding.setCartonNumber(cartonNumber);
        binding.setIsCompleted(isCompleted);

        pickPackShipPacks();
    }


    @Override
    protected void onResume() {
        super.onResume();
        pickPackShipPacks();
    }

//    private void pickPackShipPacks() {
//        MyRetrofit.initRequest(this).pickPackShipPacks(new PickPackShipPacksParameter(cartonId)).enqueue(new Callback<List<Pack>>() {
//            @Override
//            public void onResponse(Response<List<Pack>> response, Retrofit retrofit) {
//                if (response.isSuccess()) {
//                    packList2 = response.body();
//                    adapter.replace(packList);
//
//
//                    int orderQuantity = 0, quantity = 0;
//                    double orderNetWeight = 0, netWeight = 0;
//
//                    for (Pack pack : packList) {
//                        orderQuantity += pack.OrderQuantity;
//                        quantity += pack.Quantity;
//                        orderNetWeight += pack.OrderNetWeight;
//                        netWeight += pack.NetWeight;
//                    }
//
//                    binding.setOrderQuantity(orderQuantity);
//                    binding.setQuantity(quantity);
//                    binding.setOrderNetWeight(NumberFormat.getInstance().format(orderNetWeight));
//                    binding.setNetWeight(NumberFormat.getInstance().format(netWeight));
//
//                    //find curren product
//                    Pack currentItemInfor = new Pack();
//
//                    for(int i =0 ; i< packList.size();i++){
//                        if(packList.get(i).ProductNumber.equals(curBarcode.substring(1,6))){
//                            currentItemInfor = packList.get(i);
//                            break;
//                        }
//                    }
//
//                    packList = response.body();
//
//                    if( IS_LOADATA ){
//                        Utilities.speakingSomeThing("Còn " + currentItemInfor.OrderQuantity + " gói" , XongNhaCartonActivity.this);
//                    }else{
//                        if(currentItemInfor.ProductID==null){
//                            Utilities.speakingSomeThing("Điểm này không đặt hàng", XongNhaCartonActivity.this);
//                        }
//                        if(currentItemInfor.OrderQuantity==1){
//                            Utilities.speakingSomeThing("Còn một gói", XongNhaCartonActivity.this);
//                        }else if(currentItemInfor.OrderQuantity==0){
//                           // Utilities.speakingSomeThing("Đã chia xong", XongNhaCartonActivity.this);
//                        }
//                    }
//
//                    IS_LOADATA = false;
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//    }
    private void pickPackShipPacks() {
        MyRetrofit.initRequest(this).pickPackShipPacks(new PickPackShipPacksParameter(cartonId)).enqueue(new Callback<List<Pack>>() {
            @Override
            public void onResponse(Response<List<Pack>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<Pack> packList = response.body();
                    packList2 = response.body();
                    adapter.replace(packList);
                    int orderQuantity = 0, quantity = 0;
                    double orderNetWeight = 0, netWeight = 0;

                    for (Pack pack : packList) {
                        orderQuantity += pack.OrderQuantity;
                        quantity += pack.Quantity;
                        orderNetWeight += pack.OrderNetWeight;
                        netWeight += pack.NetWeight;
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

//
//        if (isCompleted) {
//            if (data.equals("CARTON-OPEN")) {
//                completePickPackShipCartons(cartonId, false);
//            } else {
//                //showMessage("Thùng đã đóng, mở thùng để scan");
//            }
//        }
       // else
        if (data.equals("CARTON-OPEN"))
            completePickPackShipCartons(cartonId, false);
        else if (data.equals("CARTON-CLOSE"))
            completePickPackShipCartons(cartonId, true);
        else if (data.equals("STATUS-REFRESH")) {
            pickPackShipPacks();
        } else if (data.contains("ENTER")) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                } else {
                    onBackPressed();
                }
            }
        } else if (data.equals(BarcodeFuncDef.PRINT)) {
            try {
                printData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data.equals(BarcodeFuncDef.NEXT_STORE_NUM)) {
            try {
                backToPickingList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (!data.equals(BarcodeFuncDef.CARTON_OPEN) && !data.equals(BarcodeFuncDef.CARTON_CLOSE) && !data.equals(BarcodeFuncDef.STATUS_REFESH) && !data.equals(!data.equals(BarcodeFuncDef.ENTER) && !data.equals(!data.equals(BarcodeFuncDef.PRINT)))) {
            if (!BarcodeFuncDef.getItemCode_MASAN(data).equals(curBarcode)) {
                Intent i = new Intent(XongNhaCartonActivity.this, ScanMasanActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(i, 123);
            } else {
                scanPickPackShipPack(data);
                barcode = data;
            }
        }


    }

    private void backToPickingList() {
        //curBarcode
        Intent intent = new Intent(XongNhaCartonActivity.this, ScanMasanActivity.class);
        intent.putExtra("BARCODE", curBarcode);
        startActivity(intent);

    }

    private void scanPickPackShipPack(String scanResult) {
        MyRetrofit.initRequest(this).scanPickPackShipPack(new PickPackShipPackScanParameter(username, androidId, scanResult, cartonId,0)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {

                    if(response.body().length()>0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(XongNhaCartonActivity.this);
                        builder.setTitle("Thông báo");
                        builder.setMessage(response.body());
                        dialog = builder.create();
                        dialog.show();
                        Utilities.speakingSomeThing(response.body(), XongNhaCartonActivity.this);
                    }

                    pickPackShipPacks();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
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

    void printData() throws Exception {
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

                            "^PON^PW600^MNN^LL%d^LH0,0" + "\r\n" +

                            "^FO0,50" + "\r\n" + "^A0,N,70,70" + "\r\n" + "^FD%s^FS" + "\r\n" +

                            "^FO150,50" + "\r\n" + "^B3N,N,45,Y,N" + "\r\n" + "^FD%s^FS" + "\r\n" +

                            "^FO0,140" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDDelivery Date:^FS" + "\r\n" +

                            "^FO225,140" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD%s^FS" + "\r\n" +

                            "^FO0,173" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDItem^FS" + "\r\n" +

                            "^FO280,173" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDQty^FS" + "\r\n" +

                            "^FO350,173" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDNetW^FS" + "\r\n" +

                            "^FO470,173" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDGross^FS" + "\r\n" +

                            "^FO0,220" + "\r\n" + "^GB550,5,5,B,0^FS";

            int headerHeight = 245;
            String body = String.format("^LH0,%d", headerHeight);

            int heightOfOneLine = 20;


            int totalQty = 0;
            float totalNetw = 0;
            float totalGross = 0;

            int i = 0;

            for (Pack p : packList2) {
                String name = p.getProductNumber()+"-"+p.ProductName;
                int quantity = p.Quantity;
                double netw = p.NetWeight;
                double gross = p.NetWeight;
                String lineItem = "^FO0,%d" + "\r\n" + "^A0,N,28,28,E:SCPROR.TTF" + "\r\n" + "^CI28^CF0,28^FD%s^FS" + "\r\n" + "^FO280,%d" + "\r\n" +
                        "^A0,N,28,28," + "\r\n" + "E:SCPROR.TTF^FD%s^FS" + "\r\n" + "^FO350,%d" + "\r\n" + "^A0,N,28,28,E:SCPROR.TTF" + "\r\n" + "^FD%s^FS" + "\r\n" + "^FO470,%d" + "\r\n" + "^A0,N,28,28,E:SCPROR.TTF" + "\r\n" + "^FD%s^FS";
//                String lineItem = "E:SCPROR.TTF^FO0,%d" + "\r\n" + "^A0,N,28,28" + "\r\n" + "^CI28^CF0,28^FD%s^FS" + "\r\n" + "^FO280,%d" + "\r\n" + "^A0,N,28,28" + "\r\n" + "^FD%s^FS" + "\r\n" + "^FO350,%d" + "\r\n" + "^A0,N,28,28" + "\r\n" + "^FD%s^FS" + "\r\n" + "^FO470,%d" + "\r\n" + "^A0,N,28,28" + "\r\n" + "^FD%s^FS";
                totalNetw += netw;
                totalQty += quantity;
                totalGross += gross;

                int totalHeight = i++ * heightOfOneLine;
                String name2 = "";
                if (name.length() >= 20) {
                    name2 = name.substring(0, 20).concat("...");
                    name = name2;
                }

                //Log.e("sb", name2);

                body += String.format(lineItem, totalHeight,Utilities.removeAccent (name), totalHeight, quantity, totalHeight, netw, totalHeight, gross);
                i += 1;
            }

            long totalBodyHeight = 470;

            long footerStartPosition = headerHeight + totalBodyHeight;

            String footer = String.format("^LH0,%d" + "\r\n" +
                    "^FO0,1" + "\r\n" + "^GB550,5,5,B,0^FS" + "\r\n" +
                    "^FO0,25" + "\r\n" + "^A0,N,40,40" + "\r\n" + "^FDTotal^FS" + "\r\n" +
//                    "^FO175,15" + "\r\n" + "^A0,N,40,40" + "\r\n" + "^FD$%.2f^FS" + "\r\n" +
                    "^FO280,25" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD%d^FS" + "\r\n" +
                    "^FO350,25" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD%.2f^FS" + "\r\n" +
                    "^FO470,25" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD%.2f^FS" + "\r\n" +
                    "^XZ", footerStartPosition, totalQty, totalNetw, totalGross);

            long footerHeight = 100;
            long labelLength = headerHeight + totalBodyHeight + footerHeight;

//            Date date = new Date();
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String[] s = OrrderDate.split("T");
            String dateString = s[0];



            String header = String.format(tmpHeader, labelLength, StoreNum, Utilities.addChar(String.valueOf(cartonId)) + cartonId, dateString);
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

//    public void onCoBlue(View view) {
//
//        printerConnection = new BluetoothConnection(bluetoothProvider.getDeviceAddress());
//
//        try {
//            printerConnection.open();
//            if (printerConnection.isConnected()) {
//                printer = ZebraPrinterFactory.getInstance(printerConnection);
//            }
//        } catch (ConnectionException | ZebraPrinterLanguageUnknownException e) {
//
//        }
//    }

//    public void onListBlue(View view) {
//        bluetoothProvider.openPrinterListActivity();
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (printerConnection != null) {
//            try {
//                printerConnection.close();
//            } catch (ConnectionException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (printerConnection != null) {
//            try {
//                printerConnection.close();
//            } catch (ConnectionException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    private void initProvider() {
//        bluetoothProvider = new BluetoothProvider(this, this);
////        tvName.setText(bluetoothProvider.getDeviceAddress());
//
//
////        mBTService.stop();
//        Set<BluetoothDevice> set = mBTService.getPairedDevices();
//        if (set.size() > 0) {
//            Iterator<BluetoothDevice> it = set.iterator();
//            while (it.hasNext()) {
//                BluetoothDevice device = it.next();
////                if(device.getName() != null && device.getName().indexOf("ABA" + LoginPref.getUsername(RingScanActivity.this).toUpperCase())>=0){
//                if (device.getName() != null && device.getName().indexOf("ZQ320") >= 0) {
//                    printerConnection = new BluetoothConnection(device.getAddress());
//
//                    try {
//                        printerConnection.open();
//                        if (printerConnection.isConnected()) {
//                            printer = ZebraPrinterFactory.getInstance(printerConnection);
//                        }
//                    } catch (ConnectionException | ZebraPrinterLanguageUnknownException e) {
//
//                    }
//                }
//            }
//        }
//    }
}
