package com.wcs.vcc.main.detailphieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


public interface Item {

    View getItem(Context context, LayoutInflater inflater, View convertView, IOrderDetailListener listener);
}
