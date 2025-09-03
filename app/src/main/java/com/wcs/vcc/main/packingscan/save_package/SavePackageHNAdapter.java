package com.wcs.vcc.main.packingscan.save_package;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemSavePackageHnBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

public class SavePackageHNAdapter extends DataBoundListAdapter<SavePackageHN , ItemSavePackageHnBinding> {

    private RecyclerViewItemListener<SavePackageHN> itemListener;

    public SavePackageHNAdapter(RecyclerViewItemListener<SavePackageHN> itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    protected ItemSavePackageHnBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_save_package_hn, parent, false);
    }

    @Override
    protected void bind(ItemSavePackageHnBinding binding, final SavePackageHN item, final int position) {
        binding.setItem(item);

        View root = binding.getRoot();

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onClick(item, position);
            }
        });

        root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemListener.onLongClick(item, position);
                return true;
            }
        });


    }

    @Override
    protected boolean areItemsTheSame(SavePackageHN oldItem, SavePackageHN newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(SavePackageHN oldItem, SavePackageHN newItem) {
        return false;
    }
}
