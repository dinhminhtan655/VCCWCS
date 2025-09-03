package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DatePref {
    private static SharedPreferences preferences;
    private static final String ORDER_DATE = "ORDER_DATE";

    public static void SaveOrderDate(Context context, String value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ORDER_DATE, value);
        editor.apply();
    }
    public static String LoadOrderDate(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getString(ORDER_DATE, "");
    }
}
