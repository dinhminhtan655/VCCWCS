package com.wcs.vcc.api;


public class EmployeeWorkingByDateParameter {
    private String varDate;
    private int DepartmentID;
    private int ShiftName;
    private int PositionID;
    private int storeId;

    public EmployeeWorkingByDateParameter(String varDate, int departmentID, int shiftName, int positionID, int storeId) {
        this.varDate = varDate;
        DepartmentID = departmentID;
        ShiftName = shiftName;
        PositionID = positionID;
        this.storeId = storeId;
    }
}
