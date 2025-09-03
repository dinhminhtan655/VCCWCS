package com.wcs.vcc.mvvm.data.api;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.MassCycleCountRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.pallet.PalletRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.MassCycleCountDetailRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.product.Product;
import com.wcs.vcc.mvvm.data.model.locations.Locations;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.POST;


public interface WCSApi {

    @POST("/api/StockMovementMassCycleCountList")
    Call<List<MassCycleCountRemote>> getMassCycleCount(@Body JsonObject jsonObject);

    @POST("/api/GetCycleCount")
    Call<List<MassCycleCountDetailRemote>> getMassCycleCountDetail(@Body JsonObject jsonObject);

    @POST("/api/MassCycleCountPalletIDUpdate")
    Call<String> updateMassCycleCountDetail(@Body JsonObject jsonObject);

    @POST("/api/ProductList")
    Call<List<Product>> getProductListCycleCountDetail(@Body JsonObject jsonObject);

    @POST("/api/StockMovementMassConfirm")
    Call<String> confirmMassCycleCountDetail(@Body JsonObject jsonObject);

    @POST("/api/PalletFind")
    Call<List<PalletRemote>> getPalletRemote(@Body JsonObject jsonObject);

    @POST("/api/MassCycleCountRandomInsert")
    Call<String> insertRandomCycleCount(@Body JsonObject jsonObject);

    @POST("/api/GetLocations")
    Call<List<Locations>> getLocations();
}
