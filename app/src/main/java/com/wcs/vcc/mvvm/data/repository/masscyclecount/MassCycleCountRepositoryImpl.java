package com.wcs.vcc.mvvm.data.repository.masscyclecount;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.domain.MassCycleCount;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.MassCycleCountRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.pallet.PalletRemote;

import java.util.List;

public class MassCycleCountRepositoryImpl implements MassCycleCountRepository {

    private final MassCycleCountDataSource.Remote massCycleCountRemote;

    private static MassCycleCountRepositoryImpl sInstance;

    private MassCycleCountRepositoryImpl(MassCycleCountDataSource.Remote massCycleCountRemote) {
        this.massCycleCountRemote = massCycleCountRemote;
    }


    public static MassCycleCountRepositoryImpl getInstance(MassCycleCountRemoteDataSource remoteDataSource) {
        if (sInstance == null) {
            sInstance = new MassCycleCountRepositoryImpl(remoteDataSource);
        }
        return sInstance;
    }


    @Override
    public void getCycleCount(loadMassCycleCountCallback callback, JsonObject jsonObject) {
        if (callback == null) return;
        getMassCycleCountFroRemoteDataSource(callback, jsonObject);
    }

    @Override
    public void getFindPalletID(loadFindPalletIDCallback callback, JsonObject jsonObject) {
        if (callback == null) return;
        getPalletID(callback, jsonObject);
    }

    @Override
    public void saveCycleCount(List<MassCycleCount> list) {

    }

    private void getPalletID(final MassCycleCountRepository.loadFindPalletIDCallback callback, JsonObject jsonObject) {
        massCycleCountRemote.getFindPallet(new loadFindPalletIDCallback() {
            @Override
            public void onFindPalletIDLoaded(List<PalletRemote> palletRemotes) {
                callback.onFindPalletIDLoaded(palletRemotes);
            }

            @Override
            public void onDataNoteAvailable() {
                callback.onDataNoteAvailable();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        }, jsonObject);
    }

    private void getMassCycleCountFroRemoteDataSource(final MassCycleCountRepository.loadMassCycleCountCallback callback, JsonObject jsonObject) {
        massCycleCountRemote.getMassCycleCountData(new loadMassCycleCountCallback() {
            @Override
            public void onMassCycleCountLoaded(List<MassCycleCountRemote> massCycleCounts) {
                callback.onMassCycleCountLoaded(massCycleCounts);
            }

            @Override
            public void onMassCycleCountNormal(List<MassCycleCountRemote> massCycleCounts) {
                callback.onMassCycleCountNormal(massCycleCounts);
            }

            @Override
            public void onMassCycleCountBlind(List<MassCycleCountRemote> massCycleCountsBlind) {
                callback.onMassCycleCountBlind(massCycleCountsBlind);
            }

            @Override
            public void onMassCycleCountRandom(List<MassCycleCountRemote> massCycleCountsRandom) {
                callback.onMassCycleCountRandom(massCycleCountsRandom);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        }, jsonObject);
    }


}
