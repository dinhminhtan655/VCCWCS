package com.wcs.vcc.main.doichieu.difference_check;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import com.wcs.vcc.main.doichieu.difference_check.current.DifferenceCheckCurrentActivity;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DifferenceCheckActivity extends ShowHomeButtonActivity {

    private RecyclerView rv;
    private DifferenceCheckAdapter adapter;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difference_check);

        storeId = LoginPref.getStoreId(this);

        rv = findViewById(R.id.lv_difference_check);
        adapter = new DifferenceCheckAdapter(new RecyclerViewItemListener<DifferenceCheck>() {
            @Override
            public void onClick(DifferenceCheck item, int position) {
                Intent intent = new Intent(DifferenceCheckActivity.this, DifferenceCheckCurrentActivity.class);
                intent.putExtra("PRODUCT_ID", item.ProductID.toString());
                intent.putExtra("PRODUCT_NAME", item.ProductName);
                intent.putExtra("PRODUCT_NUMBER", item.ProductNumber);
                startActivity(intent);
            }

            @Override
            public void onLongClick(DifferenceCheck item, int position) {

            }
        });
        rv.setAdapter(adapter);

        getDifferenceCheck();
    }

    private void getDifferenceCheck() {
        MyRetrofit.initRequest(this).getDifferenceCheck(storeId).enqueue(new Callback<List<DifferenceCheck>>() {
            @Override
            public void onResponse(Response<List<DifferenceCheck>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    adapter.replace(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
}
