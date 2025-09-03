package com.wcs.vcc.roomdb.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbl_PickPutDetail")
public class PickPutDetailOffline {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "OrderID")
    @SerializedName("orderID")
    public String orderID;

    @ColumnInfo(name = "OrderNumber")
    @SerializedName("orderNumber")
    public String orderNumber;

    @ColumnInfo(name = "OrderDate")
    @SerializedName("OrderDate")
    public String OrderDate;

    @ColumnInfo(name = "CustomerNumber")
    @SerializedName("CustomerNumber")
    public String customerNumber;

    @ColumnInfo(name = "SpecialRequirement")
    @SerializedName("SpecialRequirement")
    public String specialRequirement;

    @ColumnInfo(name = "LocationNumber")
    @SerializedName("LocationNumber")
    public String locationNumber;

    @ColumnInfo(name = "ProductNumber")
    @SerializedName("ProductNumber")
    public String productNumber;

    @ColumnInfo(name = "ProductName")
    @SerializedName("ProductName")
    public String productName;

    @ColumnInfo(name = "PalletID")
    @SerializedName("PalletID")
    public String palletId;

    @ColumnInfo(name = "PalletNumber")
    @SerializedName("PalletNumber")
    public int palletNumber;

    @ColumnInfo(name = "OriginalQuantity")
    @SerializedName("OriginalQuantity")
    public int originalQuantity;

    @ColumnInfo(name = "AfterDPQuantity")
    @SerializedName("AfterDPQuantity")
    public int afterDPQuantity;

    @ColumnInfo(name = "CurrentQuantity")
    @SerializedName("CurrentQuantity")
    public int currentQuantity;

    @ColumnInfo(name = "LocationID")
    @SerializedName("LocationID")
    public String locationId;

    @ColumnInfo(name = "Label")
    @SerializedName("Label")
    public String label;

    @ColumnInfo(name = "Cartons")
    @SerializedName("Cartons")
    public int cartons;

    @ColumnInfo(name = "ProductionDate")
    @SerializedName("ProductionDate")
    public String productionDate;

    @ColumnInfo(name = "UseByDate")
    @SerializedName("UseByDate")
    public String useByDate;

    @ColumnInfo(name = "CustomerRef")
    @SerializedName("CustomerRef")
    public String customerRef;

    @ColumnInfo(name = "ScannedBy")
    @SerializedName("ScannedBy")
    public String scannedBy;

    @ColumnInfo(name = "Status")
    @SerializedName("Status")
    public int status;

    @ColumnInfo(name = "returnResult")
    @SerializedName("returnResult")
    public String returnResult;

    @ColumnInfo(name = "UserID")
    @SerializedName("UserID")
    public String userId;

    @ColumnInfo(name = "PltScannedQty")
    @SerializedName("PltScannedQty")
    public String PltScannedQty;

    @ColumnInfo(name = "PutAwayScannedBy")
    public String putAwayScannedBy;

    @ColumnInfo(name = "PutAwayScannedTime")
    public String putAwayScannedTime;

    @ColumnInfo(name = "Reference")
    @SerializedName("Reference")
    public String reference;

    @ColumnInfo(name = "StoreId")
    public int storeId;

    public PickPutDetailOffline() {
    }

    public PickPutDetailOffline(int id, String orderID, String orderNumber, String orderDate, String customerNumber, String specialRequirement, String locationNumber, String productNumber, String productName, String palletId, int palletNumber, int originalQuantity, int afterDPQuantity, int currentQuantity, String locationId, String label, int cartons, String productionDate, String useByDate, String customerRef, String scannedBy, int status, String returnResult, String userId, String pltScannedQty, String putAwayScannedBy, String putAwayScannedTime, String reference, int storeId) {
        this.id = id;
        this.orderID = orderID;
        this.orderNumber = orderNumber;
        OrderDate = orderDate;
        this.customerNumber = customerNumber;
        this.specialRequirement = specialRequirement;
        this.locationNumber = locationNumber;
        this.productNumber = productNumber;
        this.productName = productName;
        this.palletId = palletId;
        this.palletNumber = palletNumber;
        this.originalQuantity = originalQuantity;
        this.afterDPQuantity = afterDPQuantity;
        this.currentQuantity = currentQuantity;
        this.locationId = locationId;
        this.label = label;
        this.cartons = cartons;
        this.productionDate = productionDate;
        this.useByDate = useByDate;
        this.customerRef = customerRef;
        this.scannedBy = scannedBy;
        this.status = status;
        this.returnResult = returnResult;
        this.userId = userId;
        PltScannedQty = pltScannedQty;
        this.putAwayScannedBy = putAwayScannedBy;
        this.putAwayScannedTime = putAwayScannedTime;
        this.reference = reference;
        this.storeId = storeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getSpecialRequirement() {
        return specialRequirement;
    }

    public void setSpecialRequirement(String specialRequirement) {
        this.specialRequirement = specialRequirement;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPalletId() {
        return palletId;
    }

    public void setPalletId(String palletId) {
        this.palletId = palletId;
    }

    public int getPalletNumber() {
        return palletNumber;
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

    public String getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
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

    public String getReturnResult() {
        return returnResult;
    }

    public void setReturnResult(String returnResult) {
        this.returnResult = returnResult;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPltScannedQty() {
        return PltScannedQty;
    }

    public void setPltScannedQty(String pltScannedQty) {
        PltScannedQty = pltScannedQty;
    }

    public String getPutAwayScannedBy() {
        return putAwayScannedBy;
    }

    public void setPutAwayScannedBy(String putAwayScannedBy) {
        this.putAwayScannedBy = putAwayScannedBy;
    }

    public String getPutAwayScannedTime() {
        return putAwayScannedTime;
    }

    public void setPutAwayScannedTime(String putAwayScannedTime) {
        this.putAwayScannedTime = putAwayScannedTime;
    }

    public int getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(int originalQuantity) {
        this.originalQuantity = originalQuantity;
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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
