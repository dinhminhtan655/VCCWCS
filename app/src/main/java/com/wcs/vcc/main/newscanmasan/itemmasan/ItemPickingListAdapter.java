package com.wcs.vcc.main.newscanmasan.itemmasan;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemItemMasanBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.List;

public class ItemPickingListAdapter extends DataBoundListAdapter<ItemPickingList, ItemItemMasanBinding> {

    private View root;
    private RecyclerViewItemOrderListener<ItemPickingList> onClick;

    public ItemPickingListAdapter(RecyclerViewItemOrderListener<ItemPickingList> onClick) {
        this.onClick = onClick;
    }

    public ItemPickingListAdapter() {
    }

    @Nullable
    @Override
    public List<ItemPickingList> getItems() {
        return super.getItems();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected ItemItemMasanBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_item_masan, parent, false);
    }

    @Override
    protected void bind(ItemItemMasanBinding binding, ItemPickingList item, int position) {
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
    protected boolean areItemsTheSame(ItemPickingList oldItem, ItemPickingList newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(ItemPickingList oldItem, ItemPickingList newItem) {
        return false;
    }

}
