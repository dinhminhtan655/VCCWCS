package com.wcs.vcc.main.chuyenhang;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/5/2016.
 */
public class ListLocationInfo {
    public UUID LocationID;
    @SerializedName("LocationCode")
    private int LocationCode;
    @SerializedName("LocationNumber")
    private String LocationNumber;
    @SerializedName("LocationNumberShort")
    private String LocationNumberShort;
    @SerializedName("Used")
    private int Used;

    public String getLocationNumber() {
        return LocationNumber;
    }

    public String getLocationNumberShort() {
        return LocationNumberShort;
    }

    public int getUsed() {
        return Used;
    }

    public int getLocationCode() {
        return LocationCode;
    }
}
