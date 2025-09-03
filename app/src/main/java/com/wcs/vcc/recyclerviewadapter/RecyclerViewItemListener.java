package com.wcs.vcc.recyclerviewadapter;

public interface RecyclerViewItemListener<T> {
    void onClick(T item, int position);
    void onLongClick(T item, int position);
}
