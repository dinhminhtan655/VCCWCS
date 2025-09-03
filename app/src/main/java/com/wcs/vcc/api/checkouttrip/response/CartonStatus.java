package com.wcs.vcc.api.checkouttrip.response;

import java.util.UUID;

public class CartonStatus {
    private UUID DispatchingOrderID;
    private String DispatchingOrderNumber;
    private Integer CartonNumber;
    private Integer DispatchingProductCartonID;
    private String Status;
    private float Qty;
    private float Weights;

    public CartonStatus(UUID dispatchingOrderID, String dispatchingOrderNumber, Integer cartonNumber, Integer dispatchingProductCartonID, String status, float qty, float weights) {
        DispatchingOrderID = dispatchingOrderID;
        DispatchingOrderNumber = dispatchingOrderNumber;
        CartonNumber = cartonNumber;
        DispatchingProductCartonID = dispatchingProductCartonID;
        Status = status;
        Qty = qty;
        Weights = weights;
    }

    public UUID getDispatchingOrderID() {
        return DispatchingOrderID;
    }

    public void setDispatchingOrderID(UUID dispatchingOrderID) {
        DispatchingOrderID = dispatchingOrderID;
    }

    public String getDispatchingOrderNumber() {
        return DispatchingOrderNumber;
    }

    public void setDispatchingOrderNumber(String dispatchingOrderNumber) {
        DispatchingOrderNumber = dispatchingOrderNumber;
    }

    public Integer getCartonNumber() {
        return CartonNumber;
    }

    public void setCartonNumber(Integer cartonNumber) {
        CartonNumber = cartonNumber;
    }

    public Integer getDispatchingProductCartonID() {
        return DispatchingProductCartonID;
    }

    public void setDispatchingProductCartonID(Integer dispatchingProductCartonID) {
        DispatchingProductCartonID = dispatchingProductCartonID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public float getQty() {
        return Qty;
    }

    public void setQty(float qty) {
        Qty = qty;
    }

    public float getWeights() {
        return Weights;
    }

    public void setWeights(float weights) {
        Weights = weights;
    }
}
