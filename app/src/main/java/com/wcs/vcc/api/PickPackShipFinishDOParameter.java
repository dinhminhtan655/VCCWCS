package com.wcs.vcc.api;

public class PickPackShipFinishDOParameter extends BaseParam {
    private String DispatchingOrderNumber;

    public PickPackShipFinishDOParameter(String username, String deviceNumber, String dispatchingOrderNumber) {
        super(username, deviceNumber);
        DispatchingOrderNumber = dispatchingOrderNumber;
    }
}
