package com.wcs.vcc.api;

/**
 * Created by aang on 20/05/2018.
 */

public class OrderResultsSupervisorScanViewParam {
    public String UserName;
    public String DeviceNumber;
    public Byte Flag;
    public int StoreID;
    public String ScanResult;//buu add

    public OrderResultsSupervisorScanViewParam(String userName, String deviceNumber, Byte flag, int storeID, String scanResult) {
        UserName = userName;
        DeviceNumber = deviceNumber;
        Flag = flag;
        StoreID = storeID;
        ScanResult = scanResult;//buu
    }
}
