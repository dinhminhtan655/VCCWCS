package com.wcs.vcc.api;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/8/2016.
 */
public class StockOnHandByCustomerParameter {
    private UUID CustomerID;

    public StockOnHandByCustomerParameter(UUID customerID) {
        CustomerID = customerID;
    }
}
