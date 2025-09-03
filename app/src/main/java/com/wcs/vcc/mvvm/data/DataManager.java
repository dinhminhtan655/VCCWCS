package com.wcs.vcc.mvvm.data;

import com.preference.PowerPreference;
import com.preference.Preference;
import com.wcs.vcc.mvvm.data.api.WCSApi;
import com.wcs.vcc.mvvm.data.db.LocationsDao;
import com.wcs.vcc.mvvm.data.db.WCSDatabase;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountCacheDataSource;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountLocalDataSource;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountRemoteDataSource;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountRepository;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountRepositoryImpl;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.randomcyclecountfragmentrepository.RandomCycleCountInsertFragmentRepository;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.randomcyclecountfragmentrepository.RandomCycleCountInsertFragmentRepositoryImpl;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.MassCycleCountDetailRepository;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.MassCycleCountDetailRepositoryImpl;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepository;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepositoryImpl;
import com.wcs.vcc.mvvm.data.services.WCSService;

public class DataManager {

    private static DataManager sInstance;
    private static WCSApi wcsApi;
    private static MassCycleCountRemoteDataSource massCycleCountRemoteDataSource;

    private DataManager() {

    }

    public static synchronized DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
            wcsApi = WCSService.getInstance().getWcsApi();
            massCycleCountRemoteDataSource = MassCycleCountRemoteDataSource.getInstance(wcsApi);
        }


        return sInstance;
    }

    public Preference getPrefs() {
        return PowerPreference.getDefaultFile();
    }

    public MassCycleCountRepository getMassCycleCountRepository() {
        return MassCycleCountRepositoryImpl.getInstance(massCycleCountRemoteDataSource);
    }

    public MassCycleCountDetailRepository getMassCycleCountDetailRepository() {
        return MassCycleCountDetailRepositoryImpl.getInstance(massCycleCountRemoteDataSource);
    }

    public MassCycleCountDetailUpdateFramentRepository updateMassCycleDetailRepository() {

        LocationsDao locationsDao = WCSDatabase.getInstance().locationsDao();
        MassCycleCountLocalDataSource dataSourceLocalLocation = MassCycleCountLocalDataSource.getInstance(locationsDao);

        MassCycleCountCacheDataSource cacheDataSource = MassCycleCountCacheDataSource.getInstance();

        return MassCycleCountDetailUpdateFramentRepositoryImpl.getInstance(massCycleCountRemoteDataSource, dataSourceLocalLocation,cacheDataSource);
    }

    public RandomCycleCountInsertFragmentRepository insertRandomCycleCountRepository(){
        return RandomCycleCountInsertFragmentRepositoryImpl.getInstance(massCycleCountRemoteDataSource);
    }



}
