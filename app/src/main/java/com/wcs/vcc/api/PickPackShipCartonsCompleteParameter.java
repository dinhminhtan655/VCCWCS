package com.wcs.vcc.api;

public class PickPackShipCartonsCompleteParameter extends BaseParam {
    private int DispatchingProductCartonID;
    private boolean Completed;

    public PickPackShipCartonsCompleteParameter(String username, String deviceNumber, int dispatchingProductCartonID, boolean completed) {
        super(username, deviceNumber);
        DispatchingProductCartonID = dispatchingProductCartonID;
        Completed = completed;
    }
}
