package com.wcs.vcc.main.len_ha_hang.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbl_Aisle")
public class Aisle {

    @PrimaryKey(autoGenerate = true)
    public int Id;

    @SerializedName("Aisle")
    @ColumnInfo(name = "Aisle")
    public int Aisle;

    @SerializedName("RoomNumber")
    @ColumnInfo(name = "RoomNumber")
    public String RoomNumber;

    @SerializedName("Locations")
    @ColumnInfo(name = "Locations")
    public String Locations;

    @SerializedName("Cartons")
    @ColumnInfo(name = "Cartons")
    public String Cartons;

    @Override
    public String toString() {
        return Aisle+"/"+RoomNumber+"/"+Cartons;
    }
}
