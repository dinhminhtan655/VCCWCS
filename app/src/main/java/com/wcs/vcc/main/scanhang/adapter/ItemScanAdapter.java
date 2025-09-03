package com.wcs.vcc.main.scanhang.adapter;

import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.scanhang.model.ItemScan;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

public class ItemScanAdapter extends ListAdapter<ItemScan, ItemScanAdapter.ItemScanHolder> {

    private View root;
    private RecyclerViewItemOrderListener<ItemScan> onClick;

    public ItemScanAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ItemScan> DIFF_CALLBACK = new DiffUtil.ItemCallback<ItemScan>() {
        @Override
        public boolean areItemsTheSame(@NonNull ItemScan oldItem, @NonNull ItemScan newItem) {
//            return oldItem.getId() == newItem.getId();
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemScan oldItem, @NonNull ItemScan newItem) {
//            return oldItem.getConfirm() == newItem.getConfirm() &&
//                    oldItem.getItemName().equals(newItem.getItemName()) &&
//                    oldItem.getPalletID() == newItem.getPalletID() &&
//                    oldItem.getQuantityModify() == newItem.getQuantityModify() &&
//                    oldItem.getQuantityMove() == newItem.getQuantityMove() &&
//                    oldItem.getQuantityScan() == newItem.getQuantityScan() &&
//                    oldItem.getSoBich() == newItem.getSoBich() &&
//                    oldItem.getSoThung() == newItem.getSoThung();
            return false;
        }
    };


    @NonNull
    @Override
    public ItemScanHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.aba_item_outbound_packing_all, viewGroup, false);
        return new ItemScanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemScanHolder itemScanHolder, int position) {

        ItemScan itemScan = getItem(position);
        int pos = itemScanHolder.getAdapterPosition();
        if (itemScan.getConfirm() == 1) {
            itemScanHolder.itemView.setEnabled(false);
            itemScanHolder.itemView.setBackgroundResource(R.color.waiting);
        } else if (itemScan.getConfirm() == 0) {
            itemScanHolder.itemView.setEnabled(true);
            itemScanHolder.itemView.setBackgroundResource(R.color.working);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onClick.onClick(itemScan, pos, 0);
                }
            }, 100);

            itemScanHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onClick(itemScan, pos, 0);
                }
            });
        } else {
            itemScanHolder.itemView.setEnabled(false);
            itemScanHolder.itemView.setBackgroundResource(R.color.completed);
        }
        itemScanHolder.tvPalletID.setText(String.valueOf(itemScan.getPalletID()));
        itemScanHolder.tvSoBich.setText(String.valueOf((int) itemScan.getSoBich()));
        itemScanHolder.tvSLMove.setText(String.valueOf(itemScan.getQuantityMove()));
        itemScanHolder.tvSLDieuChinh.setText(String.valueOf(itemScan.getQuantityModify()));
        itemScanHolder.tvConfirmQuantity.setText(String.valueOf(itemScan.getConfirmQuantity()));
        itemScanHolder.tvThieuDu.setText(String.valueOf(itemScan.getThieuDu()));

        itemScanHolder.tvSLMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(itemScan,position,1);
            }
        });

    }

    public ItemScan getItemScanAt(int position) {
        return getItem(position);
    }

    public class ItemScanHolder extends RecyclerView.ViewHolder {

        private TextView tvPalletID, tvSoBich, tvSLMove, tvSLDieuChinh, tvConfirmQuantity, tvThieuDu;

        public ItemScanHolder(@NonNull View itemView) {
            super(itemView);
            tvPalletID = itemView.findViewById(R.id.tvPalletID);
            tvSoBich = itemView.findViewById(R.id.tvSoBich);
            tvSLMove = itemView.findViewById(R.id.tvSLMove);
            tvSLDieuChinh = itemView.findViewById(R.id.tvSLDieuChinh);
            tvConfirmQuantity = itemView.findViewById(R.id.tvConfirmQuantity);
            tvThieuDu = itemView.findViewById(R.id.tvThieuDu);
        }
    }

    public void setOnItemClickListener(RecyclerViewItemOrderListener<ItemScan> onClick) {
        this.onClick = onClick;
    }


}
