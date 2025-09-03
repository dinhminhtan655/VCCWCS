package com.wcs.vcc.api;

/**
 * Created by aang on 10/06/2018.
 */

public class EmployeeWorkingISOUpdateParams extends BaseParam{
    public String OrderNumber;
    public String StartTime;
    public String TruckNo;
    public String SealNo;
    public String Temperature;
    public String HasThremometerTemp;
    public String SetupTemperature;
    public String DockNumber;
    public boolean HasLocker;
    public boolean HasThremometer;
    public boolean TruckContAfterDamaged;
    public boolean Electricity;
    public boolean TruckContDirty;
    public boolean TruckContSmell;
    public int StoreID;
    private int OrderStatus;


    public EmployeeWorkingISOUpdateParams(String username, String deviceNumber) {
        super(username, deviceNumber);
    }

}
