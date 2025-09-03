package com.wcs.vcc.main.scanhang.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wcs.vcc.main.scanhang.model.ItemScan;

import java.util.List;

@Dao
public interface ItemScanDao {

    @Insert
    void insert(List<ItemScan> itemScan);

    @Update
    void update(ItemScan itemScan);

    @Query("DELETE FROM item_table")
    void deleteAllItemScan();

    @Query("SELECT * FROM item_table ORDER BY CASE WHEN confirm = 0 THEN 0 ELSE 1 END , confirm DESC, palletID DESC")
    LiveData<List<ItemScan>> getAllItemScanDesc();

    @Query("SELECT * FROM item_table ORDER BY CASE WHEN confirm = 0 THEN 0 ELSE 1 END , confirm DESC, palletID ASC")
    LiveData<List<ItemScan>> getAllItemScanAsc();

    @Query("UPDATE item_table SET confirm = 1 WHERE confirm = 0")
    void updateConfirmBack1();

    @Query("UPDATE item_table SET confirm = 0 WHERE id = (SELECT id FROM item_table WHERE confirm = 1 order by palletID asc LIMIT 1)")
    void updateConfirmBack0PalletIdAsc();

    @Query("UPDATE item_table SET confirm = 0 WHERE id = (SELECT id FROM item_table WHERE confirm = 1 order by palletID desc LIMIT 1)")
    void updateConfirmBack0PalletIdDesc();


}
