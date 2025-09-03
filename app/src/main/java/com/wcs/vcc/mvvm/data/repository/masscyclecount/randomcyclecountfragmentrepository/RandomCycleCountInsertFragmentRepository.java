package com.wcs.vcc.mvvm.data.repository.masscyclecount.randomcyclecountfragmentrepository;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepository;

public interface RandomCycleCountInsertFragmentRepository {

    interface insertRandomCycleCountCallback{
        void onRandomCycleCountInserted(String success);
        void onRandomCycleCountInsertedFailed(String failed);
        void onError();
    }

    void insertRandomCycleCount(insertRandomCycleCountCallback callback, JsonObject jsonObject);

    void loadProductListCycleCountDetail(MassCycleCountDetailUpdateFramentRepository.loadMassCycleCountDetailProductListCallback callback, JsonObject jsonObject);

}
