package com.wcs.vcc.main.bigc;


import com.google.gson.annotations.SerializedName;

public class Basket {
    @SerializedName("BasketID")
    private int id;
    @SerializedName("BasketNumber")
    private String number;
    @SerializedName("BasketName")
    private String name;

    public Basket(int id, String number, String name) {
        this.id = id;
        this.number = number;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Basket) {
            return ((Basket) obj).number.equals(number);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(number);
    }
}
