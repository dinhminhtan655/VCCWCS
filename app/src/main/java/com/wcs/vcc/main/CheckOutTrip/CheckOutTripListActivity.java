package com.wcs.vcc.main.CheckOutTrip;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.checkouttrip.response.CheckOutTripResponse;
import com.wcs.vcc.api.checkouttrip.response.TripInfor;
import com.wcs.vcc.main.CheckOutTrip.Adapter.TripInforAdapter;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CheckOutTripListActivity  extends EmdkActivity {
    @BindView(R.id.rv_check_out)
    RecyclerView rv;
    @BindView(R.id.btChooceDate)
    Button btChooseDate;
    private String username;
    private int storeId;
    private Calendar calendar;
    private String reportDate;
    private ArrayList<TripInfor> tripInfors;
    private TripInforAdapter adapter;
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_trip_list);
        ButterKnife.bind(this);
        Utilities.showBackIcon(getSupportActionBar());
        setUpScan();
        calendar = Calendar.getInstance();
        username = LoginPref.getUsername(this);
        storeId = LoginPref.getStoreId(this);
        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        reportDate = dateformat.format(calendar.getTime());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        adapter = new TripInforAdapter(new RecyclerViewItemListener<TripInfor>() {
            @Override
            public void onClick(TripInfor item, int position) {
                Intent intent = new Intent(CheckOutTripListActivity.this, CustomerClientByTripActivity.class);
                intent.putExtra("TRIP_NUMBER", item.getTripNumber());
                intent.putExtra("DeliveryDate", reportDate);

                startActivity(intent);
            }

            @Override
            public void onLongClick(TripInfor item, int position) {

            }
        });
        tripDelivery();
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tripDelivery();
    }

    @Override
    public void onData(String data) {
        super.onData(data);
        String TripNumber = data;
        Intent intent = new Intent(CheckOutTripListActivity.this, CustomerClientByTripActivity.class);
        intent.putExtra("TRIP_NUMBER", TripNumber);
        startActivity(intent);
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
    private void tripDelivery() {

        MyRetrofit.initRequest(this).loadTripInforCheckOut(storeId,reportDate.substring(0,10)).enqueue(new Callback<CheckOutTripResponse>() {
            @Override
            public void onResponse(Response<CheckOutTripResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    adapter.replace(response.body().getTripList());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
