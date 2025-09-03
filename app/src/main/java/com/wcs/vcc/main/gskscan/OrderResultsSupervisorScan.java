package com.wcs.vcc.main.gskscan;

import com.wcs.vcc.utilities.Utilities;

/**
 * Created by aang on 20/05/2018.
 */

public class OrderResultsSupervisorScan {
    public String OrderNumber;//bỏ
    public String CustomerNumber;
    public String VehicleNumber;
    public String ScannedBy;//bỏ
    public int TotalPackages;//bỏ
    private String CreatedTime;//bỏ
    public boolean IsRecordNew;

    public String DockInScannedBy;
    public String DockOutScannedBy;
    public String DriverName;
    public String DriverMobilePhone;
    private String StartWorkingTime;
    private String EndWorkingTime;

    public String getCreatedTime() {
        if (CreatedTime != null) {
            return Utilities.formatDate_ddMMHHmm(CreatedTime);
        }
        return "";
    }
    public String getStartWorkingTime() {
        if (StartWorkingTime != null) {
            return Utilities.formatDate_ddMMHHmm(StartWorkingTime);
        }
        return "";
    }
    public String getEndWorkingTime() {
        if (EndWorkingTime != null) {
            return Utilities.formatDate_ddMMHHmm(EndWorkingTime);
        }
        return "";
    }
}
