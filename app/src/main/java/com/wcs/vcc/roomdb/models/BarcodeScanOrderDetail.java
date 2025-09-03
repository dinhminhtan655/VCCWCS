package com.wcs.vcc.roomdb.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_BarcodeScanOrderDetail")
public class BarcodeScanOrderDetail {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name="BarcodeDataScan")
    public String barcodeDataScan;

    @ColumnInfo(name="DataId")
    public String dataId;

    @ColumnInfo(name="OrderNumber")
    public String orderNumber;

    @ColumnInfo(name="Result")
    public String result;

    @ColumnInfo(name="ScannedBy")
    public String scannedBy;

    @ColumnInfo(name="OrderType")
    public String orderType;

    @ColumnInfo(name="ScannedType")
    public int scannedType;

    @ColumnInfo(name="DeviceNumber")
    public String deviceNumber;

    public BarcodeScanOrderDetail() {
    }

    @Ignore
    public BarcodeScanOrderDetail(int id, String barcodeDataScan, String dataId, String orderNumber, String result, String scannedBy, String orderType, int scannedType, String deviceNumber) {
        this.id = id;
        this.barcodeDataScan = barcodeDataScan;
        this.dataId = dataId;
        this.orderNumber = orderNumber;
        this.result = result;
        this.scannedBy = scannedBy;
        this.orderType = orderType;
        this.scannedType = scannedType;
        this.deviceNumber = deviceNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcodeDataScan() {
        return barcodeDataScan;
    }

    public void setBarcodeDataScan(String barcodeDataScan) {
        this.barcodeDataScan = barcodeDataScan;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(String scannedBy) {
        this.scannedBy = scannedBy;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getScannedType() {
        return scannedType;
    }

    public void setScannedType(int scannedType) {
        this.scannedType = scannedType;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }
}
