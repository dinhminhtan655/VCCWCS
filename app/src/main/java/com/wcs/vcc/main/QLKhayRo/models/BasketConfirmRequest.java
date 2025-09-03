package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketConfirmRequest {
    @SerializedName("date")
    String date;
    @SerializedName("customerCode")
    String customerCode;
    @SerializedName("routeNo")
    String routeNo;
    @SerializedName("loginName")
    String loginName;

    public BasketConfirmRequest() {
    }

    public BasketConfirmRequest(String date, String customerCode, String routeNo, String loginName) {
        this.date = date;
        this.customerCode = customerCode;
        this.routeNo = routeNo;
        this.loginName = loginName;
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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
