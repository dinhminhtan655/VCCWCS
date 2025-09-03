package com.wcs.vcc.api;

/**
 * Created by aang on 27/07/2018.
 */

public class TripDeliveryParameter {
    private String UserName;
    private String TripDate;
    private int StoreID;

    public TripDeliveryParameter(String userName, String tripDate, int storeID) {
        UserName = userName;
        TripDate = tripDate;
        StoreID = storeID;
    }
}
