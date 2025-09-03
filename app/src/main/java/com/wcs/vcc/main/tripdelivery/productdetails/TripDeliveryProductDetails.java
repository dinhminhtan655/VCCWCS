package com.wcs.vcc.main.tripdelivery.productdetails;

import com.wcs.vcc.utilities.Utilities;

import java.util.UUID;

public class TripDeliveryProductDetails {
    public UUID TripDetailID;
    public String TripDetailNumber;
    public String OrderNumber;
    public int TotalPackages;
    public int TotalUnits;
    public int DeliveryActual;
    public float TotalWeight;
    public String TripStatusDescriptions;
    public String CustomerNumber;
    public String CustomerOrderNumber;
    public String ProductNumber;
    public String ProductName;
    public String CustomerRef;
    public String DispatchingOrderRemark;


    public String getTotalWeight() {
        return Utilities.formatNumber(TotalWeight);
    }

    public String getDeliveryActual() {
        return Utilities.formatNumber(DeliveryActual);
    }

    public String getTotalPackages() {
        return Utilities.formatNumber(TotalPackages);
    }
}
