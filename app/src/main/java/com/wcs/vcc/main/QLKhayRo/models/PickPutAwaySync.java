package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class PickPutAwaySync {
    @SerializedName("UserName")
    String username;

    @SerializedName("DeviceNumber")
    String deviceNumber;

    @SerializedName("OrderID")
    String orderId;

    @SerializedName("OrderNumber")
    String orderNumber;

    @SerializedName("OrderDate")
    String orderDate;

    @SerializedName("CustomerNumber")
    String customerName;

    @SerializedName("SpecialRequirement")
    String specialRequirement;

    @SerializedName("LocationNumber")
    String locationNumber;

    @SerializedName("ProductNumber")
    String productNumber;

    @SerializedName("ProductName")
    String productName;

    @SerializedName("PalletID")
    String palletId;

    @SerializedName("PalletNumber")
    int palletNumber;

    @SerializedName("OriginalQuantity")
    int originalQty;

    @SerializedName("Cartons")
    int cartons;

    @SerializedName("ProductionDate")
    String productionDate;

    @SerializedName("UseByDate")
    String useByDate;

    @SerializedName("CustomerRef")
    String customerRef;

    @SerializedName("ScannedBy")
    String scannedBy;

    @SerializedName("Status")
    int status;

    @SerializedName("returnResult")
    String returnResult;

    @SerializedName("UserID")
    String userId;

    @SerializedName("PltScannedQty")
    String PltScannedQty;

    @SerializedName("DispatchingOrderDetailID")
    String dispatchingOrderDetailId;

    @SerializedName("CreatedTime")
    String createdTime;

    @SerializedName("PutAwayScannedBy")
    String putAwayScannedBy;

    @SerializedName("PutAwayScannedTime")
    String putAwayScannedTime;

    @SerializedName("AfterDPQuantity")
    int afterDPQty;

    @SerializedName("CurrentQuantity")
    int currentQty;

    @SerializedName("LocationID")
    String locationId;

    @SerializedName("Label")
    String label;

    public PickPutAwaySync() {
    }

    public PickPutAwaySync(String username, String deviceNumber, String orderId, String orderNumber, String orderDate, String customerName, String specialRequirement, String locationNumber, String productNumber, String productName, String palletId, int palletNumber, int originalQty, int cartons, String productionDate, String useByDate, String customerRef, String scannedBy, int status, String returnResult, String userId, String pltScannedQty, String dispatchingOrderDetailId, String createdTime, String putAwayScannedBy, String putAwayScannedTime, int afterDPQty, int currentQty, String locationId, String label) {
        this.username = username;
        this.deviceNumber = deviceNumber;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.specialRequirement = specialRequirement;
        this.locationNumber = locationNumber;
        this.productNumber = productNumber;
        this.productName = productName;
        this.palletId = palletId;
        this.palletNumber = palletNumber;
        this.originalQty = originalQty;
        this.cartons = cartons;
        this.productionDate = productionDate;
        this.useByDate = useByDate;
        this.customerRef = customerRef;
        this.scannedBy = scannedBy;
        this.status = status;
        this.returnResult = returnResult;
        this.userId = userId;
        PltScannedQty = pltScannedQty;
        this.dispatchingOrderDetailId = dispatchingOrderDetailId;
        this.createdTime = createdTime;
        this.putAwayScannedBy = putAwayScannedBy;
        this.putAwayScannedTime = putAwayScannedTime;
        this.afterDPQty = afterDPQty;
        this.currentQty = currentQty;
        this.locationId = locationId;
        this.label = label;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public int getOriginalQty() {
        return originalQty;
    }

    public void setOriginalQty(int originalQty) {
        this.originalQty = originalQty;
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

    public String getDispatchingOrderDetailId() {
        return dispatchingOrderDetailId;
    }

    public void setDispatchingOrderDetailId(String dispatchingOrderDetailId) {
        this.dispatchingOrderDetailId = dispatchingOrderDetailId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
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

    public int getAfterDPQty() {
        return afterDPQty;
    }

    public void setAfterDPQty(int afterDPQty) {
        this.afterDPQty = afterDPQty;
    }

    public int getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(int currentQty) {
        this.currentQty = currentQty;
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
}
