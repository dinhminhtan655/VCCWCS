package com.wcs.vcc.main.scanvinmart;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.XDockVinInboundSupplierSummary;
import com.wcs.wcs.databinding.ItemVinmartBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScanVinmartAdapter extends DataBoundListAdapter<XDockVinInboundSupplierSummary, ItemVinmartBinding> {

    private RecyclerViewItemListener<XDockVinInboundSupplierSummary> itemListener;
    private View root;

    private static List<XDockVinInboundSupplierSummary> stringList;
    private static ArrayList<XDockVinInboundSupplierSummary> arrayList;


    public ScanVinmartAdapter(RecyclerViewItemListener<XDockVinInboundSupplierSummary> itemListener, List<XDockVinInboundSupplierSummary> stringList) {
        this.itemListener = itemListener;
        this.stringList = stringList;
        this.arrayList = new ArrayList<XDockVinInboundSupplierSummary>();
        this.arrayList.addAll(stringList);
    }

    public ScanVinmartAdapter() {
    }


    @Override
    protected ItemVinmartBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_vinmart, parent, false);
    }

    @Override
    protected void bind(ItemVinmartBinding binding, final XDockVinInboundSupplierSummary item, final int position) {
        binding.setItem(item);

        root = binding.getRoot();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClick(item, position);
            }
        });


        if ((position % 2 == 0)) {
            root.setBackgroundResource(R.color.bigcLightGreen);
        } else {
            root.setBackgroundResource(R.color.white);
        }

    }

    @Override
    protected boolean areItemsTheSame(XDockVinInboundSupplierSummary oldItem, XDockVinInboundSupplierSummary newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(XDockVinInboundSupplierSummary oldItem, XDockVinInboundSupplierSummary newItem) {
        return false;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        stringList.clear();
        if (charText.length() == 0){
            stringList.addAll(arrayList);
        }else {
            for (XDockVinInboundSupplierSummary st: arrayList){
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
