package com.wcs.vcc.main.scanhang.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.scanhang.model.CustomerScan;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerScanAdapter extends RecyclerView.Adapter<CustomerScanAdapter.CustomerScanViewHolder> {


    private List<CustomerScan> customerScanList;
    private RecyclerViewItemListener<CustomerScan> onClick;

    private Context context;

    public CustomerScanAdapter(RecyclerViewItemListener<CustomerScan> onClick) {
        this.onClick = onClick;
    }

    public CustomerScanAdapter() {
    }

    @NonNull
    @Override
    public CustomerScanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer_scan, viewGroup, false);
        return new CustomerScanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerScanViewHolder customerScanViewHolder, int po) {
        CustomerScan customerScan = customerScanList.get(po);
        customerScanViewHolder.tvCustomerCode.setText(customerScan.customerCode);
        customerScanViewHolder.tvCustomerName.setText(customerScan.customerName);
        customerScanViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(customerScan, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (customerScanList != null) {
            return customerScanList.size();
        } else {
            return 0;
        }
    }

    public void setCustomerScan(Context context, List<CustomerScan> customerScanList) {
        this.context = context;
        this.customerScanList = customerScanList;
        notifyDataSetChanged();
    }

    public class CustomerScanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.lnCustomerScan)
        LinearLayout lnCustomerScan;
        @BindView(R.id.tvCustomerCode)
        TextView tvCustomerCode;
        @BindView(R.id.tvCustomerName)
        TextView tvCustomerName;

        public CustomerScanViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
