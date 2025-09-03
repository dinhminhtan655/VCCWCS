package com.wcs.vcc.main.scanbarcode;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemScanBarcode2Binding;
import com.wcs.vcc.main.palletcartonweighting.WeightingItemListener;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import java.util.UUID;

public class OrderResultScanBarcodeAdapter extends DataBoundListAdapter<STAndroid_CartonScannedByDO, ItemScanBarcode2Binding> {

    private int altColor;
    private View root;
    private View viewSelected;
    private WeightingItemListener itemListener;
    private RecyclerViewItemListener onClick;
    UUID doDetailID;

    public OrderResultScanBarcodeAdapter(RecyclerViewItemListener onClick) {
        this.onClick = onClick;
    }

    @Override
    protected ItemScanBarcode2Binding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);

        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_scan_barcode_2, parent, false);
    }

    @Override
    protected void bind(ItemScanBarcode2Binding binding, final STAndroid_CartonScannedByDO item, final int position) {
        binding.setItem(item);
        root = binding.getRoot();
        binding.getRoot().setBackgroundColor(position % 2 == 0 ? altColor : Color.WHITE);
        binding.getRoot().setBackgroundColor(Color.argb(255, 255, 238, 88));

        root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClick.onLongClick(item, position);
                return false;
            }
        });
    }

    @Override
    protected boolean areItemsTheSame(STAndroid_CartonScannedByDO oldItem, STAndroid_CartonScannedByDO newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(STAndroid_CartonScannedByDO oldItem, STAndroid_CartonScannedByDO newItem) {
        return false;
    }
//        @Override
//        protected ItemOrderResultSupervisorBinding createBinding(ViewGroup parent, int viewType) {
//            Context context = parent.getContext();
//            altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);
//
//            return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_scan_barcode_2, parent, false);
//        }
//
//        @Override
//        protected void bind(ItemOrderResultSupervisorBinding binding, OrderResultsSupervisorScan item, int position) {
//            binding.setItem(item);
//            binding.getRoot().setBackgroundColor(position % 2 == 0 ? altColor : Color.WHITE);
//            if (item.IsRecordNew) {
//                binding.getRoot().setBackgroundColor(Color.argb(255, 255, 238, 88));
//            }
//        }
//
//        @Override
//        protected boolean areItemsTheSame(OrderResultsSupervisorScan oldItem, OrderResultsSupervisorScan newItem) {
//            return false;
//        }
//
//        @Override
//        protected boolean areContentsTheSame(OrderResultsSupervisorScan oldItem, OrderResultsSupervisorScan newItem) {
//            return false;

}
