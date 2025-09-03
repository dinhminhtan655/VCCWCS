package com.wcs.vcc.main;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.wcs.R;import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.main.emdk.EmdkHelper;
import com.wcs.vcc.main.emdk.EmdkWrapper;
import com.wcs.vcc.main.emdk.ScanListener;
import com.wcs.vcc.preferences.DevicePref;
import com.wcs.vcc.preferences.LoginPref;

import java.util.Iterator;
import java.util.Set;

import jp.co.opto.opnsdk.BluetoothServiceState;
import jp.co.opto.opnsdk.Opn2002BluetoothService;
import jp.co.opto.opnsdk.observer.IBluetoothObserver;


public class RingScanActivity extends BaseActivity implements IBluetoothObserver, ScanListener {

    private TextView tvPrevBarcode;
    private EditText etTargetScan;
    private boolean isSetupScan;
    public boolean isClickedFromCamera;
    public View btCameraScan;
    public static Opn2002BluetoothService mBTService;
    TextToSpeech txtSpeech;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EmdkHelper.isEmdkAvailable()) {
            new EmdkWrapper(this, getLifecycle(), this);
        }

        mBTService = Opn2002BluetoothService.getInstance(this);

        if (mBTService != null) {
            mBTService.addObserver(this);
        }

    }

    public void setUpScan() {
        isSetupScan = true;

        etTargetScan = findViewById(R.id.et_target_scan);
//        etTargetScan.setMovementMethod(ScrollingMovementMethod.getInstance());
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
        try {
            btCameraScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator integrator = new IntentIntegrator(RingScanActivity.this);
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setCaptureActivity(ScanCameraPortrait.class);
                    integrator.initiateScan();
                }
            });
        } catch (Exception e) {
            Log.e("scan", e.getMessage());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();


        if (mBTService != null) {
            if (!mBTService.isEnabled()) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return;
            } else {
                if (!DevicePref.LoadItemDevice(RingScanActivity.this)) {
                    innitBlutoothService();
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
            if (mBTService.getState() == BluetoothServiceState.none) {
                mBTService.start();
            }
        }

        mBTService.showBluetoothSetting(this);

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

    @Override
    public void onData(String data) {
        if (etTargetScan != null) {
            etTargetScan.setText("");
            //etTargetScan.requestFocus();
        }
        if (tvPrevBarcode != null) {
            tvPrevBarcode.setText(data);
        }
    }

    @Override
    public void connected(BluetoothDevice bluetoothDevice) {
        Toast.makeText(this, "Đã nhận kết nối", Toast.LENGTH_LONG).show();
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        textToSpeech("Đã nhận kết nối");
        mBTService.enableAckNak();
    }

    @Override
    public void connectFailed() {
        Toast.makeText(this, "Không thể kết nối!", Toast.LENGTH_SHORT).show();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        textToSpeech("Không thể kết nối!");

    }

    @Override
    public void connectionLost() {
        Toast.makeText(this, "Mất kết nối!", Toast.LENGTH_SHORT).show();
//        textToSpeech("Mất kết nối!");
    }


    private void innitBlutoothService() {


        if (mBTService.getState() != BluetoothServiceState.connected) {
            Set<BluetoothDevice> set = mBTService.getPairedDevices();
            if (set.size() > 0) {
                Iterator<BluetoothDevice> it = set.iterator();
                while (it.hasNext()) {
                    BluetoothDevice device = it.next();
                    Log.e("sq", LoginPref.getRingDeviceName(RingScanActivity.this));
                    if ((device.getName() != null && device.getName().indexOf("ABA" + LoginPref.getUsername(RingScanActivity.this).toUpperCase()) >= 0
                            || device.getName().indexOf(LoginPref.getRingDeviceName(RingScanActivity.this).toUpperCase()) >= 0)
                    ) {
                        mBTService.connect(device, false);
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                        if (mBTService.getState() == BluetoothServiceState.connected)
                            break;
                    }
                }
                if (mBTService.getState() != BluetoothServiceState.connected)
                    mBTService.start();
            }

        }
    }

    @Override
    public void receive(String data) {
        if (data != null && data != null) {
            data = data.replace((char) 0x0D, (char) 0x0A);
            data = data.replaceAll("" + (char) 0x6, "");
            data = data.replaceAll("" + (char) 0x15, "");
        }
        onData(data.trim());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBTService != null) {
            mBTService.addObserver(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBTService != null) {
            mBTService.removeObserver(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void textToSpeech(final String text) {
        txtSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                txtSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    //Quan ly temp tu dong
    //Result 0: Tem bthuong => PA01,PA02....
    //Result 1: Tem quan ly khay ro tu dong => PA2212ID123123PRG132
    public int ValidateTypeLabel(String data){
        if(data.contains("PA")&&data.contains("ID")&&data.contains("PGR")){
            Log.d("DUONG", "1");
            return 1;
        }else
        {
            Log.d("DUONG", "0");
            return 0;
        }

    }


}
