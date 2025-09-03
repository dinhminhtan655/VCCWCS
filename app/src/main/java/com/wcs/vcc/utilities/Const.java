package com.wcs.vcc.utilities;

import android.app.AlarmManager;

import java.util.ArrayList;


public class Const {
    public static final String EMPLOYEE_ID = "2";
    public static final int IMAGE_UPLOAD_WIDTH = 400;
    public static final int SAMPLE_SIZE = 4;
    public static int timeSchedule = 0;
    public static ArrayList<Integer> arrayListIDNotify = new ArrayList<>();
    public static String STORAGE_DIRECTORY = "WCS";
    public static boolean isActivating;
    public static int timePauseActive = 0;
    public static AlarmManager alarmManager;
    public static final String IP_LOCAL = "103.144.87.200:810";//810
    public static final String IP_GLOBAL = "103.144.87.200:810";

//    public static final String IP_LOCAL = "192.168.1.13:699";//810
//    public static final String IP_GLOBAL = "192.168.1.13:699";


//    For test app
//    public static final String IP_LOCAL = "XdockingPickingListOrderActivity";
//    public static final String IP_GLOBAL = "125.212.239.2:801";
}
