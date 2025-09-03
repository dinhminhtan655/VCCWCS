package com.wcs.vcc.api;

import java.util.UUID;

/**
 * Created by tranxuanloc on 6/6/2016.
 */
public class DispatchingOrderScannedDeleteParameter {
    private UUID BarcodeScanDetailID;
    private UUID DispatchingOrderDetailID;
    private String UserName;
    private UUID PalletCartonID;
    public DispatchingOrderScannedDeleteParameter(UUID barcodeScanDetailID, UUID dispatchingOrderDetailID, String userName, UUID palletID) {
        BarcodeScanDetailID = barcodeScanDetailID;
        DispatchingOrderDetailID = dispatchingOrderDetailID;
        UserName = userName;
        PalletCartonID = palletID;
        }

    }
