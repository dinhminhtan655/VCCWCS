package com.wcs.vcc.api.xdoc.response;

import com.google.gson.annotations.SerializedName;

public class PackingList {

    @SerializedName("Doc_Entry")
    public String Doc_Entry;
    @SerializedName("Pallet_ID")
    public String Pallet_ID;
    @SerializedName("Item_Name")
    public String Item_Name;
    @SerializedName("SoBich")
    public String SoBich;
    @SerializedName("SLScan")
    public String SLScan;
    @SerializedName("SLMove")
    public String SLMove;
    @SerializedName("SLDieuChinh")
    public String SLDieuChinh;
    @SerializedName("XacNhan")
    public String XacNhan;

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
        int soBichInt = Integer.parseInt(SoBich);
        return String.valueOf(soBichInt);
    }

    public String getConfirmQuantity(){
        return String.valueOf(Integer.parseInt(getSoBich())- Integer.parseInt(SLDieuChinh));
    }

    public String getThieuDu(){
        int a = Integer.parseInt(SoBich) - Integer.parseInt(getConfirmQuantity());
        return String.valueOf(a);
    }

}
