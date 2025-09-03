package com.wcs.vcc.main.scanvinmart;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.XDockVinOutboundPackingSummaryView;
import com.wcs.wcs.databinding.ItemPackingReviewBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

public class PackingReviewAdapter extends DataBoundListAdapter<XDockVinOutboundPackingSummaryView, ItemPackingReviewBinding> {

    private View root;
    private RecyclerViewItemListener<XDockVinOutboundPackingSummaryView> onClick;


    public PackingReviewAdapter(RecyclerViewItemListener<XDockVinOutboundPackingSummaryView> onClick) {
        this.onClick = onClick;
    }

    public PackingReviewAdapter() {
    }

    @Override
    protected ItemPackingReviewBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_packing_review, parent, false);
    }

    @Override
    protected void bind(ItemPackingReviewBinding binding, final XDockVinOutboundPackingSummaryView item, final int position) {
        binding.setItem(item);

        root = binding.getRoot();

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(item,position);
            }
        });

        if (position % 2 == 0) {
            root.setBackgroundResource(R.color.bigcLightGreen);
        } else {
            root.setBackgroundResource(R.color.white);
        }
    }

    @Override
    protected boolean areItemsTheSame(XDockVinOutboundPackingSummaryView oldItem, XDockVinOutboundPackingSummaryView newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(XDockVinOutboundPackingSummaryView oldItem, XDockVinOutboundPackingSummaryView newItem) {
        return false;
    }
}
