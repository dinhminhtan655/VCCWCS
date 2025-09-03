package com.wcs.vcc.api;


public class RegisterParameter {

    private String newUserName;
    private String newPassword;
    private String newPasswordConfirm;
    private int WarehouseID;
    private String UserName;
    private boolean IsAllowOutside;
    private int StoreID;

    public RegisterParameter(String newUserName, String newPassword, String newPasswordConfirm, int warehouseID, boolean isAllowOutside, String userName, int storeID) {
        this.StoreID = storeID;
        this.newUserName = newUserName;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
        WarehouseID = warehouseID;
        UserName = userName;
        IsAllowOutside = isAllowOutside;
    }
}
