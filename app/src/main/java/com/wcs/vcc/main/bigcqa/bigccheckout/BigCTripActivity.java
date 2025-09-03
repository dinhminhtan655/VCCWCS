package com.wcs.vcc.main.bigcqa.bigccheckout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.TripDeliveryParameter;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.tripdelivery.TripDelivery;
import com.wcs.vcc.main.tripdelivery.TripDeliveryAdapter;
import com.wcs.vcc.main.tripdelivery.TripItemListener;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BigCTripActivity extends EmdkActivity {

    @BindView(R.id.btChooseDate)
    Button btChooseDate;
    @BindView(R.id.rv_trip_delivery)
    RecyclerView rv;

    private String username;
    private int storeId;
    private Calendar calendar;
    private String reportDate;
    private TripDeliveryAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_c_trip);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        username = LoginPref.getUsername(this);
        storeId = LoginPref.getStoreId(this);

        adapter = new TripDeliveryAdapter(new TripItemListener() {
            @Override
            public void onClick(TripDelivery item, int position) {
                Intent intent = new Intent(BigCTripActivity.this, BigCCheckOutTripActivity.class);
                intent.putExtra("TRIP_ID", item.TripID.toString());
                intent.putExtra("STORE_NAME", item.ROUTENO);
                Log.e("TRIP_ID", item.TripID.toString());
                startActivity(intent);
            }

        });
        rv.setAdapter(adapter);

        calendar = Calendar.getInstance();
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        tripDelivery();

    }


    private void tripDelivery() {
        progressDialog = Utilities.getProgressDialog(BigCTripActivity.this, "Loading...");
        progressDialog.show();

        TripDeliveryParameter params = new TripDeliveryParameter(username, reportDate.split("T")[0], storeId);
        MyRetrofit.initRequest(BigCTripActivity.this).tripDelivery(params).enqueue(new Callback<List<TripDelivery>>() {
            @Override
            public void onResponse(Response<List<TripDelivery>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    adapter.replace(response.body());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    @OnClick(R.id.btChooseDate)
    public void chooseDate() {
        DatePickerDialog dialog = new DatePickerDialog(BigCTripActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                tripDelivery();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        tripDelivery();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        tripDelivery();
    }

}
