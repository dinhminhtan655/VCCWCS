package com.wcs.vcc.main.containerandtruckinfor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wcs.vcc.main.containerandtruckinfor.model.InfoSignedup;
import com.wcs.wcs.R;

import java.util.ArrayList;
import java.util.List;

public class ContAutoCompleteAdapter extends ArrayAdapter<InfoSignedup> {

    private List<InfoSignedup> list;

    public ContAutoCompleteAdapter(@NonNull Context context, @NonNull List<InfoSignedup> list) {
        super(context, 0, list);
        this.list = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cont_car_number, parent, false);
        }

        TextView itemTvCarNum = convertView.findViewById(R.id.itemTvCarNum);
        TextView itemTvCarName = convertView.findViewById(R.id.itemTvCarName);

        InfoSignedup InfoSignedup = getItem(position);

        if (InfoSignedup != null) {
            itemTvCarNum.setText(InfoSignedup.getCarNumber());
            itemTvCarName.setText(InfoSignedup.getDriverName());
        }
        return convertView;
    }

    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<InfoSignedup> suggestion = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestion.addAll(list);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (InfoSignedup item : list) {
                    if (item.getCarNumber().toLowerCase().contains(filterPattern)) {
                        suggestion.add(item);
                    }
                }
            }
            results.values = suggestion;
            results.count = suggestion.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((InfoSignedup) resultValue).getCarNumber();

        }
    };

}
