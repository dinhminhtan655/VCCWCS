package com.wcs.vcc.main.phieuhomnay;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by Trần Xuân Lộc on 12/30/2015.
 */
public class InOutToDayUnfinishedInfo {
    @SerializedName("CustomerID")
    private UUID customerID;
    @SerializedName("CustomerNumber")
    private String customerNumber;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("OrderQty")
    private int orderQty;
    @SerializedName("ScannedOrderQty")
    private String ScannedOrderQty;
    @SerializedName("TotalWeight")
    private float TotalWeight;
    @SerializedName("TotalPackages")
    private int TotalPackages;

    public int getTotalPackages() {
        return TotalPackages;
    }

    public float getTotalWeight() {
        return TotalWeight;
    }

    public UUID getCustomerID() {
        return customerID;
    }


    public String getCustomerName() {
        return customerName;
    }


    public String getCustomerNumber() {
        return customerNumber;
    }


    public int getOrderQty() {
        return orderQty;
    }

    public String getScannedOrderQty() {
        return ScannedOrderQty;
    }
}
