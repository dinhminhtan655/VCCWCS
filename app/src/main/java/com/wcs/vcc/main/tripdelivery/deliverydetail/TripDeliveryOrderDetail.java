package com.wcs.vcc.main.tripdelivery.deliverydetail;

import com.wcs.vcc.utilities.Utilities;

import java.util.UUID;

/**
 * Created by aang on 27/07/2018.
 */

public class TripDeliveryOrderDetail {
    public UUID TripDetailID;
    public String OrderNumber;
    public String TripDetailNumber;
    public int TotalPackages;
    public int TotalUnits;
    public float TotalWeight;
    public float CashCollectionAmount;
    public String TripDetailRemark;
    public int TripStatus;
    public String TripStatusDescriptions;
    public String CustomerClientCode;
    public String CustomerClientName;
    public String CustomerNumber;
    public String CustomerClientAddress;
    public String ExpectedDeliveryTime;
    public String CustomerOrderNumber;
    public String DeliveredBy;
    public String DeliveredTime;
    public boolean IsRejected;
    public String RejectedRemark;

    public String getDeliveredTime() {
        return Utilities.formatDate_ddMMyyHHmm(DeliveredTime);
    }

    public String getTripDetailNumber() {
        if (TripDetailNumber != null) {
            return TripDetailNumber.trim();
        }
        return null;
    }
}
