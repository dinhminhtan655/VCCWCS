package com.wcs.vcc.roomdb.models;

import com.google.gson.annotations.SerializedName;

public class PickPutOrderDetailRequest {
    @SerializedName("Username")
    public String userName;
    @SerializedName("StoreID")
    public int storeId;

    public PickPutOrderDetailRequest() {
    }

    public PickPutOrderDetailRequest(String userName, int storeId) {
        this.userName = userName;
        this.storeId = storeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
