package com.wcs.vcc.api;

public class DistributionOrderCheckOutParameter {
    private int DistributionOrderID;
    private boolean IsCheckOut;
    private String DistributionRemark;
    private String UserName;

    public DistributionOrderCheckOutParameter(int distributionOrderID, boolean isCheckOut, String distributionRemark, String userName) {
        DistributionOrderID = distributionOrderID;
        IsCheckOut = isCheckOut;
        DistributionRemark = distributionRemark;
        UserName = userName;
    }
}
