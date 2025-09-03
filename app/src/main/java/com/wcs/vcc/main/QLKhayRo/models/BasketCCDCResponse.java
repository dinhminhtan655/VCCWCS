package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketCCDCResponse {
    @SerializedName("date")
    String date;
    @SerializedName("customerCode")
    String customerCode;
    @SerializedName("routeNo")
    String routeNo;
    @SerializedName("basketID")
    int basketId;
    @SerializedName("basketName")
    String basketName;
    @SerializedName("quantity")
    int quantity;
    @SerializedName("quantityConfirmReceived")
    int quantityConfirmReceived;

    public BasketCCDCResponse() {
    }

    public BasketCCDCResponse(String date, String customerCode, String routeNo, int basketId, String basketName, int quantity, int quantityConfirmReceived) {
        this.date = date;
        this.customerCode = customerCode;
        this.routeNo = routeNo;
        this.basketId = basketId;
        this.basketName = basketName;
        this.quantity = quantity;
        this.quantityConfirmReceived = quantityConfirmReceived;
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

    public int getBasketId() {
        return basketId;
    }

    public void setBasketId(int basketId) {
        this.basketId = basketId;
    }

    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityConfirmReceived() {
        return quantityConfirmReceived;
    }

    public void setQuantityConfirmReceived(int quantityConfirmReceived) {
        this.quantityConfirmReceived = quantityConfirmReceived;
    }
}
