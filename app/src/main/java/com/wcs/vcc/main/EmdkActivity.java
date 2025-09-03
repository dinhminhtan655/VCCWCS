package com.wcs.vcc.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.wcs.R;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.main.emdk.EmdkHelper;
import com.wcs.vcc.main.emdk.EmdkWrapper;
import com.wcs.vcc.main.emdk.ScanListener;

/**
 * Created by aang on 18/10/2017.
 */

public class EmdkActivity extends BaseActivity implements ScanListener {

    private EditText etTargetScan;
    private TextView tvPrevBarcode;

    private boolean isSetupScan;
    public boolean isClickedFromCamera;
    public View btCameraScan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EmdkHelper.isEmdkAvailable()) {
            new EmdkWrapper(this, getLifecycle(), this);
        }
    }



    public void setUpScan() {
        isSetupScan = true;

        etTargetScan = findViewById(R.id.et_target_scan);
        etTargetScan.addTextChangedListener(new TextWatcher() {
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
                    onData(contents.replaceAll("\n", "").trim());
                }
            }
        });
        tvPrevBarcode = findViewById(R.id.tv_prev_barcode);

        btCameraScan = findViewById(R.id.ivCameraScan);
        btCameraScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(EmdkActivity.this);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
                integrator.setCaptureActivity(ScanCameraPortrait.class);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE && isSetupScan) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String data = result.getContents();
                onData(data.trim());
                isClickedFromCamera = true;
            } else {
                isClickedFromCamera = false;
            }
        }
    }

    /**
     * Dữ liệu trả về khi scan barcode
     *
     * @param data Kết quả scan
     */
    @Override
    public void onData(String data) {
        stopLogoutService();
        if (etTargetScan != null) {
            etTargetScan.getText().clear();
            etTargetScan.requestFocus();
        }
        if (tvPrevBarcode != null) {
            tvPrevBarcode.setText(data);
        }
    }


}
