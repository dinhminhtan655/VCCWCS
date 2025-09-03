package com.wcs.vcc.main.packingscan.save_package;

import static java.lang.String.valueOf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.atalay.bluetoothhelper.Model.BluetoothCallback;
import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.ComboCustomerResult;
import com.wcs.vcc.api.DeleteSavePackageParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickinglistCreateEDISavePackageHNParameter;
import com.wcs.vcc.api.SaveListPackShipPackScanParameter;
import com.wcs.vcc.api.SavePackShipPackScanParameter;
import com.wcs.wcs.databinding.ActivitySavePackageHnBinding;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.detailphieu.OrderDetailActivity;
import com.wcs.vcc.main.vo.PrinterZebraSupport;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.SpinCusIdPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SavePackageHNActivity extends RingScanActivity implements BluetoothCallback {

    ActivitySavePackageHnBinding binding;
    //ActivitySavePackageHnBinding binding;
    private String username, androidId, barcode = "";
    SavePackageHNAdapter adapter;

    String date;
    SimpleDateFormat formatter;
    List<ComboCustomerResult> customers = new ArrayList<>();

    private Spinner comboCustomer;


    @BindView(R.id.et_target_scan)
    EditText edt_target_scan;

    @BindView(R.id.edtLocation)
    EditText edtLocation;

    @BindView(R.id.edtSoThung)
    EditText edtSoThung;

    Date date2;
    List<SavePackageHN> list;

    @BindView(R.id.header_scan)
    LinearLayout mainLayout;

    byte flag;
    private int totalScaned = 0;
    private ComboCustomerResult cusSelected;
    ArrayAdapter<ComboCustomerResult> spineradapter = null;
    String CustomerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_save__package__hn);
        Utilities.showBackIcon(getSupportActionBar());
        ButterKnife.bind(this);
        edt_target_scan.clearFocus();

        mainLayout.requestFocus();
        setUpScan();

        username = LoginPref.getUsername(this);
        androidId = Utilities.getAndroidID(getApplicationContext());

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        date2 = new Date(System.currentTimeMillis());
        date = valueOf(formatter.format(date2));


        Log.e("Date", date);

        getCustomer();
        comboCustomer = findViewById(R.id.sp_list_id_customer);

        String PO = getIntent().getStringExtra("ProductionOrderNumber");
        float qtyPO =getIntent().getFloatExtra("Planned_Unit",0);

        CustomerID = valueOf(SpinCusIdPref.LoadCusID(SavePackageHNActivity.this));

        if(PO!=null){
            edtSoThung.setText(qtyPO+"");
            edtLocation.setText(PO);

           // edtSoThung.setEnabled(false);
            edtLocation.setEnabled(false);
        }


        adapter = new SavePackageHNAdapter(new RecyclerViewItemListener<SavePackageHN>() {
            @Override
            public void onClick(SavePackageHN item, int position) {

            }

            @Override
            public void onLongClick(final SavePackageHN item, int position) {
                AlertDialog dialog = new AlertDialog.Builder(SavePackageHNActivity.this)
                        .setMessage("Bạn có muốn xóa thùng này không?")
                        .setPositiveButton("Xóa một thùng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                flag = 0;
                                deleteSavePackageHN(item.id, flag);
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .setNeutralButton("Xóa tất cả", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                flag = 1;
                                deleteSavePackageHN(item.id, flag);
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        binding.rvPickPackShipPack.setAdapter(adapter);

        getListSavePackageHN();
    }


    private void deleteSavePackageHN(int item, byte flag) {
        MyRetrofit.initRequest(this).deleteSavePackageHN(new DeleteSavePackageParameter(username, androidId, cusSelected.getCustomerID(), item, flag)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Toast.makeText(SavePackageHNActivity.this, response.body() + " Xóa Thành công", Toast.LENGTH_SHORT).show();
                    binding.setOrderQuantity1(0);
                    binding.setOrderNetWeight1(String.valueOf(0.0));
                    getListSavePackageHN();
                } else {
                    Toast.makeText(SavePackageHNActivity.this, response.body() + " Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(SavePackageHNActivity.this, "Kiểm tra lại mạng đi nha", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public EditText getEdtSoThung(String thung) {
        edtSoThung.getText();
        return edtSoThung;
    }

    private void getListSavePackageHN() {

        if (cusSelected == null) {
            return;
        }
        this.totalScaned = 0;
        MyRetrofit.initRequest(this).getListSavePackageHN(new SaveListPackShipPackScanParameter(username, androidId, cusSelected.getCustomerID(), date)).enqueue(new Callback<List<SavePackageHN>>() {
            @Override
            public void onResponse(Response<List<SavePackageHN>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    list = new ArrayList<SavePackageHN>();
                    list = response.body();
                    adapter.replace(list);

                    int sumSoLuong = 0;
                    double sumKhoiLuong = 0.0;

                    for (int i = 0; i < list.size(); ) {
                        if (i == list.size()) {
                            break;
                        } else {
                            sumSoLuong += list.get(i).Quantity;
                            sumKhoiLuong += list.get(i).NetWeight;
                            binding.setOrderQuantity1(sumSoLuong);
                            binding.setOrderNetWeight1(String.valueOf(sumKhoiLuong));
                            i++;
                            totalScaned = sumSoLuong;
                        }

                    }

                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListSavePackageHN();
    }

    private void getCustomer() {
        //khoi tao doi tuong jsonObject
        JsonObject jsonObject = new JsonObject();
        //dua vao kieu tra ve
        jsonObject.addProperty("StoreID", LoginPref.getStoreId(this));
        //goi API
        MyRetrofit.initRequest(this).loadListCustomer(jsonObject).enqueue(new Callback<List<ComboCustomerResult>>() {
            @Override
            public void onResponse(Response<List<ComboCustomerResult>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    customers = response.body();

                    spineradapter = new ArrayAdapter<ComboCustomerResult>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, customers);
                    comboCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            cusSelected = customers.get(position);
                            getListSavePackageHN();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    if(customers.size()>0){
                        for(int i=0;i<customers.size();i++){
                            if(customers.get(i).getCustomerID().toString().equals(CustomerID)){
                                cusSelected = customers.get(i);
                                break;
                            }
                        }
                    }
                    comboCustomer.setAdapter(spineradapter);
                }
            }


            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SavePackageHNActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không lấy được thông tin khách hàng!");
                alert.setPositiveButton("OK", null);
                alert.show();
        }
        });
    }

    @Override
    public void onData(String data) {
        super.onData(data);
        scanToSavePackage(data);

    }


    private void scanToSavePackage(String scanResult) {

        String scanResult2 = null;
//        if (scanResult.contains("meatdeli.com.vn/txng/")) {
//            String strSplit[] = scanResult.split("/");
//            scanResult2 = strSplit[strSplit.length - 1].trim();
//        }else {
//            scanResult2 = scanResult;
//        }

        scanResult2 = scanResult.replace("meatdeli.com.vn/txng/","");

        Log.d("scan", scanResult);
        if (list != null) {
            for (SavePackageHN item : this.list) {
                if (item.BarcodeString.equals(scanResult2)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SavePackageHNActivity.this);
                    alert.setTitle("Thông Báo");
                    alert.setMessage("Trùng mã !");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                    break;
                }
            }
        }

        int soThungNhap = 0;
        if(edtSoThung.getText().length() > 0){
            float qty = Float.parseFloat(edtSoThung.getText().toString());
            soThungNhap =(int) qty;
        }



        if (scanResult.substring(0, 2).equals("MS") == false && soThungNhap < totalScaned + 1 || !scanResult.contains("meatdeli.com.vn") &&  soThungNhap < totalScaned + 1 )  {
            AlertDialog.Builder alert2 = new AlertDialog.Builder(SavePackageHNActivity.this);
            alert2.setTitle("Thông Báo");
            alert2.setMessage("Số thùng không khớp ! Kiểm tra lại");
            alert2.setPositiveButton("OK", null);
            alert2.show();
            return;
        }


//        if (scanResult.contains("meatdeli.com.vn")) {
//            String strSplit[] = scanResult.split("/");
//            scanResult = strSplit[strSplit.length - 1].trim();
//        }

        //if (scanResult.substring(0, 2).equals("MS")) {
            MyRetrofit.initRequest(this).savePickPackShipPack(new SavePackShipPackScanParameter(username, androidId, scanResult, cusSelected.getCustomerID())).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        showMessage(response.body());
                        getListSavePackageHN();
                        //Toast.makeText(SavePackageHNActivity.this, response.body() + " Thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        // Toast.makeText(SavePackageHNActivity.this, "Lỗi rồi nha thử lại đi", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alert = new AlertDialog.Builder(SavePackageHNActivity.this);
                        alert.setTitle("Thông Báo");
                        alert.setMessage("Lỗi rồi. Vui lòng  thử lại !");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    //Toast.makeText(SavePackageHNActivity.this, "Lỗi rồi! Kiểm tra lại đường truyền nha!", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alert = new AlertDialog.Builder(SavePackageHNActivity.this);
                    alert.setTitle("Thông Báo");
                    alert.setMessage("Lỗi đường truyền! Vui lòng iểm tra lại");
                    alert.setPositiveButton("OK", null);
                    alert.show();


                }
            });

    }

    public void onXong(View view) throws Exception {

        {

            int soThungNhap = 0;

            // Kiem tra textView nhap so thung la khac rong
            if (!isEmpty(edtSoThung))
            {
                float qty = Float.parseFloat(edtSoThung.getText().toString());
                soThungNhap = (int) qty;
            }


            if (this.totalScaned < soThungNhap) {
                //Toast.makeText(SavePackageHNActivity.this, "Số thùng không khớp ! Kiểm tra lại", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alert = new AlertDialog.Builder(SavePackageHNActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Số thùng không khớp ! Kiểm tra lại!");
                alert.setPositiveButton("OK", null);
                alert.show();
            } else {

                printLabel();
                MyRetrofit.initRequest(SavePackageHNActivity.this).PickinglistCreateEDISavePackageHN(new PickinglistCreateEDISavePackageHNParameter(username, androidId, cusSelected.getCustomerID(), date, edtLocation.getText().toString())).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess()) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(SavePackageHNActivity.this);
                            alert.setTitle("Thông Báo");
                            alert.setMessage("Thành công !");
                            alert.setPositiveButton("OK", null);
                            alert.show();

                            //Toast.makeText(SavePackageHNActivity.this, response.body() + " Thành công", Toast.LENGTH_SHORT).show();


                            Intent i = new Intent(SavePackageHNActivity.this, OrderDetailActivity.class);
                            i.putExtra(ORDER_NUMBER, response.body());
                            i.putExtra("SCAN_TYPE", 1);
                            i.putExtra("CUSTOMER_TYPE", "ByCartonID");
                            startActivity(i);

                        } else {
                            //Toast.makeText(SavePackageHNActivity.this, response.body() + " Thất bại", Toast.LENGTH_SHORT).show();

                            AlertDialog.Builder alert = new AlertDialog.Builder(SavePackageHNActivity.this);
                            alert.setTitle("Thông Báo");
                            alert.setMessage("Thất bại !");
                            alert.setPositiveButton("OK", null);
                            alert.show();

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        //Toast.makeText(SavePackageHNActivity.this, "Lỗi rồi! Kiểm tra lại đường truyền nha!", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder alert = new AlertDialog.Builder(SavePackageHNActivity.this);
                        alert.setTitle("Thông Báo");
                        alert.setMessage("Lỗi đường truyền. Kiểm tra lại !");
                        alert.setPositiveButton("OK", null);
                        alert.show();


                    }
                });
            }

        }
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

