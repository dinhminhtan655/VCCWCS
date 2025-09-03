package com.wcs.vcc.main.nhapngoaigio.detail;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/22/2016.
 */
public class OverTimeViewInfo {
    @SerializedName("EmployeeOTSupervisorID")
    private UUID EmployeeOTSupervisorID;
    @SerializedName("EmployeeOTSupervisorDate")
    private String EmployeeOTSupervisorDate;
    @SerializedName("AuthorisedBy")
    private String AuthorisedBy;
    @SerializedName("EmployeeID")
    private UUID EmployeeID;
    @SerializedName("VietnamName")
    private String VietnamName;
    @SerializedName("HourQuantity")
    private float HourQuantity;
    @SerializedName("EmployeeOTSupervisorConfirm")
    private boolean EmployeeOTSupervisorConfirm;
    @SerializedName("Remarks")
    private String Remarks;
    @SerializedName("DayStatus")
    private String DayStatus;
    @SerializedName("TimeWork")
    private String TimeWork;
    public int EmployeeCode;

    public String getAuthorisedBy() {
        return AuthorisedBy;
    }

    public String getDayStatus() {
        return DayStatus;
    }

    public UUID getEmployeeID() {
        return EmployeeID;
    }

    public boolean isEmployeeOTSupervisorConfirm() {
        return EmployeeOTSupervisorConfirm;
    }

    public String getEmployeeOTSupervisorDate() {
        return EmployeeOTSupervisorDate;
    }

    public UUID getEmployeeOTSupervisorID() {
        return EmployeeOTSupervisorID;
    }

    public float getHourQuantity() {
        return HourQuantity;
    }

    public String getRemarks() {
        return Remarks;
    }

    public String getTimeWork() {
        return TimeWork;
    }

    public String getVietnamName() {
        return VietnamName;
    }
}
