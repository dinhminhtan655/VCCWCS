package com.wcs.vcc.main.bigc;


import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

public class BasketMovement {
    @SerializedName("BasketMovementNumber")
    private String number;
    @SerializedName("FromWhere")
    private String fromWhere;
    @SerializedName("ToWhere")
    private String toWhere;
    @SerializedName("BasketMovementRemark")
    private String remark;
    @SerializedName("BasketMovementDate")
    private String date;
    @SerializedName("CreatedBy")
    private String createdBy;
    @SerializedName("CreatedTime")
    private String createdTime;
    @SerializedName("BasketNumber")
    private String basketNumber;
    @SerializedName("BasketName")
    private String basketName;
    @SerializedName("Quantity")
    private int quantity;

    public String getNumber() {
        return number;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public String getToWhere() {
        return toWhere;
    }

    public String getRemark() {
        return remark;
    }

    public String getDate() {
        return Utilities.formatDate_ddMMyyyy(date);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedTime() {
        return Utilities.formatDate_ddMMyyyyHHmm(createdTime);
    }

    public String getBasketNumber() {
        return basketNumber;
    }

    public String getBasketName() {
        return basketName;
    }

    public int getQuantity() {
        return quantity;
    }
}
