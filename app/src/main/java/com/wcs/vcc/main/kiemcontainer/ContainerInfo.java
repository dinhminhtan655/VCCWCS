package com.wcs.vcc.main.kiemcontainer;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

import java.util.UUID;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class ContainerInfo {
    @SerializedName("ContInOutID")
    private UUID contInOutID;
    @SerializedName("CustomerID")
    private UUID customerID;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("ContainerNum")
    private String containerNum;
    @SerializedName("ContainerType")
    private String containerType;
    @SerializedName("Reason")
    private String reason;
    @SerializedName("LastCheck")
    private String lastCheck;
    @SerializedName("TimeIn")
    private String timeIn;
    @SerializedName("CheckingTime")
    private String checkingTime;
    @SerializedName("UserCheck")
    private String userCheck;
    @SerializedName("DockNumber")
    private String dockNumber;
    @SerializedName("VehicleType")
    private String VehicleType;

    public String getVehicleType() {
        return VehicleType;
    }

    public String getContainerNum() {
        return containerNum;
    }

    public String getContainerType() {
        return containerType;
    }

    public UUID getContInOutID() {
        return contInOutID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCheckingTime() {
        return checkingTime;
    }

    public String getDockNumber() {
        return dockNumber;
    }

    public void setDockNumber(String dockNumber) {
        this.dockNumber = dockNumber;
    }

    public String getTimeIn() {
        return Utilities.formatDate_ddMMyyHHmm(timeIn);
    }

    public String getUserCheck() {
        return userCheck;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
