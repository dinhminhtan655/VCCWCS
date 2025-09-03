package com.wcs.vcc.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.wcs.vcc.roomdb.models.BarcodeScanOrderDetail;

import java.util.List;

@Dao
public interface BarcodeScanOrderDetailDao {

    @Query("SELECT * FROM tbl_BarcodeScanOrderDetail")
    List<BarcodeScanOrderDetail> getAll();

    @Query("DELETE from tbl_BarcodeScanOrderDetail")
    void deleteAll();

    @Insert
    void insert(BarcodeScanOrderDetail barcodeScanOrderDetail);
}
