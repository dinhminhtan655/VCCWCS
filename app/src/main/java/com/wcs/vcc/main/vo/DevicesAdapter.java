package com.wcs.vcc.main.vo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wcs.wcs.R;import com.wcs.vcc.main.scanhang.model.PrinterDevices;

import java.util.List;

public class DevicesAdapter extends ArrayAdapter<PrinterDevices> {


    Context context;
    List<PrinterDevices> list;
    int layoutResource;

    public DevicesAdapter(@NonNull Context context, int resource, @NonNull List<PrinterDevices> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        layoutResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layoutResource, null);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvMac = convertView.findViewById(R.id.tvMac);

        tvName.setText(list.get(position).name);
        tvMac.setText(list.get(position).mac);

        return convertView;
    }
}
