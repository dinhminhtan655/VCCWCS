package com.wcs.vcc.main.scanvinmart;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.XDockVinInboundWeightReceiveViewDetail;
import com.wcs.wcs.databinding.ItemVinmartDetailBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;

public class ViewVinmartDetailAdapter extends DataBoundListAdapter<XDockVinInboundWeightReceiveViewDetail, ItemVinmartDetailBinding> {

    private View root;

    public ViewVinmartDetailAdapter() {

    }

    @Override
    protected ItemVinmartDetailBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_vinmart_detail, parent, false);
    }

    @Override
    protected void bind(ItemVinmartDetailBinding binding, XDockVinInboundWeightReceiveViewDetail item, int position) {
        binding.setItem(item);
        root = binding.getRoot();

        if (position % 2 == 0) {
            root.setBackgroundResource(R.color.bigcLightGreen);
        } else {
            root.setBackgroundResource(R.color.white);
        }
    }

    @Override
    protected boolean areItemsTheSame(XDockVinInboundWeightReceiveViewDetail oldItem, XDockVinInboundWeightReceiveViewDetail newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(XDockVinInboundWeightReceiveViewDetail oldItem, XDockVinInboundWeightReceiveViewDetail newItem) {
        return false;
    }
}
