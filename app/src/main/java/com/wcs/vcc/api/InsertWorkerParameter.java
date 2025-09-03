package com.wcs.vcc.api;

import java.util.UUID;

/**
 * Created by Trần Xuân Lộc on 1/19/2016.
 */
public class InsertWorkerParameter {
    private String UserName;
    private UUID tmpEmployeeWorkingID;
    private UUID SupervisorID;
    private UUID ForkliftDriverID1;
    private UUID ForkliftDriverID2;
    private int Percentage;
    private String StartTime;
    private String EndTime;
    private UUID GeneralHandID1;
    private UUID GeneralHandID2;
    private UUID GeneralHandID3;
    private UUID GeneralHandID4;
    private UUID GeneralHandID5;
    private UUID WalkieID1;
    private UUID WalkieID2;
    private UUID WalkieID3;
    private UUID WalkieID4;
    private int TotalPackages;
    private String OrderNumber;
    private boolean Smell;
    private boolean Wet;
    private boolean LidOpening;
    private boolean Clean;
    private boolean Torn;
    private boolean Missing;
    private boolean Denting;
    private boolean Damages;
    private boolean Fall_Break;
    private boolean Soft;
    private boolean Leak;
    private boolean Dirty;
    private boolean Musty;
    private boolean InsectsRisk;
    private boolean Glass_WoodFragments;
    private boolean Others;
    private boolean Confirmed;
    private int PercentGH1;
    private int PercentGH2;
    private int PercentGH3;
    private int PercentGH4;
    private int PercentGH5;
    private String UserUpdate;
    private int PercentWalkieID1;
    private int PercentWalkieID2;
    private int PercentWalkieID3;
    private int PercentWalkieID4;
    private int PercentFD1;
    private int PercentFD2;
    private boolean Approve;
    private String Remark;
        private int TotalPercentGH;
    private boolean IsGoodsOnPallet;
    private int PalletQty;
    private boolean TruckContAfterNormal;
    private int DifferentQty;
    private int OrderStatus;

    public InsertWorkerParameter(boolean clean, boolean damages, boolean denting, boolean dirty, boolean fallBreak, boolean glassWoodFragments, boolean insectsRisk, boolean leak, boolean lidOpening, boolean musty, boolean others, boolean smell, boolean soft, boolean torn, boolean wet, boolean missing) {
        Clean = clean;
        Damages = damages;
        Denting = denting;
        Dirty = dirty;
        this.Fall_Break = fallBreak;
        this.Glass_WoodFragments = glassWoodFragments;
        InsectsRisk = insectsRisk;
        Leak = leak;
        LidOpening = lidOpening;
        Musty = musty;
        Others = others;
        Smell = smell;
        Soft = soft;
        Torn = torn;
        Wet = wet;
        Missing = missing;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public void setGoodsOnPallet(boolean goodsOnPallet) {
        IsGoodsOnPallet = goodsOnPallet;
    }

    public void setPalletQty(int palletQty) {
        PalletQty = palletQty;
    }

    public void setTruckContAfterNormal(boolean truckContAfterNormal) {
        TruckContAfterNormal = truckContAfterNormal;
    }

    public void setDifferentQty(int differentQty) {
        DifferentQty = differentQty;
    }

    public void setTotalPercentGH(int totalPercentGH) {
        TotalPercentGH = totalPercentGH;
    }

    public void setForkliftDriverID1(UUID forkliftDriverID1) {
        ForkliftDriverID1 = forkliftDriverID1;
    }

    public void setForkliftDriverID2(UUID forkliftDriverID2) {
        ForkliftDriverID2 = forkliftDriverID2;
    }

    public void setPercentFD1(int percentFD1) {
        PercentFD1 = percentFD1;
    }

    public void setPercentFD2(int percentFD2) {
        PercentFD2 = percentFD2;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public void setGeneralHandID1(UUID generalHandID1) {
        GeneralHandID1 = generalHandID1;
    }

    public void setGeneralHandID2(UUID generalHandID2) {
        GeneralHandID2 = generalHandID2;
    }

    public void setGeneralHandID3(UUID generalHandID3) {
        GeneralHandID3 = generalHandID3;
    }

    public void setGeneralHandID4(UUID generalHandID4) {
        GeneralHandID4 = generalHandID4;
    }

    public void setGeneralHandID5(UUID generalHandID5) {
        GeneralHandID5 = generalHandID5;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public void setPercentGH1(int percentGH1) {
        PercentGH1 = percentGH1;
    }

    public void setPercentGH2(int percentGH2) {
        PercentGH2 = percentGH2;
    }

    public void setPercentGH3(int percentGH3) {
        PercentGH3 = percentGH3;
    }

    public void setPercentGH4(int percentGH4) {
        PercentGH4 = percentGH4;
    }

    public void setPercentGH5(int percentGH5) {
        PercentGH5 = percentGH5;
    }

    public void setPercentWalkieID1(int percentWalkieID1) {
        PercentWalkieID1 = percentWalkieID1;
    }

    public void setPercentWalkieID2(int percentWalkieID2) {
        PercentWalkieID2 = percentWalkieID2;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public void setTotalPackages(int totalPackages) {
        TotalPackages = totalPackages;
    }

    public void setWalkieID1(UUID walkieID1) {
        WalkieID1 = walkieID1;
    }

    public void setWalkieID2(UUID walkieID2) {
        WalkieID2 = walkieID2;
    }

    public void setWalkieID4(UUID walkieID4) {
        WalkieID4 = walkieID4;
    }

    public void setWalkieID3(UUID walkieID3) {
        WalkieID3 = walkieID3;
    }

    public void setPercentWalkieID4(int percentWalkieID4) {
        PercentWalkieID4 = percentWalkieID4;
    }

    public void setPercentWalkieID3(int percentWalkieID3) {
        PercentWalkieID3 = percentWalkieID3;
    }
}
