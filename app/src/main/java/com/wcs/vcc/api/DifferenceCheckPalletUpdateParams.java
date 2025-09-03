package com.wcs.vcc.api;

import java.util.UUID;

public class DifferenceCheckPalletUpdateParams extends BaseParam {
    public UUID PalletID;
    public int newQuantity;
    public int Flag;

    public DifferenceCheckPalletUpdateParams(String username, String deviceNumber, UUID palletID, int newQuantity, int flag) {
        super(username, deviceNumber);
        PalletID = palletID;
        this.newQuantity = newQuantity;
        Flag = flag;
    }
}
