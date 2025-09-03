package com.wcs.vcc.main.containerandtruckinfor.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Door implements Serializable {

    @SerializedName("DockDoorID")
    public int dockDoorID;
    @SerializedName("DockNumber")
    public String dockNumber;
    @SerializedName("WarehouseDescription")
    public String warehouseDescription;

    @Override
    public String toString() {
        return dockNumber;
    }
}
