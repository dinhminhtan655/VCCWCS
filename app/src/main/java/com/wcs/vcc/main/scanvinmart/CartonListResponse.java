package com.wcs.vcc.main.scanvinmart;

import java.util.ArrayList;

public class CartonListResponse {
    private ArrayList<CartonInfor> Cartons;
    private String Message;

    public ArrayList<CartonInfor> getCartons() {
        return Cartons;
    }

    public void setCartons(ArrayList<CartonInfor> cartons) {
        Cartons = cartons;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
