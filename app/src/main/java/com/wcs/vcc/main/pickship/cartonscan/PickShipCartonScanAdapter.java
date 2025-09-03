package com.wcs.vcc.main.pickship.cartonscan;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemPickShipCartonScanBinding;
import com.wcs.vcc.main.pickship.EventsListener;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;


public class PickShipCartonScanAdapter extends DataBoundListAdapter<PickShipCartonScan, ItemPickShipCartonScanBinding> {

    private int altColor;
    private EventsListener<PickShipCartonScan> itemListener;
    private View root;

    public PickShipCartonScanAdapter(EventsListener<PickShipCartonScan> itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    protected ItemPickShipCartonScanBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_pick_ship_carton_scan, parent, false);
    }

    @Override
    protected void bind(ItemPickShipCartonScanBinding binding, final PickShipCartonScan item, final int position) {
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
    protected boolean areItemsTheSame(PickShipCartonScan oldItem, PickShipCartonScan newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(PickShipCartonScan oldItem, PickShipCartonScan newItem) {
        return false;
    }
}
