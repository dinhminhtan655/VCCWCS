package com.wcs.vcc.main.soki.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;import com.wcs.wcs.databinding.ItemGhiSoKiBinding;
import com.wcs.vcc.main.soki.model.ProductKg;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

public class ProductKgAdapter extends DataBoundListAdapter<ProductKg, ItemGhiSoKiBinding> {

    private View root;
    private RecyclerViewItemOrderListener<ProductKg> onClick;

    public ProductKgAdapter(RecyclerViewItemOrderListener<ProductKg> onClick) {
        this.onClick = onClick;
    }

    @Override
    protected ItemGhiSoKiBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_ghi_so_ki, parent, false);
    }

    @Override
    protected void bind(ItemGhiSoKiBinding binding, ProductKg item, int position) {
        binding.setItem(item);
        root = binding.getRoot();

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(item, position, 0);
            }
        });

        root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onClick.onLongClick(item,position,0);
                return true;
            }
        });

        if (position % 2 == 0){
            root.setBackgroundResource(R.color.generalGreen);
            if (position == 0){
                root.setBackgroundColor(Color.YELLOW);
            }
        }else{
            root.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    protected boolean areItemsTheSame(ProductKg oldItem, ProductKg newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(ProductKg oldItem, ProductKg newItem) {
        return false;
    }
}
