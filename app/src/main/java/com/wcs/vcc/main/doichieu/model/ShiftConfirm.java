package com.wcs.vcc.main.doichieu.model;

import com.google.gson.annotations.SerializedName;

public class ShiftConfirm {

    @SerializedName("atM_OrderreleaseID")
    public String atM_OrderreleaseID;
    @SerializedName("routeNo")
    public String routeNo;
    @SerializedName("palletID")
    public int palletID;
    @SerializedName("shipToName")
    public String shipToName;
    @SerializedName("deliveryDate")
    public String deliveryDate;
    @SerializedName("customerCode")
    public String customerCode;
    @SerializedName("box_ConfirmDriver")
    public int box_ConfirmDriver;
    @SerializedName("qty_ConfirmDriver")
    public int qty_ConfirmDriver;


    public ShiftConfirm(String atM_OrderreleaseID, String routeNo, int palletID, String shipToName, String deliveryDate, String customerCode, int box_ConfirmDriver, int qty_ConfirmDriver) {
        this.atM_OrderreleaseID = atM_OrderreleaseID;
        this.routeNo = routeNo;
        this.palletID = palletID;
        this.shipToName = shipToName;
        this.deliveryDate = deliveryDate;
        this.customerCode = customerCode;
        this.box_ConfirmDriver = box_ConfirmDriver;
        this.qty_ConfirmDriver = qty_ConfirmDriver;
    }

    public int getBox_ConfirmDriver() {
        return box_ConfirmDriver;
    }

    public void setBox_ConfirmDriver(int box_ConfirmDriver) {
        this.box_ConfirmDriver = box_ConfirmDriver;
    }

    public int getTongBich() {
        return qty_ConfirmDriver;
    }

    public void setTongBich(int tongBich) {
        this.qty_ConfirmDriver = tongBich;
    }
}
