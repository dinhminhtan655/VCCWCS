package com.wcs.vcc.main.capdau;

import com.wcs.vcc.utilities.Utilities;

public class TruckDriver {
    public int TruckDriverHistoryID;
    public int TruckID;
    public String TruckNumber;
    public String DriverCode;
    public String DriverName;
    public int DistributionOrderID;
    public String TimeIn;
    public double Quantity;
    public double Kilometer;
    public double AVGQty;
    public double KilometerQty;
    public String DistributionRemark;
    public double KilometerLatest;
    public double NormsWarning;
    public String AttachmentFile = "";

    public String getTimeIn() {
        return Utilities.formatDate_ddMMyyHHmm(TimeIn);
    }

    @Override
    public String toString() {
        return TruckNumber;
    }
}
