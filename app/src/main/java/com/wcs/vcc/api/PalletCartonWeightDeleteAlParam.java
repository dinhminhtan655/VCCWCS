package com.wcs.vcc.api;

public class PalletCartonWeightDeleteAlParam extends BaseParam {
    private int PalletNumber;
    private String Reason;
    private String DispatchingOrderNumber;

    public PalletCartonWeightDeleteAlParam(String username, String deviceNumber, int palletNumber, String reason, String dispatchingOrderNumber) {
        super(username, deviceNumber);
        PalletNumber = palletNumber;
        Reason = reason;
        DispatchingOrderNumber = dispatchingOrderNumber;
    }
}
