package com.wcs.vcc.api.checkouttrip.response;

public class TripInfor {
    private String TripNumber;
    private String TripRemark;
    private String RouteDescriptions;
    private String RouteCode;
    private String Status;
    private float TotalCarton;

    public TripInfor(String tripNumber, String tripRemark, String routeDescriptions, String routeCode, String status, float totalCarton) {
        TripNumber = tripNumber;
        TripRemark = tripRemark;
        RouteDescriptions = routeDescriptions;
        RouteCode = routeCode;
        Status = status;
        TotalCarton = totalCarton;
    }

    public TripInfor() {
    }

    public String getTripNumber() {
        return TripNumber;
    }

    public void setTripNumber(String tripNumber) {
        TripNumber = tripNumber;
    }

    public String getTripRemark() {
        return TripRemark;
    }

    public void setTripRemark(String tripRemark) {
        TripRemark = tripRemark;
    }

    public String getRouteDescriptions() {
        return RouteDescriptions;
    }

    public void setRouteDescriptions(String routeDescriptions) {
        RouteDescriptions = routeDescriptions;
    }

    public String getRouteCode() {
        return RouteCode;
    }

    public void setRouteCode(String routeCode) {
        RouteCode = routeCode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public float getTotalCarton() {
        return TotalCarton;
    }

    public void setTotalCarton(float totalCarton) {
        TotalCarton = totalCarton;
    }
}
