package com.wcs.vcc.main.vesinhantoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemThumbImageBinding;
import com.wcs.vcc.main.vesinhantoan.model.ImageThumb;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.ArrayList;
import java.util.List;

public class ImageThumbAdapter extends DataBoundListAdapter<ImageThumb, ItemThumbImageBinding> {


    private View root;
    private RecyclerViewItemOrderListener<ImageThumb> onClick;

    private static List<ImageThumb> stringList;
    private static ArrayList<ImageThumb> arrayList;

    public ImageThumbAdapter(RecyclerViewItemOrderListener<ImageThumb> onClick, List<ImageThumb> stringList) {
        this.onClick = onClick;
        this.stringList = stringList;
        this.arrayList = new ArrayList<ImageThumb>();
        this.arrayList.addAll(stringList);
    }

    @Override
    protected ItemThumbImageBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_thumb_image, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<ItemThumbImageBinding> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.binding.itemThumbIv.setImageBitmap(stringList.get(position).getbImageThumb());


    }

    @Override
    protected void bind(ItemThumbImageBinding binding, ImageThumb item, int position) {
        binding.setItem(item);
        root = binding.getRoot();

        binding.itemCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(item,position,0);
            }
        });
    }

    @Override
    protected boolean areItemsTheSame(ImageThumb oldItem, ImageThumb newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(ImageThumb oldItem, ImageThumb newItem) {
        return false;
    }
}
