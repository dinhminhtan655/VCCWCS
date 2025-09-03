package com.wcs.vcc.api;

public class SuppliersTodayParameter {
    private String customerID = "466";
    private String purchasingOrderDate;

    private SuppliersTodayParameter(String purchasingOrderDate) {
        this.purchasingOrderDate = purchasingOrderDate;
    }

    public static SuppliersTodayParameter date(String purchasingOrderDate) {
        return new SuppliersTodayParameter(purchasingOrderDate);
    }
}
