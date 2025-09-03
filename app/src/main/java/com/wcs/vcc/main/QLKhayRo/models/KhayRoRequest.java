package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class KhayRoRequest {
    @SerializedName("date")
    String date;
    @SerializedName("customerCode")
    String customerCode;
    @SerializedName("routeNo")
    String routeNo;
    @SerializedName("palletID")
    String palletId;
    @SerializedName("type")
    int type;

    public KhayRoRequest() {
    }

    public KhayRoRequest(String date, String customerCode, String routeNo, String palletId, int type) {
        this.date = date;
        this.customerCode = customerCode;
        this.routeNo = routeNo;
        this.palletId = palletId;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getPalletId() {
        return palletId;
    }

    public void setPalletId(String palletId) {
        this.palletId = palletId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
