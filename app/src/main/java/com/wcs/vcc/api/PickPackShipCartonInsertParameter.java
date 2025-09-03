package com.wcs.vcc.api;

public class PickPackShipCartonInsertParameter extends BaseParam {
    private String DispatchingOrderNumber;

    public PickPackShipCartonInsertParameter(String username, String deviceNumber, String dispatchingOrderNumber) {
        super(username, deviceNumber);
        DispatchingOrderNumber = dispatchingOrderNumber;
    }
}
