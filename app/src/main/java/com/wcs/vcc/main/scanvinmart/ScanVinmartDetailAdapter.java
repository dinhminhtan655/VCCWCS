package com.wcs.vcc.main.scanvinmart;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.XDockVinInboundWeightReceiveView;
import com.wcs.wcs.databinding.ItemVinmartScanBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ScanVinmartDetailAdapter extends DataBoundListAdapter<XDockVinInboundWeightReceiveView, ItemVinmartScanBinding> {

    private RecyclerViewItemOrderListener<XDockVinInboundWeightReceiveView> itemListener;
    private View root;

    private static List<XDockVinInboundWeightReceiveView> stringList;
    private static ArrayList<XDockVinInboundWeightReceiveView> arrayList;

    public ScanVinmartDetailAdapter(RecyclerViewItemOrderListener<XDockVinInboundWeightReceiveView> itemListener, List<XDockVinInboundWeightReceiveView> stringList) {
        this.itemListener = itemListener;
        this.stringList = stringList;
        this.arrayList = new ArrayList<XDockVinInboundWeightReceiveView>();
        this.arrayList.addAll(stringList);
    }

    public ScanVinmartDetailAdapter() {

    }

    @Override
    protected ItemVinmartScanBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_vinmart_scan, parent, false);
    }

    @Override
    protected void bind(ItemVinmartScanBinding binding, final XDockVinInboundWeightReceiveView item, final int position) {
        binding.setItem(item);

        root = binding.getRoot();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClick(item, position, 0);
            }
        });

        root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemListener.onClick(item, position, 1);
                return false;
            }
        });

        if (position % 2 == 0) {
            root.setBackgroundResource(R.color.bigcLightGreen);
        } else {
            root.setBackgroundResource(R.color.white);
        }
    }

    @Override
    protected boolean areItemsTheSame(XDockVinInboundWeightReceiveView oldItem, XDockVinInboundWeightReceiveView newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(XDockVinInboundWeightReceiveView oldItem, XDockVinInboundWeightReceiveView newItem) {
        return false;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        stringList.clear();
        if (charText.length() == 0){
            stringList.addAll(arrayList);
        }else {
            for (XDockVinInboundWeightReceiveView st: arrayList){
                if (st.Supplier_Name.toLowerCase(Locale.getDefault()).contains(charText)){
                    stringList.add(st);
                }else if (st.Supplier_Code.toLowerCase(Locale.getDefault()).contains(charText)){
                    stringList.add(st);
                }
            }
        }

        notifyDataSetChanged();

    }
}
