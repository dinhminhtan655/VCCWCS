package com.wcs.vcc.api;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/9/2016.
 */
public class StockOnHandDetailsParameter {
    private UUID CustomerID;
    private UUID ProductID;

    public StockOnHandDetailsParameter(UUID customerID, UUID productID) {
        CustomerID = customerID;
        ProductID = productID;
    }
}

