package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class UpdateKhayRoResponse {
    @SerializedName("Code")
    int code;
    @SerializedName("Message")
    String message;

    public UpdateKhayRoResponse() {
    }

    public UpdateKhayRoResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
