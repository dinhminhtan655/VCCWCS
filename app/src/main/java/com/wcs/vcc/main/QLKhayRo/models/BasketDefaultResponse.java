package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketDefaultResponse {
    @SerializedName("date")
    String date;
    @SerializedName("customerCode")
    String customerCode;
    @SerializedName("type")
    int type;
    @SerializedName("basketID")
    int basketID;
    @SerializedName("basketName")
    String basketName;

    public BasketDefaultResponse() {
    }

    public BasketDefaultResponse(String date, String customerCode, int type, int basketID, String basketName) {
        this.date = date;
        this.customerCode = customerCode;
        this.type = type;
        this.basketID = basketID;
        this.basketName = basketName;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBasketID() {
        return basketID;
    }

    public void setBasketID(int basketID) {
        this.basketID = basketID;
    }

    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }
}
