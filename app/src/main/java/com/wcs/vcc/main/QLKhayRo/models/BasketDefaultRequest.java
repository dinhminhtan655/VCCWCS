package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketDefaultRequest {
    @SerializedName("customercode")
    String customerCode;

    public BasketDefaultRequest() {
    }

    public BasketDefaultRequest(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
