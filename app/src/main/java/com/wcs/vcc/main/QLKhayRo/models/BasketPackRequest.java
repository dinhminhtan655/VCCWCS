package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketPackRequest {
    @SerializedName("date")
    String date;
    @SerializedName("customercode")
    String customerCode;
    @SerializedName("routeNo")
    String routeNo;
    @SerializedName("palletID")
    int palletId;

    public BasketPackRequest() {
    }

    public BasketPackRequest(String date, String customerCode, String routeNo, int palletId) {
        this.date = date;
        this.customerCode = customerCode;
        this.routeNo = routeNo;
        this.palletId = palletId;
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

    public int getPalletId() {
        return palletId;
    }

    public void setPalletId(int palletId) {
        this.palletId = palletId;
    }
}
