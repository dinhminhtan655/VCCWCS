package com.wcs.vcc.api;

public class MassanPackage extends BaseParam {
    private String ScanResult;
    private String DispatchingProductCartonID;
    public MassanPackage(String username, String deviceNumber, String ScanResult, String DispatchingProductCartonID ) {
        super(username, deviceNumber);
        this.ScanResult = ScanResult;
        this.DispatchingProductCartonID = DispatchingProductCartonID;
    }
}
