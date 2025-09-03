package com.wcs.vcc.api;

public class ContInOutParameter {

    private String CustomerNumber;
    private String ContainerNum;
    private String DriverMobilePhone;
    private String IDCard;
    private String Reason;

    public ContInOutParameter(String customerNumber, String containerNum, String driverMobilePhone, String IDCard, String reason) {
        CustomerNumber = customerNumber;
        ContainerNum = containerNum;
        DriverMobilePhone = driverMobilePhone;
        this.IDCard = IDCard;
        Reason = reason;
    }
}
