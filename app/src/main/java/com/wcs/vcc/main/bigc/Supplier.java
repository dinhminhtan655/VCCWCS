package com.wcs.vcc.main.bigc;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Supplier implements Comparable<Supplier> {
    @SerializedName("PurchasingOrderID")
    private int id;
    @SerializedName("SupplierNumber")
    private String number;
    @SerializedName("SupplierNameShort")
    private String nameShort;
    @SerializedName("BookingWeight")
    private float bookingWeight;
    @SerializedName("ActualWeight")
    private float actualWeight;
    @SerializedName("BasketQuantity")
    private int basketQuantity;
    @SerializedName("PurchasingOrderNumber")
    private String purchasingOrderNumber;
    @SerializedName("BasketMovementNumber")
    private String basketMovementNumber;
    @SerializedName("BasketQuantityReturn")
    private int basketQuantityReturn;

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number != null ? number : "";
    }

    public String getNameShort() {
        return nameShort != null ? nameShort : "";
    }

    public float getBookingWeight() {
        return bookingWeight;
    }

    public float getActualWeight() {
        return actualWeight;
    }

    public int getBasketQuantity() {
        return basketQuantity;
    }

    public String getPurchasingOrderNumber() {
        return purchasingOrderNumber;
    }

    public String getBasketMovementNumber() {
        return basketMovementNumber;
    }

    public void setBasketMovementNumber(String basketMovementNumber) {
        this.basketMovementNumber = basketMovementNumber;
    }

    public int getBasketQuantityReturn() {
        return basketQuantityReturn;
    }

    @Override
    public int compareTo(@NonNull Supplier supplier) {
        return nameShort.compareTo(supplier.getNameShort());
    }
}
