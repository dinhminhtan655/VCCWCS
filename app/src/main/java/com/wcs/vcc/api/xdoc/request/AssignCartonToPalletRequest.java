package com.wcs.vcc.api.xdoc.request;

public class AssignCartonToPalletRequest {
    private String UserName;
    private  String OrderDate;
    private Integer StoreNumber;
    private String DeviceNumber;
    private String BarcodeString;
    private Integer Inner;
    private String DispatchingOrderNumber;

    public String getDispatchingOrderNumber() {
        return DispatchingOrderNumber;
    }

    public void setDispatchingOrderNumber(String dispatchingOrderNumber) {
        DispatchingOrderNumber = dispatchingOrderNumber;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public Integer getStoreNumber() {
        return StoreNumber;
    }

    public void setStoreNumber(Integer storeNumber) {
        StoreNumber = storeNumber;
    }

    public String getDeviceNumber() {
        return DeviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        DeviceNumber = deviceNumber;
    }

    public String getBarcodeString() {
        return BarcodeString;
    }

    public void setBarcodeString(String barcodeString) {
        BarcodeString = barcodeString;
    }

    public AssignCartonToPalletRequest() {
    }

    public Integer getInner() {
        return Inner;
    }

    public void setInner(Integer inner) {
        Inner = inner;
    }

    public AssignCartonToPalletRequest(String userName, String orderDate, Integer storeNumber, String deviceNumber, String barcodeString) {
        UserName = userName;
        OrderDate = orderDate;
        StoreNumber = storeNumber;
        DeviceNumber = deviceNumber;
        BarcodeString = barcodeString;
    }

    public AssignCartonToPalletRequest(String userName, String orderDate, Integer storeNumber, String deviceNumber, String barcodeString, Integer inner, String dispatchingOrderNumber) {
        UserName = userName;
        OrderDate = orderDate;
        StoreNumber = storeNumber;
        DeviceNumber = deviceNumber;
        BarcodeString = barcodeString;
        Inner = inner;
        DispatchingOrderNumber = dispatchingOrderNumber;
    }
}
