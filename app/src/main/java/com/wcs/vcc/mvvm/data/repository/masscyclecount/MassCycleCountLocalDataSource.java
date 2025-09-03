package com.wcs.vcc.mvvm.data.repository.masscyclecount;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.db.LocationsDao;
import com.wcs.vcc.mvvm.data.domain.Locations;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepository;
import com.wcs.vcc.mvvm.util.diskexecutor.DiskExecutor;

import java.util.List;
import java.util.concurrent.Executor;

public class MassCycleCountLocalDataSource implements MassCycleCountDataSource.LocalLocation {

    private final Executor executor;
    private final LocationsDao locationsDao;


    private static MassCycleCountLocalDataSource instance;

    public MassCycleCountLocalDataSource(Executor executor, LocationsDao locationsDao) {
        this.executor = executor;
        this.locationsDao = locationsDao;
    }

    public static MassCycleCountLocalDataSource getInstance(LocationsDao locationsDao) {
        if (instance == null)
            instance = new MassCycleCountLocalDataSource(new DiskExecutor(), locationsDao);
        return instance;
    }


    @Override
    public void updateMassCycleCountDetail(MassCycleCountDetailUpdateFramentRepository.updateMassCycleCountDetailCallback callback, JsonObject jsonObject) {

    }

    @Override
    public void getMassCycleCountDetailProductList(MassCycleCountDetailUpdateFramentRepository.loadMassCycleCountDetailProductListCallback callback, JsonObject jsonObject) {

    }

    @Override
    public void getLocations(MassCycleCountDetailUpdateFramentRepository.loadLocationsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<Locations> locations = locationsDao.getLocations();
                if (!locations.isEmpty()) {
                    callback.onLoaded(locations);
                } else {
                    callback.onLoadFailed("Không có dữ liệu");
                }
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void savedLocations(List<Locations> locations) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                locationsDao.saveLocations(locations);
            }
        };
        executor.execute(runnable);
    }
}
