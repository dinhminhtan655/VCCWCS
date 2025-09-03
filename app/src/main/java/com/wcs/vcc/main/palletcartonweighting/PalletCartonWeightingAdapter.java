package com.wcs.vcc.main.palletcartonweighting;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemPalletCartonWeightingBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;

import java.util.List;
import java.util.UUID;

/**
 * Created by aang on 02/06/2018.
 */

public class PalletCartonWeightingAdapter extends DataBoundListAdapter<PalletCartonWeighting, ItemPalletCartonWeightingBinding> {

    private int altColor, altColor2;
    private WeightingItemListener itemListener;
    private View viewSelected;
    private UUID cartonId;
    private View root;
    public static int count = 0;

    private List<PalletCartonWeighting> stringList;
    private List<PalletCartonWeighting> arrayListfilterd;

    public PalletCartonWeightingAdapter(WeightingItemListener itemListener, List<PalletCartonWeighting> stringList) {
        this.itemListener = itemListener;
        this.stringList = stringList;
        this.arrayListfilterd = stringList;
    }


    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<ItemPalletCartonWeightingBinding> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
//        holder.setIsRecyclable(false);
        if (arrayListfilterd.size() > 0) {
            PalletCartonWeighting palletCartonWeighting = arrayListfilterd.get(holder.getAdapterPosition());

            holder.binding.tvItemPalletCartonWeightingPalletId.setText(palletCartonWeighting.PalletCartonNumber + "");
            holder.binding.tvItemPalletCartonWeightingWeight.setText(palletCartonWeighting.CartonWeight + "");
//        holder.binding.tvItemPalletCartonWeightingUnits.setText(stringList.get(holder.getAdapterPosition()).CartonUnits+"");
            holder.binding.tvItemPalletCartonWeightingGross.setText(palletCartonWeighting.PalletGrossWeight + "");
            holder.binding.tvItemPalletCartonWeightingRemark.setText(palletCartonWeighting.PalletRemark);

            holder.binding.cbItemPalletCartonWeighting.setOnCheckedChangeListener((buttonView, isChecked) -> {
                arrayListfilterd.get(holder.getAdapterPosition()).setChecked(isChecked);
                if (arrayListfilterd.get(holder.getAdapterPosition()).isChecked) {
                    count++;
                } else {
                    count--;
                }
                itemListener.onChecked(count);
            });

            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.binding.getRoot() != null) {
//                    arrayListfilterd.get(holder.getAdapterPosition()).setColor(holder.getAdapterPosition() % 2 == 0 ? altColor : Color.WHITE);
//                    holder.binding.getRoot().setBackgroundColor(arrayListfilterd.get(holder.getAdapterPosition()).getColor());
                        arrayListfilterd.get(holder.getAdapterPosition()).setColor(altColor2);
                        holder.binding.getRoot().setBackgroundColor(arrayListfilterd.get(holder.getAdapterPosition()).getColor());
                    }
//
                    itemListener.onClick(arrayListfilterd.get(holder.getAdapterPosition()), holder.getAdapterPosition());
//                viewSelected = holder.binding.getRoot();
                    cartonId = arrayListfilterd.get(holder.getAdapterPosition()).PalletCartonID;
                    notifyDataSetChanged();
                }
            });

            if (cartonId == arrayListfilterd.get(position).PalletCartonID) {
                arrayListfilterd.get(holder.getAdapterPosition()).setColor(altColor2);
                holder.binding.getRoot().setBackgroundColor(arrayListfilterd.get(holder.getAdapterPosition()).getColor());
            } else {
                arrayListfilterd.get(holder.getAdapterPosition()).setColor(holder.getAdapterPosition() % 2 == 0 ? altColor : Color.WHITE);
                arrayListfilterd.get(0).setColor(Color.YELLOW);
                holder.binding.getRoot().setBackgroundColor(arrayListfilterd.get(holder.getAdapterPosition()).getColor());
            }
        }


    }

    @Override
    protected ItemPalletCartonWeightingBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);
        altColor2 = ContextCompat.getColor(context, R.color.completed);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_pallet_carton_weighting, parent, false);
    }

    @Override
    public void replace(List<PalletCartonWeighting> update) {
        super.replace(update);
        arrayListfilterd = update;
        notifyDataSetChanged();
    }

    @Override
    protected void bind(ItemPalletCartonWeightingBinding binding, final PalletCartonWeighting item, final int position) {
        binding.setItem(item);
        root = binding.getRoot();
        root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemListener.onLongClick(item, position);
                return true;
            }
        });


    }

    @Override
    protected boolean areItemsTheSame(PalletCartonWeighting oldItem, PalletCartonWeighting newItem) {
        return oldItem.PalletCartonID == newItem.PalletCartonID;
    }

    @Override
    protected boolean areContentsTheSame(PalletCartonWeighting oldItem, PalletCartonWeighting newItem) {
        return oldItem.PalletCartonID == newItem.PalletCartonID;
    }


}
