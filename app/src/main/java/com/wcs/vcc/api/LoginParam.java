package com.wcs.vcc.api;


public class LoginParam {
    private String Wifi_MAC_Address;
    private String username;
    private String password;

    public LoginParam(String mac, String username, String password) {
        this.Wifi_MAC_Address = mac;
        this.username = username;
        this.password = password;
    }
}
