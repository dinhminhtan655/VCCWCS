package com.wcs.vcc.api;

import java.util.UUID;

/**
 * Created by Trần Xuân Lộc on 1/13/2016.
 */
public class UpdateDispatchingOrderDetailParameter {
    public UUID DispatchingOrderDetailID;
    public UUID PalletID;
    public String Remark;
    public boolean Checked;
    public String UserName;
    public String OrderNumber;
    public String DeviceNumber;
    public int ActualQuantity;
    public String CustomerRef;
    public String CustomerRef2;
    public String ProductionDate;
    public String UseByDate;
    public String ScanResult;

    public UpdateDispatchingOrderDetailParameter(boolean checked, UUID dispatchingOrderDetailID,UUID palletID, String remark, String userName,
                                                 String OrderNumber, String DeviceNumber, int iSL, String strCusRef, String strCusRef2,
                                                 String strNSX, String strHSD, String ScanResult) {
        Checked = checked;
        DispatchingOrderDetailID = dispatchingOrderDetailID;
        PalletID = palletID;
        Remark = remark;
        UserName = userName;
        this.OrderNumber = OrderNumber;
        this.DeviceNumber = DeviceNumber;
        this.ActualQuantity = iSL;
        this.CustomerRef = strCusRef;
        this.CustomerRef2 = strCusRef2;
        this.ProductionDate = strNSX;
        this.UseByDate = strHSD;
        this.ScanResult = ScanResult;
    }

    public boolean isChecked() {
        return Checked;
    }


    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
