package com.wcs.vcc.main.packingscan.pack;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemPickPackShipPackBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

public class PackAdapter extends DataBoundListAdapter<Pack, ItemPickPackShipPackBinding> {

    private int altColor;
    private RecyclerViewItemListener<Pack> itemListener;

    public PackAdapter(RecyclerViewItemListener<Pack> listener) {
        this.itemListener = listener;
    }

    @Override
    protected ItemPickPackShipPackBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_pick_pack_ship_pack, parent, false);
    }

    @Override
    protected void bind(ItemPickPackShipPackBinding binding, final Pack item, final int position) {
        binding.setItem(item);

        View root = binding.getRoot();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        root.setBackgroundColor(position % 2 == 0 ? altColor : Color.WHITE);
    }

    @Override
    protected boolean areItemsTheSame(Pack oldItem, Pack newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(Pack oldItem, Pack newItem) {
        return false;
    }
}