package com.wcs.vcc.api;

import com.google.gson.annotations.SerializedName;

public class XDockVinInboundItemScan {

    @SerializedName("Supplier_Code")
    public String Supplier_Code;
    @SerializedName("Supplier_Name")
    public String Supplier_Name;
    @SerializedName("Item_Code")
    public String Item_Code;
    @SerializedName("Item_Name")
    public String Item_Name;
    @SerializedName("Booking")
    public int Booking;
    @SerializedName("UoM_Code")
    public String UoM_Code;

}
