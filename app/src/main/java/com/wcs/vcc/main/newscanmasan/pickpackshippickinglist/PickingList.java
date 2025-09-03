package com.wcs.vcc.main.newscanmasan.pickpackshippickinglist;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class PickingList {

    @SerializedName("OrderDate")
    public String OrderDate;

    @SerializedName("ProductNumber")
    public String ProductNumber;

    @SerializedName("ProductName")
    public String ProductName;

    @SerializedName("StoreNumber")
    public String StoreNumber;

    @SerializedName("Quantity")
    public int Quantity;

    @SerializedName("CustomerNumber")
    public String CustomerNumber;

    @SerializedName("CustomerName")
    public String CustomerName;

    @SerializedName("CustomerRef2")
    public String CustomerRef2;

    @SerializedName("CustomerRef")
    public String CustomerRef;

    @SerializedName("CustomerClientName")
    public String CustomerClientName;

    @SerializedName("DispatchingOrderNumber")
    public String DispatchingOrderNumber;

    @SerializedName("Weights")
    public double Weights;

    @SerializedName("RouteCode")
    public String RouteCode;

    @SerializedName("NetQty")
    public int NetQty;

    @SerializedName("NetWeight")
    public double NetWeight;

    @SerializedName("XdocPickingListID")
    public UUID XdocPickingListID;

    private String NextStore;
    public Integer CartonNumber;
    public String PackageType;

    public PickingList() {

    }

    public String getNextStore() {
        return NextStore;
    }

    public void setNextStore(String nextStore) {
        NextStore = nextStore;
    }

    public PickingList(String orderDate, String productNumber, String productName, String storeNumber, int quantity, String customerNumber, String customerName, String customerRef2, String customerRef, String customerClientName, String dispatchingOrderNumber, double weights, String routeCode, int netQty, double netWeight) {
        OrderDate = orderDate;
        ProductNumber = productNumber;
        ProductName = productName;
        StoreNumber = storeNumber;
        Quantity = quantity;
        CustomerNumber = customerNumber;
        CustomerName = customerName;
        CustomerRef2 = customerRef2;
        CustomerRef = customerRef;
        CustomerClientName = customerClientName;
        DispatchingOrderNumber = dispatchingOrderNumber;
        Weights = weights;
        RouteCode = routeCode;
        NetQty = netQty;
        NetWeight = netWeight;
    }

    public PickingList(String orderDate, String productNumber, String productName, String storeNumber, int quantity, String customerNumber, String customerName, String customerRef2, String customerRef, String customerClientName, String dispatchingOrderNumber, double weights, String routeCode, int netQty, double netWeight, UUID xdocPickingListID, String nextStore, Integer cartonNumber, String packageType) {
        OrderDate = orderDate;
        ProductNumber = productNumber;
        ProductName = productName;
        StoreNumber = storeNumber;
        Quantity = quantity;
        CustomerNumber = customerNumber;
        CustomerName = customerName;
        CustomerRef2 = customerRef2;
        CustomerRef = customerRef;
        CustomerClientName = customerClientName;
        DispatchingOrderNumber = dispatchingOrderNumber;
        Weights = weights;
        RouteCode = routeCode;
        NetQty = netQty;
        NetWeight = netWeight;
        XdocPickingListID = xdocPickingListID;
        NextStore = nextStore;
        CartonNumber = cartonNumber;
        PackageType = packageType;
    }
}
