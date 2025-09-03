package com.wcs.vcc.api;

public class SCMSalesOrderProductParameter {
    public int SalesOrderProductID;
    public String UserName;
    public int BasketID;
    public int BasketQuantity;
    public int Flag;

    public SCMSalesOrderProductParameter(int salesOrderProductID, String userName) {
        SalesOrderProductID = salesOrderProductID;
        UserName = userName;
    }

    public SCMSalesOrderProductParameter(int salesOrderProductID, String userName, int basketID, int basketQuantity) {
        SalesOrderProductID = salesOrderProductID;
        UserName = userName;
        BasketID = basketID;
        BasketQuantity = basketQuantity;
    }
}
