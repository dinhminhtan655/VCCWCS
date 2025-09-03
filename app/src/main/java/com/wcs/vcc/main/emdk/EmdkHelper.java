package com.wcs.vcc.main.emdk;

/**
 * Created by aang on 17/10/2017.
 */

public class EmdkHelper {
    public static boolean isEmdkAvailable() {
        return android.os.Build.MANUFACTURER.contains("Zebra Technologies") || android.os.Build.MANUFACTURER.contains("Motorola Solutions");
    }
}
