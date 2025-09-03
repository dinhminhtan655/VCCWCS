package com.wcs.vcc.main.doichieu.model;

import com.google.gson.annotations.SerializedName;

public class ShiftConfirmDetail {

    @SerializedName("id")
    public int id;
    @SerializedName("itemID")
    public String itemID;
    @SerializedName("itemName")
    public String itemName;
    @SerializedName("acctualSortQuantity")
    public int acctualSortQuantity;
    @SerializedName("qty_ConfirmDriver")
    public int qty_ConfirmDriver;

    public ShiftConfirmDetail(int id, int qty_ConfirmDriver) {
        this.id = id;
        this.qty_ConfirmDriver = qty_ConfirmDriver;
    }

    public int getQty_ConfirmDriver() {
        return qty_ConfirmDriver;
    }

    public void setQty_ConfirmDriver(int qty_ConfirmDriver) {
        this.qty_ConfirmDriver = qty_ConfirmDriver;
    }
}
