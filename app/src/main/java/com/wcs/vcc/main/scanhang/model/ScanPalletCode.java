package com.wcs.vcc.main.scanhang.model;

import com.google.gson.annotations.SerializedName;

public class ScanPalletCode {

    @SerializedName("id")
    public int id;
    @SerializedName("palletId")
    public int palletId;
    @SerializedName("palletCode")
    public String palletCode;
    @SerializedName("deliveryDate")
    public String deliveryDate;
    @SerializedName("username")
    public String username;
    @SerializedName("atTime")
    public String atTime;
    @SerializedName("isActive")
    public boolean isActive;
    @SerializedName("groupSorting")
    public int groupSorting;
    @SerializedName("customerCode")
    public String customerCode;
    @SerializedName("region")
    public String region;
    @SerializedName("usernameInActive")
    public String usernameInActive;
    @SerializedName("inActiveAt")
    public String inActiveAt;
    @SerializedName("flag")
    public String flag;

    public ScanPalletCode(int palletId, String palletCode, String deliveryDate, String username, boolean isActive, int groupSorting, String customerCode, String region, String flag) {
        this.palletId = palletId;
        this.palletCode = palletCode;
        this.deliveryDate = deliveryDate;
        this.username = username;
        this.isActive = isActive;
        this.groupSorting = groupSorting;
        this.customerCode = customerCode;
        this.region = region;
        this.flag = flag;
    }

    public ScanPalletCode(int id, int palletId, String palletCode, String deliveryDate, String username, String atTime, boolean isActive, int groupSorting, String customerCode, String region, String usernameInActive, String inActiveAt, String flag) {
        this.id = id;
        this.palletId = palletId;
        this.palletCode = palletCode;
        this.deliveryDate = deliveryDate;
        this.username = username;
        this.atTime = atTime;
        this.isActive = isActive;
        this.groupSorting = groupSorting;
        this.customerCode = customerCode;
        this.region = region;
        this.usernameInActive = usernameInActive;
        this.inActiveAt = inActiveAt;
        this.flag = flag;
    }
}
