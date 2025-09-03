package com.wcs.vcc.api;


public class StockMovementParameter {
    private String LocationBarcode;
    private String UserName;
    private int storeId;


    public StockMovementParameter(String locationBarcode, String userName, int storeId) {
        LocationBarcode = locationBarcode;
        UserName = userName;
        this.storeId = storeId;
    }
}

