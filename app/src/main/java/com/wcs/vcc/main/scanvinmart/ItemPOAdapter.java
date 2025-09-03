package com.wcs.vcc.main.scanvinmart;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemXdocPoListBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

public class ItemPOAdapter  extends DataBoundListAdapter<ItemPoInfor, ItemXdocPoListBinding> {
    private int altColor, orange;
    private RecyclerViewItemListener<ItemPoInfor> itemListener;
    private View root;

    public ItemPOAdapter(RecyclerViewItemListener<ItemPoInfor> listener) {
        this.itemListener = listener;
    }

    @Override
    protected ItemXdocPoListBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);
        orange = ContextCompat.getColor(context, R.color.colorIndigoAccent);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_xdoc_po_list, parent, false);
    }

    @Override
    protected void bind(ItemXdocPoListBinding binding, final ItemPoInfor item, final int position) {
        binding.setItem(item);

        root = binding.getRoot();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClick(item, position);
            }
        });

        if (item.getStatus() == 100) {
            root.setBackgroundColor(orange);
        } else if (item.getStatus() > 90) {
            root.setBackgroundColor(Color.YELLOW);
        }
    }

    @Override
    protected boolean areItemsTheSame(ItemPoInfor oldItem, ItemPoInfor newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(ItemPoInfor oldItem, ItemPoInfor newItem) {
        return false;
    }
}
