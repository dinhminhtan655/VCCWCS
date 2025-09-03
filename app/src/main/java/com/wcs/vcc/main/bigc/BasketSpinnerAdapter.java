package com.wcs.vcc.main.bigc;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcs.wcs.R;
import java.util.List;


public class BasketSpinnerAdapter extends ArrayAdapter<Basket> {

    private final LayoutInflater inflater;
    private View.OnClickListener basketOnClickListener;

    public BasketSpinnerAdapter(@NonNull Context context, @NonNull List<Basket> objects, View.OnClickListener basketOnClickListener) {
        super(context, 0, objects);
        inflater = LayoutInflater.from(getContext());
        this.basketOnClickListener = basketOnClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_selected_simple_line, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Basket item = getItem(position);
        if (item != null) {
            holder.tvTitle.setText(item.getNumber());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DropDownViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_2_line, parent, false);
            holder = new DropDownViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.item_tv_description);
            holder.ll = (LinearLayout) convertView.findViewById(R.id.item_ll_container);
            convertView.setTag(holder);
        } else {
            holder = (DropDownViewHolder) convertView.getTag();
        }

        Basket item = getItem(position);
        if (item != null) {
            holder.tvTitle.setText(item.getNumber());
            holder.ll.setTag(item);
            holder.tvDescription.setText(item.getName());
        }
        if (position % 2 != 0) {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }
        holder.ll.setOnClickListener(basketOnClickListener);
        return convertView;
    }

    private class DropDownViewHolder {
        TextView tvTitle;
        TextView tvDescription;
        LinearLayout ll;
    }

    private class ViewHolder {
        TextView tvTitle;
    }

}
