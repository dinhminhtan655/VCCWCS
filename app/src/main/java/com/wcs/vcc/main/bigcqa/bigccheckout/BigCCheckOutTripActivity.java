package com.wcs.vcc.main.bigcqa.bigccheckout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BigCCheckOutTripActivity extends RingScanActivity {
    private static final String CODE_SP = "SP";

    @BindView(R.id.btnChua)
    Button btnChua;
    @BindView(R.id.btnSai)
    Button btnSai;
    @BindView(R.id.btnDung)
    Button btnDung;
    @BindView(R.id.rv_bigc_checkout_trip)
    RecyclerView rv;
    @BindView(R.id.tvStoreName)
    TextView tvStoreName;

    private BigCCheckOutTripAdapter adapter;
    String tripID, routeNO;

    List<BigDock_TripDetails> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_c_check_out_trip);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpScan();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        list = new ArrayList<>();

        tripID = getIntent().getStringExtra("TRIP_ID");
        routeNO = getIntent().getStringExtra("STORE_NAME");
        tvStoreName.setText(routeNO);
        tripDeliveryDetail(tripID,0);
    }


    private void tripDeliveryDetail(String tripID, int num) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TripID", tripID);
        jsonObject.addProperty("ScannedStatus", num);
        MyRetrofit.initRequest(BigCCheckOutTripActivity.this).loadBigDockTripDetail(jsonObject).enqueue(new Callback<List<BigDock_TripDetails>>() {
            @Override
            public void onResponse(Response<List<BigDock_TripDetails>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null){

                    list = response.body();

                    adapter = new BigCCheckOutTripAdapter(new RecyclerViewItemOrderListener<BigDock_TripDetails>() {
                        @Override
                        public void onClick(BigDock_TripDetails item, int position, int order) {

                        }
                        @Override
                        public void onLongClick(BigDock_TripDetails item, int position, int order) {

                        }
                    },list);
                    adapter.replace(list);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onData(String data) {
        super.onData(data);
//        if (data.substring(0,2).equals(CODE_SP)){
            JsonObject j = new JsonObject();
            j.addProperty("TripID", tripID);
            j.addProperty("ScanResult", data);
            j.addProperty("UserName", LoginPref.getUsername(BigCCheckOutTripActivity.this));
            j.addProperty("DeviceNumber", Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID));

            MyRetrofit.initRequest(BigCCheckOutTripActivity.this).updateScanCheckOutBigC(j).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    if (response.isSuccess() && response.body() != null){
                        if (!response.body().equals("OK")){
                            Utilities.speakingSomeThing(response.body(), BigCCheckOutTripActivity.this);
                            AlertDialog.Builder b = new AlertDialog.Builder(BigCCheckOutTripActivity.this);
                            b.setTitle("Thông báo");
                            b.setMessage(response.body());
                            tripDeliveryDetail(tripID,2);
                            Dialog d = b.create();
                            d.show();
                            changeColorButtonDung();
                        }else {
                            tripDeliveryDetail(tripID,2);
                            Toast.makeText(BigCCheckOutTripActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                            changeColorButtonDung();
                        }
                    }else if (response.code() == 500){
                        Utilities.speakingSomeThing("Lỗi rồi!", BigCCheckOutTripActivity.this);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(BigCCheckOutTripActivity.this, "Vui lòng kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
                }
            });
//        }
    }


    @OnClick(R.id.btnChua)
    public void filterChua(View view){
        tripDeliveryDetail(tripID,0);
        changeColorButtonChua();
    }

    @OnClick(R.id.btnSai)
    public void filterSai(View view){
        tripDeliveryDetail(tripID,1);
        changeColorButtonSai();
    }

    @OnClick(R.id.btnDung)
    public void filterDung(View view){
        tripDeliveryDetail(tripID,2);
        changeColorButtonDung();
    }

    @OnClick(R.id.btnBreakSO)
    public void breakSO(View view){

        AlertDialog.Builder b = new AlertDialog.Builder(BigCCheckOutTripActivity.this);
        b.setTitle("Thông báo");
        b.setMessage("Bạn có chắc là muốn Break SO");
        b.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                JsonObject j = new JsonObject();
                j.addProperty("TripID",tripID);
                j.addProperty("UserName",LoginPref.getUsername(BigCCheckOutTripActivity.this));
                j.addProperty("DeviceNumber",Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID));
                MyRetrofit.initRequest(BigCCheckOutTripActivity.this).updateScanCheckOutBreakSOBigC(j).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if(response.isSuccess() && response.body() != null){
                            if (response.body().equals("OK")){
                                Utilities.speakingSomeThing("Thành công!", BigCCheckOutTripActivity.this);
                                tripDeliveryDetail(tripID,0);
                            }else {
                                Utilities.speakingSomeThing("Thất bại!", BigCCheckOutTripActivity.this);
                            }
                        }else {
                            Toast.makeText(BigCCheckOutTripActivity.this, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(BigCCheckOutTripActivity.this, "Kiểm tra kết nối Internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        Dialog d = b.create();
        d.show();


    }

    public void changeColorButtonChua(){
        btnChua.setBackgroundColor(getResources().getColor(R.color.caldroid_light_red));
        btnSai.setBackgroundColor(android.R.drawable.btn_default);
        btnDung.setBackgroundColor(android.R.drawable.btn_default);
    }

    public void changeColorButtonSai(){
        btnChua.setBackgroundColor(android.R.drawable.btn_default);
        btnSai.setBackgroundColor(getResources().getColor(R.color.caldroid_light_red));
        btnDung.setBackgroundColor(android.R.drawable.btn_default);
    }

    public void changeColorButtonDung(){
        btnChua.setBackgroundColor(android.R.drawable.btn_default);
        btnSai.setBackgroundColor(android.R.drawable.btn_default);
        btnDung.setBackgroundColor(getResources().getColor(R.color.caldroid_light_red));
    }
}
