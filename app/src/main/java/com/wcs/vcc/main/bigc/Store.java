package com.wcs.vcc.main.bigc;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Store implements Comparable<Store> {
    @SerializedName("PurchasingOrderID")
    private int purchasingOrderID;
    @SerializedName("SalesOrderID")
    private int salesOrderID;
    @SerializedName("StoreNumber")
    private String number;
    @SerializedName("StoreName")
    private String name;
    @SerializedName("BookingWeight")
    private float bookingWeight;
    @SerializedName("DispatchedGrossWeight")
    private float actualWeight;
    @SerializedName("ActualCarton")
    private int actualCarton;
    @SerializedName("BasketQuantity")
    private int basketQuantity;

    public int getActualCarton() {
        return actualCarton;
    }

    public int getBasketQuantity() {
        return basketQuantity;
    }

    public int getPurchasingOrderID() {
        return purchasingOrderID;
    }

    public int getSalesOrderID() {
        return salesOrderID;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public float getBookingWeight() {
        return bookingWeight;
    }

    public float getActualWeight() {
        return actualWeight;
    }

    @Override
    public int compareTo(@NonNull Store store) {
        float r = actualWeight - store.getActualWeight();
        if (r != 0) {
            return (int) r;
        }
        return name.compareTo(store.getName());
    }
}
