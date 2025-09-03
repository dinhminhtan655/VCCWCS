package com.wcs.vcc.api;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/25/2016.
 */
public class OverTimeDelUpdateParameter {
    private UUID EmployeeOTSupervisorID;
    private String EmployeeOTSupervisorDate;
    private float HourQuantity;
    private String DayStatus;
    private String UserName;
    private String Remarks;
    private int Flag;

    public OverTimeDelUpdateParameter(String dayStatus, String employeeOTSupervisorDate, UUID employeeOTSupervisorID, int flag, float hourQuantity, String remarks, String userName) {
        DayStatus = dayStatus;
        EmployeeOTSupervisorDate = employeeOTSupervisorDate;
        EmployeeOTSupervisorID = employeeOTSupervisorID;
        Flag = flag;
        HourQuantity = hourQuantity;
        Remarks = remarks;
        UserName = userName;
    }
}
