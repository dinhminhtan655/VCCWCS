package com.wcs.vcc.api;

public class DistributionOrderUpdateParameter {
    private int DistributionOrderID;
    private double Kilometer;
    private double Quantity;
    private String DistributionRemark;
    private String UserName;
    private int PumpTrigger;

    public DistributionOrderUpdateParameter(int distributionOrderID, double kilometer, double quantity, int pumpTrigger, String distributionRemark, String userName) {
        DistributionOrderID = distributionOrderID;
        Kilometer = kilometer;
        Quantity = quantity;
        DistributionRemark = distributionRemark;
        UserName = userName;
        PumpTrigger = pumpTrigger;
    }
}
