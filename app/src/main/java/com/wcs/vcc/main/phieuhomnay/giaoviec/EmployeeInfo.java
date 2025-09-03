package com.wcs.vcc.main.phieuhomnay.giaoviec;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.main.mms.employee.EmployeeAbstract;
import com.wcs.vcc.utilities.Utilities;

import java.util.UUID;

/**
 * Created by Trần Xuân Lộc on 1/12/2016.
 */
public class EmployeeInfo extends EmployeeAbstract {
    @SerializedName("Position")
    public String Position;
    @SerializedName("EmployeeID")
    private UUID employeeID;
    @SerializedName("EmployeeName")
    private String employeeName;
    @SerializedName("TimeIn")
    private String TimeIn;
    public int EmployeeCode;

    public EmployeeInfo(int employeeCode, String employeeName) {
        EmployeeCode = employeeCode;
        this.employeeName = employeeName;
    }


    public String getTimeIn() {
        return Utilities.formatDate_HHmm(TimeIn);
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    @Override
    public String toString() {
        return String.valueOf(EmployeeCode);
    }

    @Override
    public boolean isDetail() {
        return false;
    }
}
