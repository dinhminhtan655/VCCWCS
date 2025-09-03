package com.wcs.vcc.main.newscanmasan.pickpackshippickinglist;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemPickingListMasanBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PickingListAdapter extends DataBoundListAdapter<PickingList, ItemPickingListMasanBinding> {

    private View root;
    private RecyclerViewItemOrderListener<PickingList> onClick;
    private static List<PickingList> stringList;
    private static ArrayList<PickingList> arrayList;

    public PickingListAdapter(RecyclerViewItemOrderListener<PickingList> onClick,List<PickingList> stringList) {
        this.onClick = onClick;
        this.stringList = stringList;
        this.arrayList = new ArrayList<PickingList>();
        this.arrayList.addAll(stringList);
    }

    public PickingListAdapter() {
    }

    @Nullable
    @Override
    public List<PickingList> getItems() {
        return super.getItems();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected ItemPickingListMasanBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_picking_list_masan, parent, false);
    }

    @Override
    protected void bind(ItemPickingListMasanBinding binding, PickingList item, int position) {
        binding.setItem(item);
        root = binding.getRoot();
        if(position>0){
            root.setBackgroundColor(Color.GRAY);
        }

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(item,position,0);
            }
        });

        root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onClick.onLongClick(item,position,0);
                return true;
            }
        });

    }

    @Override
    protected boolean areItemsTheSame(PickingList oldItem, PickingList newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(PickingList oldItem, PickingList newItem) {
        return false;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        int i = Integer.parseInt(charText);
        String char2 = String.valueOf(i);
        stringList.clear();
        if (charText.length() == 0) {
            stringList.addAll(arrayList);
        } else {
            String a = "";
            for (PickingList st : arrayList) {
                a = st.StoreNumber.trim();
                if (a.toLowerCase(Locale.getDefault()).contains(char2)) {
                    stringList.add(st);
                }
            }
        }
        notifyDataSetChanged();
    }
}
