package com.wcs.vcc.main.scanhang;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.wcs.wcs.R;
import com.wcs.vcc.utilities.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegionActivity extends AppCompatActivity implements View.OnClickListener  {


    @BindView(R.id.btnMB)
    Button btnMB;

    @BindView(R.id.btnMN)
    Button btnMN;

    String customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        btnMB.setOnClickListener(this::onClick);
        btnMN.setOnClickListener(this::onClick);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            customer = bundle.getString("customer");
        } else {
            customer = "";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMB:
                navigationGroup("MB");
                break;
            case R.id.btnMN:
                navigationGroup("MN");
                break;
        }
    }

    private void navigationGroup(String region) {
        Intent intent = new Intent(RegionActivity.this, GroupScanActivity.class);
        intent.putExtra("region",region);
        intent.putExtra("customer",customer);
        startActivity(intent);
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