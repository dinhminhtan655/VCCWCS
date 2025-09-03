package com.wcs.vcc.api;

import java.util.UUID;

public  class DeleteDOParameter{
    public UUID DispatchingCartonID ;
    public String UserName ;
    public String DONumber;
    public DeleteDOParameter(UUID dispatchingCartonID, String userName, String DOnumber) {
        this.DispatchingCartonID = dispatchingCartonID;
        this.UserName = userName;
        this.DONumber = DOnumber;
    }
}
