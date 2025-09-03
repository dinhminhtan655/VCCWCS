package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ShowDialogPref {

    private static SharedPreferences preferences;
    private static final String DIALOG_SHOW = "DIALOG_SHOW";

    public static void SaveDialogShow(Context context, boolean value) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DIALOG_SHOW, value);
        editor.apply();
    }

    public static Boolean LoadDialogShow(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getBoolean(DIALOG_SHOW, false);
    }

}
