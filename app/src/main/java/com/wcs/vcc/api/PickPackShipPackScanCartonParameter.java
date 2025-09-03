package com.wcs.vcc.api;

public class PickPackShipPackScanCartonParameter extends BaseParam {
    public String ScanResult;
    public String DispatchingOrderNumber;

    public PickPackShipPackScanCartonParameter(String username, String deviceNumber, String scanResult, String dispatchingOrderNumber) {
        super(username, deviceNumber);
        ScanResult = scanResult;
        DispatchingOrderNumber = dispatchingOrderNumber;
    }
}
