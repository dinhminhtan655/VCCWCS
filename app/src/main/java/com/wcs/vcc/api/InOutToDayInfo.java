package com.wcs.vcc.api;


import java.util.UUID;

public class InOutToDayInfo {
    private UUID CustomerID;
    private String varDate;
    private String OrderType;
    private int StoreID;

    public InOutToDayInfo(UUID customerID, String varDate, String OrderType, int StoreID) {
        CustomerID = customerID;
        this.varDate = varDate;
        this.OrderType = OrderType;
        this.StoreID = StoreID;
    }
}
