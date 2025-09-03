package com.wcs.vcc.main.newscanmasan.itemmasan;

import java.util.UUID;

public class XdocPackingPickingListOrder {
    private String PickingListOrderNumber;
    private UUID ProductID ;
    private String ProductName ;
    private String LotNumber;
    private int Version ;
    private int Qty ;
    private Double  Weight ;
    private UUID ReceivingOrderDetailID ;

    public XdocPackingPickingListOrder(String pickingListOrderNumber, UUID productID, String productName, String lotNumber, int version, int qty, Double weight, UUID receivingOrderDetailID) {
        PickingListOrderNumber = pickingListOrderNumber;
        ProductID = productID;
        ProductName = productName;
        LotNumber = lotNumber;
        Version = version;
        Qty = qty;
        Weight = weight;
        ReceivingOrderDetailID = receivingOrderDetailID;
    }

    public String getPickingListOrderNumber() {
        return PickingListOrderNumber;
    }

    public void setPickingListOrderNumber(String pickingListOrderNumber) {
        PickingListOrderNumber = pickingListOrderNumber;
    }

    public UUID getProductID() {
        return ProductID;
    }

    public void setProductID(UUID productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getLotNumber() {
        return LotNumber;
    }

    public void setLotNumber(String lotNumber) {
        LotNumber = lotNumber;
    }

    public int getVersion() {
        return Version;
    }

    public void setVersion(int version) {
        Version = version;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public Double getWeight() {
        return Weight;
    }

    public void setWeight(Double weight) {
        Weight = weight;
    }

    public UUID getReceivingOrderDetailID() {
        return ReceivingOrderDetailID;
    }

    public void setReceivingOrderDetailID(UUID receivingOrderDetailID) {
        ReceivingOrderDetailID = receivingOrderDetailID;
    }
}
