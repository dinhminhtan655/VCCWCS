package com.wcs.vcc.api;

/**
 * Created by tranxuanloc on 4/13/2016.
 */
public class OverTimeEntryParameter {
    private int EmployeeCode;
    private String FromDate;
    private String ToDate;
    private float HourQuantity;
    private String DayStatus;
    private String UserName;//AuthorisedBy
    private String Remarks;
    private boolean Flag;
    private int StoreID = 1;

    public OverTimeEntryParameter(String dayStatus,
                                  int employeeCode,
                                  boolean flag,
                                  String fromDate,
                                  float hourQuantity,
                                  String remarks,
                                  String toDate,
                                  String userName) {
        DayStatus = dayStatus;
        EmployeeCode = employeeCode;
        Flag = flag;
        FromDate = fromDate;
        HourQuantity = hourQuantity;
        Remarks = remarks;
        ToDate = toDate;
        UserName = userName;
    }
}
