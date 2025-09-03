package com.wcs.vcc.api;

public class DriverChangeParameter extends BaseParameter{
    private int DriverID;
    private int TruckID;
    private int DistributionOrderID;

    public DriverChangeParameter(String userName, int driverId, int truckId, int distributionOrderID) {
        super(userName);
        DriverID = driverId;
        TruckID = truckId;
        DistributionOrderID = distributionOrderID;
    }
}
