package com.wcs.vcc.mvvm.data.model.MassCycleCountDetail;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

import java.io.Serializable;

public class MassCycleCountDetailRemote implements Serializable {

    @SerializedName("ReportTitle")
    private String reportTitle;
    @SerializedName("CustomerNumber")
    private String customerNumber;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("AfterDPQuantity")
    private int afterDPQuantity;
    @SerializedName("CurrentQuantity")
    private int currentQuantity;
    @SerializedName("StockMovementMassDate")
    private String stockMovementMassDate;
    @SerializedName("StockMovementMassID")
    private String stockMovementMassID;
    @SerializedName("LocationID")
    private String locationID;
    @SerializedName("PalletID")
    private String palletID;
    @SerializedName("StockMovementMassNumber")
    private String stockMovementMassNumber;
    @SerializedName("PalletWeights")
    private double palletWeights;
    @SerializedName("OrderNumber")
    private String orderNumber;
    @SerializedName("ProductionDate")
    private String productionDate;
    @SerializedName("UseByDate")
    private String useByDate;
    @SerializedName("CustomerRef")
    private String customerRef;
    @SerializedName("CustomerRef2")
    private String customerRef2;
    @SerializedName("Remark")
    private String remark;
    @SerializedName("PalletNumber")
    private int palletNumber;
    @SerializedName("OriginalQuantity")
    private int originalQuantity;
    @SerializedName("OnHold")
    private boolean onHold;
    @SerializedName("CheckingStatus")
    private boolean checkingStatus;
    @SerializedName("Expr1")
    private String expr1;
    @SerializedName("OldPalletNumber")
    private String oldPalletNumber;
    @SerializedName("LocationNumber")
    private String locationNumber;
    @SerializedName("CheckingQuantity")
    private int checkingQuantity;
    @SerializedName("CheckingScannedBy")
    private String checkingScannedBy;
    @SerializedName("CheckingScannedTime")
    private String checkingScannedTime;
    @SerializedName("CheckingReason")
    private String checkingReason;
    @SerializedName("ProductName")
    private String productName;
    @SerializedName("ProductID")
    private String productID;
    @SerializedName("CustomerID")
    private String CustomerID;
    @SerializedName("CheckingProductID")
    private String CheckingProductID;
    @SerializedName("CheckingProductionDate")
    private String CheckingProductionDate;
    @SerializedName("CheckingUseByDate")
    private String CheckingUseByDate;
    @SerializedName("CheckingLocationID")
    private String CheckingLocationID;
    @SerializedName("CheckingCustomerRef")
    private String CheckingCustomerRef;
    @SerializedName("CheckingCustomerRef2")
    private String CheckingCustomerRef2;

    private boolean isGone;

    private boolean isChecked;

    private boolean isRed;

    private boolean isDateRed;

    private boolean isLocationRed;

    public MassCycleCountDetailRemote() {
    }



    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
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

    public String getTotalAfterDPQuantity() {
        return String.valueOf(afterDPQuantity);
    }

    public void setTotalAfterDPQuantity(int totalAfterDPQuantity) {
        this.afterDPQuantity = totalAfterDPQuantity;
    }

    public int getTotalOriginalQuantity() {
        return currentQuantity;
    }

    public void setTotalOriginalQuantity(int totalOriginalQuantity) {
        this.currentQuantity = totalOriginalQuantity;
    }

    public String getStockMovementMassDate() {
        return stockMovementMassDate;
    }

    public void setStockMovementMassDate(String stockMovementMassDate) {
        this.stockMovementMassDate = stockMovementMassDate;
    }

    public String getStockMovementMassID() {
        return stockMovementMassID;
    }

    public void setStockMovementMassID(String stockMovementMassID) {
        this.stockMovementMassID = stockMovementMassID;
    }

    public String getStockMovementMassNumber() {
        return stockMovementMassNumber;
    }

    public void setStockMovementMassNumber(String stockMovementMassNumber) {
        this.stockMovementMassNumber = stockMovementMassNumber;
    }

