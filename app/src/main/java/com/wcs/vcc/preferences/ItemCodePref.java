package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ItemCodePref {

    private static final String PREF_ITEMCODE = "pref_itemcode";
    public static final String ITEMCODE = "itemcode";

    private static SharedPreferences preferences;

    public static void SaveItemCode(Context context, String value) {
        if(value.length()>0){
            if(value.length()==13){
                preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(ITEMCODE, value);
                editor.commit();
            }
            else if(value.length()==5){
                preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(ITEMCODE, value);
                editor.commit();
            }else if(value.length()==20){
                preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(ITEMCODE, value.substring(1,6));
                editor.commit();
            }else if(value.length()>20){
                String[] arr = value.split("/");
                String ItemCode = arr[arr.length-1];
                preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(ITEMCODE, ItemCode);
                editor.commit();
            }

        }

    }


    public static String LoadItemCode(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getString(ITEMCODE, "");
    }
}
