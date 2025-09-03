package com.wcs.vcc.main.bigcqa.bigccheckout;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemBigcCheckoutTripBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.List;

public class BigCCheckOutTripAdapter extends DataBoundListAdapter<BigDock_TripDetails, ItemBigcCheckoutTripBinding> {

    private View root;
    private RecyclerViewItemOrderListener<BigDock_TripDetails> onClick;

    private static List<BigDock_TripDetails> stringList;

    public BigCCheckOutTripAdapter(RecyclerViewItemOrderListener<BigDock_TripDetails> onClick, List<BigDock_TripDetails> stringList) {
        this.onClick = onClick;
        this.stringList = stringList;

    }

    public BigCCheckOutTripAdapter() {
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Nullable
    @Override
    public List<BigDock_TripDetails> getItems() {
        return super.getItems();
    }

    @Override
    protected ItemBigcCheckoutTripBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_bigc_checkout_trip, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<ItemBigcCheckoutTripBinding> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (stringList.get(position).ScannedStatus == 0){
            stringList.get(holder.getAdapterPosition()).setColorWaiting(R.color.waiting);
            holder.itemView.setBackgroundResource(stringList.get(holder.getAdapterPosition()).getColorWaiting());
//            stringList.get(holder.getAdapterPosition()).setColorCompleted(R.color.bigcLightGreen);
//            holder.itemView.setBackgroundResource(stringList.get(holder.getAdapterPosition()).getColorCompleted());
        } else if (stringList.get(position).ScannedStatus == 1){
            stringList.get(holder.getAdapterPosition()).setColorFailed(R.color.colorPrimaryDark);
            holder.itemView.setBackgroundResource(stringList.get(holder.getAdapterPosition()).getColorFailed());
        }else if (stringList.get(position).ScannedStatus == 2){
            stringList.get(holder.getAdapterPosition()).setColorCompleted(R.color.bigcLightGreen);
            holder.itemView.setBackgroundResource(stringList.get(holder.getAdapterPosition()).getColorCompleted());
        }

//        if (position % 2 == 0) {
//            stringList.get(holder.getAdapterPosition()).setColorCompleted(R.color.bigcLightGreen);
//            holder.itemView.setBackgroundResource(stringList.get(holder.getAdapterPosition()).getColorCompleted());
//        } else {
//            stringList.get(holder.getAdapterPosition()).setColorFailed(R.color.colorPrimaryDark);
//            holder.itemView.setBackgroundResource(stringList.get(holder.getAdapterPosition()).getColorFailed());
//        }

    }

    @Override
    protected void bind(ItemBigcCheckoutTripBinding binding, BigDock_TripDetails item, int position) {
        binding.setItem(item);
        root = binding.getRoot();


    }

    @Override
    protected boolean areItemsTheSame(BigDock_TripDetails oldItem, BigDock_TripDetails newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(BigDock_TripDetails oldItem, BigDock_TripDetails newItem) {
        return false;
    }
}
