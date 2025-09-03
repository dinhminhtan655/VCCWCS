package com.wcs.vcc.main.tripdelivery.statuslist;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wcs.wcs.R;

import java.util.List;

/**
 * Created by aang on 27/07/2018.
 */

public class TripDeliveryStatusAdapter extends ArrayAdapter<TripDeliveryStatusList> {
    public TripDeliveryStatusAdapter(@NonNull Context context, @NonNull List<TripDeliveryStatusList> objects) {
        super(context, R.layout.item_trip_delivery_status, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_delivery_status, parent, false);
            holder = new ViewHolder();
            holder.tvStatus = (TextView) convertView.findViewById(R.id.item_tv_trip_delivery_status);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.item_tv_trip_delivery_status_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TripDeliveryStatusList item = getItem(position);
        if (item != null) {
            holder.tvStatus.setText(String.valueOf(item.TripStatus));
            holder.tvDescription.setText(item.TripStatusDescriptions);
        }

        return convertView;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_delivery_status, parent, false);
            holder = new ViewHolder();
            holder.tvStatus = (TextView) convertView.findViewById(R.id.item_tv_trip_delivery_status);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.item_tv_trip_delivery_status_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TripDeliveryStatusList item = getItem(position);
        if (item != null) {
            holder.tvStatus.setText(String.valueOf(item.TripStatus));
            holder.tvDescription.setText(item.TripStatusDescriptions);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvStatus;
        TextView tvDescription;
    }
}
