package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class UpdateBasketPackRequest {
    @SerializedName("date")
    String date;
    @SerializedName("customercode")
    String customerCode;
    @SerializedName("routeNo")
    String routeNo;
    @SerializedName("palletID")
    int palletId;
    @SerializedName("itemID")
    String itemID;
    @SerializedName("quantity")
    int quantity;

    public UpdateBasketPackRequest() {
    }

    public UpdateBasketPackRequest(String date, String customerCode, String routeNo, int palletId, String itemID, int quantity) {
        this.date = date;
        this.customerCode = customerCode;
        this.routeNo = routeNo;
        this.palletId = palletId;
        this.itemID = itemID;
        this.quantity = quantity;
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

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
