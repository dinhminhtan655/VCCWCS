package com.wcs.vcc.main.newscanmasan.pickpackshippickinglist;

import java.util.UUID;

public class CheckTotalInfo {
    private Integer Qty;
    private Float NetWeight;
    private Integer TotalCarton;
    private  Integer PalletNumber;
    private UUID CustomerID;
    private UUID DispatchingOrderID;

    public Integer getQty() {
        return Qty;
    }

    public Float getNetWeight() {
        return NetWeight;
    }

    public Integer getTotalCarton() {
        return TotalCarton;
    }

    public Integer getPalletNumber() {
        return PalletNumber;
    }

    public UUID getCustomerID() {
        return CustomerID;
    }

    public UUID getDispatchingOrderID() {
        return DispatchingOrderID;
    }
}
