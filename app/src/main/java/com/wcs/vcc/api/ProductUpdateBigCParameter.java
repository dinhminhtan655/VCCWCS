package com.wcs.vcc.api;


public class ProductUpdateBigCParameter {
    private int salesOrderProductID;
    private float dispatchedGrossWeight;
    private int dispatchedActualCarton;
    private int basketID;
    private int basketQuantity;
    private String userName;

    public ProductUpdateBigCParameter(int salesOrderProductID, float dispatchedGrossWeight, int dispatchedActualCarton, int basketID, int basketQuantity, String userName) {
        this.salesOrderProductID = salesOrderProductID;
        this.dispatchedGrossWeight = dispatchedGrossWeight;
        this.dispatchedActualCarton = dispatchedActualCarton;
        this.basketID = basketID;
        this.basketQuantity = basketQuantity;
        this.userName = userName;
    }
}
