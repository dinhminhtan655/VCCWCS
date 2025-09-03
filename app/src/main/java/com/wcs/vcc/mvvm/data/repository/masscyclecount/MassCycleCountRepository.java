package com.wcs.vcc.mvvm.data.repository.masscyclecount;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.domain.MassCycleCount;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.MassCycleCountRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.pallet.PalletRemote;

import java.util.List;

public interface MassCycleCountRepository {

    interface loadMassCycleCountCallback {
        void onMassCycleCountLoaded(List<MassCycleCountRemote> massCycleCounts);
        void onMassCycleCountNormal(List<MassCycleCountRemote> massCycleCounts);
        void onMassCycleCountBlind(List<MassCycleCountRemote> massCycleCountsBlind);
        void onMassCycleCountRandom(List<MassCycleCountRemote> massCycleCountsRandom);
        void onDataNotAvailable();
        void onError();
    }

    interface loadFindPalletIDCallback{
        void onFindPalletIDLoaded(List<PalletRemote> palletRemotes);
        void onDataNoteAvailable();
        void onError();
    }

    void getCycleCount(loadMassCycleCountCallback callback, JsonObject jsonObject);

    void getFindPalletID(loadFindPalletIDCallback callback, JsonObject jsonObject);

    void saveCycleCount(List<MassCycleCount> list);

}
