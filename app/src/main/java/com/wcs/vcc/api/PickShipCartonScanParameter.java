package com.wcs.vcc.api;

public class PickShipCartonScanParameter {
    private String UserName;
    private String DeviceNumber;
    private String TripDetailCartonNumber;
    private String ScanResult;

    public PickShipCartonScanParameter(String userName, String deviceNumber, String cartonNumber, String scanResult) {
        UserName = userName;
        DeviceNumber = deviceNumber;
        TripDetailCartonNumber = cartonNumber;
        ScanResult = scanResult;
    }
}
