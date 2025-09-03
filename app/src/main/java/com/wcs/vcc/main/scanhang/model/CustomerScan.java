package com.wcs.vcc.main.scanhang.model;

import com.google.gson.annotations.SerializedName;

public class CustomerScan {

    @SerializedName("id")
    public int id;
    @SerializedName("customerCode")
    public String customerCode;
    @SerializedName("customerName")
    public String customerName;


    @Override
    public String toString() {
        return customerCode;
    }
}
