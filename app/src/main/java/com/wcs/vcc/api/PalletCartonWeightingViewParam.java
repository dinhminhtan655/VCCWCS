package com.wcs.vcc.api;

/**
 * Created by aang on 02/06/2018.
 */

public class PalletCartonWeightingViewParam extends BaseParam{
    private int PalletNumber;
    private String DispatchingOrderNumber;
    private String CartonWeight;

    public PalletCartonWeightingViewParam(String username, String deviceNumber, int palletNumber,String dispatchingOrderNumber) {
        super(username, deviceNumber);
        PalletNumber = palletNumber;
        DispatchingOrderNumber  = dispatchingOrderNumber;
    }

    public PalletCartonWeightingViewParam(String username, String deviceNumber, int palletNumber,String dispatchingOrderNumber, String cartonWeight) {
        super(username, deviceNumber);
        PalletNumber = palletNumber;
        DispatchingOrderNumber  = dispatchingOrderNumber;
        CartonWeight = cartonWeight;
    }



}
