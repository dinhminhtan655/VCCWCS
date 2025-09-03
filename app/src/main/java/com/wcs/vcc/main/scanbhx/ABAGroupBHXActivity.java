package com.wcs.vcc.main.scanbhx;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wcs.wcs.R;import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ABAGroupBHXActivity extends EmdkActivity {

    ListView lvNhom;

    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_b_a_group_b_h_x);

        lvNhom = findViewById(R.id.lvNhom);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        for (int i = 1; i <= 30; i++){
            list.add(String.valueOf(i));
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,list);
        lvNhom.setAdapter(adapter);


        lvNhom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent i1 = new Intent(ABAGroupBHXActivity.this, ABAOutboundBHXActivity.class);
                i1.putExtra("nhom", String.valueOf(i+1));
                startActivity(i1);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
//        Const.isActivating = true;
    }


    @Override
    protected void onStop() {
//        Const.isActivating = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
