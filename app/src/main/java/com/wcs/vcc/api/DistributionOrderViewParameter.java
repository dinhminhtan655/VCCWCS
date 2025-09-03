package com.wcs.vcc.api;

public class DistributionOrderViewParameter {
    private String  varDate;
    private int StoreID;

    public DistributionOrderViewParameter(String varDate, int storeID) {
        this.varDate = varDate;
        StoreID = storeID;
    }
}
