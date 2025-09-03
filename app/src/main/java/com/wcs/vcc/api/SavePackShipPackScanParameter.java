package com.wcs.vcc.api;

import java.util.UUID;

public class SavePackShipPackScanParameter extends BaseParam {

    private String ScanResult;
    private UUID CustomerID;

    public SavePackShipPackScanParameter(String username, String deviceNumber, String ScanResult, UUID CustomerID) {
        super(username, deviceNumber);
        this.ScanResult = ScanResult;
        this.CustomerID = CustomerID;
    }

}
