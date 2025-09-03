package com.wcs.vcc.api;

public class PickPackShipCartonDeleteParameter extends BaseParam{
    private int DispatchingProductCartonID;

    public PickPackShipCartonDeleteParameter(String username, String deviceNumber, int dispatchingProductCartonID) {
        super(username, deviceNumber);
        DispatchingProductCartonID = dispatchingProductCartonID;
    }
}
