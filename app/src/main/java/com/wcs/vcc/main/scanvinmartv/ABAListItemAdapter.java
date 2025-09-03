package com.wcs.vcc.main.scanvinmartv;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.XDockVinOutboundPackingViewSupplierProductsABA;
import com.wcs.wcs.databinding.AbaItemSupplierProductsBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.List;

public class ABAListItemAdapter  extends DataBoundListAdapter<XDockVinOutboundPackingViewSupplierProductsABA, AbaItemSupplierProductsBinding> {

    private View root;
    private RecyclerViewItemOrderListener<XDockVinOutboundPackingViewSupplierProductsABA> onClick;
    private static List<XDockVinOutboundPackingViewSupplierProductsABA> stringList;

    public ABAListItemAdapter(RecyclerViewItemOrderListener<XDockVinOutboundPackingViewSupplierProductsABA> onClick, List<XDockVinOutboundPackingViewSupplierProductsABA> stringList) {
        this.onClick = onClick;
        this.stringList = stringList;
    }

    @Override
    protected AbaItemSupplierProductsBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.aba_item_supplier_products, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<AbaItemSupplierProductsBinding> holder, int position, @NonNull List<Object> payloads) {
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
    protected void bind(AbaItemSupplierProductsBinding binding, XDockVinOutboundPackingViewSupplierProductsABA item, int position) {
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
    protected boolean areItemsTheSame(XDockVinOutboundPackingViewSupplierProductsABA oldItem, XDockVinOutboundPackingViewSupplierProductsABA newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(XDockVinOutboundPackingViewSupplierProductsABA oldItem, XDockVinOutboundPackingViewSupplierProductsABA newItem) {
        return false;
    }
}
