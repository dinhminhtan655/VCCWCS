package com.wcs.vcc.main.pickship;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;import com.wcs.wcs.databinding.ItemPickShipBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;


public class PickShipAdapter extends DataBoundListAdapter<PickShip, ItemPickShipBinding> {

    private int altColor;
    private EventsListener<PickShip> itemListener;
    private View root;

    public PickShipAdapter(EventsListener<PickShip> itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    protected ItemPickShipBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_pick_ship, parent, false);
    }

    @Override
    protected void bind(ItemPickShipBinding binding, final PickShip item, final int position) {
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
    protected boolean areItemsTheSame(PickShip oldItem, PickShip newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(PickShip oldItem, PickShip newItem) {
        return false;
    }
}
