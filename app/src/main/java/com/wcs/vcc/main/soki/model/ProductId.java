package com.wcs.vcc.main.soki.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ProductId {

    @SerializedName("ProductID")
    public String ProductID;
    @SerializedName("ProductNumber")
    public String ProductNumber;
    @SerializedName("ProductName")
    public String ProductName;
    @SerializedName("WeightPerPackage")
    public double WeightPerPackage;
    @SerializedName("TemperatureRequire")
    public double TemperatureRequire;
    @SerializedName("Origin")
    public String Origin;
    @SerializedName("IsWeightingRequire")
    public boolean IsWeightingRequire;
    @SerializedName("Inners")
    public int Inners;
    @SerializedName("CBM")
    public double CBM;
    @SerializedName("GrossWeightPerPackage")
    public double GrossWeightPerPackage;
    @SerializedName("Packages")
    public String Packages;
    @SerializedName("PackagesPerPallet")
    public int PackagesPerPallet;
    @SerializedName("Shelfdate")
    public String Shelfdate;

    @NonNull
    @Override
    public String toString() {
        return String.format(this.ProductName);
    }
}
