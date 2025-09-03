package com.wcs.vcc.main.scannewzealand.model;

import com.google.gson.annotations.SerializedName;

public class XDockVinOutboundPackingViewSupplierNewZealandABA {

    @SerializedName("supplierName")
    public String Supplier_Name ;
    @SerializedName("supplierID")
    public String Supplier_Code;
    @SerializedName("totalBich")
    public int totalBich;
    @SerializedName("totalMove")
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
