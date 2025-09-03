package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketRouteRequest {
    @SerializedName("customercode")
    String customerCode;
    @SerializedName("date")
    String date;

    public BasketRouteRequest() {
    }

    public BasketRouteRequest(String customerCode, String date) {
        this.customerCode = customerCode;
        this.date = date;
    }



    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
