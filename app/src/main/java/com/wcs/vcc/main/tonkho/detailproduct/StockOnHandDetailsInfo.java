package com.wcs.vcc.main.tonkho.detailproduct;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/9/2016.
 */
public class StockOnHandDetailsInfo {
    @SerializedName("LocationNumber")
    private String LocationNumber;
    @SerializedName("TotalCurrentCtns")
    private int TotalCurrentCtns;
    @SerializedName("TotalAfterDPCtns")
    private int TotalAfterDPCtns;
    @SerializedName("TotalWeight")
    private float TotalWeight;
    @SerializedName("TotalUnits")
    private int TotalUnits;
    @SerializedName("PalletStatus")
    private String PalletStatus;
    @SerializedName("ProductionDate")
    private String ProductionDate;
    @SerializedName("UseByDate")
    private String UseByDate;
    @SerializedName("CustomerRef")
    private String CustomerRef;
    @SerializedName("Remark")
    private String Remark;
    @SerializedName("ReceivingOrderDate")
    private String ReceivingOrderDate;
    public UUID ReceivingOrderID;
    public int ReceivingOrderLocalID;
    @SerializedName("PickQuantity")
    private int TotalPicked;
    @SerializedName("HoldQuantity")
    private int TotalHold;
    private int TotalExport;

    public String getCustomerRef() {
        return CustomerRef;
    }

    public String getLocationNumber() {
        return LocationNumber;
    }

    public String getPalletStatus() {
        return PalletStatus;
    }

    public String getProductionDate() {
        return Utilities.formatDate_ddMMyy(ProductionDate);
    }

    public String getReceivingOrderDate() {
        return Utilities.formatDate_ddMMyy(ReceivingOrderDate);
    }

    public String getRemark() {
        return Remark;
    }

    public int getTotalAfterDPCtns() {
        return TotalAfterDPCtns;
    }

    public int getTotalCurrentCtns() {
        return TotalCurrentCtns;
    }

    public int getTotalUnits() {
        return TotalUnits;
    }

    public float getTotalWeight() {
        return TotalWeight;
    }

    public String getUseByDate() {
        return Utilities.formatDate_ddMMyy(UseByDate);
    }

    public int getTotalPicked() {
        return TotalPicked;
    }

    public int getTotalHold() {
        return TotalHold;
    }

    public int getTotalExport() {
        TotalExport = getTotalCurrentCtns() - getTotalAfterDPCtns();
        return TotalExport < 0 ? 0 : TotalExport;
    }
}
