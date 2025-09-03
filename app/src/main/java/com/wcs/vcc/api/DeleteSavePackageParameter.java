package com.wcs.vcc.api;

import java.util.UUID;

public class DeleteSavePackageParameter extends BaseParam {

    private int id;
    private UUID CustomerID;
    private String DONumber;
    private byte flag;

    public DeleteSavePackageParameter(String username, String deviceNumber, UUID CustomerID,int id, byte flag) {
        super(username, deviceNumber);
        this.CustomerID = CustomerID;
        this.id = id;
        this.flag = flag;
    }
}
