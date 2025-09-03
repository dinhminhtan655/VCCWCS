package com.wcs.vcc.main.tonkho.detailproduct;

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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 1/8/2016.
 */
public class StockOnHandDetailAdapter extends ArrayAdapter<StockOnHandDetailsInfo> implements Filterable {
    private LayoutInflater inflater;
    List<StockOnHandDetailsInfo> dataOrigin;
    List<StockOnHandDetailsInfo> dataRelease;

    public StockOnHandDetailAdapter(Context context, List<StockOnHandDetailsInfo> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataOrigin = objects;
        dataRelease = objects;
    }

    @Nullable
    @Override
    public StockOnHandDetailsInfo getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_stock_on_hand_detail, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        StockOnHandDetailsInfo info = getItem(position);
        holder.tvCustomerRef.setText(info.getCustomerRef());
        holder.tvLocationNumber.setText(info.getLocationNumber());
        holder.tvProductionDate.setText(info.getProductionDate());
        holder.tvReceivingOrderDate.setText(info.getReceivingOrderDate());
        holder.tvReceivingOrderID.setText(String.format(Locale.CANADA, "%d", info.ReceivingOrderLocalID));
        holder.tvRemark.setText(info.getRemark());
        holder.tvStatus.setText(info.getPalletStatus());
        holder.tvUseByDater.setText(info.getUseByDate());
        holder.tvTotalAfter.setText(NumberFormat.getInstance().format(info.getTotalAfterDPCtns()));
        holder.tvTotalCurrent.setText(NumberFormat.getInstance().format(info.getTotalCurrentCtns()));
        holder.tvTotalWeight.setText(NumberFormat.getInstance().format(info.getTotalWeight()));
        holder.tvTotalUnit.setText(NumberFormat.getInstance().format(info.getTotalUnits()));
        holder.tvTotalHold.setText(NumberFormat.getInstance().format(info.getTotalHold()));
        holder.tvTotalPicked.setText(NumberFormat.getInstance().format(info.getTotalPicked()));
        holder.tvTotalExported.setText(NumberFormat.getInstance().format(info.getTotalExport()));

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
                    ArrayList<StockOnHandDetailsInfo> arrayFilter = new ArrayList<>();
                    for (int i = 0; i < dataOrigin.size(); i++) {
                        StockOnHandDetailsInfo info = dataOrigin.get(i);
                        String name = Normalizer.normalize(info.getLocationNumber().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
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
                dataRelease = (ArrayList<StockOnHandDetailsInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class ViewHolder {
        @BindView(R.id.item_tv_stock_on_hand_detail_Status)
        TextView tvStatus;
        @BindView(R.id.item_tv_stock_on_hand_detail_TotalUnit)
        TextView tvTotalUnit;
        @BindView(R.id.item_tv_stock_on_hand_detail_CustomerRef)
        TextView tvCustomerRef;
        @BindView(R.id.item_tv_stock_on_hand_detail_LocationNumber)
        TextView tvLocationNumber;
        @BindView(R.id.item_tv_stock_on_hand_detail_ProductionDate)
        TextView tvProductionDate;
        @BindView(R.id.item_tv_stock_on_hand_detail_ReceivingOrderDate)
        TextView tvReceivingOrderDate;
        @BindView(R.id.item_tv_stock_on_hand_detail_ReceivingOrderID)
        TextView tvReceivingOrderID;
        @BindView(R.id.item_tv_stock_on_hand_detail_Remark)
        TextView tvRemark;
        @BindView(R.id.item_tv_stock_on_hand_detail_TotalAfter)
        TextView tvTotalAfter;
        @BindView(R.id.item_tv_stock_on_hand_detail_TotalCurrent)
        TextView tvTotalCurrent;
        @BindView(R.id.item_tv_stock_on_hand_detail_TotalWeight)
        TextView tvTotalWeight;
        @BindView(R.id.item_tv_stock_on_hand_detail_UseByDater)
        TextView tvUseByDater;
        @BindView(R.id.tv_stock_on_hand_detail_TotalPicked)
        TextView tvTotalPicked;
        @BindView(R.id.tv_stock_on_hand_detail_TotalHold)
        TextView tvTotalHold;
        @BindView(R.id.tv_stock_on_hand_detail_TotalExported)
        TextView tvTotalExported;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }



}
