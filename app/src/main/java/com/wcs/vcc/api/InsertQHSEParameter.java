package com.wcs.vcc.api;


public class InsertQHSEParameter {
    private String UserName = "";
    private String Comment = "";
    private String Category = "";
    private String Subject = "";
    private int Department = 0;
    private String Location = "";
    private int Flag;
    private int QHSEID;
    private String Deadline = "1900/01/01T00:00:00";
    private boolean AssigmentReject;
    private int TaskProgress;
    private String OrderNumber = "";
    private int storeId;

    //insert
    public InsertQHSEParameter(String category, String comment, int flag, String location, String subject, String userName, int storeId) {
        Category = category;
        Comment = comment;
        Flag = flag;
        Location = location;
        Subject = subject;
        UserName = userName;
        this.storeId = storeId;
    }

    //delete
    public InsertQHSEParameter(String userName, int QHSEID, int flag, int storeId) {
        UserName = userName;
        this.QHSEID = QHSEID;
        Flag = flag;
        this.storeId = storeId;
    }


    //update qhse
    public InsertQHSEParameter(int QHSEID, String category, String comment, int flag, String location, String subject, String userName, int storeId) {
        this.QHSEID = QHSEID;
        Category = category;
        Comment = comment;
        Flag = flag;
        Location = location;
        Subject = subject;
        UserName = userName;
        this.storeId = storeId;
    }
}
