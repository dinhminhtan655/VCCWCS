package com.wcs.vcc.api.xdoc.response;

import com.google.gson.annotations.SerializedName;

public class CheckStatusPickPackResponse {
    @SerializedName("StoreNumber")
    public   Integer StoreNumber;
    @SerializedName("SHIP_TO_LOCATION")
    public  String SHIP_TO_LOCATION;
    @SerializedName("ProductNumber")
    public  String ProductNumber;
    @SerializedName("ProductName")
    public  String ProductName;
    @SerializedName("TotalPackages")
    public  String TotalPackages;
    @SerializedName("QtyMiss")
    public  String QtyMiss;



}