//                            "^FO340,20" + "\r\n" + "^B3N,N,45,Y,N" + "\r\n" + "^FD%s^FS" + "\r\n" +

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

            for (SavePackageHN p : list) {
                String name = p.ProductName;
                int quantity = p.Quantity;
                double netw = p.NetWeight;
                String lineItem = "^FO10,%d" + "\r\n" + "^A0,N,28,28,E:TT0003M_.TTF" + "\r\n" + "^CI28^CF0,28^FD%s^FS" + "\r\n"
                        + "^FO480,%d" + "\r\n" + "^A0,N,28,50," + "\r\n" + "E:TT0003M_.TTF^FD%s^FS" + "\r\n"
                        + "^FO520,%d" + "\r\n" + "^A0,N,28,50,E:TT0003M_.TTF" + "\r\n" + "^FD%s^FS" + "\r\n"
                        + "^FO620,%d" + "\r\n" + "^A0,N,28,50,E:TT0003M_.TTF" + "\r\n" + "^FD%s^FS";
                totalNetw += netw;
                totalQty += quantity;

                int totalHeight = i++ * heightOfOneLine;
                String name2 = "";
                if (name.length() >= 50) {
                    name2 = name.substring(0, 50);
                    name = name2;
                }

                //Log.e("sb", name2);

                body += String.format(lineItem, totalHeight, Utilities.removeAccent(name), totalHeight, quantity, totalHeight, netw, totalHeight, 0);
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
            String dateString = date;


            String header = String.format(tmpHeader, labelLength, cusSelected.getCustomerNumber()
                   ,edtLocation.getText() +  "-"  +  dateString);


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

    public static class MyDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("THÔNG BÁO");
            builder.setMessage("Barcode không đúng định dạng, Vui lòng kiểm tra lại !");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


}