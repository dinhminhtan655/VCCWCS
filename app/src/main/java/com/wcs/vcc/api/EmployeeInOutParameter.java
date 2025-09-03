package com.wcs.vcc.api;


public class EmployeeInOutParameter {
    private String UserName;
    private String ReportDate;
    private int Department;
    private int storeId;

    public EmployeeInOutParameter(int department, String reportDate, String userName, int storeId) {
        Department = department;
        ReportDate = reportDate;
        UserName = userName;
        this.storeId = storeId;
    }
}



