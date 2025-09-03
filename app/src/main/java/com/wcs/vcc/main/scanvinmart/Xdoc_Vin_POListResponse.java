package com.wcs.vcc.main.scanvinmart;

import java.util.ArrayList;

public class Xdoc_Vin_POListResponse {
    private ArrayList<ItemPoInfor> Order;
    private String Message;

    public ArrayList<ItemPoInfor> getOrder() {
        return Order;
    }

    public void setOrder(ArrayList<ItemPoInfor> order) {
        Order = order;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
