package com.wcs.vcc.main.scanhang;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.wcs.wcs.R;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.scanhang.adapter.CustomerScanAdapter;
import com.wcs.vcc.main.scanhang.model.CustomerScan;
import com.wcs.vcc.main.scanhang.viewmodel.AllViewModel;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerActivity extends EmdkActivity {

    @BindView(R.id.rvCustomer)
    RecyclerView rvCustomer;

    CustomerScanAdapter adapter;

    List<CustomerScan> customerScanList;
    AllViewModel allViewModel;

    private int numOfColumnRecyclerView = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        customerScanList = new ArrayList<>();

        rvCustomer.setLayoutManager(new GridLayoutManager(this, numOfColumnRecyclerView));

        allViewModel = ViewModelProviders.of(CustomerActivity.this).get(AllViewModel.class);

        customerScanList = populateList();

    }


    private List<CustomerScan> populateList() {

        List<CustomerScan> list = new ArrayList<>();

        allViewModel.getAllCustomerScan(CustomerActivity.this).observe(this, new Observer<List<CustomerScan>>() {
            @Override
            public void onChanged(@Nullable List<CustomerScan> customerScans) {
                adapter = new CustomerScanAdapter(new RecyclerViewItemListener<CustomerScan>() {
                    @Override
                    public void onClick(CustomerScan item, int position) {
                        switch (position) {
                            case 0:
                                Intent intent = new Intent(CustomerActivity.this, RegionActivity.class);
                                intent.putExtra("customer", item.customerCode);
                                startActivity(intent);
                                break;
                        }
                    }

                    @Override
                    public void onLongClick(CustomerScan item, int position) {

                    }
                });
                adapter.setCustomerScan(CustomerActivity.this, customerScans);
                rvCustomer.setAdapter(adapter);
                rvCustomer.invalidate();
            }
        });
        return list;

    }
}