package com.wcs.vcc.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.wcs.vcc.main.len_ha_hang.model.Aisle;

import java.util.List;

@Dao
public interface AisleDao {
    @Query("SELECT * FROM tbl_Aisle")
    List<Aisle> getAll();

    @Insert
    void insert(Aisle aisleModel);

    @Query("DELETE from tbl_Aisle")
    void deleteAll();
}
