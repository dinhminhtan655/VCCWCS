package com.wcs.vcc.main.phieucuatoi;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

/**
 * Created by Trần Xuân Lộc on 12/30/2015.
 */
public class PhieuCuaToiInfo {
    @SerializedName("DispatchingOrderNumber")
    private String orderNumber;
    @SerializedName("DispatchingOrderDate")
    private String orderDate;
    @SerializedName("SpecialRequirement")
    private String specialRequirement;
    @SerializedName("CustomerNumber")
    private String customerNumber;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("DockNumber")
    private String dockNumber;
    @SerializedName("ScannedType")
    private byte ScannedType;
    @SerializedName("CustomerType")
    private String customerType;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return Utilities.formatDate(orderDate.split(" ")[0]);
    }

    public String getSpecialRequirement() {
        return specialRequirement;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public byte getScannedType() {
        return ScannedType;
    }

    public String getCustomerType() {
        return customerType;
    }

    public String getDockNumber() {
        return dockNumber;
    }

    public void setDockNumber(String dockNumber) {
        this.dockNumber = dockNumber;
    }
}
