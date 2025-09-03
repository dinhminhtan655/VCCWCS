package com.wcs.vcc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.UUID;

/**
 * Created by Trần Xuân Lộc on 12/26/2015.
 */
public class LoginPref {
    private static final String PREF_LOGIN = "pref_login";
    public static final String USERNAME = "username";
    public static final String RING_DEVICE = "ringscanner_name";
    public static final String PRINTER = "PRINTER_NAME";
    public static final String POSITION_GROUP = "position_group";
    public static final String REAL_NAME = "real_name";
    public static final String WAREHOUSE_ID = "warehouse_id";
    public static final String AUTO_SIGN_OUT = "auto_sign_out";
    public static final String STORE_ID = "store_id";
    public static final String EMPLOYEE_ID = "employee_id";

    private static SharedPreferences preferences;

    private LoginPref() {
    }

    public static void putInfoUser(Context context, String username, String position_group, String real_name, int warehouseID, boolean isAuto, int storeId, UUID employeeID,String device) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(USERNAME, username);
        edit.putString(POSITION_GROUP, position_group);
        edit.putString(REAL_NAME, real_name);
        edit.putString(RING_DEVICE, device);
        edit.putInt(WAREHOUSE_ID, warehouseID);
        edit.putBoolean(AUTO_SIGN_OUT, isAuto);
        edit.putInt(STORE_ID, storeId);
        edit.putString(EMPLOYEE_ID, employeeID.toString());
        edit.apply();
    }

    public static void resetInfoUserByUser(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(USERNAME, "-1");
        edit.putString(POSITION_GROUP, "-1");
        edit.putString(REAL_NAME, "-1");
        edit.putInt(WAREHOUSE_ID, -1);
        edit.putBoolean(AUTO_SIGN_OUT, true);
        edit.apply();
        SpinCusPref.remove(context);
    }

    public static void putStoreId(Context context, int storeId) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(STORE_ID, storeId);
        edit.apply();
    }

    public static void putWarehouseId(Context context, int warehouseId) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(WAREHOUSE_ID, warehouseId);
        edit.apply();
    }


    public static String getInfoUser(Context context, String key) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getString(key, "-1");
    }

    public static String getUsername(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getString(USERNAME, "-1");
    }


    public static String getPrinter(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getString(PRINTER, "-1");
    }

    public  static void setPrinter(Context context,String printerName){
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PRINTER, printerName);
        editor.commit();
    }


    public static String getRingDeviceName(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getString(RING_DEVICE, "-1");
    }

    public static String getPositionGroup(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getString(POSITION_GROUP, "-1");
    }

    public static String getRealName(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getString(REAL_NAME, "-1");
    }

    public static boolean isAutoSignOut(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getBoolean(AUTO_SIGN_OUT, true);
    }


    public static int getWarehouseID(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getInt(WAREHOUSE_ID, 0);
    }

    public static int getStoreId(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getInt(STORE_ID, 0);
    }

    public static String getEmployeeId(Context context) {
        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        return preferences.getString(EMPLOYEE_ID, "");
    }

}