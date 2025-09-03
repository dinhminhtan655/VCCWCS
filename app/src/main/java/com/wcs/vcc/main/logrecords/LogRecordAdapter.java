package com.wcs.vcc.main.logrecords;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemLogRecordsBinding;

public class LogRecordAdapter extends DataBoundListAdapter<LogRecord, ItemLogRecordsBinding> {

    private View root;

    @Override
    protected ItemLogRecordsBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_log_records, parent, false);
    }

    @Override
    protected void bind(ItemLogRecordsBinding binding, LogRecord item, int position) {
        binding.setItem(item);
        root = binding.getRoot();

        if (position % 2 == 0){
            root.setBackgroundResource(R.color.generalGreen);
            if (position == 0){
                root.setBackgroundColor(Color.YELLOW);
            }
        }else{
            root.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    protected boolean areItemsTheSame(LogRecord oldItem, LogRecord newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(LogRecord oldItem, LogRecord newItem) {
        return false;
    }
}
