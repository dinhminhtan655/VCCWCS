package com.wcs.vcc.api;

import com.google.gson.annotations.SerializedName;

public class XDockVinOutboundPackingSummaryView {

    @SerializedName("Store_Code")
    public String Store_Code;
    @SerializedName("Pallet_ID")
    public String Pallet_ID;
    @SerializedName("Booking")
    public double Booking ;
    @SerializedName("SLChia")
    public double SLChia;
    @SerializedName("SLScan")
    public String SLScan;
    @SerializedName("SLMove")
    public String SLMove;
    @SerializedName("SLDieuChinh")
    public String SLDieuChinh;


    public String strBooking(){
        int iBooking = (int) Booking;
        return String.valueOf(iBooking);
    }

    public String strSLChia(){
        int iSLChia = (int) SLChia;
        return String.valueOf(iSLChia);
    }


    public String getConfirmQuantity(){
        return String.valueOf(Integer.parseInt(SLScan) + Integer.parseInt(SLMove) + Integer.parseInt(SLDieuChinh));
    }


    public String getThieuDu(){
        int a = (int)SLChia - Integer.parseInt(getConfirmQuantity());
        return String.valueOf(a);
    }

    public String getState(){
        if (Integer.parseInt(getThieuDu()) == 0){
            return "Đủ";
        }else if (Integer.parseInt(getThieuDu()) < (int)SLChia && Integer.parseInt(getThieuDu()) != 0){
            return "Thiếu";
        }else if(Integer.parseInt(getThieuDu()) == (int)SLChia){
            return "Chưa";
        }
        return "NaN";
    }


}
