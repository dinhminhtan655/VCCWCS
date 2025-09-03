package com.wcs.vcc.main.containerandtruckinfor;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wcs.vcc.main.vesinhantoan.model.ImageThumb;
import com.wcs.wcs.databinding.ItemViewpagerImageBinding;

import java.util.List;

public class ViewPagerImageAdapter extends RecyclerView.Adapter<ViewPagerImageAdapter.ViewHolder>{
    private float mScaleFactor = 1.0f;
    private List<ImageThumb> list;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private ViewHolder holder;
    private OnClickItem click;

    public ViewPagerImageAdapter(List<ImageThumb> list, Context context, OnClickItem onClickItem) {
        this.list = list;
        this.context = context;
        click = onClickItem;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewPagerImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewpagerImageBinding binding = ItemViewpagerImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.holder = holder;
        ImageThumb imageThumb = list.get(position);
        holder.bind(imageThumb.getbImageThumb());
        holder.itemViewpagerImageBinding.imgViewPager.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                click.onLongClick(imageThumb);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemViewpagerImageBinding itemViewpagerImageBinding;

        public ViewHolder(ItemViewpagerImageBinding binding) {
            super(binding.getRoot());
            itemViewpagerImageBinding = binding;
        }

        void bind(Bitmap bitmap) {
            itemViewpagerImageBinding.imgViewPager.setImageBitmap(bitmap);
        }
    }


    interface OnClickItem{
        void onClick(ImageThumb imageThumb);
        void onLongClick(ImageThumb imageThumb);
    }
}
