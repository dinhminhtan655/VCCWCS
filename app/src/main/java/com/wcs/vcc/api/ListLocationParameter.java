package com.wcs.vcc.api;

/**
 * Created by tranxuanloc on 4/6/2016.
 */
public class ListLocationParameter {
    private String LocationNumberShort;
    private int storeID;

    public ListLocationParameter(String varlocationNumberShort, int varStoreID) {
        LocationNumberShort = varlocationNumberShort;
        storeID = varStoreID;
    }
}
