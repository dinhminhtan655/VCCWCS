package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketCCDCRequest {
    @SerializedName("date")
    String date;
    @SerializedName("customercode")
    String customerCode;
    @SerializedName("routeno")
    String routeNo;

    public BasketCCDCRequest() {
    }

    public BasketCCDCRequest(String date, String customerCode, String routeNo) {
        this.date = date;
        this.customerCode = customerCode;
        this.routeNo = routeNo;
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
}
