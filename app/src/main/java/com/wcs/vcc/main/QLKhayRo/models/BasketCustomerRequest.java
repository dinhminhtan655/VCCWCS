package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketCustomerRequest {
    @SerializedName("StoreID")
    int StoreID;

    public BasketCustomerRequest() {
    }

    public BasketCustomerRequest(int storeID) {
        StoreID = storeID;
    }

    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }
}
