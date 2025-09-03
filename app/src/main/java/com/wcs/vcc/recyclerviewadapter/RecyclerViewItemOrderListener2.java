package com.wcs.vcc.recyclerviewadapter;

public interface RecyclerViewItemOrderListener2<T> {
    void onClick(T item, int position, int order, int order2);
    void onLongClick(T item, int position, int  order,int order2);
}
