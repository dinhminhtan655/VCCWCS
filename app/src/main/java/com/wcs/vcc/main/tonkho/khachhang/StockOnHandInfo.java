package com.wcs.vcc.main.tonkho.khachhang;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/8/2016.
 */
public class StockOnHandInfo {
    @SerializedName("CustomerNumber")
    private String CustomerNumber;
    @SerializedName("CustomerName")
    private String CustomerName;
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
    public UUID CustomerID;
    private int TotalExported;
    @SerializedName("PickQuantity")
    private int TotalPicked;
    @SerializedName("HoldQuantity")
    private int TotalHold;

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
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

    public int getTotalExported() {
        TotalExported = getTotalCurrentCtns() - TotalAfterDPCtns;
        return TotalExported < 0 ? 0 : TotalExported;
    }

    public int getTotalHold() {
        return TotalHold;
    }

    public float getTotalWeight() {
        return TotalWeight;
    }

    public int getTotalPicked(){
        return TotalPicked;
    }

}
