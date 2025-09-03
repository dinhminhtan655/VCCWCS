package com.wcs.vcc.main.pickship.detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickShipDetailParameter;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.pickship.EventsListener;
import com.wcs.vcc.main.pickship.carton.PickShipCartonActivity;
import com.wcs.vcc.main.pickship.cartonscan.PickShipCartonScanActivity;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PickShipDetailActivity extends EmdkActivity {

    @BindView(R.id.rv_pick_ship_detail)
    RecyclerView rv;

    private PickShipDetailAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_ship_detail);
        Utilities.showBackIcon(getSupportActionBar());
        ButterKnife.bind(this);

        String tripNumber = getIntent().getStringExtra("TRIP_NUMBER");

        setUpScan();

        adapter = new PickShipDetailAdapter(new EventsListener<PickShipDetail>() {
            @Override
            public void onClick(PickShipDetail item, int position) {
                Intent intent = new Intent(PickShipDetailActivity.this, PickShipCartonActivity.class);
                intent.putExtra("ORDER_NUMBER", item.OrderNumber);
                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);
        pickShipDetail(tripNumber);
    }

    private void pickShipDetail(String tripNumber) {
        progressDialog = Utilities.getProgressDialog(this, "Loading...");
        progressDialog.show();

        PickShipDetailParameter params = new PickShipDetailParameter(tripNumber);
        MyRetrofit.initRequest(this).pickShipDetail(params).enqueue(new Callback<List<PickShipDetail>>() {
            @Override
            public void onResponse(Response<List<PickShipDetail>> response, Retrofit retrofit) {
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

        if (data.contains("tc")) {
            Intent intent = new Intent(this, PickShipCartonScanActivity.class);
            intent.putExtra("CARTON_NUMBER", data);
            startActivity(intent);
        } else if (data.contains("tw")) {
            pickShipDetail(data);
        }
    }

}
