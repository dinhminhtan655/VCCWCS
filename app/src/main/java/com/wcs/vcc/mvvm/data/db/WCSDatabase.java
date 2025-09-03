package com.wcs.vcc.mvvm.data.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.wcs.vcc.App;
import com.wcs.vcc.mvvm.data.domain.Locations;
import com.wcs.vcc.mvvm.data.domain.MassCycleCount;

@Database(entities = {MassCycleCount.class, Locations.class}, version = 2, exportSchema = false)
public abstract class WCSDatabase extends RoomDatabase {

    public abstract LocationsDao locationsDao();

    private static WCSDatabase sInstance;

    public static WCSDatabase getInstance() {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(App.getInstance(), WCSDatabase.class, "WCS.db").allowMainThreadQueries().build();
        }
        return sInstance;
    }

}
