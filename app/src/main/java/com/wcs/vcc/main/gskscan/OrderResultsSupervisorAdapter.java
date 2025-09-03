package com.wcs.vcc.main.gskscan;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wcs.wcs.R;import com.wcs.wcs.databinding.ItemOrderResultSupervisorBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;

/**
 * Created by aang on 20/05/2018.
 */

public class OrderResultsSupervisorAdapter extends DataBoundListAdapter<OrderResultsSupervisorScan, ItemOrderResultSupervisorBinding> {

    private int altColor;

    @Override
    protected ItemOrderResultSupervisorBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order_result_supervisor, parent, false);
    }

    @Override
    protected void bind(ItemOrderResultSupervisorBinding binding, OrderResultsSupervisorScan item, int position) {
        binding.setItem(item);
        binding.getRoot().setBackgroundColor(position % 2 == 0 ? altColor : Color.WHITE);
        if (item.IsRecordNew) {
            binding.getRoot().setBackgroundColor(Color.argb(255, 255, 238, 88));
        }
    }

    @Override
    protected boolean areItemsTheSame(OrderResultsSupervisorScan oldItem, OrderResultsSupervisorScan newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(OrderResultsSupervisorScan oldItem, OrderResultsSupervisorScan newItem) {
        return false;
    }
}
