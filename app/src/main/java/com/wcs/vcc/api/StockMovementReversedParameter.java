package com.wcs.vcc.api;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/6/2016.
 */
public class StockMovementReversedParameter {
    private UUID SourceLocationID;
    private String SourceLocationNumber;
    private UUID DestinationLocationID;
    private String DestinationLocationNumber;
    private String Reason;
    private int EmployeeID;
    private String UserName;

    public StockMovementReversedParameter(UUID destinationLocationID,
                                          String destinationLocationNumber,
                                          int employeeID,
                                          String reason,
                                          UUID sourceLocationID,
                                          String sourceLocationNumber,
                                          String userName) {
        DestinationLocationID = destinationLocationID;
        DestinationLocationNumber = destinationLocationNumber;
        EmployeeID = employeeID;
        Reason = reason;
        SourceLocationID = sourceLocationID;
        SourceLocationNumber = sourceLocationNumber;
        UserName = userName;
    }
}
