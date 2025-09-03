package com.wcs.vcc.main.pickship.detail;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemPickShipDetailBinding;
import com.wcs.vcc.main.pickship.EventsListener;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;


public class PickShipDetailAdapter extends DataBoundListAdapter<PickShipDetail, ItemPickShipDetailBinding> {

    private int altColor;
    private EventsListener<PickShipDetail> itemListener;
    private View root;

    public PickShipDetailAdapter(EventsListener<PickShipDetail> itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    protected ItemPickShipDetailBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_pick_ship_detail, parent, false);
    }

    @Override
    protected void bind(ItemPickShipDetailBinding binding, final PickShipDetail item, final int position) {
        binding.setItem(item);

        root = binding.getRoot();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClick(item, position);
            }
        });

        root.setBackgroundColor(position % 2 == 0 ? altColor : Color.WHITE);
    }

    @Override
    protected boolean areItemsTheSame(PickShipDetail oldItem, PickShipDetail newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(PickShipDetail oldItem, PickShipDetail newItem) {
        return false;
    }
}
