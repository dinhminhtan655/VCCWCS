package com.wcs.vcc.api;


public class InOutToDayUnfinishedParameter {
    private int WareHouseId;
    private String UserName;
    private String varDate;
    private int storeID;

    public InOutToDayUnfinishedParameter(int WareHouseId, String UserName, String varDate, int storeID) {
        this.WareHouseId = WareHouseId;
        this.UserName = UserName;
        this.varDate = varDate;
        this.storeID = storeID;
    }
}
