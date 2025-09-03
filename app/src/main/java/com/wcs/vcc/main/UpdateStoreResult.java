package com.wcs.vcc.main;


import com.google.gson.annotations.SerializedName;

public class UpdateStoreResult {
    @SerializedName("WarehouseID")
    private int warehousId;

    public int getWarehousId() {
        return warehousId;
    }
}
