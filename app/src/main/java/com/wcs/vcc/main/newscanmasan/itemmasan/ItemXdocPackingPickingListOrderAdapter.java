package com.wcs.vcc.main.newscanmasan.itemmasan;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemXdocPickinglistOrderBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.List;

public class ItemXdocPackingPickingListOrderAdapter extends DataBoundListAdapter<XdocPackingPickingListOrder, ItemXdocPickinglistOrderBinding> {
    private View root;
    private RecyclerViewItemOrderListener<XdocPackingPickingListOrder> onClick;
    public ItemXdocPackingPickingListOrderAdapter(RecyclerViewItemOrderListener<XdocPackingPickingListOrder> onClick) {
        this.onClick=onClick;
    }

    public ItemXdocPackingPickingListOrderAdapter() {
    }

    @Nullable
    @Override
    public List<XdocPackingPickingListOrder> getItems() {
        return super.getItems();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected ItemXdocPickinglistOrderBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_xdoc_pickinglist_order, parent, false);
    }

    @Override
    protected void bind(ItemXdocPickinglistOrderBinding binding, XdocPackingPickingListOrder item, int position) {
        binding.setItem(item);
        root = binding.getRoot();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(item,position,0);
            }
        });

    }

    @Override
    protected boolean areItemsTheSame(XdocPackingPickingListOrder oldItem, XdocPackingPickingListOrder newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(XdocPackingPickingListOrder oldItem, XdocPackingPickingListOrder newItem) {
        return false;
    }


}
