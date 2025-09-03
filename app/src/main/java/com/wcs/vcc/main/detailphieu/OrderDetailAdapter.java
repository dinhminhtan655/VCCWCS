package com.wcs.vcc.main.detailphieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class OrderDetailAdapter extends ArrayAdapter<Item> {
    private IOrderDetailListener listener;

    public OrderDetailAdapter(Context context, List<Item> objects, IOrderDetailListener listener) {
        super(context, 0, objects);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Item info = getItem(position);
        return info.getItem(getContext(), inflater, convertView, listener);
    }

}
