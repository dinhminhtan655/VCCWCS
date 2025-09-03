package com.wcs.vcc.main.scanvinmart;

import java.util.ArrayList;

public class LoadPackInCartonResponse {
    private ArrayList<PackInCartonInfo> Packs;
    private String Message;

    public ArrayList<PackInCartonInfo> getPacks() {
        return Packs;
    }

    public void setPacks(ArrayList<PackInCartonInfo> packs) {
        Packs = packs;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
