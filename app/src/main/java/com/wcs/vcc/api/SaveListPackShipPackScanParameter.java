package com.wcs.vcc.api;

import java.util.UUID;

public class SaveListPackShipPackScanParameter extends BaseParam {

    private UUID CustomerID;
    private String Pick_date;


    public SaveListPackShipPackScanParameter(String username, String deviceNumber, UUID CustomerID, String Pick_date) {
        super(username, deviceNumber);
        this.CustomerID = CustomerID;
        this.Pick_date = Pick_date;
    }


}
