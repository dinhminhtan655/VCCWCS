package com.wcs.vcc.main.scanvinmart;

public class XDockDeleteCartonDispatchingParam {
    private Integer DispatchingCartonID;
    private String UserName;

    public XDockDeleteCartonDispatchingParam(int cartonId, String username) {
        this.DispatchingCartonID = cartonId;
        this.UserName = username;
    }

    public Integer getDispatchingCartonID() {
        return DispatchingCartonID;
    }

    public void setDispatchingCartonID(Integer dispatchingCartonID) {
        DispatchingCartonID = dispatchingCartonID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
