package com.wcs.vcc.api;


public class InsertBasketMovementParameter {
    private int purchasingOrderID;
    private int customerID = 466;
    private String userName;

    public InsertBasketMovementParameter(int purchasingOrderID, String userName) {
        this.purchasingOrderID = purchasingOrderID;
        this.userName = userName;
    }
}
