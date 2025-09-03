package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.utilities.Const;


public class SettingPref {
    private static final String PREF_SETTING = "pref_setting";
    private static final String PREF_CODE = "pref_code";
    private static final String PREF_SPINCUS = "pref_spincus";
    public static final String IP = "ip";
    public static final String POSITION = "position";
    public static final String ACCESS = "access";


    private static SharedPreferences preferences;

    public static final String CODE = "code";

    private SettingPref() {

    }

    public static void setInfoNetwork(Context context, String ip, int position) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(IP, ip);
        edit.putString(POSITION, Integer.toString(position));
        edit.apply();
    }



    public static void setAccessLocation(Context context, boolean isAccess) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(ACCESS, isAccess);
        edit.apply();
    }

    public static void setCodeItem(Context context, String strcode) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_CODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(CODE, strcode);
        edit.apply();
    }

    public static String[] getInfoNetwork(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        return new String[]{preferences.getString(IP, MyRetrofit.IP), preferences.getString(POSITION, "0")};
    }



    public static boolean getAccessLocation(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        return preferences.getBoolean(ACCESS, false);
    }

    public static String getCodeItem(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_CODE, Context.MODE_PRIVATE);
        return preferences.getString(CODE, "");
    }


    public static void removeCodeItem(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_CODE, Context.MODE_PRIVATE);
        settings.edit().remove(CODE).commit();
    }
}
