package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SpinCusPrefVin {

    private static final String PREF_SPINCUS = "pref_spincus_vin";
    public static final String SPIN_CUS = "spin_cus_vin";
    public static final String SPIN_PRO = "spin_pro";
    public static final String SPIN_RO = "spin_ro";

    private static SharedPreferences preferences;



    public static void SaveIntVin(Context context, int value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SPIN_CUS, value);
        editor.commit();
    }

    public static int LoadInt(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getInt(SPIN_CUS, 0);
    }


    public static void SaveIntProduct(Context context, int value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SPIN_PRO, value);
        editor.commit();
    }

    public static int LoadIntProduct(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getInt(SPIN_PRO, 0);
    }

    public static void SaveIntRO(Context context, int value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SPIN_RO, value);
        editor.commit();
    }

    public static int LoadIntRO(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getInt(SPIN_RO, 0);
    }

}
