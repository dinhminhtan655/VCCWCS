package com.wcs.vcc.main.scanbhx.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SealGroup implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("palletID")
    public String palletID;
    @SerializedName("sealName")
    public String sealName;
    @SerializedName("deliveryDate")
    public String deliveryDate;
    @SerializedName("scanDate")
    public String scanDate;
    @SerializedName("usernameScan")
    public String usernameScan;

}
