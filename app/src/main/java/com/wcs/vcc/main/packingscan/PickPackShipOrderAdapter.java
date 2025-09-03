package com.wcs.vcc.main.packingscan;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemPickPackShipOrderBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

public class PickPackShipOrderAdapter extends DataBoundListAdapter<PickPackShipOrder, ItemPickPackShipOrderBinding> {

    private int altColor, orange;
    private RecyclerViewItemListener<PickPackShipOrder> itemListener;
    private View root;

    public PickPackShipOrderAdapter(RecyclerViewItemListener<PickPackShipOrder> listener) {
        this.itemListener = listener;
    }

    @Override
    protected ItemPickPackShipOrderBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);
        orange = ContextCompat.getColor(context, R.color.colorIndigoAccent);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_pick_pack_ship_order, parent, false);
    }

    @Override
    protected void bind(ItemPickPackShipOrderBinding binding, final PickPackShipOrder item, final int position) {
        binding.setItem(item);

        root = binding.getRoot();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClick(item, position);
            }
        });

        if (item.PackingPercentage == 100) {
            root.setBackgroundColor(orange);
        } else if (item.PackingPercentage > 90) {
            root.setBackgroundColor(Color.YELLOW);
        } else {
            root.setBackgroundColor(position % 2 == 0 ? altColor : Color.WHITE);
        }
    }

    @Override
    protected boolean areItemsTheSame(PickPackShipOrder oldItem, PickPackShipOrder newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(PickPackShipOrder oldItem, PickPackShipOrder newItem) {
        return false;
    }
}