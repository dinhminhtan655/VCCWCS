package com.wcs.vcc.main.pickship;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;
import com.wcs.vcc.api.xdoc.response.CheckStatusPickPackResponse;
import com.wcs.wcs.databinding.ItemPackingStatusBinding;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;

public class PackingStatusAdateper extends DataBoundListAdapter<CheckStatusPickPackResponse, ItemPackingStatusBinding> {
//    private EventsListener<CheckStatusPickPackResponse> itemListener;
    private View root;
    public PackingStatusAdateper() {
    }

    @Override
    protected ItemPackingStatusBinding createBinding(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_packing_status, parent, false);
    }

    @Override
    protected void bind(ItemPackingStatusBinding binding, CheckStatusPickPackResponse item, int position) {
        binding.setItem(item);
        root = binding.getRoot();
        if (position%2==0) {
            root.setBackgroundResource(R.color.colorAlternativeRow);
        }
        else{
            root.setBackgroundResource(R.color.white);
        }
       // root.setOnClickListener(new View.OnClickListener() {
    //        @Override
    //        public void onClick(View view) {
//                storeNumber = getIntent().getIntExtra("STORE_NUMBER", 1);
//                date = getIntent().getStringExtra("DATE");
//                orderNumber = getIntent().getStringExtra("ORDER_NUMBER");
//                String clientName = getIntent().getStringExtra("CLIENT_NAME");
//                barcode = getIntent().getStringExtra("BARCODE");
//                    Intent intent = new Intent(root.getContext(), CartonsActivity.class);
//                    intent.putExtra("STORE_NUMBER", item.StoreNumber);
//                    intent.putExtra("DATE", item.DispatchingOrderDate);
//                    intent.putExtra("ORDER_NUMBER", item.DispatchingOrderNumber);
   //         }
    //    });
    }

    @Override
    protected boolean areItemsTheSame(CheckStatusPickPackResponse oldItem, CheckStatusPickPackResponse newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(CheckStatusPickPackResponse oldItem, CheckStatusPickPackResponse newItem) {
        return false;
    }
}
