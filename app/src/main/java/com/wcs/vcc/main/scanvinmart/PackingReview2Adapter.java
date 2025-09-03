package com.wcs.vcc.main.scanvinmart;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.XDockVinOutboundPackingByStoreView;
import com.wcs.wcs.databinding.ItemPackingReview2Binding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;

public class PackingReview2Adapter extends DataBoundListAdapter<XDockVinOutboundPackingByStoreView, ItemPackingReview2Binding> {

    private View root;

    public PackingReview2Adapter() {
    }

    @Override
    protected ItemPackingReview2Binding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_packing_review2, parent, false);
    }

    @Override
    protected void bind(ItemPackingReview2Binding binding, XDockVinOutboundPackingByStoreView item, int position) {
        binding.setItem(item);

        root = binding.getRoot();

        if (position % 2 == 0) {
            root.setBackgroundResource(R.color.bigcLightGreen);
        } else {
            root.setBackgroundResource(R.color.white);
        }
    }

    @Override
    protected boolean areItemsTheSame(XDockVinOutboundPackingByStoreView oldItem, XDockVinOutboundPackingByStoreView newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(XDockVinOutboundPackingByStoreView oldItem, XDockVinOutboundPackingByStoreView newItem) {
        return false;
    }
}
