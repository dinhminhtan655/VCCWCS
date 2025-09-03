package com.wcs.vcc.api;

import java.util.UUID;

public class PalletCartonWeightDeleteParam extends BaseParam{
    private UUID PalletCartonID;
    private String Reason;
    private String DispatchingOrderNumber;//buu-2021-08-08

    public PalletCartonWeightDeleteParam(String username, String deviceNumber, UUID palletCartonID, String reason, String dispatchingOrderNumber) {
        super(username, deviceNumber);
        PalletCartonID = palletCartonID;
        Reason = reason;
        DispatchingOrderNumber  = dispatchingOrderNumber;
    }
}
