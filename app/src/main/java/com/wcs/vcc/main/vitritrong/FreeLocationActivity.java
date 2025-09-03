package com.wcs.vcc.main.vitritrong;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.wcs.wcs.R;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FreeLocationActivity extends BaseActivity {
    public static final String TAG = FreeLocationActivity.class.getSimpleName();
    @BindView(R.id.lvOrderDetail)
    ListView listView;
//    @BindView(R.id.tvFreeLocationFreeQtyBot)
//    TextView tvFreeQty;
//    @BindView(R.id.tvFreeLocationHBot)
//    TextView tvH;
//    @BindView(R.id.tvFreeLocationLBot)
//    TextView tvL;
//    @BindView(R.id.tvFreeLocationVHBot)
//    TextView tvVH;
//    @BindView(R.id.tvFreeLocationVLBot)
//    TextView tvVL;
//    @BindView(R.id.tvFreeLocationWFBot)
//    TextView tvWF;
//    @BindView(R.id.tvFreeLocationPalletQtyBot)
//    TextView tvPalletQty;
//    @BindView(R.id.tvFreeLocationTotalBot)
//    TextView tvTotal;
    private View.OnClickListener tryAgain;
    private FreeLocationAdapter adapter;
    private ProgressDialog dialog;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_location);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        storeId = LoginPref.getStoreId(this);

        initialUI();
    }

    private void initialUI() {

        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFreeLocation(listView);
            }
        };
        adapter = new FreeLocationAdapter(this, new ArrayList<FreeLocationInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FreeLocationDetailsActivity.class);
                FreeLocationInfo item = adapter.getItem(position);
                if (item != null) {
                    intent.putExtra("ROOM_ID", item.RoomID.toString());
                    startActivity(intent);
                }
            }
        });
        executeFreeLocationUpdate(listView);
    }


    private void getFreeLocation(final View view) {
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).getFreeLocation(storeId).enqueue(new Callback<List<FreeLocationInfo>>() {
            @Override
            public void onResponse(Response<List<FreeLocationInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    if (response.body().size() > 0) {
                        int qtyOfFree = 0;
                        int qtyOfPallets_onHand = 0;
                        int qtyFree_high = 0;
                        int qtyFree_low = 0;
                        int qtyFree_veryHigh = 0;
                        int qtyFree_veryLow = 0;
                        int qtyBusy = 0;
                        int qtyTotal = 0;
                        for (FreeLocationInfo info : response.body()) {
                            qtyOfFree += info.getQtyOfFree();
                            qtyOfPallets_onHand += info.getQtyOfPallets_OnHand();
                            qtyFree_high += info.getQtyFree_High();
                            qtyFree_low += info.getQtyFree_Low();
                            qtyFree_veryHigh += info.getQtyFree_VeryHigh();
                            qtyFree_veryLow += info.getQtyFree_VeryLow();
                            qtyBusy += info.getQtyBusy();
                            qtyTotal += info.getQtyLocation();
                        }
//                        tvFreeQty.setText(NumberFormat.getInstance().format(qtyOfFree));
//                        tvH.setText(NumberFormat.getInstance().format(qtyFree_high));
//                        tvL.setText(NumberFormat.getInstance().format(qtyFree_low));
//                        tvVH.setText(NumberFormat.getInstance().format(qtyFree_veryHigh));
//                        tvVL.setText(NumberFormat.getInstance().format(qtyFree_veryLow));
//                        tvWF.setText(NumberFormat.getInstance().format(qtyBusy));
//                        tvPalletQty.setText(NumberFormat.getInstance().format(qtyOfPallets_onHand));
//                        tvTotal.setText(NumberFormat.getInstance().format(qtyTotal));

                    } else {
//                        tvFreeQty.setText("0");
//                        tvH.setText("0");
//                        tvL.setText("0");
//                        tvVH.setText("0");
//                        tvVL.setText("0");
//                        tvWF.setText("0");
//                        tvPalletQty.setText("0");
                    }

                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(FreeLocationActivity.this, t, TAG, view, tryAgain);
            }
        });
    }

    private void executeFreeLocationUpdate(final View view) {
        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).executeFreeLocationUpdate(storeId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    getFreeLocation(view);
                } else
                    dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(FreeLocationActivity.this, t, TAG, view, tryAgain);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        Const.isActivating = true;
        super.onResume();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }
}
