package com.wcs.vcc.main.soki.model;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

public class ProductKg {

    @SerializedName("id")
    public String id;
    @SerializedName("ProductionDate")
    public String ProductionDate;
    @SerializedName("ExpiryDate")
    public String ExpiryDate;
    @SerializedName("ProductNumber")
    public String ProductNumber;
    @SerializedName("ProductName")
    public String ProductName;
    @SerializedName("Quantity")
    public String Quantity;
    @SerializedName("NetWeight")
    public String NetWeight;
    @SerializedName("LotNumber")
    public String LotNumber;
    @SerializedName("BarcodeString")
    public String BarcodeString;

    public String ngaySX(){
        return Utilities.formatDate_ddMMyyyy(ProductionDate);
    }

    public String ngayHSD(){
        return Utilities.formatDate_ddMMyyyy(ExpiryDate);
    }

}
