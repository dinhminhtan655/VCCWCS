package com.wcs.vcc.main.vo;

import com.google.gson.annotations.SerializedName;

public class QaPoProduct {
    @SerializedName("PurchasingOrderProductID")
    int id;
    @SerializedName("PurchasingOrderProductNumber")
    String PurchasingOrderProductNumber;
    @SerializedName("ProductNumber")
    String productNumber;
    @SerializedName("ProductName")
    String productName;
    @SerializedName("PODetailDamage")
    double poDetailDamage;
    @SerializedName("PODetailDamageError")
    double poDetailDamageError;
    @SerializedName("PurchasingOrderProductRemark")
    String remark;
    @SerializedName("QABy")
    String qaBy;
    @SerializedName("Qty")
    int qty;
    @SerializedName("QtyQA")
    int qtyQA;
    @SerializedName("QtyQADamageVH")
    double qtyQADamageVH;
    @SerializedName("QtyQADamageH")
    double qtyQADamageH;
    @SerializedName("QtyQADamageL")
    double qtyQADamageL;
    @SerializedName("PODamage")
    double poDamage;
    @SerializedName("PurchasingOrderRemark")
    String purchasingOrderRemark;
    @SerializedName("UpdateTime")
    String updateTime;

    boolean isChanging;
    int positionInList;

    public int getId() {
        return id;
    }

    public String getPurchasingOrderProductNumber() {
        return PurchasingOrderProductNumber;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public String getRemark() {
        return remark;
    }

    public String getQaBy() {
        return qaBy;
    }

    public double getPoDetailDamage() {
        return poDetailDamage;
    }

    public double getPoDetailDamageError() {
        return poDetailDamageError;
    }

    public int getQty() {
        return qty;
    }

    public int getQtyQA() {
        return qtyQA;
    }

    public double getQtyQADamageVH() {
        return qtyQADamageVH;
    }

    public double getQtyQADamageH() {
        return qtyQADamageH;
    }

    public double getQtyQADamageL() {
        return qtyQADamageL;
    }

    public double getPoDamage() {
        return poDamage;
    }

    public String getPurchasingOrderRemark() {
        return purchasingOrderRemark;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public boolean isChanging() {
        return isChanging;
    }

    public void setChanging(boolean changing) {
        isChanging = changing;
    }

    public void setQtyQA(int qtyQA) {
        this.qtyQA = qtyQA;
    }

    public void setPurchasingOrderProductNumber(String purchasingOrderProductNumber) {
        PurchasingOrderProductNumber = purchasingOrderProductNumber;
    }

    public void setPoDetailDamage(double poDetailDamage) {
        this.poDetailDamage = poDetailDamage;
    }

    public void setPoDetailDamageError(double poDetailDamageError) {
        this.poDetailDamageError = poDetailDamageError;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setQaBy(String qaBy) {
        this.qaBy = qaBy;
    }

    public void setQtyQADamageVH(double qtyQADamageVH) {
        this.qtyQADamageVH = qtyQADamageVH;
    }

    public void setQtyQADamageH(double qtyQADamageH) {
        this.qtyQADamageH = qtyQADamageH;
    }

    public void setQtyQADamageL(double qtyQADamageL) {
        this.qtyQADamageL = qtyQADamageL;
    }

    public void setPoDamage(double poDamage) {
        this.poDamage = poDamage;
    }

    public void setPurchasingOrderRemark(String purchasingOrderRemark) {
        this.purchasingOrderRemark = purchasingOrderRemark;
    }

    public int getPositionInList() {
        return positionInList;
    }

    public void setPositionInList(int positionInList) {
        this.positionInList = positionInList;
    }
}
