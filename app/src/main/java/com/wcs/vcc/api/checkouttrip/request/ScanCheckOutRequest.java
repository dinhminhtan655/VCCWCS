package com.wcs.vcc.api.checkouttrip.request;

public class ScanCheckOutRequest {
    private String TripNumber ;
    private String CartonBarcode ;
    private String UserName ;
    private String DeviceNumber ;
    private String CustomerID ;
    private String ATMShipmentID ;
    private String Vehicle ;
    private String DriverName ;
    private String Remark ;
    private String DeliveryDate ;

    public String getTripNumber() {
        return TripNumber;
    }

    public String getCartonBarcode() {
        return CartonBarcode;
    }

    public String getUserName() {
        return UserName;
    }

    public String getDeviceNumber() {
        return DeviceNumber;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public String getATMShipmentID() {
        return ATMShipmentID;
    }

    public String getVehicle() {
        return Vehicle;
    }

    public String getDriverName() {
        return DriverName;
    }

    public String getRemark() {
        return Remark;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setTripNumber(String tripNumber) {
        TripNumber = tripNumber;
    }

    public void setCartonBarcode(String cartonBarcode) {
        CartonBarcode = cartonBarcode;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setDeviceNumber(String deviceNumber) {
        DeviceNumber = deviceNumber;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public void setATMShipmentID(String ATMShipmentID) {
        this.ATMShipmentID = ATMShipmentID;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }
}
