package com.wcs.vcc.roomdb.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.wcs.vcc.main.len_ha_hang.model.Aisle;
import com.wcs.vcc.main.len_ha_hang.model.Ticket;
import com.wcs.vcc.main.vo.PickPut;
import com.wcs.vcc.roomdb.dao.AisleDao;
import com.wcs.vcc.roomdb.dao.BarcodeScanOrderDetailDao;
import com.wcs.vcc.roomdb.dao.PickPutDao;
import com.wcs.vcc.roomdb.dao.PickPutDetailDao;
import com.wcs.vcc.roomdb.dao.TicketDao;
import com.wcs.vcc.roomdb.models.BarcodeScanOrderDetail;
import com.wcs.vcc.roomdb.models.PickPutDetailOffline;

@Database(entities = {PickPut.class, Aisle.class, Ticket.class, PickPutDetailOffline.class, BarcodeScanOrderDetail.class},
        version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PickPutDao pickPutDao();
    public abstract AisleDao aisleDao();
    public abstract TicketDao ticketDao();
    public abstract PickPutDetailDao pickPutDetailDao();
    public abstract BarcodeScanOrderDetailDao barcodeScanOrderDetailDao();

    private static AppDatabase database;
    private static String DATABASE_NAME = "wcs_db_scan_offline";

    public synchronized static AppDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME
            )
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }
}
