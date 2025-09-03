package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketRouteResponse {
    @SerializedName("routeNo")
    String routeNo;
    @SerializedName("packTotal")
    int packTotal;
    @SerializedName("basketTotal")
    int basketTotal;
    @SerializedName("basketTotalReceived")
    int basketTotalReceived;
    @SerializedName("ccdcTotal")
    int totalCCDC;
    @SerializedName("status")
    int status;
    @SerializedName("ccdcTotalReceived")
    int ccdcTotalReceived;

    public BasketRouteResponse() {
    }

    public BasketRouteResponse(String routeNo, int packTotal, int basketTotal, int basketTotalReceived, int totalCCDC, int status, int ccdcTotalReceived) {
        this.routeNo = routeNo;
        this.packTotal = packTotal;
        this.basketTotal = basketTotal;
        this.basketTotalReceived = basketTotalReceived;
        this.totalCCDC = totalCCDC;
        this.status = status;
        this.ccdcTotalReceived = ccdcTotalReceived;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public int getPackTotal() {
        return packTotal;
    }

    public void setPackTotal(int packTotal) {
        this.packTotal = packTotal;
    }

    public int getBasketTotal() {
        return basketTotal;
    }

    public void setBasketTotal(int basketTotal) {
        this.basketTotal = basketTotal;
    }

    public int getBasketTotalReceived() {
        return basketTotalReceived;
    }

    public void setBasketTotalReceived(int basketTotalReceived) {
        this.basketTotalReceived = basketTotalReceived;
    }

    public int getTotalCCDC() {
        return totalCCDC;
    }

    public void setTotalCCDC(int totalCCDC) {
        this.totalCCDC = totalCCDC;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCcdcTotalReceived() {
        return ccdcTotalReceived;
    }

    public void setCcdcTotalReceived(int ccdcTotalReceived) {
        this.ccdcTotalReceived = ccdcTotalReceived;
    }
}
