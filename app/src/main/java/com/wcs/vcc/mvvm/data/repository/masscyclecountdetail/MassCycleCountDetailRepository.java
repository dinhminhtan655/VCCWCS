package com.wcs.vcc.mvvm.data.repository.masscyclecountdetail;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.MassCycleCountDetailRemote;

import java.util.List;

public interface MassCycleCountDetailRepository {

    interface loadMassCycleCountDetailCallback{
        void onMassCycleCountDetailLoaded(List<MassCycleCountDetailRemote> remoteList);
        void onDataNotAvailable();
        void onError();
    }

    interface confirmMassCycleCountDetailCallback{
        void onConfirmedCycleCountDetailUpdated(String success);
        void onConfirmedCycleCountDetailFailed(String failed);
        void onConfirmError();
    }

    void getCycleCountDetail(loadMassCycleCountDetailCallback callback, JsonObject jsonObject);

    boolean checkExistingLocation(List<MassCycleCountDetailRemote> remoteList, String locationID);

    MassCycleCountDetailRemote sendMassCycleCountDetailRemote(List<MassCycleCountDetailRemote> remoteList, String sData);

    boolean checkingExistingPallet(List<MassCycleCountDetailRemote> remoteList, String palletID);

    void confirmCycleCountDetail(confirmMassCycleCountDetailCallback callback, JsonObject jsonObject);

}
