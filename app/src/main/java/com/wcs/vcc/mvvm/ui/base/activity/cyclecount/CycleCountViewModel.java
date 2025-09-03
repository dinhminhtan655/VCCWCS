package com.wcs.vcc.mvvm.ui.base.activity.cyclecount;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.wcs.vcc.main.emdk.ScanListener;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.MassCycleCountRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.pallet.PalletRemote;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountRepository;
import com.wcs.vcc.mvvm.ui.base.activity.base.BaseViewModel;
import com.wcs.vcc.utilities.Const;

import java.util.List;

public class CycleCountViewModel extends BaseViewModel implements ScanListener {

    private final MutableLiveData<List<MassCycleCountRemote>> massCycleCountLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MassCycleCountRemote>> massCycleCountNormalLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MassCycleCountRemote>> massCycleCountBlindLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MassCycleCountRemote>> massCycleCountRandomLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> showTextFromDateOnButton = new MutableLiveData<>();
    private final MutableLiveData<String> showTextToDateOnButton = new MutableLiveData<>();
    private final MutableLiveData<String> showMessageErrorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> showLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> hideLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<MassCycleCountRemote> navigateToMassID = new MutableLiveData<>();
    private final MutableLiveData<String> sendScanData = new MutableLiveData<>();

    //Find PalletID
    private final MutableLiveData<List<PalletRemote>> palletRemote = new MutableLiveData<>();
    private final MutableLiveData<String> palletMessage = new MutableLiveData<>();

    private final MassCycleCountRepository massCycleCountRepository;

    private final MassCycleCountCallback massCycleCountCallback = new MassCycleCountCallback();

    private final MassCycleCountPalletIDCallback massCycleCountPalletIDCallback = new MassCycleCountPalletIDCallback();

    CycleCountViewModel(MassCycleCountRepository massCycleCountRepository) {
        this.massCycleCountRepository = massCycleCountRepository;
    }


    public void loadMassCycleCount(JsonObject jsonObject) {
        setIsLoading(true);
        massCycleCountRepository.getCycleCount(massCycleCountCallback, jsonObject);
    }

    public void loadPalletID(JsonObject jsonObject) {
        massCycleCountRepository.getFindPalletID(massCycleCountPalletIDCallback, jsonObject);
    }

    private void setIsLoading(boolean loading) {
        if (loading) {
            showLoadingLiveData.postValue(null);
        } else
            hideLoadingLiveData.postValue(null);
    }

    public void onMassCycleRemoteClicked(MassCycleCountRemote remote) {
        navigateToMassID.postValue(remote);
    }


    public void onDatePreNextFromButton(String strDate) {
        showTextFromDateOnButton.postValue(strDate);
    }

    public void onDatePreNextToButton(String strDate) {
        showTextToDateOnButton.postValue(strDate);
    }

    @Override
    public void onData(String data) {
        ProcessingString(data);
    }

    private void ProcessingString(String data) {
        Const.timePauseActive = 0;
        data = data.trim();
        if (!data.equals("") && !data.contains("\t\t")) {
            sendScanData.postValue(data);
        }
    }

    private class MassCycleCountPalletIDCallback implements MassCycleCountRepository.loadFindPalletIDCallback {

        @Override
        public void onFindPalletIDLoaded(List<PalletRemote> palletRemotes) {
            palletRemote.postValue(palletRemotes);
        }

        @Override
        public void onDataNoteAvailable() {
            palletMessage.postValue("Không có dữ liệu");
        }

        @Override
        public void onError() {
            palletMessage.postValue("Lỗi chưa xác định");
        }
    }

    private class MassCycleCountCallback implements MassCycleCountRepository.loadMassCycleCountCallback {

        @Override
        public void onMassCycleCountLoaded(List<MassCycleCountRemote> massCycleCounts) {
            setIsLoading(false);
            massCycleCountLiveData.postValue(massCycleCounts);
        }

        @Override
        public void onMassCycleCountNormal(List<MassCycleCountRemote> massCycleCounts) {
            massCycleCountNormalLiveData.postValue(massCycleCounts);
        }

        @Override
        public void onMassCycleCountBlind(List<MassCycleCountRemote> massCycleCountsBlind) {
            massCycleCountBlindLiveData.postValue(massCycleCountsBlind);
        }

        @Override
        public void onMassCycleCountRandom(List<MassCycleCountRemote> massCycleCountsRandom) {
            massCycleCountRandomLiveData.postValue(massCycleCountsRandom);
        }

        @Override
        public void onDataNotAvailable() {
            setIsLoading(false);
            showMessageErrorLiveData.postValue("Không có dữ liệu");
        }

        @Override
        public void onError() {
            setIsLoading(false);
            showMessageErrorLiveData.postValue("Lỗi chưa xác định");
        }
    }

    public LiveData<List<MassCycleCountRemote>> getMassCycleCountLiveData() {
        return massCycleCountLiveData;
    }

    public LiveData<List<MassCycleCountRemote>> getMassCycleCountNormalLiveData() {
        return massCycleCountNormalLiveData;
    }

    public LiveData<List<MassCycleCountRemote>> getMassCycleCountBlindLiveData() {
        return massCycleCountBlindLiveData;
    }

    public LiveData<List<MassCycleCountRemote>> getMassCycleCountRandomLiveData() {
        return massCycleCountRandomLiveData;
    }

    public LiveData<Void> getShowLoadingLiveData() {
        return showLoadingLiveData;
    }

    public LiveData<Void> getHideLoadingLiveData() {
        return hideLoadingLiveData;
    }

    public LiveData<String> getShowErrorMessageLiveData() {
        return showMessageErrorLiveData;
    }

    public LiveData<String> getShowTextFromDateOnButton() {
        return showTextFromDateOnButton;
    }

    public LiveData<String> getShowTextToDateOnButton() {
        return showTextToDateOnButton;
    }

    public LiveData<MassCycleCountRemote> getNavigateToMassID() {
        return navigateToMassID;
    }

    public LiveData<String> getSendScanData() {
        return sendScanData;
    }

    public LiveData<List<PalletRemote>> getPalletRemote() {
        return palletRemote;
    }

}
