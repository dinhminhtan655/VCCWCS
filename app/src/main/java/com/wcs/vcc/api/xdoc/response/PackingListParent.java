package com.wcs.vcc.api.xdoc.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackingListParent {

    @SerializedName("DispatchingOrders")
    public String DispatchingOrders;
    @SerializedName("Cartons")
    public String Cartons;
    @SerializedName("PackingList")
    public List<PackingList> PackingList;
    @SerializedName("Message")
    public String Message2;

}
