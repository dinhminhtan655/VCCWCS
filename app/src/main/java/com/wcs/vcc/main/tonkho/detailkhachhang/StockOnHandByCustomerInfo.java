package com.wcs.vcc.main.tonkho.detailkhachhang;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/8/2016.
 */
public class StockOnHandByCustomerInfo {
    @SerializedName("ProductNumber")
    private String ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("TotalCurrentCtns")
    private int TotalCurrentCtns;
    @SerializedName("TotalAfterDPCtns")
    private int TotalAfterDPCtns;
    @SerializedName("TotalWeight")
    private float TotalWeight;
    @SerializedName("TotalLocation")
    private int TotalLocation;
    @SerializedName("TotalPallet")
    private int TotalPallet;
    @SerializedName("TotalUnit")
    private int TotalUnit;
    @SerializedName("PickQuantity")
    private int TotalPicked;
    @SerializedName("HoldQuantity")
    private int TotalHold;
    private int TotalExported;
    public UUID ProductID;

    public String getProductName() {
        return ProductName;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public int getTotalAfterDPCtns() {
        return TotalAfterDPCtns;
    }

    public int getTotalCurrentCtns() {
        return TotalCurrentCtns;
    }

    public int getTotalLocation() {
        return TotalLocation;
    }

    public int getTotalPallet() {
        return TotalPallet;
    }

    public int getTotalUnit() {
        return TotalUnit;
    }

    public int getTotalExported() {
        TotalExported = getTotalCurrentCtns() - getTotalAfterDPCtns();
        return TotalExported < 0 ? 0 : TotalExported;
    }


    public int getTotalPicked() {
        return TotalPicked;
    }

    public int getTotalHold() {
        return TotalHold;
    }

    public float getTotalWeight() {
        return TotalWeight;
    }


}
