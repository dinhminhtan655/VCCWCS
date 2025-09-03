package com.wcs.vcc.mvvm.data.repository.masscyclecount;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.domain.Locations;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.randomcyclecountfragmentrepository.RandomCycleCountInsertFragmentRepository;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.MassCycleCountDetailRepository;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepository;

import java.util.List;

public interface MassCycleCountDataSource {

    interface Remote {
        void getMassCycleCountData(MassCycleCountRepository.loadMassCycleCountCallback callback, JsonObject jsonObject);

        void getFindPallet(MassCycleCountRepository.loadFindPalletIDCallback callback, JsonObject jsonObject);
    }

    interface RandomCycleCountInsert {
        void insertRandomCycleCount(RandomCycleCountInsertFragmentRepository.insertRandomCycleCountCallback callback, JsonObject jsonObject);

        void getMassCycleCountDetailProductList(MassCycleCountDetailUpdateFramentRepository.loadMassCycleCountDetailProductListCallback callback, JsonObject jsonObject);
    }

    interface MassCycleCountDetailRemote {
        void getMassCycleCountDetailData(MassCycleCountDetailRepository.loadMassCycleCountDetailCallback callback, JsonObject jsonObject);

        void confirmMassCycleCountDetail(MassCycleCountDetailRepository.confirmMassCycleCountDetailCallback callback, JsonObject jsonObject);
    }

    interface MassCycleCountDetaillUpdate{
        void updateMassCycleCountDetail(MassCycleCountDetailUpdateFramentRepository.updateMassCycleCountDetailCallback callback, JsonObject jsonObject);

        void getMassCycleCountDetailProductList(MassCycleCountDetailUpdateFramentRepository.loadMassCycleCountDetailProductListCallback callback, JsonObject jsonObject);

        void getLocations(MassCycleCountDetailUpdateFramentRepository.loadLocationsCallback callback);

    }

    interface LocalLocation extends MassCycleCountDetaillUpdate {
        void savedLocations(List<Locations> locations);
    }




}
