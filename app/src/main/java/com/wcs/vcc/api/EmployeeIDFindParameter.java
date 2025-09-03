package com.wcs.vcc.api;

/**
 * Created by Trần Xuân Lộc on 1/22/2016.
 */
public class EmployeeIDFindParameter {
    private int EmployeeCode;
    private int StoreID;
    private String DateIn;

    public EmployeeIDFindParameter(int employeeCode, int storeID, String dateIn) {
        EmployeeCode = employeeCode;
        StoreID = storeID;
        DateIn = dateIn;
    }
}
