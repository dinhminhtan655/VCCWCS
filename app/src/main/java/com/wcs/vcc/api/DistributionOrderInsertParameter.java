package com.wcs.vcc.api;

public class DistributionOrderInsertParameter {
    private int TruckDriverHistoryID;
    private int TruckID;
    private Double Kilometer;
    private Double Quantity;
    private String DistributionRemark;
    private String UserName;

    public DistributionOrderInsertParameter(int truckDriverHistoryID, int truckID, Double kilometer, Double quantity, String distributionRemark, String userName) {
        TruckDriverHistoryID = truckDriverHistoryID;
        TruckID = truckID;
        Kilometer = kilometer;
        Quantity = quantity;
        DistributionRemark = distributionRemark;
        UserName = userName;
    }
}
