package com.wcs.vcc.main.len_ha_hang;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickAndPutAwayLocationScanParam;
import com.wcs.wcs.databinding.ActivityPickPutDetailBinding;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.vo.PalletPickPut;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PickPutDetailActivity extends EmdkActivity {

    public static final String PALLET_BARCODE = "pallet_barcode";
    private ActivityPickPutDetailBinding binding;

    private String username;
    private String androidID;
    private PalletPickPut pallet;
    private int storeId;
    private StringBuilder locationBuilder = new StringBuilder();
    private int timeScanToCreateLocation;
    private TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick_put_detail);
        Utilities.showBackIcon(getSupportActionBar());
        setUpScan();

        tvMsg = findViewById(R.id.tv_msg);
        username = LoginPref.getUsername(this);
        androidID = Utilities.getAndroidID(this);
        storeId = LoginPref.getStoreId(this);

        String barcode = getIntent().getStringExtra(PALLET_BARCODE);
        loadPickAndPutAwayPalletScan(barcode);
    }

    // Lấy chi tiết thông tin Pallet
    private void loadPickAndPutAwayPalletScan(String barcode) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("UserName=%s&ScanResult=%s&DeviceNumber=%s", username, barcode, androidID));
        MyRetrofit.initRequest(this).loadPickAndPutAwayPalletScan(body).enqueue(new Callback<List<PalletPickPut>>() {
            @Override
            public void onResponse(Response<List<PalletPickPut>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<PalletPickPut> data = response.body();
                    if (data != null && data.size() > 0) {
                        pallet = data.get(0);
                        binding.setPallet(pallet);
                        updateTitle(pallet.OrderNumber);
                        setReturnResultTextColor();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void updateTitle(String title) {
        if (title.trim().length() > 0) {
            setTitle(new StringBuilder(getTitle()).append(" ").append(title));
        }
    }

    public void onMove(View view) {
        String locationBarcode = binding.etBarcodeLocation.getText().toString().trim();

        if (locationBarcode.length() == 0) {
            Toast.makeText(getApplicationContext(), "Bạn phải nhập Location", Toast.LENGTH_LONG).show();
            binding.etBarcodeLocation.requestFocus();
            Utilities.showKeyboard(getApplicationContext(), binding.etBarcodeLocation);
            return;
        }

        if (locationBarcode.contains("LO000")) {
            loadPickAndPutAwayLocationScan(locationBarcode);
        } else {
            loadPickAndPutAwayLocationScan("LO000" + locationBarcode);
        }
    }

    private void loadPickAndPutAwayLocationScan(String barcode) {
        if (pallet == null) {
            return;
        }
        final String oldLocation = pallet.LocationNumber;
        final PickAndPutAwayLocationScanParam param = new PickAndPutAwayLocationScanParam(barcode, pallet.PalletID, pallet.OrderID, pallet.OrderNumber, username, androidID, storeId);
        MyRetrofit.initRequest(this).loadPickAndPutAwayLocationScan(param).enqueue(new Callback<List<PalletPickPut>>() {
            @Override
            public void onResponse(Response<List<PalletPickPut>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<PalletPickPut> data = response.body();
                    if (data != null && data.size() > 0) {
                        pallet = data.get(0);
                        binding.setPallet(pallet);
                        setReturnResultTextColor();

                        updateMsgDone(oldLocation, pallet.LocationNumber, pallet.returnStatus);
                    } else {
                        tvMsg.setText(R.string.pick_put_update_failed);
                    }
                } else {
                    tvMsg.setText(R.string.pick_put_update_failed);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                tvMsg.setText(R.string.pick_put_update_failed);
            }
        });
    }

    private void updateMsgDone(String oldLocation, String newLocation, String returnStatus) {
        String msg = String.format(getString(R.string.pick_put_ab_hi), oldLocation, newLocation, returnStatus);
        int start1 = msg.indexOf(oldLocation);
        int start2 = msg.indexOf(newLocation);
        int start3 = msg.indexOf(returnStatus);

        SpannableString span = new SpannableString(msg);
        span.setSpan(new ForegroundColorSpan(Color.RED), start1, start1 + oldLocation.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.RED), start2, start2 + newLocation.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.RED), start3, start3 + returnStatus.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        Utilities.speakingSomeThingslow(returnStatus, PickPutDetailActivity.this);

        tvMsg.setText(span);
    }

    private void setReturnResultTextColor() {
        binding.tvResult.setTextColor(pallet.returnResult.equalsIgnoreCase("ok") ? 0xFF04FF00 : 0xFFFF0000);
    }


    @Override
    public void onData(String data) {
        super.onData(data);
        if (data.length() > 2) {
            String type = data.substring(0, 2);
            if (type.equalsIgnoreCase("LO")) {
//                loadPickAndPutAwayLocationScan(data);
                locationBuilder.append(data.substring(2));
                tvMsg.setText(R.string.pick_put_updating);
                loadPickAndPutAwayLocationScan("LO000" + locationBuilder.toString());
            } else if (type.equalsIgnoreCase("PI")) {
                loadPickAndPutAwayPalletScan(data);
            } else {
                String stringNumber = data.substring(2);
                if (type.equalsIgnoreCase("RK")) {
                    if (timeScanToCreateLocation == 0) {
                        locationBuilder.append(stringNumber);
                    } else {
                        locationBuilder = new StringBuilder();
                        locationBuilder.append(stringNumber);
                        timeScanToCreateLocation = 0;
                    }
                    tvMsg.setText(String.format(getString(R.string.pick_put_rk), stringNumber));
                    timeScanToCreateLocation++;
                } else if (type.equalsIgnoreCase("HI")) {
                    if (timeScanToCreateLocation == 1) {
                        locationBuilder.append(stringNumber);
                        tvMsg.setText(R.string.pick_put_updating);
                        loadPickAndPutAwayLocationScan("LO000" + locationBuilder.toString());

                        timeScanToCreateLocation = 0;
                        locationBuilder = new StringBuilder();
                    }
                }
            }
        }
    }
}
