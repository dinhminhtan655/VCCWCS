package com.wcs.vcc.api;

import java.util.UUID;

/**
 * Created by aang on 02/06/2018.
 */

public class PalletCartonWeightingParam extends BaseParam{
    public UUID PalletCartonID;
    public int PalletNumber;
    public double CartonWeight;
    public double PalletGrossWeight;
    public int CartonUnits;
    public String PalletRemark;

    public PalletCartonWeightingParam(String username, String deviceNumber) {
        super(username, deviceNumber);
    }

}
