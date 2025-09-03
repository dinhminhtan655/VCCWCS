package com.wcs.vcc.api;

/**
 * Created by xuanloc on 3/18/2017.
 */

public class HouseKeepingCheckParameter {
    private String userName;
    private int storeId;

    public HouseKeepingCheckParameter(String userName, int storeId) {
        this.userName = userName;
        this.storeId = storeId;
    }
}
