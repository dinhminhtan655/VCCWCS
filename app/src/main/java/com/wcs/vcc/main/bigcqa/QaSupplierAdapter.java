package com.wcs.vcc.main.bigcqa;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.wcs.wcs.R;import com.wcs.vcc.main.bigc.Supplier;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


class QaSupplierAdapter extends ArrayAdapter<Supplier> implements Filterable {
    private final LayoutInflater inflater;
    private List<Supplier> listOrigin;
    private List<Supplier> listRelease;

    QaSupplierAdapter(Context context, @NonNull ArrayList<Supplier> objects) {
        super(context, 0, objects);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listRelease = objects;
        listOrigin = objects;
    }

    @Nullable
    @Override
    public Supplier getItem(int position) {
        return listRelease.get(position);
    }

    @Override
    public int getCount() {
        return listRelease.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_suppliers_today, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_suppliers_today_name);
            holder.tvOrderWeight = (TextView) convertView.findViewById(R.id.item_suppliers_today_booking_weight);
            holder.tvActualWeight = (TextView) convertView.findViewById(R.id.item_suppliers_today_actual_weight);
            holder.tvBasket = (TextView) convertView.findViewById(R.id.item_suppliers_today_basket);
            holder.tvBasketMovement = (TextView) convertView.findViewById(R.id.item_suppliers_today_basket_movement);

            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        final Supplier item = getItem(position);
        if (item != null) {
            holder.tvName.setText(item.getNameShort());
            holder.tvOrderWeight.setText(String.valueOf(item.getBookingWeight()));
            float actualWeight = item.getActualWeight();
            holder.tvActualWeight.setText(String.valueOf(actualWeight));

            if (item.getBasketMovementNumber().trim().length() > 0) {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bigcLightCyan));
            } else if (actualWeight > 0) {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bigcLightGreen));
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }
            holder.tvBasketMovement.setText(String.valueOf(item.getBasketQuantityReturn()));
            holder.tvBasket.setText(String.valueOf(item.getBasketQuantity()));
        }

        return convertView;
    }


    private static class ViewHolder {
        TextView tvName;
        TextView tvOrderWeight;
        TextView tvActualWeight;
        TextView tvBasket;
        TextView tvBasketMovement;

    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String keyword = constraint.toString().toLowerCase();
                if (keyword.length() > 0) {
                    ArrayList<Supplier> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < listOrigin.size(); i++) {
                        Supplier info = listOrigin.get(i);
                        String name = Normalizer.normalize(info.getNameShort().toLowerCase(), Normalizer.Form.NFD);
                        if (name.contains(keyword))
                            arrayFilter.add(info);
                    }
                    results.count = arrayFilter.size();
                    results.values = arrayFilter;
                } else {
                    results.count = listOrigin.size();
                    results.values = listOrigin;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
                listRelease = (ArrayList<Supplier>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
