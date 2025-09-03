package com.wcs.vcc.main.scanvcm;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wcs.wcs.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ABARegion3FActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.btnMB)
    Button btnMB;

    @BindView(R.id.btnMN)
    Button btnMN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_b_a_region3_f);
        ButterKnife.bind(this);

        btnMB.setOnClickListener(this::onClick);
        btnMN.setOnClickListener(this::onClick);
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
        Intent intent = new Intent(ABARegion3FActivity.this, ABAGroup3FActivity.class);
        intent.putExtra("region",region);
        startActivity(intent);
    }
}