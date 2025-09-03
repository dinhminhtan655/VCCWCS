package com.wcs.vcc.main.containerandtruckinfor.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by tranxuanloc on 6/17/2016.
 */
public class ContainerAndTruckInfo implements Serializable {
    public UUID ContInOutID;
    public UUID CustomerID;
    @SerializedName("CustomerNumber")
    public String CustomerNumber;
    @SerializedName("CustomerName")
    public String CustomerName;
    @SerializedName("ContainerNum")
    public String ContainerNum;
    @SerializedName("TruckIn")
    public String TruckIn;
    @SerializedName("OrderNumber")
    public String OrderNumber;
    @SerializedName("ContainerType")
    public String ContainerType;
    @SerializedName("Reason")
    public String Reason;
    @SerializedName("TimeIn")
    public String TimeIn;
    @SerializedName("TimeOut")
    public String TimeOut;
    @SerializedName("ExpectedProcessTime")
    public String ExpectedProcessTime;
    @SerializedName("DefaultProcessTime")
    public String DefaultProcessTime;
    @SerializedName("DriverMobilePhone")
    public long DriverMobilePhone;
    @SerializedName("DriverName")
    public String DriverName;
    @SerializedName("SealNumber")
    public String SealNumber;
    @SerializedName("CustomerRequirement")
    public String CustomerRequirement;
    @SerializedName("TaskProgress")
    public int TaskProgress;
    @SerializedName("DriverIDCardNo")
    public String DriverIDCardNo;
    @SerializedName("ContInOutNumber")
    public String ContInOutNumber;
    @SerializedName("DockDoorID")
    public int DockDoorID;
    @SerializedName("Remarks")
    public String Remarks;
    @SerializedName("CheckOut")
    public boolean CheckOut;
    @SerializedName("UserCheckOut")
    public boolean UserCheckOut;
    public boolean bInOut;
    @SerializedName("OrderDate")
    public String OrderDate;
    @SerializedName("StartTime")
    public String StartTime;
    @SerializedName("EndTime")
    public String EndTime;
    @SerializedName("StartTime1")
    public String StartTime1;
    @SerializedName("EndTime1")
    public String EndTime1;

    public ContainerAndTruckInfo( String customerNumber, String customerName, String containerNum, String containerType, String reason, String timeIn,
                                  String expectedProcessTime, String defaultProcessTime, long driverMobilePhone, String customerRequirement, int taskProgress,
                                  String driverIDCardNo, String contInOutNumber) {
        CustomerNumber = customerNumber;
        CustomerName = customerName;
        ContainerNum = containerNum;
        ContainerType = containerType;
        Reason = reason;
        TimeIn = timeIn;
        ExpectedProcessTime = expectedProcessTime;
        DefaultProcessTime = defaultProcessTime;
        DriverMobilePhone = driverMobilePhone;
        CustomerRequirement = customerRequirement;
        TaskProgress = taskProgress;
        DriverIDCardNo = driverIDCardNo;
        ContInOutNumber = contInOutNumber;
    }

    @Override
    public String toString() {
        return ContainerNum;
    }
}
