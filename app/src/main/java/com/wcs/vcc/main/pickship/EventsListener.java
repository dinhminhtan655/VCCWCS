package com.wcs.vcc.main.pickship;

/**
 * Created by aang on 02/06/2018.
 */

public interface EventsListener<T> {
    void onClick(T item, int position);
}
