package com.wcs.vcc.api;

/**
 * Created by aang on 05/09/2017.
 */

public class InsertBasketMovementReturnParameter {
    private String basketMovementNumber;
    private int customerID = 466;
    private String userName;

    public InsertBasketMovementReturnParameter(String basketMovementNumber, String userName) {
        this.basketMovementNumber = basketMovementNumber;
        this.userName = userName;
    }
}
