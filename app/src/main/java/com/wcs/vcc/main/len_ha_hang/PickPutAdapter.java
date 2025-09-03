package com.wcs.vcc.main.len_ha_hang;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemPickPutBinding;
import com.wcs.vcc.main.vo.PickPut;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;


public class PickPutAdapter extends DataBoundListAdapter<PickPut, ItemPickPutBinding> {

    private int altColor;
    private Context context;

    public PickPutAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected ItemPickPutBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_pick_put, parent, false);
    }

    @Override
    protected void bind(ItemPickPutBinding binding, PickPut item, int position) {
        binding.setItem(item);
        binding.getRoot().setBackgroundColor(position % 2 == 0 ? altColor : Color.WHITE);

        if (!item.scannedBy.equals("")) {
            binding.getRoot().setBackgroundColor(context.getResources().getColor(R.color.completed));
        }
    }


    @Override
    protected boolean areItemsTheSame(PickPut oldItem, PickPut newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(PickPut oldItem, PickPut newItem) {
        return false;
    }



}
