package com.wcs.vcc.api;

/**
 * Created by aang on 20/05/2018.
 */

public class OrderResultsSupervisorScanParam {
    public String ScanResult;
    public String UserName;
    public String DeviceNumber;
    public String StrDockNumber;

    public OrderResultsSupervisorScanParam(String scanResult, String userName, String deviceNumber, String dockNumberStr) {
        ScanResult = scanResult;
        UserName = userName;
        DeviceNumber = deviceNumber;
        StrDockNumber = dockNumberStr;
    }
}
