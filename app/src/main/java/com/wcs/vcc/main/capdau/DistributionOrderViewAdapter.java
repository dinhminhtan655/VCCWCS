package com.wcs.vcc.main.capdau;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.wcs.wcs.R;import com.wcs.wcs.databinding.ItemDistributionOrderBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class DistributionOrderViewAdapter extends DataBoundListAdapter<DistributionOrderView, ItemDistributionOrderBinding>
        implements Filterable {
    private int altColor;
    private RecyclerViewItemListener<DistributionOrderView> itemListener;

    public DistributionOrderViewAdapter(RecyclerViewItemListener<DistributionOrderView> itemListener) {
        this.itemListener = itemListener;
    }

    public interface OnItemClickListener {
        void onClick(DistributionOrderView data);
    }

    @Override
    protected ItemDistributionOrderBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_distribution_order, parent, false);
    }

    @Override
    protected void bind(ItemDistributionOrderBinding binding, final DistributionOrderView item, final int position) {
        binding.setItem(item);

        View root = binding.getRoot();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onClick(item, position);
            }
        });
        root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemListener.onLongClick(item, position);
                return true;
            }
        });
        binding.getRoot().setBackgroundColor(position % 2 == 0 ? altColor : Color.WHITE);
    }

    @Override
    protected boolean areItemsTheSame(DistributionOrderView oldItem, DistributionOrderView newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(DistributionOrderView oldItem, DistributionOrderView newItem) {
        return false;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String keyword = constraint.toString().toLowerCase();
                List<DistributionOrderView> originalData = getOriginalData();
                if (originalData != null) {
                    int itemCount = originalData.size();
                    if (keyword.length() > 0) {
                        List<DistributionOrderView> arrayFilter = new ArrayList<>();
                        for (int i = 0; i < itemCount; i++) {
                            DistributionOrderView info = originalData.get(i);
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
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                replace((List<DistributionOrderView>) results.values);
            }
        };
    }
}
