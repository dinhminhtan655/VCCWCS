package com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.product;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    @SerializedName("ProductID")
    private String productID;
    @SerializedName("ProductNumber")
    private String productNumber;
    @SerializedName("ProductName")
    private String productName;
    @SerializedName("WeightPerPackage")
    private double weightPerPackage;
    @SerializedName("TemperatureRequire")
    private double temperatureRequire;
    @SerializedName("Inners")
    private int inners;
    @SerializedName("CBM")
    private double cBM;

    @NonNull
    @Override
    public String toString() {
        return productNumber+"~~"+productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getWeightPerPackage() {
        return weightPerPackage;
    }

    public void setWeightPerPackage(double weightPerPackage) {
        this.weightPerPackage = weightPerPackage;
    }

    public double getTemperatureRequire() {
        return temperatureRequire;
    }

    public void setTemperatureRequire(double temperatureRequire) {
        this.temperatureRequire = temperatureRequire;
    }

    public int getInners() {
        return inners;
    }

    public void setInners(int inners) {
        this.inners = inners;
    }

    public double getcBM() {
        return cBM;
    }

    public void setcBM(double cBM) {
        this.cBM = cBM;
    }
}
