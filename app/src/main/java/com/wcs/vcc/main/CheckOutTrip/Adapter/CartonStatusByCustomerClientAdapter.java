package com.wcs.vcc.main.CheckOutTrip.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.checkouttrip.response.CartonStatus;
import com.wcs.wcs.databinding.ItemCartonStatusByCustomerclientBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import butterknife.ButterKnife;

public class CartonStatusByCustomerClientAdapter extends DataBoundListAdapter<CartonStatus, ItemCartonStatusByCustomerclientBinding> {

    private View root;
    private Context context;
    private RecyclerViewItemListener<CartonStatus> itemListener;


    public CartonStatusByCustomerClientAdapter(RecyclerViewItemListener<CartonStatus> itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    protected ItemCartonStatusByCustomerclientBinding createBinding(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ButterKnife.bind((Activity) context);
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_carton_status_by_customerclient, parent, false);
    }

    @Override
    protected void bind(ItemCartonStatusByCustomerclientBinding binding, CartonStatus item, int position) {
        binding.setItem(item);
        root = binding.getRoot();
        if(item.getStatus().equals("OK")){
            root.setBackgroundResource(R.color.bigcLightGreen);
        }
    }

    @Override
    protected boolean areItemsTheSame(CartonStatus oldItem, CartonStatus newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(CartonStatus oldItem, CartonStatus newItem) {
        return false;
    }
}
