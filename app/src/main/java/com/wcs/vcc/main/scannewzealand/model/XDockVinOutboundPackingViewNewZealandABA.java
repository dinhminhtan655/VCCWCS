package com.wcs.vcc.main.scannewzealand.model;

import com.google.gson.annotations.SerializedName;

public class XDockVinOutboundPackingViewNewZealandABA {

    @SerializedName("id")
    public String Doc_Entry;
    @SerializedName("palletID")
    public String Pallet_ID;
    @SerializedName("soBich")
    public double SoBich;
    @SerializedName("quantityScan")
    public String SLScan;
    @SerializedName("quantityMove")
    public String SLMove;
    @SerializedName("quantityModify")
    public String SLDieuChinh;
    @SerializedName("confirm")
    public String XacNhan;
    @SerializedName("itemName")
    public String Item_Name;
    @SerializedName("soThung")
    public String soThung;

    public boolean enalble;

    public int colorWaiting;

    public int colorWorking;

    public int colorCompleted;

    public boolean isEnalble() {
        return enalble;
    }

    public void setEnalble(boolean enalble) {
        this.enalble = enalble;
    }

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

    public String getSoBich(){
        int soBichInt = (int)SoBich;
        return String.valueOf(soBichInt);
    }

    public String getConfirmQuantity(){
        return String.valueOf(Integer.parseInt(SLScan) + Integer.parseInt(SLMove) + Integer.parseInt(SLDieuChinh));
    }

    public String getThieuDu(){
        int a = (int) SoBich - Integer.parseInt(getConfirmQuantity());
        return String.valueOf(a);
    }
}
