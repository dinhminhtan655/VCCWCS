package com.wcs.vcc.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.main.emdk.EmdkHelper;
import com.wcs.vcc.main.emdk.EmdkWrapper;
import com.wcs.vcc.main.emdk.ScanListener;
import com.wcs.wcs.R;

public class Emdk2Activity extends BaseActivity implements ScanListener {

    private EditText etTargetScan;
    public boolean isClickedFromCamera;
    private boolean isSetupScan;
    public View btCameraScan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EmdkHelper.isEmdkAvailable()){
            new EmdkWrapper(this,getLifecycle(),this);
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

        btCameraScan = findViewById(R.id.ivCameraScan);
        btCameraScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(Emdk2Activity.this);
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
        super.onActivityResult(requestCode, resultCode, intent);
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

    }
}
