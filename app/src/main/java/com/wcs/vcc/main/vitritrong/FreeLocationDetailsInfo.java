package com.wcs.vcc.main.vitritrong;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 7/28/2016.
 */
public class FreeLocationDetailsInfo {
    @SerializedName("QtyLow")
    private int QtyLow;
    @SerializedName("QtyHigh")
    private int QtyHigh;
    @SerializedName("QtyOfFree")
    private int QtyOfFree;
    @SerializedName("QtyFreeAfterDP")
    private int QtyFreeAfterDP;
    @SerializedName("QtyFree_Low")
    private int QtyFree_Low;
    @SerializedName("QtyStandards")
    private int QtyStandards;
    @SerializedName("QtyOfPallets_OnHand")
    private int QtyOfPallets_OnHand;
    @SerializedName("QtyFree_VeryLow")
    private int QtyFree_VeryLow;
    @SerializedName("QtyFree_VeryHigh")
    private int QtyFree_VeryHigh;
    @SerializedName("QtyFree_High")
    private int QtyFree_High;
    @SerializedName("QtyLocation")
    private int QtyLocation;
    @SerializedName("UpdateTime")
    private String UpdateTime;
    @SerializedName("RoomNumber")
    public String RoomNumber;
    @SerializedName("Aisle")
    public int Aisle;
    @SerializedName("QtyLocationOff")
    private int QtyLocationOff;


    public int getQtyFree_High() {
        return QtyFree_High;
    }

    public int getQtyFree_Low() {
        return QtyFree_Low;
    }

    public int getQtyFree_VeryHigh() {
        return QtyFree_VeryHigh;
    }

    public int getQtyFree_VeryLow() {
        return QtyFree_VeryLow;
    }

    public int getQtyFreeAfterDP() {
        return QtyFreeAfterDP;
    }

    public int getQtyLocation() {
        return QtyLocation;
    }

    public int getQtyOfFree() {
        return QtyOfFree;
    }

    public int getQtyOfPallets_OnHand() {
        return QtyOfPallets_OnHand;
    }

    public int getQtyBusy() {
        int totalBusy = QtyLocation - QtyOfFree;

        return totalBusy < 0 ? 0 : totalBusy;
    }

    public int getQtyLocationOff() {
        return QtyLocationOff != 0 ? QtyLocationOff : 0;
    }
}

