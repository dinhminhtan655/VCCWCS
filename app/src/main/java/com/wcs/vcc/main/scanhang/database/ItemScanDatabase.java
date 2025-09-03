package com.wcs.vcc.main.scanhang.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

import com.wcs.vcc.main.scanhang.dao.ItemScanDao;
import com.wcs.vcc.main.scanhang.model.ItemScan;

@Database(entities = {ItemScan.class}, version = 3, exportSchema = false)
public abstract class ItemScanDatabase extends RoomDatabase {

    private static ItemScanDatabase instance;

    public abstract ItemScanDao itemScanDao();

    public static synchronized ItemScanDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ItemScanDatabase.class,
                    "itemscan_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback).build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

}
