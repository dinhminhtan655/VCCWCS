package com.wcs.vcc.api;

/**
 * Created by tranxuanloc on 6/3/2016.
 */
public class QHSEAssignmentInsertParameter {
    private String UserName;
    private String QHSENumber;
    private String strEmployeeCode;
    private int StoreID = 1;

    public QHSEAssignmentInsertParameter(String userName, String QHSENumber, String strEmployeeCode) {
        UserName = userName;
        this.QHSENumber = QHSENumber;
        this.strEmployeeCode = strEmployeeCode;
    }
}
