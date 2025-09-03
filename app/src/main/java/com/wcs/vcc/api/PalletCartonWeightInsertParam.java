package com.wcs.vcc.api;

import java.util.UUID;

public class PalletCartonWeightInsertParam extends BaseParam {

    private String ScanResult;
    private int ScannedType = 10;
    private int PalletNumber;
    private int StoreID;
    private int BarcodeType;
    private UUID PalletCartonID;
    private UUID DispatchingOrderDetailID;

    public PalletCartonWeightInsertParam(String username, String deviceNumber, String scanResult, int palletNumber, int storeID, int barcodeType) {
        super(username, deviceNumber);
        ScanResult = scanResult;
        PalletNumber = palletNumber;
        StoreID = storeID;
        BarcodeType = barcodeType;
    }

    public PalletCartonWeightInsertParam(String userName, String deviceNumber, String scanResult, UUID palletCartonID, UUID dispatchingOrderDetailID) {
        super(userName, deviceNumber);
        ScanResult = scanResult;
        PalletCartonID = palletCartonID;
        DispatchingOrderDetailID = dispatchingOrderDetailID;
    }
}
