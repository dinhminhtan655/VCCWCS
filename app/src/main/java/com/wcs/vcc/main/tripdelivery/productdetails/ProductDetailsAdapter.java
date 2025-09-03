package com.wcs.vcc.main.tripdelivery.productdetails;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemTripProductDetailsBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

public class ProductDetailsAdapter extends DataBoundListAdapter<TripDeliveryProductDetails, ItemTripProductDetailsBinding> {

    private int altColor;
    private RecyclerViewItemListener itemListener;
    private View root;

    public ProductDetailsAdapter(RecyclerViewItemListener<TripDeliveryProductDetails> itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    protected ItemTripProductDetailsBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_trip_product_details, parent, false);
    }

    @Override
    protected void bind(ItemTripProductDetailsBinding binding, final TripDeliveryProductDetails item, final int position) {
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
    protected boolean areItemsTheSame(TripDeliveryProductDetails oldItem, TripDeliveryProductDetails newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(TripDeliveryProductDetails oldItem, TripDeliveryProductDetails newItem) {
        return false;
    }
}
