package com.wcs.vcc.main.tripdelivery.orderlist;

import java.util.UUID;

/**
 * Created by aang on 27/07/2018.
 */

public class TripDeliveryDetail {
    public UUID TripDetailID;
    public String OrderNumber;
    public int TotalPackages;
    public int TotalUnits;
    public float TotalWeight;
    public float CashCollectionAmount;
    public String TripStatusDescriptions;
    public String TripDetailRemark;
    public String CustomerClientCode;
    public String CustomerClientName;
    public String CustomerClientAddress;
    public String ExpectedDeliveryTime;
    public double LAT;
    public double LON;
}
