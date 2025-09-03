package com.wcs.vcc.mvvm.data.model.MassCycleCount;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class MassCycleCountRemote implements Parcelable {

    @SerializedName("CustomerNumber")
    private String customerNumber;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("StockMovementMassDate")
    private String stockMovementMassDate;
    @SerializedName("StockMovementMassRemark")
    private String stockMovementMassRemark;
    @SerializedName("StockMovementMassConfirm")
    private boolean stockMovementMassConfirm;
    @SerializedName("StockMovementMassNumber")
    private String stockMovementMassNumber;
    @SerializedName("CreatedTime")
    private String createdTime;
    @SerializedName("StockMovementMassID")
    private String stockMovementMassID;
    @SerializedName("CycleCountHideQty")
    private boolean CycleCountHideQty;

    public MassCycleCountRemote(String customerNumber, String customerName, String stockMovementMassDate, String stockMovementMassRemark, boolean stockMovementMassConfirm, String stockMovementMassNumber, String createdTime, String stockMovementMassID, boolean cycleCountHideQty) {
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.stockMovementMassDate = stockMovementMassDate;
        this.stockMovementMassRemark = stockMovementMassRemark;
        this.stockMovementMassConfirm = stockMovementMassConfirm;
        this.stockMovementMassNumber = stockMovementMassNumber;
        this.createdTime = createdTime;
        this.stockMovementMassID = stockMovementMassID;
        CycleCountHideQty = cycleCountHideQty;
    }

    protected MassCycleCountRemote(Parcel in) {
        customerNumber = in.readString();
        customerName = in.readString();
        stockMovementMassDate = in.readString();
        stockMovementMassRemark = in.readString();
        stockMovementMassConfirm = in.readByte() != 0;
        stockMovementMassNumber = in.readString();
        createdTime = in.readString();
        stockMovementMassID = in.readString();
        CycleCountHideQty = in.readByte() != 0;
    }

    public static final Creator<MassCycleCountRemote> CREATOR = new Creator<MassCycleCountRemote>() {
        @Override
        public MassCycleCountRemote createFromParcel(Parcel in) {
            return new MassCycleCountRemote(in);
        }

        @Override
        public MassCycleCountRemote[] newArray(int size) {
            return new MassCycleCountRemote[size];
        }
    };

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

    public String getStockMovementMassDate() {
        return stockMovementMassDate;
    }

    public void setStockMovementMassDate(String stockMovementMassDate) {
        this.stockMovementMassDate = stockMovementMassDate;
    }

    public String getStockMovementMassRemark() {
        return stockMovementMassRemark;
    }

    public void setStockMovementMassRemark(String stockMovementMassRemark) {
        this.stockMovementMassRemark = stockMovementMassRemark;
    }

    public boolean isStockMovementMassConfirm() {
        return stockMovementMassConfirm;
    }

    public void setStockMovementMassConfirm(boolean stockMovementMassConfirm) {
        this.stockMovementMassConfirm = stockMovementMassConfirm;
    }

    public String getStockMovementMassNumber() {
        return stockMovementMassNumber;
    }

    public void setStockMovementMassNumber(String stockMovementMassNumber) {
        this.stockMovementMassNumber = stockMovementMassNumber;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getStockMovementMassID() {
        return stockMovementMassID;
    }

    public void setStockMovementMassID(String stockMovementMassID) {
        this.stockMovementMassID = stockMovementMassID;
    }

    public boolean isCycleCountHideQty() {
        return CycleCountHideQty;
    }

    public void setCycleCountHideQty(boolean cycleCountHideQty) {
        CycleCountHideQty = cycleCountHideQty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(customerNumber);
        dest.writeString(customerName);
        dest.writeString(stockMovementMassDate);
        dest.writeString(stockMovementMassRemark);
        dest.writeByte((byte) (stockMovementMassConfirm ? 1 : 0));
        dest.writeString(stockMovementMassNumber);
        dest.writeString(createdTime);
        dest.writeString(stockMovementMassID);
        dest.writeByte((byte) (CycleCountHideQty ? 1 : 0));
    }
}
