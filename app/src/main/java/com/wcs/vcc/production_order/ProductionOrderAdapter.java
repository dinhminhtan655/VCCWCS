package com.wcs.vcc.production_order;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.production_order.ProductionOrder;
import com.wcs.wcs.databinding.ItemProductionOrderBinding;
import com.wcs.vcc.main.packingscan.save_package.SavePackageHNActivity;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

public class ProductionOrderAdapter  extends DataBoundListAdapter<ProductionOrder, ItemProductionOrderBinding> {
    private int altColor, orange;
    private View root;
    Context context;

    public ProductionOrderAdapter(RecyclerViewItemListener<ProductionOrder> productionOrderRecyclerViewItemListener) {
        super();
    }

    public ProductionOrderAdapter() {

    }

    @Override
    protected ItemProductionOrderBinding createBinding(ViewGroup parent, int viewType) {
        context = parent.getContext();
        altColor = ContextCompat.getColor(context, R.color.colorAlternativeRow);
        orange = ContextCompat.getColor(context, R.color.colorIndigoAccent);
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_production_order, parent, false);
    }

    @Override
    protected void bind(ItemProductionOrderBinding binding, ProductionOrder item, int position) {
        binding.setItem(item);

        root = binding.getRoot();
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SavePackageHNActivity.class);
                intent.putExtra("ProductionOrderNumber",item.getProductionOrderNumber());
                intent.putExtra("Planned_Unit",item.getPlanned_Unit());
                context.startActivity(intent);
            }
        });

        root.setBackgroundColor(position % 2 == 0 ? altColor : Color.WHITE);
    }

    @Override
    protected boolean areItemsTheSame(ProductionOrder oldItem, ProductionOrder newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(ProductionOrder oldItem, ProductionOrder newItem) {
        return false;
    }
}
