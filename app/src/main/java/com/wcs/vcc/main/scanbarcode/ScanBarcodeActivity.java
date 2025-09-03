package com.wcs.vcc.main.scanbarcode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.wcs.R;
import com.wcs.vcc.api.DeleteDOParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PalletToLocationParam;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ScanBarcodeActivity extends ShowHomeButtonActivity {

    //????
    // public static final AlertDialog.Builder ALERT = new AlertDialog.Builder(ScanBarcodeActivity.this);

    //ScanBarcodeACtivityBinding binding;
    public static final String TAG = "ScanBarcodeActivity";
    private String username;
    private OrderResultScanBarcodeAdapter adapter;
    private TextView tvPrevBarcode;
    private EditText etTargetScan;
    private int storeId;
    private TextView tv_total;
    private TextView showDO;
    private TextView showLocationNumber;

    private TextView countRecords;
    private TextView sumCartons;
    //    private Spinner spiner_id_custumer;
    private String androidID;
    private Byte flag = 1;
    @BindView(R.id.include_item_scan_barcode_2)
    RecyclerView lv;
    private String DONumber = "";
    //private EditText et_inputDO;
    String strDO = "";
    // khai bao nhung chua khoi tao
    List<STAndroid_CartonScannedByDO> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        Utilities.showBackIcon(getSupportActionBar());
        ButterKnife.bind(this);
        username = LoginPref.getUsername(this);
        androidID = Utilities.getAndroidID(getApplicationContext());
//        storeId = LoginPref.getStoreId(this);
        storeId = 2;
        Utilities.showBackIcon(getSupportActionBar());
        getSupportActionBar().setTitle("Scan Barcode");

        adapter = new OrderResultScanBarcodeAdapter(new RecyclerViewItemListener() {
            @Override
            public void onClick(Object item, int position) {

            }

            @Override
            public void onLongClick(final Object item, final int position) {
                AlertDialog dialog = new AlertDialog.Builder(ScanBarcodeActivity.this)
                        .setMessage("Bạn có muốn xóa thùng này không?")
                        .setPositiveButton("Xóa một thùng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                flag = 0;
                                // xoa 1 thung
                                deletePackage(position, username, "");
                            }
                        })

                        .setNegativeButton("Hủy", null)
                        .setNeutralButton("Xóa tất cả", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                flag = 1;
                                // xoa ca DO
//                                String DONumber = DONumber;
                                deletePackage(position, username, DONumber);
                            }

                        })
                        .create();
                dialog.show();
                getScanMassan(DONumber);

            }
        });
        showDO = findViewById(R.id.tv_showDONumBer);
        showLocationNumber = findViewById(R.id.tv_LocationNumber);
        // et_inputDO = findViewById(R.id.et_nhapDO);
        tv_total = (TextView) findViewById(R.id.tv_gsk_scan_carton_sum);


