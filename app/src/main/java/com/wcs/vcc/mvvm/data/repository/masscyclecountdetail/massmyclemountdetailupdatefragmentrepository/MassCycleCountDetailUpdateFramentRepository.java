package com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.domain.Locations;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.product.Product;

import java.util.List;

public interface MassCycleCountDetailUpdateFramentRepository {

    interface updateMassCycleCountDetailCallback{
        void onMassCycleCountDetailUpdated(String success);
        void onMassCycleCountDetailUpdatedFailed(String failed);
        void onUpdatedError();
    }

    interface loadMassCycleCountDetailProductListCallback{
        void onLoaded(List<Product> list);
        void onLoadFailed(String failed);
        void onError();
    }

    interface loadLocationsCallback{
        void onLoaded(List<Locations> locations);
        void onLoadFailed(String failed);
        void onError();
    }



    void updateCycleCountDetail(updateMassCycleCountDetailCallback callback, JsonObject jsonObject);

    void loadProductListCycleCountDetail(loadMassCycleCountDetailProductListCallback callback, JsonObject jsonObject);

    void loadLocations(loadLocationsCallback callback);

    void saveLocations(List<Locations> locations);


}
