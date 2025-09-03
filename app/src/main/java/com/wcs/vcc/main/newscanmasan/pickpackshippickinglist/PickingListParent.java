package com.wcs.vcc.main.newscanmasan.pickpackshippickinglist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PickingListParent {

    @SerializedName("PickingList")
    public List<PickingList> PickingList;
    @SerializedName("Message")
    public String Message;
    @SerializedName("GroupSortingInfo")
    public GroupSortingInfoResponse GroupSortingInfo;

}
