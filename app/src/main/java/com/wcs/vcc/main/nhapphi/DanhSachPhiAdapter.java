package com.wcs.vcc.main.nhapphi;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;import com.wcs.wcs.databinding.ItemLoaiphiBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import java.util.List;

public class DanhSachPhiAdapter extends DataBoundListAdapter<ChiPhi, ItemLoaiphiBinding> {

    private RecyclerViewItemListener<ChiPhi> itemListener;
    private static int sSelected = -1;

    public DanhSachPhiAdapter(RecyclerViewItemListener<ChiPhi> itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final DataBoundViewHolder<ItemLoaiphiBinding> holder, final int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.binding.rdChiPhi.setChecked(sSelected == position);
        holder.binding.rdChiPhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sSelected = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

    }

    @Override
    protected ItemLoaiphiBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_loaiphi, parent,false);
    }

    @Override
    protected void bind(final ItemLoaiphiBinding binding, ChiPhi item, final int position) {
        binding.setItem(item);
        View root = binding.getRoot();



    }



    @Override
    protected boolean areItemsTheSame(ChiPhi oldItem, ChiPhi newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(ChiPhi oldItem, ChiPhi newItem) {
        return false;
    }
}
