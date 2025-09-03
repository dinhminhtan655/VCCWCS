package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RadiLenHaHangPref {

    public static final String ORDER_RADI = "order_radi";

    private static SharedPreferences preferences;

    public static void saveOrderRadi(Context context, int value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ORDER_RADI, value);
        editor.commit();
    }


    public static int loadOrderRadi(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getInt(ORDER_RADI, 1);
    }
}
