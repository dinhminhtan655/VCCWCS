package com.wcs.vcc.api;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/13/2016.
 */
public class OverTimeOrderDetailsParameter {
    private UUID EmployeeID;
    private String OrderDate;

    public OverTimeOrderDetailsParameter(UUID employeeID, String orderDate) {
        EmployeeID = employeeID;
        OrderDate = orderDate;
    }
}
