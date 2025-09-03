package com.wcs.vcc.main.bigc;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Product implements Comparable<Product> {
    @SerializedName("SalesOrderProductID")
    private int id;
    @SerializedName("ProductNumber")
    private String number;
    @SerializedName("ProductName")
    private String name;
    @SerializedName("StoreBookingWeight")
    private float storeBookingWeight;
    @SerializedName("DispatchedGrossWeight")
    private float dispatchedActualWeight;
    @SerializedName("BasketNumber")
    private String basketNumber;
    @SerializedName("BasketQuantity")
    private int basketQuantity;
    @SerializedName("DispatchedActualCarton")
    private int dispatchedActualCarton;

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public float getStoreBookingWeight() {
        return storeBookingWeight;
    }

    public float getDispatchedActualWeight() {
        return dispatchedActualWeight;
    }

    public String getBasketNumber() {
        return basketNumber;
    }

    public int getBasketQuantity() {
        return basketQuantity;
    }


    public int getDispatchedActualCarton() {
        return dispatchedActualCarton;
    }

    @Override
    public int compareTo(@NonNull Product product) {
        float r = dispatchedActualWeight - product.getDispatchedActualWeight();
        if (r != 0) {
            return (int) r;
        }
        return name.compareTo(product.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (getClass() != o.getClass()) {
            return false;
        }

        Product product = (Product) o;
        if (id != product.id) {
            return false;
        }
        if (number != null ? !number.equals(product.number) : product.number != null) {
            return false;
        }
        return name != null ? !name.equals(product.name) : product.name != null;
    }

    @Override
    public int hashCode() {
        int code = id;
        code = 31 * code + (name == null ? 0 : name.hashCode());
        code = 31 * code + (number == null ? 0 : number.hashCode());
        return code;
    }
}
