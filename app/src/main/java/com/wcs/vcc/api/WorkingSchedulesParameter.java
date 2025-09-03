package com.wcs.vcc.api;

/**
 * Created by tranxuanloc on 4/2/2016.
 */
public class WorkingSchedulesParameter {
    private String UserName;
    private String ReportDate;

    public WorkingSchedulesParameter(String reportDate, String userName) {
        ReportDate = reportDate;
        UserName = userName;
    }
}
