package com.wcs.vcc.main.scanhang;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.wcs.wcs.R;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.scanhang.adapter.GroupAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class GroupScanActivity extends EmdkActivity {


    RecyclerView rvNhom;

    List<String> list = new ArrayList<>();
    String customer, region;

    GroupAdapter adapter;

    int numOfColumnRecyclerView = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_scan);

        rvNhom = findViewById(R.id.rvNhom);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            customer = bundle.getString("customer");
            region = bundle.getString("region");
        } else {
            customer = "";
            region = "";
        }

        rvNhom.setLayoutManager(new GridLayoutManager(this, numOfColumnRecyclerView));

        for (int i = 1; i <= 30; i++) {
            list.add(String.valueOf(i));
        }

        adapter = new GroupAdapter(new RecyclerViewItemListener<String>() {
            @Override
            public void onClick(String item, int position) {
                switch (position) {
                    case 0:
                        Intent i1 = new Intent(GroupScanActivity.this, Outbound2Activity.class);
                        i1.putExtra("nhom", item);
                        i1.putExtra("customer", customer);
                        i1.putExtra("region", region);
                        startActivity(i1);
                        break;
                }

            }

            @Override
            public void onLongClick(String item, int position) {

            }
        });
        adapter.setCustomerScan(GroupScanActivity.this, list);
        rvNhom.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}