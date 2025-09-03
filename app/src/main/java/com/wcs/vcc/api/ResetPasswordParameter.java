package com.wcs.vcc.api;

/**
 * Created by Buu on 21/05/2018.
 */

public class ResetPasswordParameter {

    private String userName;
    private String newPassword;

    public ResetPasswordParameter(String userName, String newPassword) {
        this.userName = userName;
        this.newPassword = newPassword;
    }

}
