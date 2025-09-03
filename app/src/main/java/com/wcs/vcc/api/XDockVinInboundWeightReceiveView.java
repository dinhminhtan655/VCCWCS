package com.wcs.vcc.api;

import com.google.gson.annotations.SerializedName;

public class XDockVinInboundWeightReceiveView {

    @SerializedName("Doc_Number")
    public String Doc_Number;
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
    @SerializedName("Actual")
    public int Actual;
    @SerializedName("LoaiCan")
    public String LoaiCan;

    public String returnStrBooking(int Booking){
        return String.valueOf(Booking);
    }

    public String returnStrActual(int Actual){
        return String.valueOf(Actual);
    }

}
