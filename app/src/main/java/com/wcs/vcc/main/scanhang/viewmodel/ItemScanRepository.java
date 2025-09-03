package com.wcs.vcc.main.scanhang.viewmodel;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import com.wcs.vcc.main.scanhang.dao.ItemScanDao;
import com.wcs.vcc.main.scanhang.database.ItemScanDatabase;
import com.wcs.vcc.main.scanhang.model.ItemScan;

import java.util.List;

public class ItemScanRepository {

    private ItemScanDao itemScanDao;
    private LiveData<List<ItemScan>> allItemScan;

    public ItemScanRepository(Application application) {
        ItemScanDatabase database = ItemScanDatabase.getInstance(application);
        itemScanDao = database.itemScanDao();
    }

    public LiveData<List<ItemScan>> getAllItemScanAsc() {
        return itemScanDao.getAllItemScanAsc();
    }

    public LiveData<List<ItemScan>> getAllItemScanDesc() {
        return itemScanDao.getAllItemScanDesc();
    }

    public void insert(List<ItemScan> itemScanList) {
        new InsertItemScanAsyncTask(itemScanDao).execute(itemScanList);
    }

    public void update(ItemScan itemScan) {
        new updateAsyncTask(itemScanDao).execute(itemScan);
    }

    public void updateConfirmBack1() {
        new updateConfirmBack1AsyncTask(itemScanDao).execute();
    }

    public void updateConfirmBack0PalletIdAsc() {
        new updateConfirmBack0PalletIdAscAsyncTask(itemScanDao).execute();
    }

    public void updateConfirmBack0PalletIdDesc() {
        new updateConfirmBack0PalletIdDescAsyncTask(itemScanDao).execute();
    }

    public void deleteAllItemScan() {
        new deleteAllItemScanAsyncTask(itemScanDao).execute();
    }


    private static class InsertItemScanAsyncTask extends AsyncTask<List<ItemScan>, Void, Void> {

        private ItemScanDao itemScanDao;

        public InsertItemScanAsyncTask(ItemScanDao itemScanDao) {
            this.itemScanDao = itemScanDao;
        }

        @Override
        protected Void doInBackground(List<ItemScan>... lists) {
            itemScanDao.insert(lists[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<ItemScan, Void, Void> {

        private ItemScanDao itemScanDao;

        public updateAsyncTask(ItemScanDao itemScanDao) {
            this.itemScanDao = itemScanDao;
        }

        @Override
        protected Void doInBackground(ItemScan... itemScans) {
            itemScanDao.update(itemScans[0]);
            return null;
        }
    }

    private static class updateConfirmBack1AsyncTask extends AsyncTask<Void, Void, Void> {

        private ItemScanDao itemScanDao;

        public updateConfirmBack1AsyncTask(ItemScanDao itemScanDao) {
            this.itemScanDao = itemScanDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemScanDao.updateConfirmBack1();
            return null;
        }
    }



    private static class updateConfirmBack0PalletIdAscAsyncTask extends AsyncTask<Void, Void, Void> {

        private ItemScanDao itemScanDao;

        public updateConfirmBack0PalletIdAscAsyncTask(ItemScanDao itemScanDao) {
            this.itemScanDao = itemScanDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemScanDao.updateConfirmBack0PalletIdAsc();
            return null;
        }
    }

    private static class updateConfirmBack0PalletIdDescAsyncTask extends AsyncTask<Void, Void, Void> {

        private ItemScanDao itemScanDao;

        public updateConfirmBack0PalletIdDescAsyncTask(ItemScanDao itemScanDao) {
            this.itemScanDao = itemScanDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemScanDao.updateConfirmBack0PalletIdDesc();
            return null;
        }
    }

    private static class deleteAllItemScanAsyncTask extends AsyncTask<Void, Void, Void> {

        private ItemScanDao itemScanDao;

        public deleteAllItemScanAsyncTask(ItemScanDao itemScanDao) {
            this.itemScanDao = itemScanDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemScanDao.deleteAllItemScan();
            return null;
        }
    }


}
