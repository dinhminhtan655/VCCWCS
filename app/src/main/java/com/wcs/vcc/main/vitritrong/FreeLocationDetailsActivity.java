package com.wcs.vcc.main.vitritrong;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.FreeLocationDetailsParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FreeLocationDetailsActivity extends BaseActivity {

    public static final String TAG = "FreeLocationDetailsActivity";
    @BindView(R.id.tvFreeLocationFreeQtyBot)
    TextView tvFreeQty;
    @BindView(R.id.tvFreeLocationHBot)
    TextView tvH;
    @BindView(R.id.tvFreeLocationLBot)
    TextView tvL;
    @BindView(R.id.tvFreeLocationVHBot)
    TextView tvVH;
    @BindView(R.id.tvFreeLocationVLBot)
    TextView tvVL;
    @BindView(R.id.tvFreeLocationBusyFBot)
    TextView tvBusyBot;
    @BindView(R.id.tvFreeLocationPalletQtyBot)
    TextView tvPalletQty;
    @BindView(R.id.tvFreeLocationTotalBot)
    TextView tvTotal;
    @BindView(R.id.tvFreeLocationAisle)
    TextView tvAisle;
    @BindView(R.id.tvOffLocationTotalBot)
    TextView tvOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_location_details);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        String roomId = getIntent().getStringExtra("ROOM_ID");
        ListView listView = findViewById(R.id.lv_free_location_detail);
        getFreeLocationDetails(listView, roomId);
    }

    private void getFreeLocationDetails(final ListView listView, String roomID) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, listView);
            return;
        }
        MyRetrofit.initRequest(this).getFreeLocationDetails(new FreeLocationDetailsParameter(roomID, LoginPref.getStoreId(this))).enqueue(new Callback<List<FreeLocationDetailsInfo>>() {
            @Override
            public void onResponse(Response<List<FreeLocationDetailsInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    List<FreeLocationDetailsInfo> body = response.body();
                    listView.setAdapter(new FreeLocationDetailsAdapter(FreeLocationDetailsActivity.this, body));
                    if (response.body().size() > 0) {
                        int qtyOfFree = 0;
                        int qtyOfPallets_onHand = 0;
                        int qtyFree_high = 0;
                        int qtyFree_low = 0;
                        int qtyFree_veryHigh = 0;
                        int qtyFree_veryLow = 0;
                        int qtyFreeAfterDP = 0;
                        int qtyTotal = 0;
                        int qtyAisle = 0;
                        int qtyOff = 0;
                        int qtyBusy = 0;
                        for (FreeLocationDetailsInfo info : response.body()) {
                            qtyOfFree += info.getQtyOfFree();
                            qtyOfPallets_onHand += info.getQtyOfPallets_OnHand();
                            qtyFree_high += info.getQtyFree_High();
                            qtyFree_low += info.getQtyFree_Low();
                            qtyFree_veryHigh += info.getQtyFree_VeryHigh();
                            qtyFree_veryLow += info.getQtyFree_VeryLow();
                            qtyFreeAfterDP += info.getQtyFreeAfterDP();
                            qtyTotal += info.getQtyLocation();
                            qtyAisle += 1;
                            qtyOff += info.getQtyLocationOff();
                            qtyBusy += info.getQtyBusy();

                        }
                        tvFreeQty.setText(NumberFormat.getInstance().format(qtyOfFree));
                        tvH.setText(NumberFormat.getInstance().format(qtyFree_high));
                        tvL.setText(NumberFormat.getInstance().format(qtyFree_low));
                        tvVH.setText(NumberFormat.getInstance().format(qtyFree_veryHigh));
                        tvVL.setText(NumberFormat.getInstance().format(qtyFree_veryLow));
                        tvBusyBot.setText(NumberFormat.getInstance().format(qtyBusy));
                        tvPalletQty.setText(NumberFormat.getInstance().format(qtyOfPallets_onHand));
                        tvTotal.setText(NumberFormat.getInstance().format(qtyTotal));
                        tvAisle.setText(NumberFormat.getInstance().format(qtyAisle));
                        tvOff.setText(NumberFormat.getInstance().format(qtyOff));

                    } else {
                        tvFreeQty.setText("0");
                        tvH.setText("0");
                        tvL.setText("0");
                        tvVH.setText("0");
                        tvVL.setText("0");
                        tvBusyBot.setText("0");
                        tvPalletQty.setText("0");
                        tvAisle.setText("0");
                        tvOff.setText("0");
                    }
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(FreeLocationDetailsActivity.this, t, TAG, listView);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


}
