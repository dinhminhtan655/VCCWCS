package com.wcs.vcc.api;

/**
 * Created by aang on 14/09/2018.
 */

public class CustomerRequirementParam {
    private String OrderNumber;
    private int StoreID = 1;

    public CustomerRequirementParam(String orderNumber) {
        OrderNumber = orderNumber;
    }
}