//
//        et_inputDO.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (DONumber.equals("") == false) {
//                }
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                if (et_inputDO.getText().length() == 11 && !isEmpty(et_inputDO)) {
////
//
//                    AlertDialog dialog = new AlertDialog.Builder(ScanBarcodeActivity.this)
//                            .setMessage("Bạn có muốn nhap DO nay khong?")
//
//                            .setNegativeButton("Hủy", null)
//                            .setNeutralButton("oke", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    getScanMassan(DONumber);
//                                }
//                            })
//                            .create();
//                    dialog.show();
//                    etTargetScan.requestFocus();
//
////                }
//
//                if(DONumber.contains("\n")){
//                    getScanMassan(DONumber);
//                }
//
//
//            }
//
//        });

//        strDO = DONumber;

        countRecords = (TextView) findViewById(R.id.tv_gsk_scan_order_count);
//        sumCartons = (TextView) findViewById(R.id.tv_gsk_scan_carton_sum);
        etTargetScan = (EditText) findViewById(R.id.et_target_scan);


        etTargetScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String contents = s.toString();
                if (contents.contains("\n")) {
                    onData(contents.replaceAll("\n", ""));
                }
            }
        });


        tvPrevBarcode = (TextView) findViewById(R.id.tv_prev_barcode);
        lv.setAdapter(adapter);
    }

    /// Ham nay chi co tac dung xoa 1 cartonID
    public void deletePackage(int position, String username, String doNumber) {
        UUID dispatchingCarton = list.get(position).DispatchingCartonID;
        MyRetrofit.initRequest(this).DeleteDO_byID(new DeleteDOParameter(dispatchingCarton, username, doNumber)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response != null && response.isSuccess()) {
                    DialogFragment dialog = new MyDialogFragment();
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

                }
            }

            @Override
            public void onFailure(Throwable t) {
                //Toast.makeText(ScanBarcodeActivity.this, "không có kết nối mạng", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(ScanBarcodeActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("không có kết nối mạng!");
                alert.setPositiveButton("OK", null);
                alert.show();

            }
        });

        getScanMassan(this.DONumber);
    }

    public void getScanMassan(final int PalletNumber) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("PalletNumber", PalletNumber);
        String label = "";
        MyRetrofit.initRequest(this).PalletToLocation(new PalletToLocationParam(PalletNumber)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    showLocationNumber.setText(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        getScanMassan(this.DONumber);
    }

//    public void selectIDCustomer(final String id_customer)
//    {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("CustomerID", id_customer);
//
//
//        MyRetrofit.initRequest(this).loadListCustomer(jsonObject).enqueue(new Callback<List<ListCustumerID>>() {
//            @Override
//            public void onResponse(Response<List<ListCustumerID>> response, Retrofit retrofit) {
//                if (response != null && response.isSuccess())
//                {
//                    lst_id_customer = response.body();
//                    if (lst_id_customer != null)
//                    {
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//    }


    public void getScanMassan(final String disDO) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("DispatchingOrderNumber", disDO);
        if (showLocationNumber.getText().length() > 0) {
            jsonObject.addProperty("Label", " " + showLocationNumber.getText().toString());
        }

        MyRetrofit.initRequest(this).loadScanMassan(jsonObject).enqueue(new Callback<List<STAndroid_CartonScannedByDO>>() {
            @Override
            public void onResponse(Response<List<STAndroid_CartonScannedByDO>> response, Retrofit retrofit) {
                if (response != null && response.isSuccess()) {
                    // Toast.makeText(ScanBarcodeActivity.this, "Bạn có muốn scan DO  này không?", Toast.LENGTH_SHORT).show();
                    list = response.body();
                    if (list != null) {
                        double totalQty = 0;
                        for (STAndroid_CartonScannedByDO order : list) {
                            totalQty += Double.parseDouble(order.CartonWeight);
                        }
                        countRecords.setText(String.valueOf(list.size()));
                        tv_total.setText(String.valueOf(totalQty));
                        adapter.replace(list);
//                        onBindViewHolder(String.copyValueOf(disDO));

                    }
                    etTargetScan.requestFocus();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    public void onData(String data) {
        data = data.trim();
        etTargetScan.setText("");
        //tvPrevBarcode.setText(data);
//        orderResultsSupervisorScan(data);
        scanMassan(data);
    }

    public void scanMassan(String scanResult) {

        if (scanResult.substring(0, 2).equals("DO")) {
            DONumber = scanResult;
            //scanResult = showDO.getText().toString();
            showDO.setText(scanResult);
            getScanMassan(scanResult);
            return;
        } else if (scanResult.substring(0, 2).equals("PI")) {
            int palletnumer = Integer.parseInt(scanResult.substring(2, scanResult.length()));
            getScanMassan(palletnumer);
            return;
        }


        for (STAndroid_CartonScannedByDO item : this.list) {
            if (item.BarcodeString.equals(scanResult)) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ScanBarcodeActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Mã bị trùng !");
                alert.setPositiveButton("OK", null);
                alert.show();
                break;
            }
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("DispatchingOrderNumber", DONumber);
        jsonObject.addProperty("UserName", username);
        jsonObject.addProperty("DeviceNumber", androidID);
        jsonObject.addProperty("ScanResult", scanResult);
        if (showLocationNumber.getText().length() > 0) {
            jsonObject.addProperty("LocationNumber", " " + showLocationNumber.getText().toString());
            MyRetrofit.initRequest(this).scanMassanByPallet(jsonObject).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        if (response.body().equals("")) {
                            getScanMassan(DONumber);
                        } else {
                            AlertDialog dialog = new AlertDialog.Builder(ScanBarcodeActivity.this)
                                    .setMessage(response.body())
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .setNegativeButton("", null).create();
                            dialog.show();
                        }
                    }

                }

                @Override
                public void onFailure(Throwable t) {


                    AlertDialog.Builder alert = new AlertDialog.Builder(ScanBarcodeActivity.this);
                    alert.setTitle("Thông Báo");
                    alert.setMessage("không có mạng Thất bại!");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                }
            });
        } else {
            MyRetrofit.initRequest(this).scanMassan(jsonObject).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        if (response.body().equals("")) {
                            getScanMassan(DONumber);
                        } else {
                            AlertDialog dialog = new AlertDialog.Builder(ScanBarcodeActivity.this)
                                    .setMessage(response.body())
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .setNegativeButton("", null).create();
                            dialog.show();
                        }
                    }

                }

                @Override
                public void onFailure(Throwable t) {


                    AlertDialog.Builder alert = new AlertDialog.Builder(ScanBarcodeActivity.this);
                    alert.setTitle("Thông Báo");
                    alert.setMessage("không có mạng Thất bại!");
                    alert.setPositiveButton("OK", null);
                    alert.show();

                }
            });
        }

    }

    /* Scan camera */
    // Start
    @OnClick(R.id.ivCameraScan)
    public void cameraScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(ScanCameraPortrait.class);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String data = result.getContents();
                onData(data);

            }
        }
    }

    public static class MyDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("THÔNG BÁO");
            builder.setMessage("Xóa thành công !");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // You don't have to do anything here if you just
                    // want it dismissed when clicked
                }
            });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public static class MyDialogFragment2 extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("THÔNG BÁO");
            builder.setMessage("");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // You don't have to do anything here if you just
                    // want it dismissed when clicked
                }
            });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


}