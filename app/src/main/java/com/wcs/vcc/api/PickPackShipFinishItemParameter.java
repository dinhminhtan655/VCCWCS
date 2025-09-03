package com.wcs.vcc.api;

public class PickPackShipFinishItemParameter extends BaseParam {
    private String DispatchingOrderNumber;
    private String ProductNumber;

    public PickPackShipFinishItemParameter(String username, String deviceNumber, String dispatchingOrderNumber, String productNumber) {
        super(username, deviceNumber);
        DispatchingOrderNumber = dispatchingOrderNumber;
        ProductNumber = productNumber;
    }
}
