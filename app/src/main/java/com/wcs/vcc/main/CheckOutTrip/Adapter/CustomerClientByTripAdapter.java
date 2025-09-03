package com.wcs.vcc.main.CheckOutTrip.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.checkouttrip.response.TripCarton;
import com.wcs.wcs.databinding.ItemScanCheckOutCartonBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import butterknife.ButterKnife;

public class CustomerClientByTripAdapter  extends DataBoundListAdapter<TripCarton, ItemScanCheckOutCartonBinding> {
    private View root;
    private Context context;
    private RecyclerViewItemListener<TripCarton> itemListener;

    public CustomerClientByTripAdapter(RecyclerViewItemListener<TripCarton> itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    protected ItemScanCheckOutCartonBinding createBinding(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ButterKnife.bind((Activity) context);
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_scan_check_out_carton, parent, false);
    }

    @Override
    protected void bind(ItemScanCheckOutCartonBinding binding,final TripCarton item,final int position) {
        binding.setItem(item);
        root = binding.getRoot();
        if(item.getStatus().equals("OK")){
            root.setBackgroundResource(R.color.bigcLightGreen);
        }
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClick(item,position);
            }
        });
    }

    @Override
    protected boolean areItemsTheSame(TripCarton oldItem, TripCarton newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(TripCarton oldItem, TripCarton newItem) {
        return false;
    }
}
