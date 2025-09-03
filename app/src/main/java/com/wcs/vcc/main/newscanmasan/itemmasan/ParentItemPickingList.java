package com.wcs.vcc.main.newscanmasan.itemmasan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParentItemPickingList {

    @SerializedName("PickingList")
    public List<ItemPickingList> PickingList;

    @SerializedName("Message")
    public String Message;
}
