package com.wcs.vcc.mvvm.data.repository.masscyclecountdetail;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.MassCycleCountDetailRemote;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountDataSource;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountRemoteDataSource;

import java.util.List;

public class MassCycleCountDetailRepositoryImpl implements MassCycleCountDetailRepository {

    private final MassCycleCountDataSource.MassCycleCountDetailRemote massCycleCountDetailRemote;


    private static MassCycleCountDetailRepositoryImpl sInstance;


    public MassCycleCountDetailRepositoryImpl(MassCycleCountDataSource.MassCycleCountDetailRemote massCycleCountDetailRemote) {
        this.massCycleCountDetailRemote = massCycleCountDetailRemote;
    }

    public static MassCycleCountDetailRepositoryImpl getInstance(MassCycleCountRemoteDataSource remoteDataSource) {
        if (sInstance == null) {
            sInstance = new MassCycleCountDetailRepositoryImpl(remoteDataSource);
        }
        return sInstance;
    }


    @Override
    public void getCycleCountDetail(loadMassCycleCountDetailCallback callback, JsonObject jsonObject) {
        if (callback == null) return;
        getMassCycleCountDetailFromRemoteDataSource(callback, jsonObject);

    }

    @Override
    public boolean checkExistingLocation(List<MassCycleCountDetailRemote> remoteList, String locationID) {
        for (int i = 0; i < remoteList.size(); i++) {
            if (remoteList.get(i).getLocationNumber().trim().equalsIgnoreCase(locationID.trim()))
                return true;
        }
        return false;
    }

    @Override
    public boolean checkingExistingPallet(List<MassCycleCountDetailRemote> remoteList, String palletID) {
        for (int i = 0; i < remoteList.size(); i++) {
            if (remoteList.get(i).getPalletNumber().trim().equalsIgnoreCase(palletID.trim()))
                return true;
        }
        return false;
    }

    @Override
    public void confirmCycleCountDetail(confirmMassCycleCountDetailCallback callback, JsonObject jsonObject) {
        massCycleCountDetailRemote.confirmMassCycleCountDetail(new confirmMassCycleCountDetailCallback() {
            @Override
            public void onConfirmedCycleCountDetailUpdated(String success) {
                callback.onConfirmedCycleCountDetailUpdated(success);
            }

            @Override
            public void onConfirmedCycleCountDetailFailed(String failed) {
                callback.onConfirmedCycleCountDetailFailed(failed);
            }

            @Override
            public void onConfirmError() {
                callback.onConfirmError();
            }
        }, jsonObject);
    }


    @Override
    public MassCycleCountDetailRemote sendMassCycleCountDetailRemote(List<MassCycleCountDetailRemote> remoteList, String sData) {
        for (int i = 0; i < remoteList.size(); i++) {
            if (remoteList.get(i).getPalletNumber().trim().equalsIgnoreCase(sData) || remoteList.get(i).getLocationNumber().trim().equalsIgnoreCase(sData))
                return remoteList.get(i);
        }
        return null;
    }


    private void getMassCycleCountDetailFromRemoteDataSource(final MassCycleCountDetailRepository.loadMassCycleCountDetailCallback callback, JsonObject jsonObject) {
        massCycleCountDetailRemote.getMassCycleCountDetailData(new loadMassCycleCountDetailCallback() {
            @Override
            public void onMassCycleCountDetailLoaded(List<MassCycleCountDetailRemote> remoteList) {
                callback.onMassCycleCountDetailLoaded(remoteList);
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
