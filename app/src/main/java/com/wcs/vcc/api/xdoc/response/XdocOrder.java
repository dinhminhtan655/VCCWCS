package com.wcs.vcc.api.xdoc.response;

import java.util.UUID;


public class XdocOrder {
    private UUID CustomerClientID ;
    private String ShipTo;
    private Integer StoreNumber ;
    private String DispatchingOrderNumber ;
    private UUID DispatchingOrderID ;
    private String CustomerNumber;
    private String CustomerName ;
    private int TotalPackages;
    private Double TotalWeight ;

    public UUID getCustomerClientID() {
        return CustomerClientID;
    }

    public XdocOrder(UUID customerClientID, String shipTo, Integer storeNumber, String dispatchingOrderNumber, UUID dispatchingOrderID, String customerNumber, String customerName, int totalPackages, Double totalWeight) {
        CustomerClientID = customerClientID;
        ShipTo = shipTo;
        StoreNumber = storeNumber;
        DispatchingOrderNumber = dispatchingOrderNumber;
        DispatchingOrderID = dispatchingOrderID;
        CustomerNumber = customerNumber;
        CustomerName = customerName;
        TotalPackages = totalPackages;
        TotalWeight = totalWeight;
    }

    public void setCustomerClientID(UUID customerClientID) {
        CustomerClientID = customerClientID;
    }

    public String getShipTo() {
        return ShipTo;
    }

    public void setShipTo(String shipTo) {
        ShipTo = shipTo;
    }

    public Integer getStoreNumber() {
        return StoreNumber;
    }

    public void setStoreNumber(Integer storeNumber) {
        StoreNumber = storeNumber;
    }

    public String getDispatchingOrderNumber() {
        return DispatchingOrderNumber;
    }

    public void setDispatchingOrderNumber(String dispatchingOrderNumber) {
        DispatchingOrderNumber = dispatchingOrderNumber;
    }

    public UUID getDispatchingOrderID() {
        return DispatchingOrderID;
    }

    public void setDispatchingOrderID(UUID dispatchingOrderID) {
        DispatchingOrderID = dispatchingOrderID;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        CustomerNumber = customerNumber;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }


    public int getTotalPackages() {
        return TotalPackages;
    }

    public void setTotalPackages(int totalPackages) {
        TotalPackages = totalPackages;
    }

    public Double getTotalWeight() {
        return TotalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        TotalWeight = totalWeight;
    }
}

