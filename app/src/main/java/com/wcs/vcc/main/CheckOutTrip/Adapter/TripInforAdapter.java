package com.wcs.vcc.main.CheckOutTrip.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.checkouttrip.response.TripInfor;
import com.wcs.wcs.databinding.ItemTripCheckOutBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import butterknife.ButterKnife;

public class TripInforAdapter extends DataBoundListAdapter<TripInfor,ItemTripCheckOutBinding>{

    private View root;
    private Context context;
    private RecyclerViewItemListener<TripInfor> itemListener;

    public TripInforAdapter(RecyclerViewItemListener<TripInfor> itemListener) {
        this.itemListener = itemListener;
    }


    //khởi tạo view
    @Override
    protected ItemTripCheckOutBinding createBinding(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ButterKnife.bind((Activity) context);
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_trip_check_out, parent, false);
    }

    //set data
    @Override
    protected void bind(ItemTripCheckOutBinding binding, final TripInfor item, final int position) {
        binding.setItem(item);
        root = binding.getRoot();
        if(position%2==0){
            root.setBackgroundResource(R.color.bigcLightGreen);
        }else{
            root.setBackgroundResource(R.color.white);
        }

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClick(item,position);
            }
        });
    }


    @Override
    protected boolean areItemsTheSame(TripInfor oldItem, TripInfor newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(TripInfor oldItem, TripInfor newItem) {
        return false;
    }
}
