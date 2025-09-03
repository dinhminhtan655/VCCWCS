package com.wcs.vcc.main.pickship.cartonscan;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickShipCartonScanParameter;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.pickship.EventsListener;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PickShipCartonScanActivity extends EmdkActivity {

    @BindView(R.id.rv_pick_ship_carton_scan)
    RecyclerView rv;

    private PickShipCartonScanAdapter adapter;
    private ProgressDialog progressDialog;
    private String username, androidId;
    private String cartonNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_ship_carton_scan);
        Utilities.showBackIcon(getSupportActionBar());
        ButterKnife.bind(this);

        username = LoginPref.getUsername(this);
        androidId = Utilities.getAndroidID(this);

        cartonNumber = getIntent().getStringExtra("CARTON_NUMBER");

        setUpScan();

        adapter = new PickShipCartonScanAdapter(new EventsListener<PickShipCartonScan>() {
            @Override
            public void onClick(PickShipCartonScan item, int position) {
//                Intent intent = new Intent(PickShipCartonScanActivity.this, PickShipCartonScanActivity.class);
//                intent.putExtra("CARTON_NUMBER", item.TripDetailCartonNumber);
//                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);
        pickShipCartonScan("TC");
    }


    private void pickShipCartonScan(String scanResult) {
        progressDialog = Utilities.getProgressDialog(this, "Loading...");
        progressDialog.show();

        PickShipCartonScanParameter params = new PickShipCartonScanParameter(username, androidId, cartonNumber, scanResult);
        MyRetrofit.initRequest(this).pickShipCartonScan(params).enqueue(new Callback<List<PickShipCartonScan>>() {
            @Override
            public void onResponse(Response<List<PickShipCartonScan>> response, Retrofit retrofit) {
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
            cartonNumber = data;
        }
        pickShipCartonScan(data);
    }
}
