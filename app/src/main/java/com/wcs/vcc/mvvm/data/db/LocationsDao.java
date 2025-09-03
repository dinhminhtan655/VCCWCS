package com.wcs.vcc.mvvm.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wcs.vcc.mvvm.data.domain.Locations;

import java.util.List;

@Dao
public interface LocationsDao {

    @Query("SELECT * FROM locations")
    List<Locations> getLocations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveLocations(List<Locations> locations);

    @Query("DELETE FROM locations")
    void deleteLocations();

}
