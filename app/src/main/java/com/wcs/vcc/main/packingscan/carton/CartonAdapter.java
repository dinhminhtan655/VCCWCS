package com.wcs.vcc.main.packingscan.carton;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemPickPackShipCartonBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartonAdapter extends DataBoundListAdapter<Carton, ItemPickPackShipCartonBinding> {

    private int altColor;
    private RecyclerViewItemListener<Carton> itemListener;
    private PackageTypeListener typeListener;
    private static List<Carton> stringList;
    private static ArrayList<Carton> arrayList;

    public CartonAdapter(RecyclerViewItemListener<Carton> listener, PackageTypeListener typeListener,List<Carton> stringList) {
        this.itemListener = listener;
        this.typeListener = typeListener;
        this.arrayList = new ArrayList<Carton>();
        this.arrayList.addAll(stringList);
    }

    public CartonAdapter(RecyclerViewItemListener<Carton> itemListener, PackageTypeListener typeListener) {
        this.itemListener = itemListener;
        this.typeListener = typeListener;
    }

    public CartonAdapter() {
    }

    @Override
    protected ItemPickPackShipCartonBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_pick_pack_ship_carton, parent, false);
    }

    @Override
    protected void bind(ItemPickPackShipCartonBinding binding, final Carton item, final int position) {
        binding.setItem(item);

        View root = binding.getRoot();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        binding.itemTvPpscPackageType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeListener.onClickListen(item);
            }
        });

        if (item.Completed) {
            root.setBackgroundColor(Color.YELLOW);
            binding.setIsBold(true);
        } else if (item.Quantity > 0) {
            root.setBackgroundColor(altColor);
            binding.setIsBold(true);
        } else {
            binding.setIsBold(false);
            root.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    protected boolean areItemsTheSame(Carton oldItem, Carton newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(Carton oldItem, Carton newItem) {
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
            for (Carton ct : arrayList) {
                a = String.valueOf(ct.CartonNumber).trim();
                if (a.toLowerCase(Locale.getDefault()).contains(char2)) {
                    stringList.add(ct);
                }
            }
        }
        notifyDataSetChanged();
    }
}