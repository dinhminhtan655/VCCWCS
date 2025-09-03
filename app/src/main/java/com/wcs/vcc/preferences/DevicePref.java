package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DevicePref {

    public static final String ITEMDEVICE = "itemdevice";

    private static SharedPreferences preferences;

    public static void SaveItemDevice(Context context, boolean value) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ITEMDEVICE, value);
        editor.commit();
    }


    public static boolean LoadItemDevice(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getBoolean(ITEMDEVICE, false);
    }

}
