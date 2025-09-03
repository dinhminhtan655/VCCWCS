package com.wcs.vcc.api;

import com.google.gson.annotations.SerializedName;

public class XDockVinOutboundPackingViewSupplierABA {

    @SerializedName("SupplierName")
    public String Supplier_Name;
    @SerializedName("SupplierID")
    public String Supplier_Code;
    @SerializedName("TotalBich")
    public int totalBich;
    @SerializedName("TotalMove")
    public int totalMove;

    public int colorWaiting;

    public int colorWorking;

    public int colorCompleted;

    public int getColorWaiting() {
        return colorWaiting;
    }

    public void setColorWaiting(int colorWaiting) {
        this.colorWaiting = colorWaiting;
    }

    public int getColorWorking() {
        return colorWorking;
    }

    public void setColorWorking(int colorWorking) {
        this.colorWorking = colorWorking;
    }

    public int getColorCompleted() {
        return colorCompleted;
    }

    public void setColorCompleted(int colorCompleted) {
        this.colorCompleted = colorCompleted;
    }

}
