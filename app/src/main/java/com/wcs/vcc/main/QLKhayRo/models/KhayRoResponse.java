package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class KhayRoResponse {
    @SerializedName("date")
    String date;
    @SerializedName("customerCode")
    String customerCode;
    @SerializedName("routeNo")
    String routeNo;
    @SerializedName("palletID")
    int palletId;
    @SerializedName("type")
    int type;
    @SerializedName("basketID")
    int basketId;
    @SerializedName("basketName")
    String basketName;
    @SerializedName("quantity")
    String quantity;
    @SerializedName("quantityConfirmReceived")
    String quantityConfirmReceived;

    public KhayRoResponse() {
    }

    public KhayRoResponse(String date, String customerCode, String routeNo, int palletId, int type, int basketId, String basketName, String quantity, String quantityConfirmReceived) {
        this.date = date;
        this.customerCode = customerCode;
        this.routeNo = routeNo;
        this.palletId = palletId;
        this.type = type;
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

    public int getPalletId() {
        return palletId;
    }

    public void setPalletId(int palletId) {
        this.palletId = palletId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityConfirmReceived() {
        return quantityConfirmReceived;
    }

    public void setQuantityConfirmReceived(String quantityConfirmReceived) {
        this.quantityConfirmReceived = quantityConfirmReceived;
    }
}
