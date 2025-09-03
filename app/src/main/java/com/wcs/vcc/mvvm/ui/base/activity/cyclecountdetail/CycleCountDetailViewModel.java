package com.wcs.vcc.mvvm.ui.base.activity.cyclecountdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.wcs.vcc.main.emdk.ScanListener;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.MassCycleCountDetailRemote;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.MassCycleCountDetailRepository;
import com.wcs.vcc.mvvm.ui.base.activity.base.BaseViewModel;
import com.wcs.vcc.utilities.Const;

import java.util.ArrayList;
import java.util.List;

public class CycleCountDetailViewModel extends BaseViewModel implements ScanListener {

    private final MutableLiveData<List<MassCycleCountDetailRemote>> listMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MassCycleCountDetailRemote> remoteMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Void> showProgressbar = new MutableLiveData<>();
    private final MutableLiveData<Void> hideProgressbar = new MutableLiveData<>();
    private final MutableLiveData<String> showErrorText = new MutableLiveData<>();
    private final MutableLiveData<String> showScanText = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isExistingLocation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isScanLocationFirst = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isExitstingPallet = new MutableLiveData<>();
    private final MutableLiveData<String> showTextConfirm = new MutableLiveData<>();

    private final List<MassCycleCountDetailRemote> list = new ArrayList<>();

    private final MassCycleCountDetailRepository repository;

    private final MassCycleCountDetailCallback callback = new MassCycleCountDetailCallback();

    private final MassCycleCountDetailConfirmCallback confirmCallback = new MassCycleCountDetailConfirmCallback();

    public CycleCountDetailViewModel(MassCycleCountDetailRepository repository) {
        this.repository = repository;
    }

    public void loadMassCycleCountDetail(JsonObject jsonObject) {
        setIsLoading(true);
        repository.getCycleCountDetail(callback, jsonObject);
    }

    public void confirmMassCycleCountDetail(JsonObject jsonObject) {
        repository.confirmCycleCountDetail(confirmCallback, jsonObject);
    }

    public MassCycleCountDetailRemote sendRemoteObjectTo(String data){
        return sendRemoteObject(data);
    }


    private void setIsLoading(boolean loading) {
        if (loading) {
            showProgressbar.postValue(null);
        } else
            hideProgressbar.postValue(null);
    }

    @Override
    public void onData(String data) {
        ProcessString(data);
    }

    private void ProcessString(String data) {
        Const.timePauseActive = 0;
        data = data.trim();
        if (!data.equals("") && !data.contains("\t\t")) {
            showScanText.postValue(data);
//            if (isScanLocationFirst.getValue() == null || !isScanLocationFirst.getValue()) {
//                if (!data.startsWith("PI")) {
//                    isScanLocationFirst.setValue(true);
//                    if (checkingExistingLocation(data)) {
//                        isExistingLocation.setValue(true);
//                    } else {
//                        isScanLocationFirst.setValue(false);
//                        isExistingLocation.setValue(false);
//                    }
//                    return;
//                } else {
//                    isScanLocationFirst.setValue(false);
//                }
//            } else if (isScanLocationFirst.getValue() && isExistingLocation.getValue()) {
//                if (!data.startsWith("PI")) {
//                    isScanLocationFirst.setValue(true);
//                } else {
//                    if (checkingExistingPallet(data)) {
//                        isScanLocationFirst.setValue(null);
//                        remoteMutableLiveData.setValue(sendRemoteObject(data));
//                    } else {
//                        isExitstingPallet.setValue(false);
//                    }
//                }
//                return;
//            }
            if (data.startsWith("PI") || data.startsWith("pi")) {
                if (checkingExistingPallet(data)) {
                    remoteMutableLiveData.postValue(sendRemoteObject(data));
                } else
                    isExitstingPallet.postValue(false);
            }else {
                if (checkingExistingLocation(data)){
                    remoteMutableLiveData.postValue(sendRemoteObject(data));
                }else {
                    isExistingLocation.postValue(false);
                }
            }
        }
    }


    private MassCycleCountDetailRemote sendRemoteObject(String data) {
        return repository.sendMassCycleCountDetailRemote(listMutableLiveData.getValue(), data);
    }

    private boolean checkingExistingLocation(String data) {
        return repository.checkExistingLocation(listMutableLiveData.getValue(), data);
    }

    private boolean checkingExistingPallet(String data) {
        return repository.checkingExistingPallet(listMutableLiveData.getValue(), data);
    }

    private class MassCycleCountDetailCallback implements MassCycleCountDetailRepository.loadMassCycleCountDetailCallback {

        @Override
        public void onMassCycleCountDetailLoaded(List<MassCycleCountDetailRemote> remoteList) {
            setIsLoading(false);
            listMutableLiveData.postValue(remoteList);
        }

        @Override
        public void onDataNotAvailable() {
            setIsLoading(false);
            showErrorText.postValue("Không có dữ liệu");
        }

        @Override
        public void onError() {
            setIsLoading(false);
            showErrorText.postValue("Lỗi không xác định");
        }
    }

    private class MassCycleCountDetailConfirmCallback implements MassCycleCountDetailRepository.confirmMassCycleCountDetailCallback {

        @Override
        public void onConfirmedCycleCountDetailUpdated(String success) {
            showTextConfirm.postValue(success);
        }

        @Override
        public void onConfirmedCycleCountDetailFailed(String failed) {
            showTextConfirm.postValue(failed);
        }

        @Override
        public void onConfirmError() {
            showTextConfirm.postValue("Lỗi chưa xác định");
        }
    }

    public LiveData<Void> getShowProgressbar() {
        return showProgressbar;
    }

    public LiveData<Void> getHideProgressbar() {
        return hideProgressbar;
    }

    public LiveData<String> getErrorText() {
        return showErrorText;
    }

    public LiveData<List<MassCycleCountDetailRemote>> getListMassCycleCountDetailRemote() {
        return listMutableLiveData;
    }

    public LiveData<String> getScanText() {
        return showScanText;
    }

    public LiveData<Boolean> getIsExitingLocation() {
        return isExistingLocation;
    }

    public LiveData<Boolean> getIsScanLocationFirst() {
        return isScanLocationFirst;
    }

    public LiveData<Boolean> getIsExistingPallet() {
        return isExitstingPallet;
    }

    public LiveData<MassCycleCountDetailRemote> getRemoteMutableLiveData() {
        return remoteMutableLiveData;
    }

    public LiveData<String> getMessTextConfirm() {
        return showTextConfirm;
    }
}
