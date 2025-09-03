package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class PickPutSyncBarcodeScan {
    @SerializedName("UserName")
    String username;

    @SerializedName("DeviceNumber")
    String deviceNumber;

    @SerializedName("OrderNumber")
    String orderNumber;

    @SerializedName("OrderType")
    String orderType;

    @SerializedName("BarcodeDataScanned")
    String barcodeDataScanned;

    @SerializedName("CreatedTime")
    String createdTime;

    @SerializedName("ScannedBy")
    String scannedBy;

    @SerializedName("Result")
    String result;

    @SerializedName("Remark")
    String remark;

    @SerializedName("UpdatedBy")
    String updatedBy;

    @SerializedName("ScannedType")
    int scannedType;

    @SerializedName("Data_ID")
    String dataId;

    public PickPutSyncBarcodeScan() {
    }

    public PickPutSyncBarcodeScan(String username, String deviceNumber, String orderNumber, String orderType, String barcodeDataScanned, String createdTime, String scannedBy, String result, String remark, String updatedBy, int scannedType, String dataId) {
        this.username = username;
        this.deviceNumber = deviceNumber;
        this.orderNumber = orderNumber;
        this.orderType = orderType;
        this.barcodeDataScanned = barcodeDataScanned;
        this.createdTime = createdTime;
        this.scannedBy = scannedBy;
        this.result = result;
        this.remark = remark;
        this.updatedBy = updatedBy;
        this.scannedType = scannedType;
        this.dataId = dataId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBarcodeDataScanned() {
        return barcodeDataScanned;
    }

    public void setBarcodeDataScanned(String barcodeDataScanned) {
        this.barcodeDataScanned = barcodeDataScanned;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(String scannedBy) {
        this.scannedBy = scannedBy;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getScannedType() {
        return scannedType;
    }

    public void setScannedType(int scannedType) {
        this.scannedType = scannedType;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}
