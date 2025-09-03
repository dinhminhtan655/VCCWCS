package com.wcs.vcc.api;

import java.util.UUID;

/**
 * Created by tranxuanloc on 4/6/2016.
 */
public class StockMovementInsertParameter {
    private UUID SourceLocationID;
    private UUID DestinationLocationID;
    private String DestinationLocationNumber;
    private String Reason;
    private String UserName;
    private String stringPalletID;
    private String ScanResult;

    public StockMovementInsertParameter(UUID destinationLocationID,
                                        String destinationLocationNumber,
                                        String reason,
                                        UUID sourceLocationID,
                                        String userName,
                                        String stringPalletID,
                                        String scanResult) {
        DestinationLocationID = destinationLocationID;
        DestinationLocationNumber = destinationLocationNumber;
        Reason = reason;
        SourceLocationID = sourceLocationID;
        UserName = userName;
        this.stringPalletID = stringPalletID;
        ScanResult = scanResult;
    }
}
