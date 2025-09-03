package com.wcs.vcc.main.bigcqa.bigccheckout;

import com.google.gson.annotations.SerializedName;

public class BigDock_TripDetails {
    @SerializedName("ProductNumber")
    public String ProductNumber;
    @SerializedName("ProductName")
    public String ProductName;
    @SerializedName("Basket1")
    public int Basket1;
    @SerializedName("Basket2")
    public int Basket2;
    @SerializedName("Basket3")
    public int Basket3;
    @SerializedName("Basket4")
    public int Basket4;
    @SerializedName("Basket5")
    public int Basket5;
    @SerializedName("Basket6")
    public int Basket6;
    @SerializedName("Basket7")
    public int Basket7;
    @SerializedName("Basket8")
    public int Basket8;
    @SerializedName("DispatchedActualWeight")
    public double DispatchedActualWeight;
    @SerializedName("ScannedResult")
    public String ScannedResult;
    @SerializedName("ScannedStatus")
    public int ScannedStatus;
    @SerializedName("ScannedTime")
    public String ScannedTime;
    @SerializedName("ScannedBy")
    public String ScannedBy;

    public int colorWaiting;

    public int colorFailed;

    public int colorCompleted;

    public int getColorWaiting() {
        return colorWaiting;
    }

    public void setColorWaiting(int colorWaiting) {
        this.colorWaiting = colorWaiting;
    }

    public int getColorFailed() {
        return colorFailed;
    }

    public void setColorFailed(int colorFailed) {
        this.colorFailed = colorFailed;
    }

    public int getColorCompleted() {
        return colorCompleted;
    }

    public void setColorCompleted(int colorCompleted) {
        this.colorCompleted = colorCompleted;
    }
}
