package com.wcs.vcc.main.palletcartonchecking;

import android.app.ProgressDialog;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.PalletFindParameter;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.wcs.databinding.ActivityPalletCartonCheckingBinding;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class KiemPalletCartonActivity extends EmdkActivity {
    public static final String TAG = KiemPalletCartonActivity.class.getSimpleName();

    private ProgressDialog dialog;
    private View.OnClickListener tryAgain;
    private String scanResult = "";
    private MovementHistoryAdapter adapter;
    private PalletFindAdapter palletFindAdapter;
    private ActivityPalletCartonCheckingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.showBackIcon(getSupportActionBar());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pallet_carton_checking);
        setUpScan();

        if (getIntent().hasExtra("ARGS")) {
            Bundle args = getIntent().getBundleExtra("ARGS");
            onData(args.getString("BARCODE"));
        }

        snackBarView = binding.lvHistory;
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPalletFind();
            }
        };
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (scanResult.length() > 0)
                    getPalletFind();
                else
                    binding.swipeRefresh.setRefreshing(false);
            }
        });

        adapter = new MovementHistoryAdapter(this, new ArrayList<MovementHistoryInfo>());
        binding.lvHistory.setAdapter(adapter);

        palletFindAdapter = new PalletFindAdapter(this, new ArrayList<PalletFind>());
        binding.lvPallet.setAdapter(palletFindAdapter);
    }

    private void getPalletFind(final int palletId) {
        Utilities.hideKeyboard(this);
        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).getPalletFind(new PalletFindParameter(palletId)).enqueue(new Callback<List<PalletFind>>() {
            @Override
            public void onResponse(Response<List<PalletFind>> response, Retrofit retrofit) {
                List<PalletFind> body = response.body();
                if (response.isSuccess() && body != null && body.size() > 0) {
                    palletFindAdapter.clear();
                    palletFindAdapter.addAll(body);
                    PalletFind palletFind = body.get(0);
                    setTitle(String.format(Locale.getDefault(), "%s~%s", palletFind.getCustomerNumber(), palletFind.getCustomerName()));
                    String header = String.format("Product: %s \n %s", palletFind.getProductNumber(), palletFind.getProductName());
                    binding.setHeader(header);
                }
                getMovementHistory();
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(KiemPalletCartonActivity.this, t, TAG, snackBarView, tryAgain);
            }
        });
    }

    private void getMovementHistory() {
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).getMovementHistory(scanResult).enqueue(new Callback<List<MovementHistoryInfo>>() {
            @Override
            public void onResponse(Response<List<MovementHistoryInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dismissDialog(dialog);
                binding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(KiemPalletCartonActivity.this, t, TAG, snackBarView, tryAgain);
            }
        });
    }

    @Override
    public void onData(String data) {
        super.onData(data);
        Const.timePauseActive = 0;
        stopLogoutService();
//        resetLogoutTimer();
        scanResult = data.toLowerCase();
        getPalletFind();
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

    private void getPalletFind() {
        if (!scanResult.contains("pi"))
            scanResult = "PI" + scanResult;
        try {
            String idString = scanResult.replaceAll("\\D*", "");
            getPalletFind(Integer.parseInt(idString));
        } catch (NumberFormatException ignored) {

        }
    }
}
