package com.wcs.vcc.main.scannewzealand.model;

import com.google.gson.annotations.SerializedName;

public class XDockOutboundPackingViewSupplierProductsNewZealandABA {

    @SerializedName("itemID")
    public String Item_Code;
    @SerializedName("itemName")
    public String Item_Name;
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

    public String statusProduct(){
        if(totalMove == 0){
            return "Chưa";
        }else if (totalMove > 0 && totalMove < totalBich){
            return "Đang";
        }else if(totalMove == totalBich){
            return "Xong";
        }
        return "";
    }

}
