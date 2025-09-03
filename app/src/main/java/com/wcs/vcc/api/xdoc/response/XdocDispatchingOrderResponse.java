package com.wcs.vcc.api.xdoc.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class XdocDispatchingOrderResponse {
    @SerializedName("DispatchingOrders")
    private ArrayList<XdocOrder> DispatchingOrders;
    @SerializedName("Message")
    private  String Message;
    @SerializedName("Cartons")
    public List<XdocCarton> Cartons;

    public XdocDispatchingOrderResponse(ArrayList<XdocOrder> dispatchingOrders, String message, ArrayList<XdocCarton> cartons) {
        DispatchingOrders = dispatchingOrders;
        Message = message;
        Cartons = cartons;
    }

    public ArrayList<XdocOrder> getDispatchingOrders() {
        return DispatchingOrders;
    }

    public void setDispatchingOrders(ArrayList<XdocOrder> dispatchingOrders) {
        DispatchingOrders = dispatchingOrders;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<XdocCarton> getCartons() {
        return Cartons;
    }

    public void setCartons(ArrayList<XdocCarton> cartons) {
        Cartons = cartons;
    }
}
