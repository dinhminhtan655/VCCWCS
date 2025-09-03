package com.wcs.vcc.api;

import java.util.UUID;

public class PickAndPutAwayLocationScanParam {

    private String ScanResult;
    private UUID PalletID;
    private UUID OrderID;
    private String OrderNumber;
    private String UserName;
    private String DeviceNumber;
    private int StoreID;
    private UUID DispatchingOrderDetailID;
    private int PickingQuantity;

    public PickAndPutAwayLocationScanParam(String scanResult, UUID palletID, UUID orderID, String orderNumber, String userName, String deviceNumber, int storeID) {
        ScanResult = scanResult;
        PalletID = palletID;
        OrderID = orderID;
        OrderNumber = orderNumber;
        UserName = userName;
        DeviceNumber = deviceNumber;
        StoreID = storeID;
    }

    public PickAndPutAwayLocationScanParam(String scanResult, String userName, String deviceNumber) {
        ScanResult = scanResult;
        UserName = userName;
        DeviceNumber = deviceNumber;
    }

    public PickAndPutAwayLocationScanParam(UUID palletID, String userName, String deviceNumber, UUID dispatchingOrderDetailID, int pickingQuantity) {
        PalletID = palletID;
        UserName = userName;
        DeviceNumber = deviceNumber;
        DispatchingOrderDetailID = dispatchingOrderDetailID;
        PickingQuantity = pickingQuantity;
    }

    public PickAndPutAwayLocationScanParam(String scanResult,UUID palletID, String userName, String deviceNumber) {
        ScanResult = scanResult;
        PalletID = palletID;
        UserName = userName;
        DeviceNumber = deviceNumber;
    }
}

