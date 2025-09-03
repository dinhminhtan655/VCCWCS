package com.wcs.vcc.recyclerviewadapter;

public interface RecyclerViewItemOrderListener<T> {


    void onClick(T item, int position, int order);
    void onLongClick(T item, int position, int  order);
}
