package com.wcs.vcc.api;

/**
 * Created by Trần Xuân Lộc on 1/13/2016.
 */
public class DeleteEmployeeIDGiaoViecParameter {
    int EmployeeWorkingID;
    public String UserName;

    public DeleteEmployeeIDGiaoViecParameter(int employeeWorkingID, String userName) {
        EmployeeWorkingID = employeeWorkingID;
        UserName = userName;
    }
}
