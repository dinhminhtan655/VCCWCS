package com.wcs.vcc.main.capdau;

import com.wcs.vcc.utilities.Utilities;

public class DistributionOrderView {
    public int DistributionOrderID;
    public String DistributionOrderNumber;
    public String TruckNumber;
    public String DriverCode;
    public String DriverName;
    public String TimeIn;
    public String TimeOut;
    public double Quantity;
    public double Kilometer;
    public double Norm;
    public double WeightCapacity;
    public double VolumeCapacity;
    public boolean IsCheckOut;
    public String DistributionRemark;
    public String UserCheckOut;
    public String AttachmentFile;
    public double KilometerLatest;
    public double QuantityLatest;

    public String getTimeIn() {
        return Utilities.formatDate_ddMMHHmm(TimeIn);
    }

    public String getTimeOut() {
        return Utilities.formatDate_ddMMHHmm(TimeOut);
    }
}
