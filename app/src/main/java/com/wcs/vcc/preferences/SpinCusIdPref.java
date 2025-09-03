package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SpinCusIdPref {

    public static final String SPIN_CUS_ID = "spin_cus_id";

    private static SharedPreferences preferences;

    public static void saveCusID(Context context, String value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SPIN_CUS_ID, value);
        editor.commit();
    }


    public static String LoadCusID(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getString(SPIN_CUS_ID, "");
    }
}
