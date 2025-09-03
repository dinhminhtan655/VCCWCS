package com.wcs.vcc.main.vo;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

import java.util.UUID;

public class PalletPickPut {

    @SerializedName("OrderID")
    public UUID OrderID;
    @SerializedName("OrderNumber")
    public String OrderNumber;
    @SerializedName("OrderDate")
    public String  OrderDate;
    @SerializedName("CustomerNumber")
    public String CustomerNumber;
    @SerializedName("SpecialRequirement")
    public String SpecialRequirement;
    @SerializedName("ProductNumber")
    public String ProductNumber;
    @SerializedName("ProductName")
    public String ProductName;
    @SerializedName("LocationNumber")
    public String LocationNumber;
    @SerializedName("PalletID")
    public UUID PalletID;
    @SerializedName("PalletNumber")
    public int PalletNumber;
    @SerializedName("Cartons")
    public int Cartons;
    @SerializedName("ProductionDate")
    public String  ProductionDate;
    @SerializedName("UseByDate")
    public String  UseByDate;
    @SerializedName("CustomerRef")
    public String CustomerRef;
    @SerializedName("ScannedBy")
    public String ScannedBy;
    @SerializedName("Status")
    public byte Status;
    @SerializedName("returnResult")
    public String returnResult;
    @SerializedName("UserID")
    public String UserID;
    @SerializedName("PltScannedQty")
    public String PltScannedQty;
    @SerializedName("DispatchingOrderDetailID")
    public UUID DispatchingOrderDetailID;
    @SerializedName("ConfirmStatus")
    public String ConfirmStatus;
    @SerializedName("AllowUpdate")
    public int AllowUpdate;
    @SerializedName("returnStatus")
    public String returnStatus;
    @SerializedName("CanMove")
    public int CanMove;

    public String getOrderDate() {
        return Utilities.formatDate_ddMMyy(OrderDate);
    }

    public String getProductionDate() {
        return Utilities.formatDate_ddMMyyyy(ProductionDate);
    }

    public String getUseByDate() {
        return Utilities.formatDate_ddMMyyyy(UseByDate);
    }
}
