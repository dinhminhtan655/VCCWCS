package com.wcs.vcc.api.checkouttrip.response;

import java.util.UUID;

public class TripCarton {
    private String SHIP_TO_LOCATION;
    private UUID CustomerClientID;
    private String DispatchingOrderNumber;
    private int TotalPlanCarton;
    private int TotalCartonDispatch;
    private String Status;

    public TripCarton(String SHIP_TO_LOCATION, UUID customerClientID, int totalPlanCarton, int totalCartonDispatch, String status) {
        this.SHIP_TO_LOCATION = SHIP_TO_LOCATION;
        CustomerClientID = customerClientID;
        TotalPlanCarton = totalPlanCarton;
        TotalCartonDispatch = totalCartonDispatch;
        Status = status;
    }

    public String getDispatchingOrderNumber() {
        return DispatchingOrderNumber;
    }

    public void setDispatchingOrderNumber(String dispatchingOrderNumber) {
        DispatchingOrderNumber = dispatchingOrderNumber;
    }

    public String getSHIP_TO_LOCATION() {
        return SHIP_TO_LOCATION;
    }

    public void setSHIP_TO_LOCATION(String SHIP_TO_LOCATION) {
        this.SHIP_TO_LOCATION = SHIP_TO_LOCATION;
    }

    public UUID getCustomerClientID() {
        return CustomerClientID;
    }

    public void setCustomerClientID(UUID customerClientID) {
        CustomerClientID = customerClientID;
    }

    public int getTotalPlanCarton() {
        return TotalPlanCarton;
    }

    public void setTotalPlanCarton(int totalPlanCarton) {
        TotalPlanCarton = totalPlanCarton;
    }

    public int getTotalCartonDispatch() {
        return TotalCartonDispatch;
    }

    public void setTotalCartonDispatch(int totalCartonDispatch) {
        TotalCartonDispatch = totalCartonDispatch;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
