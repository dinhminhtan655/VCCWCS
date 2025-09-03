package com.wcs.vcc.main.soki.model;

import com.google.gson.annotations.SerializedName;

public class RO {

    @SerializedName("ReceivingOrderNumber")
    public String receivingOrderNumber;
    @SerializedName("ReceivingOrderDate")
    public String receivingOrderDate;
    @SerializedName("VehicleNumber")
    public String vehicleNumber;
    @SerializedName("ReceivingOrderRemark")
    public String receivingOrderRemark;
    @SerializedName("CreatedBy")
    public String createdBy;


    @Override
    public String toString() {
        return receivingOrderNumber;

    }
}
