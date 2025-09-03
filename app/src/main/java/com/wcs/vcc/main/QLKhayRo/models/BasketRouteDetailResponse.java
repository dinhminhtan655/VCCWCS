package com.wcs.vcc.main.QLKhayRo.models;

import com.google.gson.annotations.SerializedName;

public class BasketRouteDetailResponse {
    @SerializedName("palletID")
    int palletID;
    @SerializedName("packTotal")
    int packTotal;
    @SerializedName("basketTotal")
    int basketTotal;

    public int getPackConfirmTotal() {
        return packConfirmTotal;
    }

    public void setPackConfirmTotal(int packConfirmTotal) {
        this.packConfirmTotal = packConfirmTotal;
    }

    @SerializedName("packConfirmTotal")
    int packConfirmTotal;

    public BasketRouteDetailResponse() {
    }

    public BasketRouteDetailResponse(int palletID, int packTotal, int basketTotal, int packConfirmTotal) {
        this.palletID = palletID;
        this.packTotal = packTotal;
        this.basketTotal = basketTotal;
        this.packConfirmTotal = packConfirmTotal;
    }

    public int getPalletID() {
        return palletID;
    }

    public void setPalletID(int palletID) {
        this.palletID = palletID;
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
}
