package com.wcs.vcc.main.scanbarcode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

import java.util.UUID;

// day la 1 class duoc minh dinh nghia
public class STAndroid_CartonScannedByDO {
    @SerializedName("DispatchingOrderDetailID")
    public UUID DispatchingOrderDetailID;
    @SerializedName("ProductionDate")
    public String ProductionDate;
    @SerializedName("UseByDate")
    public String UseByDate;
    @SerializedName("ProductName")
    public String ProductName;
    @SerializedName("ProductNumber")
    public String ProductNumber;
    @SerializedName("CartonWeight")
    @Expose
    public String CartonWeight;
    @SerializedName("BarcodeString")
    public String BarcodeString;
    @SerializedName("PalletNumber")
    public String PalletNumber;
    @SerializedName("Label")
    public String Label;
    @SerializedName("CustomerRef")
    public String CustomerRef;
    @SerializedName("DispatchingCartonID")
    public UUID DispatchingCartonID;
    @SerializedName("CreatedTime")
    private String CreatedTime;

    public String getCreatedTime() {
        if (CreatedTime != null) {
            return Utilities.formatDate_ddMMHHmm(CreatedTime);
        }
        return "";
    }

}
