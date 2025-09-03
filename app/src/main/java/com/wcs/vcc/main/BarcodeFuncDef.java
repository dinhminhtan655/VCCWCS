package com.wcs.vcc.main;

import android.content.Context;
import android.content.Intent;

import com.wcs.vcc.main.newscanmasan.cartonnewmasan.AddNewCarton;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.ScanMasanActivity;

import java.util.ArrayList;
import java.util.Map;

public class BarcodeFuncDef {

    public static final String CARTON_NEW = "CARTON-NEW";
    public static final String CARTON_OPEN = "CARTON-OPEN";
    public static final String CARTON_CLOSE = "CARTON-CLOSE";
    public static final String STATUS_REFESH = "STATUS-REFRESH";
    public static final String GOTO_CARTON = "GOTO-CARTON";
    public static final String CHECK_STATUS = "CHECK-STATUS";
    public static final String ENTER = "ENTER";
    public static final String PRINT = "PRINT";
    public static final String NEXT_STORE_NUM = "NEXT_STORE_NUM";
    public static final String TW = "TW";


    public static final  String CT ="CT";
    public static final  String ST ="ST";
    public static final  String ITEM ="ITEM";
    public static final  String OTHER ="";

    public static String getBarcodeType(String data){
        if(data.substring(1,2).equals(CT)){
            return CT;
        }else if (data.substring(1,2).equals(ST)){
            return ST;
        }else if (data.substring(1,2).equals(TW)){
            return TW;
        }
        else{
            return ITEM;
        }
    }

    public static void execFunc(String func, Context context, Map<String, String> param) {
        if (CARTON_NEW.equals(func)) {
            Intent intent = new Intent(context, AddNewCarton.class);
            intent.putExtra("OrderDate", param.get("OrderDate"));
            intent.putExtra("STORENUM",param.get("STORENUM").toString());
            intent.putExtra("DISHPATCHING",param.get("DISHPATCHING").toString());
            context.startActivity(intent);
        } else if (CARTON_OPEN.equals(func)) {

        } else if (CARTON_CLOSE.equals(func)) {

        } else if (STATUS_REFESH.equals(func)) {

        } else if (GOTO_CARTON.equals(func)) {

        } else if (CHECK_STATUS.equals(func)) {

        } else if (!func.equals(CARTON_NEW) || !func.equals(CARTON_OPEN) || !func.equals(CARTON_CLOSE) ||
                !func.equals(STATUS_REFESH) || !func.equals(GOTO_CARTON) || !func.equals(CHECK_STATUS) || !func.equals(PRINT) || !func.substring(0, 2).equals("ST")
                || !func.substring(0, 2).equals("CT") || !func.substring(0, 2).equals("TW")) {
            Intent intent = new Intent(context, ScanMasanActivity.class);
            context.startActivity(intent);
        }

    }

    public static String getItemCode_MASAN(String barcode){
        String curBarcode20 = "";
        if(barcode.length() < 20){
            curBarcode20 = barcode;
        }
        else if (barcode.length() == 20){
            curBarcode20 = barcode.substring(1,6);
        }else if (barcode.length() > 20){
            ArrayList<String> al = new ArrayList<String>();
            String curBarcodeSplit[] = barcode.split("/");
            for (String s: curBarcodeSplit){
                al.add(s);
            }
            curBarcode20 = curBarcodeSplit[al.size() - 1];
            curBarcode20 = curBarcode20.substring(1,6);
        }
        return curBarcode20;
    }

    public static int checkFucn(String func) {
        //2: Barcode Type vd: CT=> Carton, PA=>Pallet, ST=>Store Number, TW-Trip
        //1: func
        //0: Not func

        if (CARTON_NEW.equals(func)) {
            return 1;
        } else {
            return 0;
        }
    }

}
