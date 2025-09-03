package com.wcs.vcc.api;

public class PickPackShipPackDeleteParameter extends BaseParam {
    public int DispatchingProductPackID;

    public PickPackShipPackDeleteParameter(String username, String deviceNumber, int dispatchingProductPackID) {
        super(username, deviceNumber);
        DispatchingProductPackID = dispatchingProductPackID;
    }
}
