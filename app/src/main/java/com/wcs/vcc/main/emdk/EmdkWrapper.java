package com.wcs.vcc.main.emdk;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerInfo;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by aang on 17/10/2017.
 */

public class EmdkWrapper implements LifecycleObserver, EMDKManager.EMDKListener, Scanner.DataListener, Scanner.StatusListener {

    private Context context;
    private Lifecycle lifecycle;
    private ScanListener scanListener;
    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private Scanner scanner;

    public EmdkWrapper(Context context, Lifecycle lifecycle, ScanListener scanListener) {
        this.context = context.getApplicationContext();
        this.lifecycle = lifecycle;
        this.scanListener = scanListener;
        lifecycle.addObserver(this);
    }

    public void onCreate() {
        Timber.d("OnCreate");
        EMDKResults results = EMDKManager.getEMDKManager(context, this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS)
            Toast.makeText(context, "Không thể kết nối với bộ phận scan trong máy", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        this.emdkManager = emdkManager;
        barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);

        createScanner();
        configScanner();
    }

    private void createScanner() {
        if (scanner == null) {
            List<ScannerInfo> scannerList = barcodeManager.getSupportedDevicesInfo();
            for (ScannerInfo scannerInfo : scannerList) {
                Timber.i("FriendlyName: " + scannerInfo.getFriendlyName() + '\n' +
                        "ModelNumber: " + scannerInfo.getModelNumber() + '\n' +
                        "ConnectionType: " + scannerInfo.getConnectionType() + '\n' +
                        "DecoderType: " + scannerInfo.getDecoderType() + '\n' +
                        "DeviceType: " + scannerInfo.getDeviceType() + '\n' +
                        "IsConnected: " + scannerInfo.isConnected() + '\n' +
                        "IdDefaultScanner: " + scannerInfo.isDefaultScanner());
            }
            int scannerCount = scannerList.size();
            if (scannerCount > 1) {
                scanner = barcodeManager.getDevice(scannerList.get(1));
            } else {
                Toast.makeText(context, "Không lấy được thông tin Scanner", Toast.LENGTH_SHORT).show();
            }

            if (scanner != null) {
                scanner.addDataListener(this);
                scanner.addStatusListener(this);
                try {
                    scanner.enable();
                } catch (ScannerException e) {
                    Timber.e(e);
                }
            }
        }
    }

    private void configScanner() {
        if (scanner != null)
            try {
                ScannerConfig config = scanner.getConfig();
                config.decoderParams.ean8.enabled = true;
                config.decoderParams.ean13.enabled = true;
                config.decoderParams.code39.enabled = true;
                config.decoderParams.code128.enabled = true;
                scanner.setConfig(config);

                scanner.triggerType = Scanner.TriggerType.HARD;
            } catch (ScannerException e) {
                Timber.e(e);
            }
    }


    @Override
    public void onClosed() {
        releaseScanner();
        releaseEmdk();
    }

    private void releaseScanner() {
        if (scanner != null) {
            try {
                scanner.cancelRead();
                scanner.disable();
            } catch (ScannerException e) {
                Timber.e(e);
            }
            scanner.removeDataListener(this);
            scanner.removeStatusListener(this);
            try {
                scanner.release();
            } catch (ScannerException e) {
                Timber.e(e);
            }
            scanner = null;
        }
    }

    private void releaseEmdk() {
        if (barcodeManager != null) {
            barcodeManager = null;
        }
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            ArrayList<ScanDataCollection.ScanData> scanData = scanDataCollection.getScanData();
            for (ScanDataCollection.ScanData data : scanData) {
                String barcode = data.getData();
                scanListener.onData(barcode);
                Timber.d("Emdk data: " + barcode);
            }
        }
    }

    @Override
    public void onStatus(StatusData statusData) {
        StatusData.ScannerStates state = statusData.getState();
        switch (state) {
            case IDLE:
                try {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Timber.e(e);
                    }
                    scanner.read();
                } catch (ScannerException e) {
                    Timber.e(e);
                }
        }
    }


    public void onStop() {
        releaseLifecycleObserver();
        releaseScanner();
        releaseEmdk();
    }

    private void releaseLifecycleObserver() {
        if (lifecycle != null) {
            lifecycle.removeObserver(this);
            lifecycle = null;
        }
    }
}
