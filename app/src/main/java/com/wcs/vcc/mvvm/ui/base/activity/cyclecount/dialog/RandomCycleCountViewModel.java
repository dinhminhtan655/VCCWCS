package com.wcs.vcc.mvvm.ui.base.activity.cyclecount.dialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.product.Product;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.randomcyclecountfragmentrepository.RandomCycleCountInsertFragmentRepository;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepository;
import com.wcs.vcc.mvvm.ui.base.activity.base.BaseViewModel;

import java.util.List;

public class RandomCycleCountViewModel extends BaseViewModel {
    private final MutableLiveData<Void> showLoading = new MutableLiveData<>();
    private final MutableLiveData<Void> hideLoading = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> listProducMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> showMessGetProductList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> checkingGoodBad = new MutableLiveData<>();
    private final MutableLiveData<String> showMessInsert = new MutableLiveData<>();

    private final RandomCycleCountInsertFragmentRepository repository;


    private final GetProductListMassCycleCountCallback getProductListMassCycleCountCallback = new GetProductListMassCycleCountCallback();

    private final InsertRandomCycleCountCallback insertRandomCycleCountCallback = new InsertRandomCycleCountCallback();

    public RandomCycleCountViewModel(RandomCycleCountInsertFragmentRepository repository) {
        this.repository = repository;
    }

    public void checkingGoodBad(boolean isCheck) {
        checkingGoodBad.postValue(isCheck);
    }

    public void loadProductList(JsonObject jsonObject) {
        repository.loadProductListCycleCountDetail(getProductListMassCycleCountCallback, jsonObject);
    }

    public void insertRandomCycleCount(JsonObject jsonObject) {
        setIsLoading(true);
        repository.insertRandomCycleCount(insertRandomCycleCountCallback, jsonObject);
    }
    private void setIsLoading(boolean isLoading) {
        if (isLoading)
            showLoading.postValue(null);
        else
            hideLoading.postValue(null);
    }

    private class InsertRandomCycleCountCallback implements RandomCycleCountInsertFragmentRepository.insertRandomCycleCountCallback {

        @Override
        public void onRandomCycleCountInserted(String success) {
            setIsLoading(false);
            showMessInsert.postValue(success);
        }

        @Override
        public void onRandomCycleCountInsertedFailed(String failed) {
            setIsLoading(false);
            showMessInsert.postValue(failed);
        }

        @Override
        public void onError() {
            setIsLoading(false);
            showMessInsert.postValue("Lỗi chưa xác định");
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

    public LiveData<String> getMessInsert() {
        return showMessInsert;
    }
}
