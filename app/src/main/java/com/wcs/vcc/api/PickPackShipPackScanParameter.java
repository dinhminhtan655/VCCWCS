package com.wcs.vcc.api;

import java.util.UUID;

public class PickPackShipPackScanParameter extends BaseParam {
    public String ScanResult;
    public int DispatchingProductCartonID;
    public int RequestNumber;
    public UUID XdocPickingListID;
    public int QuantityScan;
    public String Lot;
    public String ProductionDate;
    public String ExpDate;
    public Integer PalletNumber;
    public String PickingListOrderNumber;
    public String CustomerRef2;
    public String CustomerID ;

    public PickPackShipPackScanParameter(String username, String deviceNumber, String scanResult, int dispatchingProductCartonID,int requestNumber) {
        super(username, deviceNumber);
        ScanResult = scanResult;
        DispatchingProductCartonID = dispatchingProductCartonID;
        RequestNumber =requestNumber;
    }

    public PickPackShipPackScanParameter(String username, String deviceNumber, String ScanResult, int QuantityScan,String PickingListOrderNumber, UUID XdocPickingListID) {
        super(username, deviceNumber);
        this.ScanResult = ScanResult;
        this.QuantityScan = QuantityScan;
        this.PickingListOrderNumber =PickingListOrderNumber;
        this.XdocPickingListID = XdocPickingListID;
    }

    public PickPackShipPackScanParameter(String username, String deviceNumber, String scanResult, int requestNumber,int QuantityScan, UUID xdocPickingListID) {
        super(username, deviceNumber);
        ScanResult = scanResult;
        RequestNumber = requestNumber;
        XdocPickingListID = xdocPickingListID;
        this.QuantityScan = QuantityScan;
    }

    public PickPackShipPackScanParameter(String username, String deviceNumber, String scanResult, int requestNumber, UUID xdocPickingListID, String lot, String productionDate, Integer palletNumber) {
        super(username, deviceNumber);
        ScanResult = scanResult;
        XdocPickingListID = xdocPickingListID;
        QuantityScan = requestNumber;
        Lot = lot;
        ProductionDate = productionDate;
        PalletNumber = palletNumber;
    }

    public PickPackShipPackScanParameter(String username, String deviceNumber, String scanResult, int dispatchingProductCartonID, int requestNumber, UUID xdocPickingListID, int quantityScan, String lot, String productionDate, Integer palletNumber, String pickingListOrderNumber, String customerRef2) {
        super(username, deviceNumber);
        ScanResult = scanResult;
        DispatchingProductCartonID = dispatchingProductCartonID;
        RequestNumber = requestNumber;
        XdocPickingListID = xdocPickingListID;
        QuantityScan = quantityScan;
        Lot = lot;
        ProductionDate = productionDate;
        PalletNumber = palletNumber;
        PickingListOrderNumber = pickingListOrderNumber;
        CustomerRef2 = customerRef2;
    }



}
