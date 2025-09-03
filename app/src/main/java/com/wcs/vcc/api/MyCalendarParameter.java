package com.wcs.vcc.api;

/**
 * Created by tranxuanloc on 7/6/2016.
 */
public class MyCalendarParameter {
    private String UserName;
    private int Month;
    private int Year;

    public MyCalendarParameter(String userName, int month, int year) {
        UserName = userName;
        Month = month;
        Year = year;
    }
}
