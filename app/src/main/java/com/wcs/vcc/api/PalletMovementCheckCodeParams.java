package com.wcs.vcc.api;

/**
 * Created by aang on 10/06/2018.
 */

public class PalletMovementCheckCodeParams {
    public String LocationShort;
    public Byte LocationCode;

    public PalletMovementCheckCodeParams(String locationShort, Byte locationCode) {
        LocationShort = locationShort;
        LocationCode = locationCode;
    }
}
