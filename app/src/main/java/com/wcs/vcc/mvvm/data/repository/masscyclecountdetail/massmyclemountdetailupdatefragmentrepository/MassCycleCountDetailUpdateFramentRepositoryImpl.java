package com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.domain.Locations;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.product.Product;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountDataSource;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountRemoteDataSource;

import java.util.List;

public class MassCycleCountDetailUpdateFramentRepositoryImpl implements MassCycleCountDetailUpdateFramentRepository {


    private final MassCycleCountDataSource.MassCycleCountDetaillUpdate massCycleCountDetailUpdate;
    private final MassCycleCountDataSource.LocalLocation localLocation;
    private final MassCycleCountDataSource.LocalLocation cacheLocation;


    private static MassCycleCountDetailUpdateFramentRepositoryImpl sInstance;

    public MassCycleCountDetailUpdateFramentRepositoryImpl(MassCycleCountDataSource.MassCycleCountDetaillUpdate massCycleCountDetailUpdate,
                                                           MassCycleCountDataSource.LocalLocation localLocation,
                                                           MassCycleCountDataSource.LocalLocation cacheLocation) {
        this.massCycleCountDetailUpdate = massCycleCountDetailUpdate;
        this.localLocation = localLocation;
        this.cacheLocation = cacheLocation;
    }

    public static MassCycleCountDetailUpdateFramentRepositoryImpl getInstance(MassCycleCountRemoteDataSource dataSource,
                                                                              MassCycleCountDataSource.LocalLocation localLocation,
                                                                              MassCycleCountDataSource.LocalLocation cacheLocation) {
        if (sInstance == null)
            sInstance = new MassCycleCountDetailUpdateFramentRepositoryImpl(dataSource, localLocation, cacheLocation);
        return sInstance;
    }


    @Override
    public void updateCycleCountDetail(updateMassCycleCountDetailCallback callback, JsonObject jsonObject) {
        if (callback == null) return;
        updateMassCycleCountDetailFromDataSource(callback, jsonObject);
    }


    @Override
    public void loadProductListCycleCountDetail(loadMassCycleCountDetailProductListCallback callback, JsonObject jsonObject) {
        if (callback == null) return;
        getProductListMassCycleCountDetailFromDataSource(callback, jsonObject);
    }

    @Override
    public void loadLocations(loadLocationsCallback callback) {
        if (callback == null) return;
        cacheLocation.getLocations(new loadLocationsCallback() {
            @Override
            public void onLoaded(List<Locations> locations) {
                callback.onLoaded(locations);
            }

            @Override
            public void onLoadFailed(String failed) {
                loadLocationFromLocalDataSource(callback);
            }

            @Override
            public void onError() {
                //not implemented in cache data source
            }
        });

    }

    @Override
    public void saveLocations(List<Locations> locations) {
        localLocation.savedLocations(locations);
    }


    private void loadLocationFromLocalDataSource(final MassCycleCountDetailUpdateFramentRepository.loadLocationsCallback callback) {
        localLocation.getLocations(new loadLocationsCallback() {
            @Override
            public void onLoaded(List<Locations> locations) {
                callback.onLoaded(locations);
                refreshCache(locations);
            }

            @Override
            public void onLoadFailed(String failed) {
                loadLocationsFromDataSource(callback);
            }

            @Override
            public void onError() {
                //not implemented in local data source
            }
        });
    }

    private void loadLocationsFromDataSource(final MassCycleCountDetailUpdateFramentRepository.loadLocationsCallback callback) {
        massCycleCountDetailUpdate.getLocations(new loadLocationsCallback() {
            @Override
            public void onLoaded(List<Locations> locations) {
                callback.onLoaded(locations);
                saveLocations(locations);
                refreshCache(locations);
            }

            @Override
            public void onLoadFailed(String failed) {
                callback.onLoadFailed(failed);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    private void updateMassCycleCountDetailFromDataSource(final MassCycleCountDetailUpdateFramentRepository.updateMassCycleCountDetailCallback callback, JsonObject jsonObject) {
        massCycleCountDetailUpdate.updateMassCycleCountDetail(new updateMassCycleCountDetailCallback() {
            @Override
            public void onMassCycleCountDetailUpdated(String success) {
                callback.onMassCycleCountDetailUpdated(success);
            }

            @Override
            public void onMassCycleCountDetailUpdatedFailed(String failed) {
                callback.onMassCycleCountDetailUpdatedFailed(failed);
            }

            @Override
            public void onUpdatedError() {
                callback.onUpdatedError();
            }
        }, jsonObject);
    }

    private void getProductListMassCycleCountDetailFromDataSource(final MassCycleCountDetailUpdateFramentRepository.loadMassCycleCountDetailProductListCallback callback, JsonObject jsonObject) {
        massCycleCountDetailUpdate.getMassCycleCountDetailProductList(new loadMassCycleCountDetailProductListCallback() {
            @Override
            public void onLoaded(List<Product> list) {
                callback.onLoaded(list);
            }

            @Override
            public void onLoadFailed(String failed) {
                callback.onLoadFailed(failed);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        }, jsonObject);
    }

    private void refreshCache(List<Locations> locations) {
        cacheLocation.savedLocations(locations);
    }
}
