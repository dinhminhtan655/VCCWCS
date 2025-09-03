package com.wcs.vcc.mvvm.data.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "locations")
public class Locations implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @SerializedName("LocationID")
    @ColumnInfo(name = "LocationID")
    private String locationID;

    @Expose
    @SerializedName("LocationNumber")
    @ColumnInfo(name = "LocationNumber")
    private String locationNumber;

    public Locations( String locationID, String locationNumber) {

        this.locationID = locationID;
        this.locationNumber = locationNumber;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    protected Locations(Parcel in) {
        locationID = in.readString();
        locationNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationID);
        dest.writeString(locationNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Locations> CREATOR = new Creator<Locations>() {
        @Override
        public Locations createFromParcel(Parcel in) {
            return new Locations(in);
        }

        @Override
        public Locations[] newArray(int size) {
            return new Locations[size];
        }
    };

    @Override
    public String toString() {
        return  locationNumber;

    }
}
