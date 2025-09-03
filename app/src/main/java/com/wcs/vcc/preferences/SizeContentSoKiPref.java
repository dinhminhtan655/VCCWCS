package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SizeContentSoKiPref {

    public static final String SIZE_WIDTH = "size_width";
    public static final String SIZE_HEIGHT = "size_height";
    public static final String SIZE_TEXT = "size_text";

    private static SharedPreferences preferences;


    public static void saveSizeWidth(Context context, int value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SIZE_WIDTH, value);
        editor.commit();
    }


    public static int loadSizeWidth(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getInt(SIZE_WIDTH, 540);
    }

    public static void saveSizeHeight(Context context, int value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SIZE_HEIGHT, value);
        editor.commit();
    }


    public static int loadSizeHeight(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getInt(SIZE_HEIGHT, 380);
    }


    public static void saveSizeText(Context context, int value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SIZE_TEXT, value);
        editor.commit();
    }


    public static int loadSizeText(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getInt(SIZE_TEXT , 18);
    }



}
