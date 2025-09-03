package com.wcs.vcc.api;


import java.util.UUID;

public class CompletedCheckingParameter {
    public UUID CheckingID;
    public String UserName;

    public CompletedCheckingParameter(UUID CheckingID, String userName) {
        this.CheckingID = CheckingID;
        UserName = userName;
    }
}
