package com.wcs.vcc.api;

import java.util.UUID;

public class PickinglistCreateEDISavePackageHNParameter extends BaseParam {

    private UUID CustomerID;
    private String Pick_date;
    private String LocationNumber;

    public PickinglistCreateEDISavePackageHNParameter(String username, String deviceNumber, UUID CustomerID, String Pick_date, String LocationNumber) {
        super(username, deviceNumber);
        this.CustomerID = CustomerID;
        this.Pick_date = Pick_date;
        this.LocationNumber = LocationNumber;
    }


}
