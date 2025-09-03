package com.wcs.vcc.api;

/**
 * Created by aang on 27/07/2018.
 */

public class TripDeliveryDetailStatusUpdateParameter {
    private String UserName;
    private String DeviceNumber;
    private String TripDetailID;
    private int TripStatus;
    private boolean IsRejected;
    private String RejectedRemark;

    public TripDeliveryDetailStatusUpdateParameter(String userName, String deviceNumber, String tripDetailID, int tripStatus, boolean isRejected, String rejectedRemark) {
        UserName = userName;
        DeviceNumber = deviceNumber;
        TripDetailID = tripDetailID;
        TripStatus = tripStatus;
        IsRejected = isRejected;
        RejectedRemark = rejectedRemark;
    }
}
