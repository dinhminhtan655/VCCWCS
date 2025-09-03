package com.wcs.vcc.api;

/**
 * Created by tranxuanloc on 4/22/2016.
 */
public class OverTimeViewParameter {
    private String FromDate;
    private String ToDate;
    private String UserName;
    private int EmployeeCode;
    private short SortFlag;
    private int varStoreID = 1;


    public OverTimeViewParameter(int employeeCode, String fromDate, short sortFlag, String toDate, String userName) {
        EmployeeCode = employeeCode;
        FromDate = fromDate;
        SortFlag = sortFlag;
        ToDate = toDate;
        UserName = userName;
    }
}
