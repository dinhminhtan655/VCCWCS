package com.wcs.vcc.api.xdoc.request;

public class DispatchingOrderByDateRequest {
    public String OrderDate ;
    public  Integer StoreID;

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public Integer getStoreID() {
        return StoreID;
    }

    public void setStoreID(Integer storeID) {
        StoreID = storeID;
    }

    public DispatchingOrderByDateRequest(String orderDate, Integer storeID) {
        OrderDate = orderDate;
        StoreID = storeID;
    }
}
