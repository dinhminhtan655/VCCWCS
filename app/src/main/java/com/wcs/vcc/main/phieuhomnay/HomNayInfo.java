package com.wcs.vcc.main.phieuhomnay;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

/**
 * Created by Trần Xuân Lộc on 12/30/2015.
 */
public class HomNayInfo {
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
    @SerializedName("CustomerType")
    private String customerType;
    @SerializedName("DockNumber")
    private String dockNumber;
    @SerializedName("OrderType")
    private String OrderType;
    @SerializedName("TotalPackages")
    private int TotalPackages;
    @SerializedName("ScannedType")
    private byte ScannedType;

    @SerializedName("CustomerID")
    private String CustomerID;
    @SerializedName("DispatchingOrderID")
    private String DispatchingOrderID;


    public String getCustomerType() {
        return customerType;
    }

    public byte getScannedType() {
        return ScannedType;
    }

    public int getTotalPackages() {
        return TotalPackages;
    }

    public String getOrderNumber() {
        return orderNumber;
    }


    public String getOrderDate() {
        return Utilities.formatDate_ddMM(orderDate);
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


    public String getDockNumber() {
        return dockNumber;
    }

    public String getOrderType() {
        return OrderType;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public String getDispatchingOrderID() {
        return DispatchingOrderID;
    }
}
