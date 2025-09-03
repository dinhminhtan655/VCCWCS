package com.wcs.vcc.main.gskscan;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.OrderResultsSupervisorScanParam;
import com.wcs.vcc.api.OrderResultsSupervisorScanViewParam;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class GSKScanActivity extends ShowHomeButtonActivity {
    public static final String TAG = "GSKScanActivity";
    private String username;
    private OrderResultsSupervisorAdapter adapter;
    private TextView tvPrevBarcode;
    private EditText etTargetScan;
    private int storeID;
    private TextView countRecords;
    private TextView sumCartons;
    private String androidID;
    private Byte flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gskscan);
        Utilities.showBackIcon(getSupportActionBar());
        ButterKnife.bind(this);

        username = LoginPref.getUsername(this);
        androidID = Utilities.getAndroidID(getApplicationContext());
        storeID = LoginPref.getStoreId(this);

        adapter = new OrderResultsSupervisorAdapter();
        RecyclerView lv = (RecyclerView) findViewById(R.id.lv_gsk_scan);
        lv.setAdapter(adapter);

        orderResultsSupervisorScanView("");

        countRecords = (TextView) findViewById(R.id.tv_gsk_scan_order_count);
        sumCartons = (TextView) findViewById(R.id.tv_gsk_scan_carton_sum);
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

        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_gsk_scan);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                etTargetScan.requestFocus();
                flag = Byte.parseByte(findViewById(checkedId).getTag().toString());
                orderResultsSupervisorScanView("");
            }
        });
    }

    private void orderResultsSupervisorScan(String scanResult) {
        OrderResultsSupervisorScanParam body = new OrderResultsSupervisorScanParam(scanResult, username, androidID, tvPrevBarcode.getText().toString());
        MyRetrofit.initRequest(this).orderResultsSupervisorScan(body).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    String body = response.body();
                    if (!body.equals("OK"))
                    {
                        Toast.makeText(getApplicationContext(), body, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        orderResultsSupervisorScanView(scanResult);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void orderResultsSupervisorScanView(String scanResult) {
        OrderResultsSupervisorScanViewParam body = new OrderResultsSupervisorScanViewParam(username, androidID, flag, storeID, scanResult);
        MyRetrofit.initRequest(this).orderResultsSupervisorScanView(body).enqueue(new Callback<List<OrderResultsSupervisorScan>>() {
            @Override
            public void onResponse(Response<List<OrderResultsSupervisorScan>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<OrderResultsSupervisorScan> orderList = response.body();
                    if (orderList != null) {
                        int totalQty = 0;
                        for (OrderResultsSupervisorScan order : orderList) {
                            totalQty += order.TotalPackages;
                        }
                        countRecords.setText(String.valueOf(orderList.size()));
                        sumCartons.setText(String.valueOf(totalQty));
                        adapter.replace(orderList);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void onData(String data) {
        data = data.trim().replaceAll("\"","");
        etTargetScan.setText("");
        if (data.startsWith("CO-")) {
            orderResultsSupervisorScan(data);
        } else if (data.startsWith("DK")) {
            tvPrevBarcode.setText(data);
        }
    }

    /* Scan camera */
    // Start
    @OnClick(R.id.ivCameraScan)
    public void cameraScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setCaptureActivity(ScanCameraPortrait.class);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String data = result.getContents();
                onData(data);
            }
        }
    }
    // End
}