    public String getPalletWeights() {
        return String.valueOf(palletWeights);
    }

    public void setPalletWeights(double palletWeights) {
        this.palletWeights = palletWeights;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProductionDate() {
        return Utilities.formatDate_ddMMyyyy(productionDate);
    }

    public String getProductionDateNoFormat(){
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getUseByDate() {
        return Utilities.formatDate_ddMMyyyy(useByDate);
    }

    public String getUseBydateNoFormat(){
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

    public String getPalletNumber() {
        return String.valueOf("PI000" + palletNumber);
    }

    public void setPalletNumber(int palletNumber) {
        this.palletNumber = palletNumber;
    }

    public String getOriginalQuantity() {
        return String.valueOf(originalQuantity);
    }

    public void setOriginalQuantity(int originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public boolean isOnHold() {
        return onHold;
    }

    public void setOnHold(boolean onHold) {
        this.onHold = onHold;
    }

    public String getExpr1() {
        return expr1;
    }

    public void setExpr1(String expr1) {
        this.expr1 = expr1;
    }

    public String getOldPalletNumber() {
        return oldPalletNumber;
    }

    public void setOldPalletNumber(String oldPalletNumber) {
        this.oldPalletNumber = oldPalletNumber;
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    public boolean isGone() {
        return isGone;
    }

    public void setGone(boolean gone) {
        isGone = gone;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getPalletID() {
        return palletID;
    }

    public void setPalletID(String palletID) {
        this.palletID = palletID;
    }

    public boolean isCheckingStatus() {
        return checkingStatus;
    }

    public void setCheckingStatus(boolean checkingStatus) {
        this.checkingStatus = checkingStatus;
    }

    public int getCheckingQuantity() {
        return checkingQuantity;
    }

    public void setCheckingQuantity(int checkingQuantity) {
        this.checkingQuantity = checkingQuantity;
    }

    public String getCheckingScannedBy() {
        return checkingScannedBy;
    }

    public void setCheckingScannedBy(String checkingScannedBy) {
        this.checkingScannedBy = checkingScannedBy;
    }

    public String getCheckingScannedTime() {
        return Utilities.formatDate_HHmm(checkingScannedTime);
    }

    public void setCheckingScannedTime(String checkingScannedTime) {
        this.checkingScannedTime = checkingScannedTime;
    }

    public String getCheckingReason() {
        return checkingReason;
    }

    public void setCheckingReason(String checkingReason) {
        this.checkingReason = checkingReason;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getCheckingProductID() {
        return CheckingProductID;
    }

    public void setCheckingProductID(String checkingProductID) {
        CheckingProductID = checkingProductID;
    }

    public String getCheckingProductionDate() {
        return CheckingProductionDate;
    }

    public void setCheckingProductionDate(String checkingProductionDate) {
        CheckingProductionDate = checkingProductionDate;
    }

    public String getCheckingUseByDate() {
        return CheckingUseByDate;
    }

    public void setCheckingUseByDate(String checkingUseByDate) {
        CheckingUseByDate = checkingUseByDate;
    }

    public String getCheckingLocationID() {
        return CheckingLocationID;
    }

    public void setCheckingLocationID(String checkingLocationID) {
        CheckingLocationID = checkingLocationID;
    }

    public String getCheckingCustomerRef() {
        return CheckingCustomerRef;
    }

    public void setCheckingCustomerRef(String checkingCustomerRef) {
        CheckingCustomerRef = checkingCustomerRef;
    }

    public String getCheckingCustomerRef2() {
        return CheckingCustomerRef2;
    }

    public void setCheckingCustomerRef2(String checkingCustomerRef2) {
        CheckingCustomerRef2 = checkingCustomerRef2;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean red) {
        isRed = red;
    }

    public boolean isDateRed() {
        return isDateRed;
    }

    public void setDateRed(boolean dateRed) {
        isDateRed = dateRed;
    }

    public boolean isLocationRed() {
        return isLocationRed;
    }

    public void setLocationRed(boolean locationRed) {
        isLocationRed = locationRed;
    }
}
