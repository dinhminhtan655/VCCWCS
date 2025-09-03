package com.wcs.vcc.main.tonkho.detailkhachhang;

import android.content.Context;
import android.graphics.Color;

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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class SOHByCustomerAdapter extends ArrayAdapter<StockOnHandByCustomerInfo> implements Filterable {
    private LayoutInflater inflater;
    List<StockOnHandByCustomerInfo> dataOrigin;
    List<StockOnHandByCustomerInfo> dataRelease;


    public SOHByCustomerAdapter(Context context, List<StockOnHandByCustomerInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataOrigin = objects;
        dataRelease = objects;
    }

    @Nullable
    @Override
    public StockOnHandByCustomerInfo getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_stock_on_hand_by_customer, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        StockOnHandByCustomerInfo info = getItem(position);
        holder.tvProductName.setText(info.getProductName());
        holder.tvProductNumber.setText(info.getProductNumber());
        holder.tvTotalAfter.setText(NumberFormat.getInstance().format(info.getTotalAfterDPCtns()));
        holder.tvTotalCurrent.setText(NumberFormat.getInstance().format(info.getTotalCurrentCtns()));
        holder.tvTotalLocation.setText(NumberFormat.getInstance().format(info.getTotalLocation()));
        holder.tvTotalPallet.setText(NumberFormat.getInstance().format(info.getTotalPallet()));
        holder.tvTotalWeight.setText(NumberFormat.getInstance().format(info.getTotalWeight()));
        holder.tvTotalUnit.setText(NumberFormat.getInstance().format(info.getTotalUnit()));
        holder.tvTotalExported.setText(NumberFormat.getInstance().format(info.getTotalExported()));
        holder.tvTotalPicked.setText(NumberFormat.getInstance().format(info.getTotalPicked()));
        holder.tvTotalHold.setText(NumberFormat.getInstance().format(info.getTotalHold()));
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAlternativeRow));
        else convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String keyword = constraint.toString().toLowerCase();
                if (keyword.length() > 0) {
                    ArrayList<StockOnHandByCustomerInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        StockOnHandByCustomerInfo info = dataOrigin.get(i);
                        String name = Normalizer.normalize(info.getProductName().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        String nameNoSpace = Normalizer.normalize(info.getProductNumber().toLowerCase(), Normalizer.Form.NFD).replaceAll(" ", "").replaceAll("[^\\p{ASCII}]", "");
                        if (name.contains(keyword) || nameNoSpace.contains(keyword))
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
                dataRelease = (ArrayList<StockOnHandByCustomerInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        @BindView(R.id.item_tv_stock_on_hand_ProductName)
        TextView tvProductName;
        @BindView(R.id.item_tv_stock_on_hand_ProductNumber)
        TextView tvProductNumber;
        @BindView(R.id.item_tv_stock_on_hand_TotalAfter)
        TextView tvTotalAfter;
        @BindView(R.id.item_tv_stock_on_hand_TotalCurrent)
        TextView tvTotalCurrent;
        @BindView(R.id.item_tv_stock_on_hand_TotalLocation)
        TextView tvTotalLocation;
        @BindView(R.id.item_tv_stock_on_hand_TotalPallet)
        TextView tvTotalPallet;
        @BindView(R.id.item_tv_stock_on_hand_TotalWeight)
        TextView tvTotalWeight;
        @BindView(R.id.item_tv_stock_on_hand_TotalUnit)
        TextView tvTotalUnit;
        @BindView(R.id.item_tv_stock_on_hand_TotalPicked)
        TextView tvTotalPicked;
        @BindView(R.id.item_tv_stock_on_hand_TotalHold)
        TextView tvTotalHold;
        @BindView(R.id.item_tv_stock_on_hand_TotalExported)
        TextView tvTotalExported;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}