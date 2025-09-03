package com.wcs.vcc.main.scanhang.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.scanhang.model.ScanPalletCode;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;

public class PalletCodeAdapter extends ListAdapter<ScanPalletCode, PalletCodeAdapter.PalletCodeHolder> {

    private List<ScanPalletCode> scanPalletCodes;
    private RecyclerViewItemOrderListener<ScanPalletCode> onClick;

    public PalletCodeAdapter(RecyclerViewItemOrderListener<ScanPalletCode> onClick) {
        super(DIFF_CALLBACK);
        this.onClick = onClick;
    }

    private static final DiffUtil.ItemCallback<ScanPalletCode> DIFF_CALLBACK = new DiffUtil.ItemCallback<ScanPalletCode>() {
        @Override
        public boolean areItemsTheSame(@NonNull ScanPalletCode scanPalletCode, @NonNull ScanPalletCode t1) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ScanPalletCode scanPalletCode, @NonNull ScanPalletCode t1) {
            return false;
        }
    };

    @NonNull
    @Override
    public PalletCodeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_scan_palletcode, viewGroup, false);
        return new PalletCodeHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull PalletCodeHolder palletCodeHolder, int i) {
        ScanPalletCode scanPalletCode = scanPalletCodes.get(i);
        int pos = palletCodeHolder.getAdapterPosition();
        palletCodeHolder.tvPalletCode.setText(scanPalletCode.palletCode);
        palletCodeHolder.tvUsername.setText(scanPalletCode.username);
        if (scanPalletCode.atTime!= null){
            palletCodeHolder.tvAtTime.setText(Utilities.formatDate_HHmm(scanPalletCode.atTime));
        }

        palletCodeHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(scanPalletCode,i,0);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (scanPalletCodes != null) {
            return scanPalletCodes.size();
        } else {
            return 0;
        }

    }

    public void setCustomerScan(List<ScanPalletCode> scanPalletCodes) {
        this.scanPalletCodes = scanPalletCodes;
        notifyDataSetChanged();
    }

    public ScanPalletCode getScanPalletCodeAt(int position) {
        return getItem(position);
    }

    public class PalletCodeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvPalletCode, tvUsername, tvAtTime;

        public PalletCodeHolder(@NonNull View itemView) {
            super(itemView);
            tvPalletCode = itemView.findViewById(R.id.tvPalletCode);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvAtTime = itemView.findViewById(R.id.tvAtTime);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public void setOnItemClickListener(RecyclerViewItemOrderListener<ScanPalletCode> onClick) {
        this.onClick = onClick;
    }
}
