package com.wcs.vcc.api;

public class PalletCartonWeightManualInsertParam extends BaseParam {
    private int PalletNumber;
    private double CartonWeight;
    private double CartonWeightPay;
    private double PalletGrossWeight;
    private int CartonUnits;
    private String PalletRemark;
    private int StoreID;

    public PalletCartonWeightManualInsertParam(String username, String deviceNumber, int palletNumber, double cartonWeight, double cartonWeightPay, double palletGrossWeight, int cartonUnits, String palletRemark, int storeID) {
        super(username, deviceNumber);
        PalletNumber = palletNumber;
        CartonWeight = cartonWeight;
        CartonWeightPay = cartonWeightPay;
        PalletGrossWeight = palletGrossWeight;
        CartonUnits = cartonUnits;
        PalletRemark = palletRemark;
        StoreID = storeID;
    }
}
