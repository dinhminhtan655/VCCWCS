package com.wcs.vcc.main.pickship;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.DatePicker;

import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.TripDeliveryParameter;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.pickship.cartonscan.PickShipCartonScanActivity;
import com.wcs.vcc.main.pickship.detail.PickShipDetailActivity;
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

public class PickShipActivity extends EmdkActivity {

    @BindView(R.id.btChooceDate)
    Button btChooseDate;
    @BindView(R.id.rv_pick_ship)
    RecyclerView rv;

    private String username;
    private int storeId;
    private Calendar calendar;
    private String reportDate;
    private PickShipAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_ship);
        Utilities.showBackIcon(getSupportActionBar());
        ButterKnife.bind(this);

        username = LoginPref.getUsername(this);
        storeId = LoginPref.getStoreId(this);

        setUpScan();

        adapter = new PickShipAdapter(new EventsListener<PickShip>() {
            @Override
            public void onClick(PickShip item, int position) {
                Intent intent = new Intent(PickShipActivity.this, PickShipDetailActivity.class);
                intent.putExtra("TRIP_NUMBER", item.TripNumber);
                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);

        calendar = Calendar.getInstance();
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        pickShip();
    }

    private void pickShip() {
        progressDialog = Utilities.getProgressDialog(this, "Loading...");
        progressDialog.show();

        TripDeliveryParameter params = new TripDeliveryParameter(username, reportDate.split("T")[0], storeId);
        MyRetrofit.initRequest(this).pickShip(params).enqueue(new Callback<List<PickShip>>() {
            @Override
            public void onResponse(Response<List<PickShip>> response, Retrofit retrofit) {
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

    @Override
    public void onData(String data) {
        super.onData(data);
        data = data.toLowerCase();

        if (data.contains("tw")) {

        } else if (data.contains("tc")) {
            Intent intent = new Intent(PickShipActivity.this, PickShipCartonScanActivity.class);
            intent.putExtra("CARTON_NUMBER", data);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btChooceDate)
    public void chooseDate() {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                pickShip();
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
        pickShip();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        pickShip();
    }
}
