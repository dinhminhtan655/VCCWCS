package com.wcs.vcc.main.pickship.order;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.xdoc.response.PackingList;
import com.wcs.wcs.databinding.ItemDispatchingOrderPackingBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.List;

public class DispatchingOrderPackingAdapter extends DataBoundListAdapter<PackingList, ItemDispatchingOrderPackingBinding> {

    private View root;
    private RecyclerViewItemOrderListener onClick;
    private static List<PackingList> stringList;

    public DispatchingOrderPackingAdapter(RecyclerViewItemOrderListener onClick, List<PackingList> stringList) {
        this.onClick = onClick;
        this.stringList = stringList;
    }

    public DispatchingOrderPackingAdapter() {
    }

    @Override
    protected ItemDispatchingOrderPackingBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_dispatching_order_packing, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<ItemDispatchingOrderPackingBinding> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);


    }

    @Override
    protected void bind(ItemDispatchingOrderPackingBinding binding, final PackingList item, final int position) {
        binding.setItem(item);
        root = binding.getRoot();


        if (item.XacNhan.equals("1") && position != 0) {
            stringList.get(position).setColorWaiting(R.color.waiting);
            stringList.get(position).setEnalble(false);
            binding.getRoot().setBackgroundResource(stringList.get(position).getColorWaiting());
            binding.tvSLMove.setEnabled(stringList.get(position).isEnalble());
        } else if (item.XacNhan.equals("2")) {
            stringList.get(position).setColorCompleted(R.color.completed);
            stringList.get(position).setEnalble(false);
            binding.getRoot().setBackgroundResource(stringList.get(position).getColorCompleted());
            binding.tvSLMove.setEnabled(stringList.get(position).isEnalble());
        }
        else if (position == 0) {
            root.setBackgroundResource(R.color.white);
            stringList.get(position).setEnalble(true);
            binding.tvSLMove.setEnabled(stringList.get(position).isEnalble());
        }


        binding.tvSLMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(item, position, 0);
            }
        });

        binding.tvDocEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(item, position, 1);
            }
        });


    }

    @Override
    protected boolean areItemsTheSame(PackingList oldItem, PackingList newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(PackingList oldItem, PackingList newItem) {
        return false;
    }


}
