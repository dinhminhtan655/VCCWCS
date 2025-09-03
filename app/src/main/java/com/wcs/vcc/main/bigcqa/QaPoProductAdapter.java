package com.wcs.vcc.main.bigcqa;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;import com.wcs.wcs.databinding.ItemQapoProductBinding;
import com.wcs.vcc.main.vo.QaPoProduct;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;

public class QaPoProductAdapter extends DataBoundListAdapter<QaPoProduct, ItemQapoProductBinding> {
    private QaPoProductCallback callback;
    private Context context;
    private int changingColor;

    public QaPoProductAdapter(QaPoProductCallback callback) {
        this.callback = callback;
    }

    @Override
    protected ItemQapoProductBinding createBinding(ViewGroup parent, int viewType) {
        context = parent.getContext();
        changingColor = ContextCompat.getColor(context, R.color.bigcLightGreen);
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_qapo_product, parent, false);
    }

    @Override
    protected void bind(ItemQapoProductBinding binding, QaPoProduct product, int position) {
        if (product != null) {
            product.setPositionInList(position);
            binding.setProduct(product);
            binding.setPosition(position);
            binding.setCallback(callback);
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    callback.onLongClick(product);
                    return false;

                }
            });

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onClick(product);
                }
            });
            binding.getRoot().setBackgroundColor(product.isChanging() ? changingColor : Color.WHITE);

        }
    }

    @Override
    protected boolean areItemsTheSame(QaPoProduct oldItem, QaPoProduct newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected boolean areContentsTheSame(QaPoProduct oldItem, QaPoProduct newItem) {
        return true;
    }
}
