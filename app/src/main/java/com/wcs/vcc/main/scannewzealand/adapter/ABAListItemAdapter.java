package com.wcs.vcc.main.scannewzealand.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.AbaItemSupplierProductsNewzealandBinding;
import com.wcs.vcc.main.scannewzealand.model.XDockOutboundPackingViewSupplierProductsNewZealandABA;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.List;

public class ABAListItemAdapter extends DataBoundListAdapter<XDockOutboundPackingViewSupplierProductsNewZealandABA, AbaItemSupplierProductsNewzealandBinding> {

    private View root;
    private RecyclerViewItemOrderListener<XDockOutboundPackingViewSupplierProductsNewZealandABA> onClick;
    private static List<XDockOutboundPackingViewSupplierProductsNewZealandABA> stringList;

    public ABAListItemAdapter(RecyclerViewItemOrderListener<XDockOutboundPackingViewSupplierProductsNewZealandABA> onClick, List<XDockOutboundPackingViewSupplierProductsNewZealandABA> stringList) {
        this.onClick = onClick;
        this.stringList = stringList;
    }

    @Override
    protected AbaItemSupplierProductsNewzealandBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.aba_item_supplier_products_newzealand, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<AbaItemSupplierProductsNewzealandBinding> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (stringList.get(position).totalMove == 0){
            stringList.get(holder.getAdapterPosition()).setColorWaiting(R.color.waiting);
            holder.itemView.setBackgroundResource(stringList.get(position).getColorWaiting());
        }else if (stringList.get(position).totalMove > 0 && stringList.get(position).totalMove < stringList.get(position).totalBich){
            stringList.get(holder.getAdapterPosition()).setColorWorking(R.color.working);
            holder.itemView.setBackgroundResource(stringList.get(position).getColorWorking());
        }else if(stringList.get(position).totalMove == stringList.get(position).totalBich) {
            stringList.get(holder.getAdapterPosition()).setColorCompleted(R.color.completed);
            holder.itemView.setBackgroundResource(stringList.get(position).getColorCompleted());
        }
    }

    @Override
    protected void bind(AbaItemSupplierProductsNewzealandBinding binding, XDockOutboundPackingViewSupplierProductsNewZealandABA item, int position) {
        binding.setItem(item);
        root = binding.getRoot();

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(item,position,0);
            }
        });
    }

    @Override
    protected boolean areItemsTheSame(XDockOutboundPackingViewSupplierProductsNewZealandABA oldItem, XDockOutboundPackingViewSupplierProductsNewZealandABA newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(XDockOutboundPackingViewSupplierProductsNewZealandABA oldItem, XDockOutboundPackingViewSupplierProductsNewZealandABA newItem) {
        return false;
    }
}
