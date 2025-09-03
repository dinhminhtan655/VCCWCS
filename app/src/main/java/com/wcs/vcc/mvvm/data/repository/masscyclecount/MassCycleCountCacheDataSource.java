package com.wcs.vcc.mvvm.data.repository.masscyclecount;

import android.util.SparseArray;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.domain.Locations;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepository;

import java.util.ArrayList;
import java.util.List;

public class MassCycleCountCacheDataSource implements MassCycleCountDataSource.LocalLocation {

    private static MassCycleCountCacheDataSource sInstance;

    private final SparseArray<Locations> cachedLocations = new SparseArray<>();

    public static MassCycleCountCacheDataSource getInstance() {
        if (sInstance == null)
            sInstance = new MassCycleCountCacheDataSource();
        return sInstance;
    }


    @Override

    public void updateMassCycleCountDetail(MassCycleCountDetailUpdateFramentRepository.updateMassCycleCountDetailCallback callback, JsonObject jsonObject) {

    }

    @Override
    public void getMassCycleCountDetailProductList(MassCycleCountDetailUpdateFramentRepository.loadMassCycleCountDetailProductListCallback callback, JsonObject jsonObject) {

    }

    @Override
    public void getLocations(MassCycleCountDetailUpdateFramentRepository.loadLocationsCallback callback) {
        if (cachedLocations.size() > 0) {
            List<Locations> locations = new ArrayList<>();
            for (int i = 0; i < cachedLocations.size(); i++) {
                int key = cachedLocations.keyAt(i);
                locations.add(cachedLocations.get(key));
            }
            callback.onLoaded(locations);
        } else
            callback.onLoadFailed("Thất bại");
    }

    @Override
    public void savedLocations(List<Locations> locations) {
        cachedLocations.clear();
        for (Locations locations1 : locations)
            cachedLocations.put(locations1.getId(), locations1);
    }
}
