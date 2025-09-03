package com.wcs.vcc.main.CheckOutTrip.Adapter;

import com.wcs.vcc.api.checkouttrip.response.TripCarton;

public interface CustomerClientByTripItemListener {
    void onClick(TripCarton item, int position);
}
