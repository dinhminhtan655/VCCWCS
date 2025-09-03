package com.wcs.vcc.main.len_ha_hang.model;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbl_Ticket")
public class Ticket {

    @PrimaryKey(autoGenerate = true)
    public int Id;

    @SerializedName("OrderNumber")
    @ColumnInfo(name = "OrderNumber")
    public String OrderNumber;

    @SerializedName("ReceivingOrderDate")
    @ColumnInfo(name = "ReceivingOrderDate")
    public String ReceivingOrderDate;

    @SerializedName("OrderRemark")
    @ColumnInfo(name = "OrderRemark")
    public String OrderRemark;

    @SerializedName("CustomerNumber")
    @ColumnInfo(name = "CustomerNumber")
    public String CustomerNumber;

    @SerializedName("CustomerName")
    @ColumnInfo(name = "CustomerName")
    public String CustomerName;

    @Override
    public String toString() {
        return OrderNumber + "~" +
                OrderRemark + "~" +
                CustomerNumber;
    }
}
