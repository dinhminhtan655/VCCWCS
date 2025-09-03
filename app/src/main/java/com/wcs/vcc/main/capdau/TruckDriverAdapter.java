package com.wcs.vcc.main.capdau;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.wcs.wcs.R;import com.wcs.wcs.databinding.ItemTruckDriverBinding;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class TruckDriverAdapter extends ArrayAdapter<TruckDriver> implements Filterable {

    private final LayoutInflater layoutInflater;
    private List<TruckDriver> originalData;
    private List<TruckDriver> consumableData;

    TruckDriverAdapter(@NonNull Context context, @NonNull List<TruckDriver> originalData) {
        super(context, 0, originalData);
        this.originalData = originalData;
        consumableData = new ArrayList<>(originalData);
        layoutInflater = LayoutInflater.from(context);
    }

    @Nullable
    @Override
    public TruckDriver getItem(int position) {
        return consumableData.get(position);
    }

    @Override
    public int getCount() {
        return consumableData.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_truck_driver, parent, false);
            ItemTruckDriverBinding binding = DataBindingUtil.bind(convertView);
            holder = new ViewHolder(binding);
            convertView.setTag(holder);
        } else {
            holder = ((ViewHolder) convertView.getTag());
        }
        TruckDriver item = getItem(position);
        if (item != null) {
            holder.binding.setItem(item);
        }
        return convertView;
    }

    private class ViewHolder {
        ItemTruckDriverBinding binding;

        public ViewHolder(ItemTruckDriverBinding binding) {
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String keyword = constraint.toString().toLowerCase();
                int itemCount = originalData.size();
                if (keyword.length() > 0) {
                    List<TruckDriver> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < itemCount; i++) {
                        TruckDriver info = originalData.get(i);
                        String name = Utilities.normalizeString(info.DriverName);
                        String truckNumber = Utilities.normalizeString(info.TruckNumber);
                        if (name.contains(keyword) || truckNumber.contains(keyword))
                            arrayFilter.add(info);
                    }
                    results.count = arrayFilter.size();
                    results.values = arrayFilter;
                } else {
                    results.count = itemCount;
                    results.values = originalData;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                consumableData = (List<TruckDriver>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
