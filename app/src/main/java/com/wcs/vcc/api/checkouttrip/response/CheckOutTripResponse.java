package com.wcs.vcc.api.checkouttrip.response;

import java.util.ArrayList;

public class CheckOutTripResponse {
    private ArrayList<TripInfor> TripList;
    private ArrayList<TripCarton> TripCartonList;
    private ArrayList<CartonStatus> CartonStatsList;
    private String Message;

    public ArrayList<TripInfor> getTripList() {
        return TripList;
    }

    public void setTripList(ArrayList<TripInfor> tripList) {
        TripList = tripList;
    }

    public ArrayList<TripCarton> getTripCartonList() {
        return TripCartonList;
    }

    public void setTripCartonList(ArrayList<TripCarton> tripCartonList) {
        TripCartonList = tripCartonList;
    }

    public ArrayList<CartonStatus> getCartonStatsList() {
        return CartonStatsList;
    }

    public void setCartonStatsList(ArrayList<CartonStatus> cartonStatsList) {
        CartonStatsList = cartonStatsList;
    }

    public String getMessage() {
        return Message;
    }


    public void setMessage(String message) {
        Message = message;
    }
}
