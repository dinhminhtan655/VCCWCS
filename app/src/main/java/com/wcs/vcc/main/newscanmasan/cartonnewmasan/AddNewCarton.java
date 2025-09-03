package com.wcs.vcc.main.newscanmasan.cartonnewmasan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.xdoc.request.AssignCartonToPalletRequest;
import com.wcs.vcc.api.xdoc.response.XdocDispatchingOrderResponse;
import com.wcs.vcc.main.BarcodeFuncDef;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.wcs.vcc.main.pickship.order.DispatchingOrderPackingActivity.CARTON;
import static com.wcs.vcc.main.pickship.order.DispatchingOrderPackingActivity.STORE_NUMBER;

public class AddNewCarton extends RingScanActivity {

    @BindView(R.id.tv_carton_barcode_assgin)
    TextView tv_carton_barcode_assgin;
    @BindView(R.id.tv_store_lable)
    TextView tv_store_lable;
    @BindView(R.id.etInerCarton)
    EditText etInerCarton;
    @BindView(R.id.et_target_scan)
    EditText et_target_scan;
    @BindView(R.id.tv_prev_barcode)
    TextView tv_prev_barcode;
    private String reportDate, strStoreNum, strDis;
    private Calendar calendar;
    @BindView( R.id.btn_assign_carton_to_pallet)
    Button btnAssignCarton;
    Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_carton);
        ButterKnife.bind(this);
        setUpScan();
        reportDate = getIntent().getStringExtra("OrderDate");
        strStoreNum = getIntent().getStringExtra("STORENUM");
        strDis = getIntent().getStringExtra("DISHPATCHING");
        tv_store_lable.setText(BarcodeFuncDef.ST + strStoreNum);

        etInerCarton.setText("24");

        btnAssignCarton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCarton();
            }
        });
    }

    @Override
    public void onData(String data) {
        super.onData(data);
//        if (data.contains("\n")) {
        if (data.substring(0, 2).equals(CARTON)) {
            tv_carton_barcode_assgin.setText(data.replaceAll("\n", ""));
        }
        if (data.substring(0, 2).equals(STORE_NUMBER)) {
            tv_store_lable.setText(data.replaceAll("\n", ""));
        }
        if (et_target_scan != null) {
            et_target_scan.setText("");
            et_target_scan.requestFocus();
        }
        if (et_target_scan != null) {
            tv_prev_barcode.setText(data.replaceAll("\n", ""));
        }
        if (data.contains("ENTER")) {
            if (d != null) {
                if (d.isShowing()) {
                    d.dismiss();
                } else {
                    assignCarton();
                }
            } else {
                assignCarton();
            }
        }else if (!data.substring(0, 2).equals(CARTON) || data.substring(0, 2).equals(STORE_NUMBER)){
            onBackPressed();
        }else if(tv_carton_barcode_assgin.getText() != null
                && tv_carton_barcode_assgin.getText().length() != 0
                &&tv_store_lable.getText() != null
                && tv_store_lable.getText().length() != 0){
            assignCarton();
        }
    }
//    }

    private void assignCarton() {
        if (tv_carton_barcode_assgin.getText() == null || tv_carton_barcode_assgin.getText().length() == 0) {
            Utilities.speakingSomeThing("Lỗi rồi", AddNewCarton.this);
            AlertDialog dialogWaring = new AlertDialog.Builder(AddNewCarton.this)
                    .setMessage("Vui lòng scan mã thùng mới")
                    .setNegativeButton("OK", null)
                    .create();
            dialogWaring.show();
            return;
        }
        if (tv_store_lable.getText() == null || tv_store_lable.getText().length() == 0) {
            Utilities.speakingSomeThing("Lỗi rồi", AddNewCarton.this);
            AlertDialog dialogWaring = new AlertDialog.Builder(AddNewCarton.this)
                    .setMessage("Vui lòng scan mã vạch trên Pallet")
                    .setNegativeButton("OK", null)
                    .create();
            dialogWaring.show();
            return;
        }
        AssignCartonToPalletRequest request = new AssignCartonToPalletRequest();
        request.setUserName(LoginPref.getUsername(AddNewCarton.this));
        request.setBarcodeString(tv_carton_barcode_assgin.getText().toString());
        request.setStoreNumber(Integer.parseInt(tv_store_lable.getText()
                .toString().substring(2, tv_store_lable.getText().length()).replace(" ", "")));
        request.setDeviceNumber(Utilities.getAndroidID(getApplicationContext()));
        request.setOrderDate(reportDate.substring(0, 10));
        request.setInner(Integer.parseInt(etInerCarton.getText().toString()));
        request.setDispatchingOrderNumber(strDis);
        if (etInerCarton.getText() != null) {
            request.setInner(Integer.parseInt(etInerCarton.getText().toString()));
        }

        MyRetrofit.initRequest(AddNewCarton.this).assginCartonToPallet(request).enqueue(new Callback<XdocDispatchingOrderResponse>() {
            @Override
            public void onResponse(Response<XdocDispatchingOrderResponse> response, Retrofit retrofit) {
                if (response.isSuccess() && response != null) {
                    if (response.body().getMessage().contains("Đã được thêm vào điểm số:")) {
                        finish();
                        Toast.makeText(AddNewCarton.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Utilities.speakingSomeThing("Sai rồi",AddNewCarton.this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewCarton.this);
                        builder.setTitle("Thông báo");
                        builder.setMessage(response.body().getMessage());
                        d = builder.create();
                        d.show();
                    }
                } else if (response.code() == 500) {
                    Utilities.speakingSomeThing("Lỗi hệ thống", AddNewCarton.this);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utilities.speakingSomeThing("Lỗi rồi",AddNewCarton.this);
                AlertDialog dialogWaring = new AlertDialog.Builder(AddNewCarton.this)
                        .setMessage("Lỗi hệ thống 500")
                        .setNegativeButton("OK", null)
                        .create();
                dialogWaring.show();
            }
        });
    }
}
