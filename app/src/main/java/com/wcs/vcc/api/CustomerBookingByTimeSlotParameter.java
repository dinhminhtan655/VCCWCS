package com.wcs.vcc.api;

/**
 * Created by xuanloc on 12/12/2016.
 */
public class CustomerBookingByTimeSlotParameter {
    private String BookingDate;
    private Byte WarehouseID;
    private int storeId;

    public CustomerBookingByTimeSlotParameter(String bookingDate, Byte warehouseID, int storeId) {
        BookingDate = bookingDate;
        WarehouseID = warehouseID;
        this.storeId = storeId;
    }
}
