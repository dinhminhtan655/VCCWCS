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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.wcs.wcs.R;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


public class StoresOfSupplierAdapter extends ArrayAdapter<Store> implements Filterable {
    private final LayoutInflater inflater;
    private List<Store> dataRelease;
    private List<Store> dataOrigin;

    public StoresOfSupplierAdapter(Context context, @NonNull ArrayList<Store> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataOrigin = objects;
        dataRelease = objects;
    }

    @Nullable
    @Override
    public Store getItem(int position) {
        return dataRelease.get(position);
    }

    @Override
    public int getCount() {
        return dataRelease.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_stores_supplier, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_stores_supplier_name);
            holder.tvOrderWeight = (TextView) convertView.findViewById(R.id.item_stores_supplier_booking_weight);
            holder.tvActualWeight = (TextView) convertView.findViewById(R.id.item_stores_supplier_actual_weight);
            holder.tvBasketQuantity = (TextView) convertView.findViewById(R.id.item_stores_supplier_basket_quantity);
            holder.tvActualCarton = (TextView) convertView.findViewById(R.id.item_stores_supplier_actual_carton);

            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        Store item = getItem(position);
        if (item != null) {
            holder.tvName.setText(item.getName());
            holder.tvOrderWeight.setText(String.valueOf(item.getBookingWeight()));
            holder.tvActualCarton.setText(String.valueOf(item.getActualCarton()));
            holder.tvBasketQuantity.setText(String.valueOf(item.getBasketQuantity()));
            float actualWeight = item.getActualWeight();
            holder.tvActualWeight.setText(String.valueOf(actualWeight));
            if (actualWeight > 0) {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bigcLightGreen));
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvOrderWeight;
        TextView tvActualWeight;
        TextView tvBasketQuantity;
        TextView tvActualCarton;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String keyword = constraint.toString().toLowerCase();
                if (keyword.length() > 0) {
                    ArrayList<Store> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        Store info = dataOrigin.get(i);
                        String name = Normalizer.normalize(info.getName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        if (name.contains(keyword))
                            arrayFilter.add(info);
                    }
                    results.count = arrayFilter.size();
                    results.values = arrayFilter;
                } else {
                    results.count = dataOrigin.size();
                    results.values = dataOrigin;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataRelease = (ArrayList<Store>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
