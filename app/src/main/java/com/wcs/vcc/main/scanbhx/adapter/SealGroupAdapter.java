package com.wcs.vcc.main.scanbhx.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;import com.wcs.wcs.databinding.ItemSealGroupBhxBinding;
import com.wcs.vcc.main.scanbhx.model.SealGroup;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.List;

public class SealGroupAdapter extends DataBoundListAdapter<SealGroup, ItemSealGroupBhxBinding> {

    private View root;
    private RecyclerViewItemOrderListener<SealGroup> onClick;

    public SealGroupAdapter(RecyclerViewItemOrderListener<SealGroup> onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected ItemSealGroupBhxBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_seal_group_bhx, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<ItemSealGroupBhxBinding> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    protected void bind(ItemSealGroupBhxBinding binding, SealGroup item, int position) {
        binding.setItem(item);
        root = binding.getRoot();

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(item, position, 0);
            }
        });
    }

    @Override
    protected boolean areItemsTheSame(SealGroup oldItem, SealGroup newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(SealGroup oldItem, SealGroup newItem) {
        return false;
    }
}
