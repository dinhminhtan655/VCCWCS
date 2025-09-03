package com.wcs.vcc.api;


public class EmployeePresentParameter {
    private String position;
    private int Department = 1;
    private int storeId;

    public EmployeePresentParameter(int department, String position, int storeId) {
        Department = department;
        this.position = position;
        this.storeId = storeId;
    }
}
