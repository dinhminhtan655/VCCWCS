package com.wcs.vcc.main.kiemvitri;

import android.app.ProgressDialog;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.emdk.ScanListener;
import com.wcs.wcs.R;
import com.wcs.vcc.api.LocationCheckingParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.wcs.databinding.ActivityKiemViTriBinding;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class KiemViTriNoEMDKActivity extends BaseActivity implements ScanListener {
    private final String TAG = KiemViTriNoEMDKActivity.class.getSimpleName();

    private KiemViTriAdapter adapter;
    private ActivityKiemViTriBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.showBackIcon(getSupportActionBar());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kiem_vi_tri);
//        setUpScan();

        adapter = new KiemViTriAdapter(KiemViTriNoEMDKActivity.this, new ArrayList<LocationCheckingInfo>());
        binding.lvKiemViTri.setAdapter(adapter);

        if (getIntent().hasExtra("ARGS")) {
            Bundle args = getIntent().getBundleExtra("ARGS");
            onData(args.getString("BARCODE"));
        }

        binding.etTargetScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String contents = s.toString();
                if (contents.contains("\n")) {
                    onData(contents.replaceAll("\n", ""));
                }
            }
        });
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

    @Override
    public void onData(String data) {
        Const.timePauseActive = 0;
        stopLogoutService();
//        resetLogoutTimer();
        data = data.trim();
        if (!data.equals("") && !data.equals("\t\t")) {
            binding.etTargetScan.setText("");
            binding.tvPrevBarcode.setText(data);
            getLocationChecking(binding.lvKiemViTri, data);
        }

    }

    public void getLocationChecking(final View view, String locationCheck) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).getLocationChecking(new LocationCheckingParameter(locationCheck)).enqueue(new Callback<List<LocationCheckingInfo>>() {

            @Override
            public void onResponse(Response<List<LocationCheckingInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    if (response.body().size() == 0) {
                        Utilities.speakingSomeThingslow("Không có hàng", KiemViTriNoEMDKActivity.this);
                    }
                    adapter.clear();
                    adapter.addAll(response.body());
                    int totalCarton = 0;
                    for (LocationCheckingInfo info : response.body())
                        totalCarton += info.getCurrentQuantity();

                    binding.setTotalCarton(totalCarton);
                    binding.lvKiemViTri.setAdapter(adapter);
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(KiemViTriNoEMDKActivity.this, t, TAG, view);
            }
        });
    }
}