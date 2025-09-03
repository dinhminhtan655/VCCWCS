package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SpinCusPref {

    private static final String PREF_SPINCUS = "pref_spincus";
    public static final String SPIN_CUS = "spin_cus";

    private static SharedPreferences preferences;


    public static void SaveInt(Context context, int value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SPIN_CUS, value);
        editor.apply();
    }
    public static int LoadInt(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getInt(SPIN_CUS, 0);
    }

    public static void remove(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(SPIN_CUS);
        editor.apply();
    }
}
