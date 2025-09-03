package com.wcs.vcc.api;

/**
 * Created by tranxuanloc on 9/22/2016.
 */
public class MaintenanceJobDetailParameter {
    private String UserName;
    private int MaintenanceJobID;
    private String Frequence;

    public MaintenanceJobDetailParameter(String userName, int maintenanceJobID, String frequence) {
        UserName = userName;
        MaintenanceJobID = maintenanceJobID;
        Frequence = frequence;
    }
}
