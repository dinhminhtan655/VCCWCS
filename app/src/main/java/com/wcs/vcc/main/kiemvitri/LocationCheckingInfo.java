package com.wcs.vcc.main.kiemvitri;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/4/2016.
 */
public class LocationCheckingInfo {
    @SerializedName("LocationNumber")
    private String LocationNumber;
    @SerializedName("CustomerNumber")
    private String CustomerNumber;
    @SerializedName("CustomerName")
    private String CustomerName;
    @SerializedName("ProductNumber")
    private String ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("CustomerRef")
    private String CustomerRef;
    public int PalletNumber;
    public String ReceivingOrderNumber;
    private UUID PalletID;
    private UUID ReceivingOrderID;
    @SerializedName("CurrentQuantity")
    private int CurrentQuantity;
    @SerializedName("ProductionDate")
    private String ProductionDate;
    @SerializedName("UseByDate")
    private String UseByDate;
    @SerializedName("StoreID")
    private int StoreID;

    public int getCurrentQuantity() {
        return CurrentQuantity;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public String getCustomerRef() {
        return CustomerRef;
    }

    public String getLocationNumber() {
        return LocationNumber;
    }

    public String getProductionDate() {
        return Utilities.formatDate_ddMMyy(ProductionDate);
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public String getUseByDate() {
        return Utilities.formatDate_ddMMyy(UseByDate);
    }


}
