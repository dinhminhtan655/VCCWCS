package com.wcs.vcc.mvvm.ui.base.activity.cyclecountdetail.dialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.domain.Locations;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.product.Product;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepository;
import com.wcs.vcc.mvvm.ui.base.activity.base.BaseViewModel;

import java.util.Collections;
import java.util.List;

public class CycleCountDetailUpdateViewModel extends BaseViewModel {

    private final MutableLiveData<String> showText = new MutableLiveData<>();

    private final MutableLiveData<Void> showLoading = new MutableLiveData<>();
    private final MutableLiveData<Void> hideLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> checkingGoodBad = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> listProducMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> showMessGetProductList = new MutableLiveData<>();
    private final MutableLiveData<List<Locations>> listLocation = new MutableLiveData<>();
    private final MutableLiveData<String> showMessLocation = new MutableLiveData<>();


    private final MassCycleCountDetailUpdateFramentRepository repository;

    private final MassCycleCountDetailUpdateFragmentCallback callback = new MassCycleCountDetailUpdateFragmentCallback();

    private final GetProductListMassCycleCountCallback getProductListMassCycleCountCallback = new GetProductListMassCycleCountCallback();

    private final GetLocationsCallback getLocationsCallback = new GetLocationsCallback();


    public CycleCountDetailUpdateViewModel(MassCycleCountDetailUpdateFramentRepository repository) {
        this.repository = repository;
    }

    public void checkingGoodBad(boolean isCheck) {
        checkingGoodBad.postValue(isCheck);
    }

    public void updateMassCycleCountDetail(JsonObject jsonObject) {
        setIsLoading(true);
        repository.updateCycleCountDetail(callback, jsonObject);
    }

    public void loadLocations() {
        this.listLocation.postValue(Collections.<Locations>emptyList());
        repository.loadLocations(getLocationsCallback);
    }

    public void loadProductList(JsonObject jsonObject) {
        repository.loadProductListCycleCountDetail(getProductListMassCycleCountCallback, jsonObject);
    }

    private void setIsLoading(boolean isLoading) {
        if (isLoading)
            showLoading.postValue(null);
        else
            hideLoading.postValue(null);
    }


    private class MassCycleCountDetailUpdateFragmentCallback implements MassCycleCountDetailUpdateFramentRepository.updateMassCycleCountDetailCallback {

        @Override
        public void onMassCycleCountDetailUpdated(String success) {
            setIsLoading(false);
            showText.postValue(success);
        }

        @Override
        public void onMassCycleCountDetailUpdatedFailed(String failed) {
            setIsLoading(false);
            showText.postValue(failed);
        }

        @Override
        public void onUpdatedError() {
            setIsLoading(false);
            showText.postValue("Lỗi không xác định");
        }
    }

    private class GetLocationsCallback implements MassCycleCountDetailUpdateFramentRepository.loadLocationsCallback {

        @Override
        public void onLoaded(List<Locations> locations) {
            listLocation.postValue(locations);
        }

        @Override
        public void onLoadFailed(String failed) {
            showMessGetProductList.postValue(failed);
        }

        @Override
        public void onError() {
            showMessGetProductList.postValue("Lỗi chưa xác định");
        }
    }


    private class GetProductListMassCycleCountCallback implements MassCycleCountDetailUpdateFramentRepository.loadMassCycleCountDetailProductListCallback {

        @Override
        public void onLoaded(List<Product> list) {
            listProducMutableLiveData.postValue(list);
        }

        @Override
        public void onLoadFailed(String failed) {
            showMessGetProductList.postValue(failed);
        }

        @Override
        public void onError() {
            showMessGetProductList.postValue("Lỗi chưa xác định");
        }
    }


    public LiveData<String> getMessText() {
        return showText;
    }


    public LiveData<Void> getHideLoading() {
        return hideLoading;
    }

    public LiveData<Void> getShowLoading() {
        return showLoading;
    }

    public LiveData<Boolean> getCheckingGoodBad() {
        return checkingGoodBad;
    }

    public LiveData<List<Product>> getProductList() {
        return listProducMutableLiveData;
    }

    public LiveData<String> getMessGetProductList() {
        return showMessGetProductList;
    }

    public LiveData<List<Locations>> getLocationsList() {
        return listLocation;
    }

    public LiveData<String> getMessLocation() {
        return showMessLocation;
    }
}
