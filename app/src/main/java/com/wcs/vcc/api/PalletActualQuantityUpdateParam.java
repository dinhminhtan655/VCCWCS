package com.wcs.vcc.api;

import java.util.UUID;

public class PalletActualQuantityUpdateParam extends BaseParam{
    public String OrderNumber;
    public int PalletNumber;
    public int ActualQuantity;
    public UUID DispatchingOrderDetailID;

    public PalletActualQuantityUpdateParam(String username, String deviceNumber, String orderNumber, int palletNumber, int actualQuantity, UUID dispatchingOrderDetailID) {
        super(username, deviceNumber);
        OrderNumber = orderNumber;
        PalletNumber = palletNumber;
        ActualQuantity = actualQuantity;
        DispatchingOrderDetailID = dispatchingOrderDetailID;
    }
}
