package com.wcs.vcc.api;

import java.util.UUID;

/**
 * Created by Trần Xuân Lộc on 1/22/2016.
 */
public class ContainerCheckingDetailParameter {
    public UUID ContInOutID;
    public String UserName;
    public String VehicleType;

    public ContainerCheckingDetailParameter(UUID contInOutID, String userName,String vehicleType) {
        ContInOutID = contInOutID;
        UserName = userName;
        VehicleType = vehicleType;
    }
}
