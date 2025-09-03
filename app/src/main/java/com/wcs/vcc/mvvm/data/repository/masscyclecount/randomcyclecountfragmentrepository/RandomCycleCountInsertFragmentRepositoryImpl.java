package com.wcs.vcc.mvvm.data.repository.masscyclecount.randomcyclecountfragmentrepository;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.product.Product;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountDataSource;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountRemoteDataSource;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepository;

import java.util.List;

public class RandomCycleCountInsertFragmentRepositoryImpl implements RandomCycleCountInsertFragmentRepository {


    private final MassCycleCountDataSource.RandomCycleCountInsert randomCycleCountInsert;


    private static RandomCycleCountInsertFragmentRepositoryImpl instance;

    public RandomCycleCountInsertFragmentRepositoryImpl(MassCycleCountDataSource.RandomCycleCountInsert randomCycleCountInsert) {
        this.randomCycleCountInsert = randomCycleCountInsert;
    }

    public static RandomCycleCountInsertFragmentRepositoryImpl getInstance(MassCycleCountRemoteDataSource dataSource) {
        if (instance == null)
            instance = new RandomCycleCountInsertFragmentRepositoryImpl(dataSource);
        return instance;
    }

    @Override
    public void insertRandomCycleCount(insertRandomCycleCountCallback callback, JsonObject jsonObject) {
        if (callback == null) return;
        insertRandomCycleCountFromDataSource(callback, jsonObject);
    }

    @Override
    public void loadProductListCycleCountDetail(MassCycleCountDetailUpdateFramentRepository.loadMassCycleCountDetailProductListCallback callback, JsonObject jsonObject) {
        if (callback == null) return;
        randomCycleCountInsert.getMassCycleCountDetailProductList(new MassCycleCountDetailUpdateFramentRepository.loadMassCycleCountDetailProductListCallback() {
            @Override
            public void onLoaded(List<Product> list) {
                callback.onLoaded(list);
            }

            @Override
            public void onLoadFailed(String failed) {
                callback.onLoadFailed(failed);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        }, jsonObject);
    }

    private void insertRandomCycleCountFromDataSource(final RandomCycleCountInsertFragmentRepository.insertRandomCycleCountCallback callback, JsonObject jsonObject) {
        randomCycleCountInsert.insertRandomCycleCount(new insertRandomCycleCountCallback() {
            @Override
            public void onRandomCycleCountInserted(String success) {
                callback.onRandomCycleCountInserted(success);
            }

            @Override
            public void onRandomCycleCountInsertedFailed(String failed) {
                callback.onRandomCycleCountInsertedFailed(failed);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        }, jsonObject);


    }

}
