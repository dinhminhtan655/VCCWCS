package com.wcs.vcc.main.pickship.carton;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemPickShipCartonBinding;
import com.wcs.vcc.main.pickship.EventsListener;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;


public class PickShipCartonAdapter extends DataBoundListAdapter<PickShipCarton, ItemPickShipCartonBinding> {

    private int altColor;
    private EventsListener<PickShipCarton> itemListener;
    private View root;

    public PickShipCartonAdapter(EventsListener<PickShipCarton> itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    protected ItemPickShipCartonBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_pick_ship_carton, parent, false);
    }

    @Override
    protected void bind(ItemPickShipCartonBinding binding, final PickShipCarton item, final int position) {
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
    protected boolean areItemsTheSame(PickShipCarton oldItem, PickShipCarton newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(PickShipCarton oldItem, PickShipCarton newItem) {
        return false;
    }
}
