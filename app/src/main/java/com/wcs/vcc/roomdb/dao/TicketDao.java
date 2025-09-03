package com.wcs.vcc.roomdb.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.wcs.vcc.main.len_ha_hang.model.Ticket;

import java.util.List;

@Dao
public interface TicketDao {
    @Query("SELECT * FROM tbl_Ticket")
    List<Ticket> getAll();

    @Insert
    void insert(Ticket ticketModel);

    @Query("DELETE from tbl_Ticket")
    void deleteAll();
}
