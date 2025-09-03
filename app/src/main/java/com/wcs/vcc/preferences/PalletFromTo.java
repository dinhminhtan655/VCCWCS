package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PalletFromTo {
    public static final String PALLET_FROM = "pallet_from";
    public static final String PALLET_TO = "pallet_to";

    private static SharedPreferences preferences;

    public static void savePalletFrom(Context context, String value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PALLET_FROM, value);
        editor.commit();
    }


    public static String loadPalletFrom(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getString(PALLET_FROM, "");
    }
    public static void savePalletTo(Context context, String value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PALLET_TO, value);
        editor.commit();
    }


    public static String loadPalletTo(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getString(PALLET_TO, "");
    }

}
