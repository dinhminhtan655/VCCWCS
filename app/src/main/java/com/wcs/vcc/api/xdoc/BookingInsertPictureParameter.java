package com.wcs.vcc.api.xdoc;

public class BookingInsertPictureParameter {

    private String UserName;
    private String FileData;
    private String OrderNumber;

    public BookingInsertPictureParameter(String userName, String fileData, String orderNumber) {
        UserName = userName;
        FileData = fileData;
        OrderNumber = orderNumber;
    }

    public BookingInsertPictureParameter() {
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setFileData(String fileData) {
        FileData = fileData;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }
}
