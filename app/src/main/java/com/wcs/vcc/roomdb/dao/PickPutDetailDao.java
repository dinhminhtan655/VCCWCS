package com.wcs.vcc.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wcs.vcc.roomdb.models.PickPutDetailOffline;

import java.util.List;

@Dao
public interface PickPutDetailDao {
    @Query("SELECT * FROM tbl_PickPutDetail")
    List<PickPutDetailOffline> getAll();

    @Query("SELECT * FROM tbl_PickPutDetail where PalletNumber = :palletNumber")
    PickPutDetailOffline getByPalletNumber(int palletNumber);

    @Insert
    void insert(PickPutDetailOffline pickPutDetailOffline);

    @Query("DELETE from tbl_PickPutDetail")
    void deleteAll();

    @Update(onConflict = OnConflictStrategy.IGNORE)
    //@Query("UPDATE tbl_PickPutDetail SET LocationID = :locationId, Label = :label, Reference = :reference, Status = :status, PutAwayScannedBy = :username, PutAwayScannedTime = :currentDateTime where PalletID = :palletId")
    int updateScan(PickPutDetailOffline pickPutDetailOffline);

    @Query("SELECT * FROM tbl_PickPutDetail where PalletID = :palletId")
    PickPutDetailOffline getDataByPalletId(String palletId);

    // hạ hàng
    @Query("SELECT COUNT(PalletID) FROM tbl_PickPutDetail WHERE PalletNumber = :palletNumber and Status < 2")
    int countQtyPalletId(int palletNumber);

    // sync data
    @Query("SELECT * FROM tbl_PickPutDetail WHERE PutAwayScannedBy IS NOT NULL")
    List<PickPutDetailOffline> syncDataPickPutOffline();


}
