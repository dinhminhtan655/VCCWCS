package com.wcs.vcc.main.scanhang.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.wcs.vcc.main.scanhang.model.ItemScan;

import java.util.List;

public class ItemScanViewModel extends AndroidViewModel {

    private ItemScanRepository repository;

    public ItemScanViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemScanRepository(application);
    }

    public void insert(List<ItemScan> itemScanList) {
        repository.insert(itemScanList);
    }

    public void update(ItemScan itemScan) {
        repository.update(itemScan);
    }

    public void updateConfirmBack1() {
        repository.updateConfirmBack1();
    }

    public void updateConfirmBack0PalletIdAsc() {
        repository.updateConfirmBack0PalletIdAsc();
    }

    public void updateConfirmBack0PalletIdDesc() {
        repository.updateConfirmBack0PalletIdDesc();
    }

    public void deleteAllItemScan() {
        repository.deleteAllItemScan();
    }

//    public LiveData<List<ItemScan>> getAllItemScanAsc(){
//        return repository.getAllItemScanAsc();
//    }

    public LiveData<List<ItemScan>> getAllItemScanDesc(int i) {
        LiveData<List<ItemScan>>  listLiveData = null;
        if (i == 0) {
            updateConfirmBack1();
            updateConfirmBack0PalletIdDesc();
            listLiveData = repository.getAllItemScanDesc();
        } else if (i == 1) {
            updateConfirmBack1();
            updateConfirmBack0PalletIdAsc();
            listLiveData = repository.getAllItemScanAsc();
        }
        return listLiveData;

//        if (i == 0) {
//            updateConfirmBack1();
//            updateConfirmBack0PalletIdDesc();
//            return repository.getAllItemScanDesc();
//        } else {
//            updateConfirmBack1();
//            updateConfirmBack0PalletIdAsc();
//            return repository.getAllItemScanAsc();
//        }
    }


}
