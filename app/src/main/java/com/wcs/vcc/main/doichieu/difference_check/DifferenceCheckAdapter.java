package com.wcs.vcc.main.doichieu.difference_check;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemDifferenceCheckBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

public class DifferenceCheckAdapter extends DataBoundListAdapter<DifferenceCheck, ItemDifferenceCheckBinding> {

    private int altColor;
    private RecyclerViewItemListener itemListener;
    private View root;

    public DifferenceCheckAdapter(RecyclerViewItemListener<DifferenceCheck> itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    protected ItemDifferenceCheckBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_difference_check, parent, false);
    }

    @Override
    protected void bind(ItemDifferenceCheckBinding binding, final DifferenceCheck item, final int position) {
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
    protected boolean areItemsTheSame(DifferenceCheck oldItem, DifferenceCheck newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(DifferenceCheck oldItem, DifferenceCheck newItem) {
        return false;
    }
}
