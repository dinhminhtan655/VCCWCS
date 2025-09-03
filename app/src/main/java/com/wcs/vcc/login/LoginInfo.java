package com.wcs.vcc.login;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class LoginInfo {
    @SerializedName("Username")
    private String username;
    @SerializedName("PositionGroup")
    private String positionGroup;
    @SerializedName("RealName")
    private String realName;
    @SerializedName("STATUS")
    private String status;
    @SerializedName("WarehouseID")
    private int warehouseID;
    @SerializedName("IsAllowOutside")
    private boolean IsAllowOutside;
    @SerializedName("StoreID")
    private int storeID;
    @SerializedName("DeviceNumber")
    private String  DeviceNumber;
    public UUID EmployeeID;

    public int getWarehouseID() {
        return warehouseID;
    }


    public String getPositionGroup() {
        return positionGroup;
    }


    public String getRealName() {
        return realName;
    }


    public String getStatus() {
        return status;
    }


    public String getUsername() {
        return username;
    }

    public boolean isAllowOutside() {
        return IsAllowOutside;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPositionGroup(String positionGroup) {
        this.positionGroup = positionGroup;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWarehouseID(int warehouseID) {
        this.warehouseID = warehouseID;
    }

    public void setAllowOutside(boolean allowOutside) {
        IsAllowOutside = allowOutside;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getDeviceNumber() {
        return DeviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        DeviceNumber = deviceNumber;
    }

    public UUID getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        EmployeeID = employeeID;
    }
}
