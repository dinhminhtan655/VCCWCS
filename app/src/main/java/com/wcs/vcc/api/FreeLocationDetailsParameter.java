package com.wcs.vcc.api;

public class FreeLocationDetailsParameter {
    private String roomId;
    private int storeId;

    public FreeLocationDetailsParameter(String roomId, int storeId) {
        this.roomId = roomId;
        this.storeId = storeId;
    }
}
