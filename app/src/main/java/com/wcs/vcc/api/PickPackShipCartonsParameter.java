package com.wcs.vcc.api;

public class PickPackShipCartonsParameter {
    private int StoreNumber = 0;//nếu scan thì truyền = 0
    private String DispatchingOrderDate;
    public String ScanResult = "ST000000001";//nếu gõ số kho thì truyền = ''//chuổi barcode = 'ST000000001'
    public String DispatchingOrderNumber;

    public PickPackShipCartonsParameter(int storeNumber, String dispatchingOrderDate, String scanResult, String DispatchingOrderNumber) {
        StoreNumber = storeNumber;
        DispatchingOrderDate = dispatchingOrderDate;
        ScanResult = scanResult;
        this.DispatchingOrderNumber = DispatchingOrderNumber;
    }
}
