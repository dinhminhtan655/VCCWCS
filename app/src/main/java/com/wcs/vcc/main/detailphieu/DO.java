package com.wcs.vcc.main.detailphieu;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class DO implements Item {
    private String title;

    public DO(String title) {
        this.title = title;
    }


    @Override
    public View getItem(Context context, LayoutInflater inflater, View convertView, IOrderDetailListener listener) {
        TextView headerLevel1 = new TextView(context);
        headerLevel1.setGravity(Gravity.CENTER_HORIZONTAL);
        headerLevel1.setTextColor(Color.WHITE);
        headerLevel1.setBackgroundColor(Color.argb(255, 127, 0, 127));
        headerLevel1.setText(getTitle());
        return headerLevel1;
    }

    public String getTitle() {
        return title;
    }
}
