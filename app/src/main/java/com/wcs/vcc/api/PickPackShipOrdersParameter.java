package com.wcs.vcc.api;

import java.util.UUID;

public class PickPackShipOrdersParameter {
    private String varDate;
    private String customerNumber;
    private int storeNumber;
    private String DispatchingOrderNumber;
    private UUID CustomerID;
    private int StoreID;

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }

    public PickPackShipOrdersParameter() {
    }

    public UUID getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(UUID customerID) {
        CustomerID = customerID;
    }

    public PickPackShipOrdersParameter(String varDate, String DispatchingOrderNumber ) {
        this.varDate = varDate;
        this.DispatchingOrderNumber = DispatchingOrderNumber;
    }

    public PickPackShipOrdersParameter(String varDate ) {
        this.varDate = varDate;
    }

    public PickPackShipOrdersParameter(String varDate,int storeNumber) {
        this.varDate = varDate;
        this.storeNumber =  storeNumber;
    }

    public String getVarDate() {
        return varDate;
    }

    public void setVarDate(String varDate) {
        this.varDate = varDate;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public int getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(int storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getDispatchingOrderNumber() {
        return DispatchingOrderNumber;
    }

    public void setDispatchingOrderNumber(String dispatchingOrderNumber) {
        DispatchingOrderNumber = dispatchingOrderNumber;
    }
}
