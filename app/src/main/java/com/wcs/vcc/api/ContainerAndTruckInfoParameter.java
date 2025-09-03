package com.wcs.vcc.api;


public class ContainerAndTruckInfoParameter {
    private int gate;
    private int storeId;
    private String FromDate;
    private String ToDate;

    public ContainerAndTruckInfoParameter(int gate, int storeId, String FromDate, String ToDate) {
        this.gate = gate;
        this.storeId = storeId;
        this.FromDate = FromDate;
        this.ToDate = ToDate;
    }
}
