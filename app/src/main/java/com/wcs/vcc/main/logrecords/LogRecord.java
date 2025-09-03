package com.wcs.vcc.main.logrecords;

import com.google.gson.annotations.SerializedName;
import com.wcs.vcc.utilities.Utilities;

public class LogRecord {

    @SerializedName("CreateDate")
    private String createDate;
    @SerializedName("UserName")
    private String userName;
    @SerializedName("LoginLevel")
    private String loginLevel;
    @SerializedName("ActionDescription")
    private String logDescription;

    public String getCreateDate() {
        return Utilities.formatDate_HHmm(createDate);
    }

    public String getUserName() {
        return userName;
    }

    public String getLoginLevel() {
        return loginLevel;
    }

    public String getLogDescription() {
        return logDescription;
    }
}
