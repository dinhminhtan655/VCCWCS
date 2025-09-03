package com.wcs.vcc.main.newscanmasan.pickpackshippickinglist;

public class GroupSortingInfoResponse {
    public String CustomerERP;
    public  String Region;
    public  Integer GroupSorting;

    public GroupSortingInfoResponse(String customerERP, String region, Integer groupSorting) {
        CustomerERP = customerERP;
        Region = region;
        GroupSorting = groupSorting;
    }
}
