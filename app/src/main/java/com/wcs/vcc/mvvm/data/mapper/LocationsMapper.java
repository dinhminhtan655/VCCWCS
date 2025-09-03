package com.wcs.vcc.mvvm.data.mapper;

import com.wcs.vcc.mvvm.data.domain.Locations;

public class LocationsMapper {

    public static Locations toLocationDomain(com.wcs.vcc.mvvm.data.model.locations.Locations locations) {
        return new Locations(locations.getLocationID(), locations.getLocationNumber());
    }
}
