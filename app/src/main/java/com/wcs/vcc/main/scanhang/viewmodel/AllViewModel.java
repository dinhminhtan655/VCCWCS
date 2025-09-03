package com.wcs.vcc.main.scanhang.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.wcs.vcc.main.scanhang.model.CustomerScan;
import com.wcs.vcc.main.scanhang.model.ScanPalletCode;

import java.util.List;

public class AllViewModel extends AndroidViewModel {

    Repository repository;

    public AllViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public LiveData<List<CustomerScan>> getAllCustomerScan(Context context) {
        return repository.getCustomerScan(context);
    }

    public LiveData<List<ScanPalletCode>> getScanPalletCode(Context context,String deliveryDate, String region, String customerCode, String groupSorting,String flag){
        return  repository.getScanPalletCode(context,deliveryDate,region,customerCode,groupSorting, flag);
    }


}
