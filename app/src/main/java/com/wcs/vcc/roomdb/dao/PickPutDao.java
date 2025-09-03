package com.wcs.vcc.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.wcs.vcc.main.vo.PickPut;

import java.util.List;

@Dao
public interface PickPutDao {
    @Query("SELECT * FROM tbl_PickPut")
    List<PickPut> getAll();

    @Query("SELECT * FROM tbl_PickPut where Flag = :flag")
    List<PickPut> getAllByKey(int flag);

    @Query("SELECT * FROM tbl_PickPut where Flag = :flag and Aisle = :aisle")
    List<PickPut> getAllByKeyAndAisle(int flag, int aisle);

    @Query("SELECT * FROM tbl_PickPut where Flag = :flag and OrderNumber = :orderNumber")
    List<PickPut> getAllByKeyAndOrderNumber(int flag, String orderNumber);

    @Insert
    void insert(PickPut pickPutModel);

    @Query("DELETE from tbl_PickPut")
    void deleteAll();
}
