package com.wcs.vcc.api;


public class ProductsOfStoreParameter {
    private int purchasingOrderID;
    private int salesOrderID;

    public ProductsOfStoreParameter(int purchasingOrderID, int salesOrderID) {
        this.purchasingOrderID = purchasingOrderID;
        this.salesOrderID = salesOrderID;
    }
}
