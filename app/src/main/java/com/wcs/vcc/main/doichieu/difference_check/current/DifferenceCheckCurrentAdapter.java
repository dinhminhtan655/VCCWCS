package com.wcs.vcc.main.doichieu.difference_check.current;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemDifferenceCheckCurrentBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

public class DifferenceCheckCurrentAdapter extends DataBoundListAdapter<DifferenceCheckCurrent, ItemDifferenceCheckCurrentBinding> {

    private int altColor;
    private RecyclerViewItemListener itemListener;
    private IEventHandler handler;
    private View root;

    public DifferenceCheckCurrentAdapter(RecyclerViewItemListener<DifferenceCheckCurrent> itemListener, IEventHandler handler) {
        this.itemListener = itemListener;
        this.handler = handler;
    }

    @Override
    protected ItemDifferenceCheckCurrentBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_difference_check_current, parent, false);
    }

    @Override
    protected void bind(ItemDifferenceCheckCurrentBinding binding, final DifferenceCheckCurrent item, final int position) {
        binding.setItem(item);
        binding.setHandlers(handler);

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
    protected boolean areItemsTheSame(DifferenceCheckCurrent oldItem, DifferenceCheckCurrent newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(DifferenceCheckCurrent oldItem, DifferenceCheckCurrent newItem) {
        return false;
    }
}
