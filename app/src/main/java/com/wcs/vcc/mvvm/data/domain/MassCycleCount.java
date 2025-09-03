package com.wcs.vcc.mvvm.data.domain;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "masscyclecount")
public class MassCycleCount implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @Expose
    @SerializedName("customerNumber")
    @ColumnInfo(name = "customerNumber")
    private String customerNumber;
    @Expose
    @SerializedName("customerName")
    @ColumnInfo(name = "customerName")
    private String customerName;
    @Expose
    @SerializedName("stockMovementMassDate")
    @ColumnInfo(name = "stockMovementMassDate")
    private String stockMovementMassDate;
    @Expose
    @SerializedName("stockMovementMassRemark")
    @ColumnInfo(name = "stockMovementMassRemark")
    private String stockMovementMassRemark;
    @Expose
    @SerializedName("stockMovementMassConfirm")
    @ColumnInfo(name = "stockMovementMassConfirm")
    private boolean stockMovementMassConfirm;
    @Expose
    @SerializedName("stockMovementMassNumber")
    @ColumnInfo(name = "stockMovementMassNumber")
    private String stockMovementMassNumber;
    @Expose
    @SerializedName("createdTime")
    @ColumnInfo(name = "createdTime")
    private String createdTime;
    @Expose
    @SerializedName("stockMovementMassID")
    @ColumnInfo(name = "stockMovementMassID")
    private String stockMovementMassID;


    public MassCycleCount(String customerNumber,
                          String customerName,
                          String stockMovementMassDate,
                          String stockMovementMassRemark,
                          boolean stockMovementMassConfirm,
                          String stockMovementMassNumber,
                          String createdTime,
                          String stockMovementMassID) {
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.stockMovementMassDate = stockMovementMassDate;
        this.stockMovementMassRemark = stockMovementMassRemark;
        this.stockMovementMassConfirm = stockMovementMassConfirm;
        this.stockMovementMassNumber = stockMovementMassNumber;
        this.createdTime = createdTime;
        this.stockMovementMassID = stockMovementMassID;
    }

    protected MassCycleCount(Parcel in) {
        id = in.readInt();
        customerNumber = in.readString();
        customerName = in.readString();
        stockMovementMassDate = in.readString();
        stockMovementMassRemark = in.readString();
        stockMovementMassConfirm = in.readByte() != 0;
        stockMovementMassNumber = in.readString();
        createdTime = in.readString();
        stockMovementMassID = in.readString();
    }

    public static final Creator<MassCycleCount> CREATOR = new Creator<MassCycleCount>() {
        @Override
        public MassCycleCount createFromParcel(Parcel in) {
            return new MassCycleCount(in);
        }

        @Override
        public MassCycleCount[] newArray(int size) {
            return new MassCycleCount[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(customerNumber);
        dest.writeString(customerName);
        dest.writeString(stockMovementMassDate);
        dest.writeString(stockMovementMassRemark);
        dest.writeByte((byte) (stockMovementMassConfirm ? 1 : 0));
        dest.writeString(stockMovementMassNumber);
        dest.writeString(createdTime);
        dest.writeString(stockMovementMassID);
    }
}
