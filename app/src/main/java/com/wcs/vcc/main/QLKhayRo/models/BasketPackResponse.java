package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketPackResponse {
    @SerializedName("date")
    String date;
    @SerializedName("customerCode")
    String customerCode;
    @SerializedName("routeNo")
    String routeNo;
    @SerializedName("palletID")
    int palletId;
    @SerializedName("itemID")
    String itemID;
    @SerializedName("itemName")
    String itemName;
    @SerializedName("quantity")
    int quantity;

    public BasketPackResponse() {
    }

    public BasketPackResponse(String date, String customerCode, String routeNo, int palletId, String itemID, String itemName, int quantity) {
        this.date = date;
        this.customerCode = customerCode;
        this.routeNo = routeNo;
        this.palletId = palletId;
        this.itemID = itemID;
        this.itemName = itemName;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
