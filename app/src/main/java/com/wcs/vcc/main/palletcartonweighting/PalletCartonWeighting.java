package com.wcs.vcc.main.palletcartonweighting;

import java.util.UUID;

/**
 * Created by aang on 02/06/2018.
 */

public class PalletCartonWeighting {
    public UUID PalletCartonID;
    public UUID PalletID;
    public int PalletNumber;
    public int PalletCartonNumber;
    public float CartonWeight;
    public float PalletGrossWeight;
    public int CartonUnits;
    public String PalletRemark;
    private String CreatedBy;
    private String CreatedTime;
    public Boolean IsRecordNew;
    public boolean isChecked;
    public int color;
    public String BarcodeString;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getCartonWeight(){
       if(CartonWeight==0) return "";

     return   String.valueOf(CartonWeight);
   }

    public void settCartonWeight(String input){
       if(input=="")
           CartonWeight=0;
       else
           CartonWeight=Float.parseFloat(input);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
