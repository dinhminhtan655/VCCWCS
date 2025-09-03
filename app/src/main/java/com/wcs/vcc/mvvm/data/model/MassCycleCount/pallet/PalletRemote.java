package com.wcs.vcc.mvvm.data.model.MassCycleCount.pallet;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PalletRemote implements Serializable {

    @SerializedName("LocationNumber")
    private String locationNumber;
    @SerializedName("AfterDPQuantity")
    private int afterDPQuantity;
    @SerializedName("CurrentQuantity")
    private int currentQuantity;
    @SerializedName("OriginalQuantity")
    private int originalQuantity;
    @SerializedName("StringQuantity")
    private String stringQuantity;
    @SerializedName("CustomerRef")
    private String customerRef;
    @SerializedName("CustomerRef2")
    private String customerRef2;
    @SerializedName("Remark")
    private String remark;
    @SerializedName("ReceivingOrderDate")
    private String receivingOrderDate;
    @SerializedName("ProductionDate")
    private String productionDate;
    @SerializedName("UseByDate")
    private String useByDate;
    @SerializedName("PalletStatus")
    private int palletStatus;
    @SerializedName("ReceivingOrderNumber")
    private String receivingOrderNumber;
    @SerializedName("ProductNumber")
    private String productNumber;
    @SerializedName("ProductName")
    private String productName;
    @SerializedName("CustomerNumber")
    private String customerNumber;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("CreatedTime")
    private String createdTime;
    @SerializedName("ScannedBy")
    private String scannedBy;
    @SerializedName("Result")
    private String result;
    @SerializedName("DeviceNumber")
    private String deviceNumber;
    @SerializedName("LocationID")
    private String locationID;
    @SerializedName("CustomerID")
    private String customerID;
    @SerializedName("PalletID")
    private String PalletID;
    @SerializedName("PalletNumber")
    private String PalletNumber;
    @SerializedName("ProductID")
    private String ProductID;

    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    public int getAfterDPQuantity() {
        return afterDPQuantity;
    }

    public void setAfterDPQuantity(int afterDPQuantity) {
        this.afterDPQuantity = afterDPQuantity;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public int getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(int originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public String getStringQuantity() {
        return stringQuantity;
    }

    public void setStringQuantity(String stringQuantity) {
        this.stringQuantity = stringQuantity;
    }

    public String getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
    }

    public String getCustomerRef2() {
        return customerRef2;
    }

    public void setCustomerRef2(String customerRef2) {
        this.customerRef2 = customerRef2;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReceivingOrderDate() {
        return receivingOrderDate;
    }

    public void setReceivingOrderDate(String receivingOrderDate) {
        this.receivingOrderDate = receivingOrderDate;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getUseByDate() {
        return useByDate;
    }

    public void setUseByDate(String useByDate) {
        this.useByDate = useByDate;
    }

    public int getPalletStatus() {
        return palletStatus;
    }

    public void setPalletStatus(int palletStatus) {
        this.palletStatus = palletStatus;
    }

    public String getReceivingOrderNumber() {
        return receivingOrderNumber;
    }

    public void setReceivingOrderNumber(String receivingOrderNumber) {
        this.receivingOrderNumber = receivingOrderNumber;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getPalletID() {
        return PalletID;
    }

    public void setPalletID(String palletID) {
        PalletID = palletID;
    }

    public String getPalletNumber() {
        return "PI000"+PalletNumber;
    }

    public void setPalletNumber(String palletNumber) {
        PalletNumber = palletNumber;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }
}
