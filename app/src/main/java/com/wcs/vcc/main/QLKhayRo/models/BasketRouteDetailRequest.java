package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketRouteDetailRequest {
    @SerializedName("date")
    String date;
    @SerializedName("customercode")
    String customerCode;
    @SerializedName("routeNo")
    String routeNo;
    @SerializedName("palletfrom")
    String palletFrom;
    @SerializedName("palletTo")
    String palletTo;
    @SerializedName("basketID")
    int basketId;

    public BasketRouteDetailRequest() {
    }

    public BasketRouteDetailRequest(String date, String customerCode, String routeNo, String palletFrom, String palletTo, int basketId) {
        this.date = date;
        this.customerCode = customerCode;
        this.routeNo = routeNo;
        this.palletFrom = palletFrom;
        this.palletTo = palletTo;
        this.basketId = basketId;
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

    public String getPalletFrom() {
        return palletFrom;
    }

    public void setPalletFrom(String palletFrom) {
        this.palletFrom = palletFrom;
    }

    public String getPalletTo() {
        return palletTo;
    }

    public void setPalletTo(String palletTo) {
        this.palletTo = palletTo;
    }

    public int getBasketId() {
        return basketId;
    }

    public void setBasketId(int basketId) {
        this.basketId = basketId;
    }
}
