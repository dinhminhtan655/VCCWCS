package com.wcs.vcc.mvvm.util.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.zxing.integration.android.IntentIntegrator;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;

public class Common {

    private static ConnectivityManager instance;

    private static ConnectivityManager getInstance(Context context) {
        if (instance == null) {
            synchronized (Common.class) {
                if (instance == null) {
                    instance = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
                }
            }
        }
        return instance;

    }

    public static boolean isConnectedToInternet(Context context) {
        if (getInstance(context) != null) {
            NetworkInfo[] info = getInstance(context).getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo n : info) {
                    if (n.getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static void moveToScan(Activity activity){
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setCaptureActivity(ScanCameraPortrait.class);
        integrator.initiateScan();
    }

}
