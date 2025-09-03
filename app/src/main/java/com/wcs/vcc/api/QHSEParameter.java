package com.wcs.vcc.api;


public class QHSEParameter {
    private int QHSEID;
    private int Department = 0;
    private String UserName = "";
    private int storeId;

    public QHSEParameter(int QHSEID, int storeId) {
        this.QHSEID = QHSEID;
        this.storeId = storeId;
    }
}
