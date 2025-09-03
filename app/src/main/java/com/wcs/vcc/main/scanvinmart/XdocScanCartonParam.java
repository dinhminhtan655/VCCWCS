package com.wcs.vcc.main.scanvinmart;

public class XdocScanCartonParam {
    private Integer PalletID;
    private String ScanResult;
    private String DeliveryDate;
    private String UserName;

    public XdocScanCartonParam(Integer palletID, String scanResult, String deliveryDate, String userName) {
        PalletID = palletID;
        ScanResult = scanResult;
        DeliveryDate = deliveryDate;
        UserName = userName;
    }

    public Integer getPalletID() {
        return PalletID;
    }

    public void setPalletID(Integer palletID) {
        PalletID = palletID;
    }

    public String getScanResult() {
        return ScanResult;
    }

    public void setScanResult(String scanResult) {
        ScanResult = scanResult;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
