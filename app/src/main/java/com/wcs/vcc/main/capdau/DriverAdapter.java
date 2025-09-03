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

import com.wcs.wcs.R;import com.wcs.wcs.databinding.ItemDriverBinding;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class DriverAdapter extends ArrayAdapter<Driver> implements Filterable {

    private final LayoutInflater layoutInflater;
    private List<Driver> originalData;
    private List<Driver> consumableData;

    DriverAdapter(@NonNull Context context, @NonNull List<Driver> originalData) {
        super(context, 0, originalData);
        this.originalData = originalData;
        consumableData = new ArrayList<>(originalData);
        layoutInflater = LayoutInflater.from(context);
    }

    @Nullable
    @Override
    public Driver getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.item_driver, parent, false);
            ItemDriverBinding binding = DataBindingUtil.bind(convertView);
            holder = new ViewHolder(binding);
            convertView.setTag(holder);
        } else {
            holder = ((ViewHolder) convertView.getTag());
        }
        Driver item = getItem(position);
        if (item != null) {
            holder.binding.setItem(item);
        }
        return convertView;
    }

    private class ViewHolder {
        ItemDriverBinding binding;

        public ViewHolder(ItemDriverBinding binding) {
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
                    List<Driver> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < itemCount; i++) {
                        Driver info = originalData.get(i);
                        String name = Utilities.normalizeString(info.DriverName);
                        String driverCode = Utilities.normalizeString(info.DriverCode);
                        if (name.contains(keyword) || driverCode.contains(keyword))
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
                consumableData = (List<Driver>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
