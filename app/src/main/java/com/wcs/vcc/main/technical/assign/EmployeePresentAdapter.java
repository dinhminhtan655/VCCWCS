package com.wcs.vcc.main.technical.assign;

import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.phieuhomnay.giaoviec.EmployeeInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tranxuanloc on 5/13/2016.
 */
public class EmployeePresentAdapter extends ArrayAdapter<EmployeeInfo> implements Filterable {
    private Context context;
    private ArrayList<EmployeeInfo> dataRelease;
    private ArrayList<EmployeeInfo> dataOrigin;

    public EmployeePresentAdapter(Context context, ArrayList<EmployeeInfo> objects) {
        super(context, 0, objects);
        this.context = context;
        dataOrigin = objects;
        dataRelease = objects;
    }

    @Override
    public EmployeeInfo getItem(int position) {
        return dataRelease.get(position);
    }

    @Override
    public int getCount() {
        return dataRelease.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_employee_present, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        EmployeeInfo info = getItem(position);
        if (info != null) {
            holder.tvID.setText(String.valueOf(info.EmployeeCode));
            holder.tvName.setText(info.getEmployeeName());
            holder.tvTimeIn.setText(info.getTimeIn());
            if (position % 2 == 0)
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
            else
                convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<EmployeeInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        EmployeeInfo info = dataOrigin.get(i);
                        String id = String.valueOf(info.EmployeeCode);
                        if (id.contains(constraint) || info.getEmployeeName().contains(constraint))
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
                dataRelease = (ArrayList<EmployeeInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        @BindView(R.id.item_tv_employee_id)
        TextView tvID;
        @BindView(R.id.item_tv_employee_name)
        TextView tvName;
        @BindView(R.id.item_tv_employee_time_in)
        TextView tvTimeIn;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
