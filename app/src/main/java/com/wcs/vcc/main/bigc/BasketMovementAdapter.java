package com.wcs.vcc.main.bigc;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wcs.wcs.R;
import java.util.ArrayList;


class BasketMovementAdapter extends ArrayAdapter<BasketMovement> {
    private final LayoutInflater inflater;

    BasketMovementAdapter(Context context, @NonNull ArrayList<BasketMovement> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_basket_movement, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_basket_movement_name);
            holder.tvCode = (TextView) convertView.findViewById(R.id.item_basket_movement_code);
            holder.tvQuantity = (TextView) convertView.findViewById(R.id.item_basket_movement_quantity);

            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        BasketMovement item = getItem(position);
        if (item != null) {
            holder.tvName.setText(item.getBasketName());
            holder.tvCode.setText(item.getBasketNumber());
            holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvCode;
        TextView tvQuantity;
    }
}
