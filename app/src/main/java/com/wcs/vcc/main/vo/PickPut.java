package com.wcs.vcc.main.vo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

@Entity(tableName = "tbl_PickPut")
public class PickPut {
    @PrimaryKey(autoGenerate = true)
    public int Id;

    @SerializedName("FunctionType")
    @ColumnInfo(name = "FunctionType")
    public String functionType;

    @SerializedName("OrderNumber")
    @ColumnInfo(name = "OrderNumber")
    public String orderNumber;

    @SerializedName("LocationNumber")
    @ColumnInfo(name = "LocationNumber")
    public String locationNumber;

    @SerializedName("ProductNumber")
    @ColumnInfo(name = "ProductNumber")
    public String productNumber;

    @SerializedName("PalletID")
    @ColumnInfo(name = "PalletID")
    public String palletID;

    @SerializedName("PalletNumber")
    @ColumnInfo(name = "PalletNumber")
    public int palletNumber;

    @SerializedName("Cartons")
    @ColumnInfo(name = "Cartons")
    public int cartons;

    @SerializedName("ScannedBy")
    @ColumnInfo(name = "ScannedBy")
    public String scannedBy;

    @SerializedName("Status")
    @ColumnInfo(name = "Status")
    public int status;

    @SerializedName("ScannedTime")
    @ColumnInfo(name = "ScannedTime")
    public String scannedTime;

    @SerializedName("ProductName")
    @ColumnInfo(name = "ProductName")
    public String productName;

    @SerializedName("CustomerNumber")
    @ColumnInfo(name = "CustomerNumber")
    public String customerNumber;

    @SerializedName("Remain")
    @ColumnInfo(name = "Remain")
    public int remain;

    @SerializedName("Flag")
    @ColumnInfo(name = "Flag")
    public int flag;

    @SerializedName("Aisle")
    @ColumnInfo(name = "Aisle")
    public int aisle;

    @SerializedName("PickingQuantity")
    @ColumnInfo(name = "PickingQuantity")
    public int pickingQuantity;


    public PickPut() {
    }

    public PickPut(int id, String functionType, String orderNumber, String locationNumber, String productNumber, String palletID, int palletNumber, int cartons, String scannedBy, int status, String scannedTime, String productName, String customerNumber, int remain, int flag, int aisle) {
        Id = id;
        this.functionType = functionType;
        this.orderNumber = orderNumber;
        this.locationNumber = locationNumber;
        this.productNumber = productNumber;
        this.palletID = palletID;
        this.palletNumber = palletNumber;
        this.cartons = cartons;
        this.scannedBy = scannedBy;
        this.status = status;
        this.scannedTime = scannedTime;
        this.productName = productName;
        this.customerNumber = customerNumber;
        this.remain = remain;
        this.flag = flag;
        this.aisle = aisle;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getPalletID() {
        return palletID;
    }

    public void setPalletID(String palletID) {
        this.palletID = palletID;
    }

    public int getPalletNumber() {
        return palletNumber;
    }

    public String getPalletNumberPI() {
        String barcode = "PI";
        for (int i = 0; i < 11 - (String.valueOf(palletNumber).length() + 2); i++) {
            barcode = barcode.concat("0");
        }
        return barcode+palletNumber;
    }

    public void setPalletNumber(int palletNumber) {
        this.palletNumber = palletNumber;
    }

    public int getCartons() {
        return cartons;
    }

    public void setCartons(int cartons) {
        this.cartons = cartons;
    }

    public String getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(String scannedBy) {
        this.scannedBy = scannedBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getScannedTime() {
        return Utilities.formatDate_ddMMyy(scannedTime);
    }

    public void setScannedTime(String scannedTime) {
        this.scannedTime = scannedTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getAisle() {
        return aisle;
    }

    public void setAisle(int aisle) {
        this.aisle = aisle;
    }

    public int getPickingQuantity() {
        return pickingQuantity;
    }

    public void setPickingQuantity(int pickingQuantity) {
        this.pickingQuantity = pickingQuantity;
    }
}
