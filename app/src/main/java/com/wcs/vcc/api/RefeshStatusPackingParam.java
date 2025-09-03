package com.wcs.vcc.api;

import java.util.UUID;

public class RefeshStatusPackingParam {
    public UUID DispatchingOrderID;

    public RefeshStatusPackingParam(UUID dispatchingOrderID) {
        DispatchingOrderID = dispatchingOrderID;
    }
}
