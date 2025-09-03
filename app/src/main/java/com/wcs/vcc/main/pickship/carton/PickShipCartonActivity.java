package com.wcs.vcc.main.pickship.carton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickShipDetailParameter;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.pickship.EventsListener;
import com.wcs.vcc.main.pickship.cartonscan.PickShipCartonScanActivity;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PickShipCartonActivity extends EmdkActivity {

    @BindView(R.id.rv_pick_ship_carton)
    RecyclerView rv;

    private PickShipCartonAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_ship_carton);
        Utilities.showBackIcon(getSupportActionBar());
        ButterKnife.bind(this);

        String orderNumber = getIntent().getStringExtra("ORDER_NUMBER");

        setUpScan();

        adapter = new PickShipCartonAdapter(new EventsListener<PickShipCarton>() {
            @Override
            public void onClick(PickShipCarton item, int position) {
                Intent intent = new Intent(PickShipCartonActivity.this, PickShipCartonScanActivity.class);
                intent.putExtra("CARTON_NUMBER", item.TripDetailCartonNumber);
                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);
        pickShipCarton(orderNumber);
    }

    private void pickShipCarton(String tripNumber) {
        progressDialog = Utilities.getProgressDialog(this, "Loading...");
        progressDialog.show();

        PickShipDetailParameter params = new PickShipDetailParameter(tripNumber);
        MyRetrofit.initRequest(this).pickShipCarton(params).enqueue(new Callback<List<PickShipCarton>>() {
            @Override
            public void onResponse(Response<List<PickShipCarton>> response, Retrofit retrofit) {
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

        if (data.contains("do")) {

        } else if (data.contains("tc")) {
            Intent intent = new Intent(PickShipCartonActivity.this, PickShipCartonScanActivity.class);
            intent.putExtra("CARTON_NUMBER", data);
            startActivity(intent);
        }
    }

}
