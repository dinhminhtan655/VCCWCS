package com.wcs.vcc.main.chuyenhang;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/5/2016.
 */
public class LocationInfo {
    private static final String TAG = LocationInfo.class.getSimpleName();

    @SerializedName("ProductNumber")
    private String ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("CustomerRef")
    private String CustomerRef;
    @SerializedName("CurrentQuantity")
    private int CurrentQuantity;
    @SerializedName("AfterDPQuantity")
    private int AfterDPQuantity;
    public UUID PalletID;
    public int PalletNumber;
    @SerializedName("RO")
    private String RO;
    @SerializedName("CanMove")
    private boolean CanMove;
    @SerializedName("CheckAllowcate")
    private int CheckAllowcate;
    private boolean isChecked = true;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        Log.e(TAG, "setIsChecked: " + isChecked);
        this.isChecked = isChecked;
    }

    public int getAfterDPQuantity() {
        return AfterDPQuantity;
    }

    public boolean isCanMove() {
        return CanMove;
    }

    public int getCurrentQuantity() {
        return CurrentQuantity;
    }

    public String getCustomerRef() {
        return CustomerRef;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public String getRO() {
        return RO;
    }

    public int getCheckAllowcate() {
        return CheckAllowcate;
    }

    public void setCheckAllowcate(int checkAllowcate) {
        CheckAllowcate = checkAllowcate;
    }
}
