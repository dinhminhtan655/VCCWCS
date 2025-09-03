package com.wcs.vcc.main.containerandtruckinfor.model;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

public class CarNumber {

    @SerializedName("UserCheckOut")
    public boolean userCheckOut;
    @SerializedName("CheckOut")
    public boolean checkOut;
    @SerializedName("ContainerNum")
    public String containerNum;
    @SerializedName("TimeIn")
    private String TimeIn;
    @SerializedName("TimeOut")
    private String TimeOut;
    @SerializedName("OrderNumber")
    private String OrderNumber;

    public String getTimeIn() {
        return Utilities.formatDate_HHmm(TimeIn);
    }

    public void setTimeIn(String timeIn) {
        TimeIn = timeIn;
    }

    public String getTimeOut() {
        return Utilities.formatDate_HHmm(TimeOut);
    }

    public void setTimeOut(String timeOut) {
        TimeOut = timeOut;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return
                "Số xe: " + containerNum + "\n" +
                "Thời gian vào: " + getTimeIn() + "\n" +
                "Thời gian ra: " + getTimeOut();
    }
}
