package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketCustomer {
    @SerializedName("Code")
    String Code;
    @SerializedName("Name")
    String Name;
    @SerializedName("Status")
    int Status;

    public BasketCustomer() {
    }

    public BasketCustomer(String code, String name, int status) {
        Code = code;
        Name = name;
        Status = status;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
