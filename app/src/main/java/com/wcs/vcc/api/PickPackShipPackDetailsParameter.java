package com.wcs.vcc.api;

import java.util.UUID;

public class PickPackShipPackDetailsParameter {
    public UUID ProductID;
    public int DispatchingProductCartonID;

    public PickPackShipPackDetailsParameter(UUID productID, int dispatchingProductCartonID) {
        ProductID = productID;
        DispatchingProductCartonID = dispatchingProductCartonID;
    }
}
