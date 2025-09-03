package com.wcs.vcc.api;

/**
 * Created by aang on 02/06/2018.
 */

public class BaseParam {
    private String Username;
    private String DeviceNumber;

    public BaseParam(String username, String deviceNumber) {
        Username = username;
        DeviceNumber = deviceNumber;
    }
}
