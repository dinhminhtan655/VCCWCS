package com.wcs.vcc.main.packingscan;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickPackShipOrdersParameter;
import com.wcs.vcc.main.packingscan.carton.CartonsActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PickPackShipOrderByCustomerActivity extends AppCompatActivity {

    @BindView(R.id.rv_pick_pack_ship_order_by_customer)
    RecyclerView rv;
    private PickPackShipOrderAdapter adapter;
    private String orderDate;
    private int storeNumber = 0;
    private boolean isStart = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_pack_ship_order_by_customer);
        ButterKnife.bind(this);
        Utilities.showBackIcon(getSupportActionBar());
        getSupportActionBar().setTitle("Back");
        orderDate = getIntent().getStringExtra("DATE");
        storeNumber = getIntent().getIntExtra("STORE_NUMBER",0);

        adapter = new PickPackShipOrderAdapter(new RecyclerViewItemListener<PickPackShipOrder>() {
            @Override
            public void onClick(PickPackShipOrder item, int position) {
                gotoCarton(item);
            }

            @Override
            public void onLongClick(PickPackShipOrder item, int position) {

            }
        });
        rv.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        pickPackShipOrders();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void gotoCarton(PickPackShipOrder item) {
        Intent intent = new Intent(PickPackShipOrderByCustomerActivity.this, CartonsActivity.class);
        intent.putExtra("STORE_NUMBER", item.StoreNumber);
        intent.putExtra("DATE", item.DispatchingOrderDate);
        intent.putExtra("ORDER_NUMBER", item.DispatchingOrderNumber);
        intent.putExtra("CLIENT_NAME", item.CustomerClientName);
        intent.putExtra("BARCODE", item.StoreNumber_Barcode);

        startActivity(intent);
    }


    private void pickPackShipOrders() {
        PickPackShipOrdersParameter pickPackShipOrdersParameter = new PickPackShipOrdersParameter(orderDate,storeNumber);
        pickPackShipOrdersParameter.setStoreID( LoginPref.getStoreId(this));
        pickPackShipOrdersParameter.setStoreID( LoginPref.getStoreId(this));
        MyRetrofit.initRequest(this).pickPackShipOrderByCustomer(pickPackShipOrdersParameter).enqueue(new Callback<List<PickPackShipOrder>>() {
            @Override
            public void onResponse(Response<List<PickPackShipOrder>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<PickPackShipOrder> list = response.body();

                    if(list!=null && list.size()==1 && isStart){
                        gotoCarton(list.get(0));
                        isStart = false;
                    }
                    else{
                        adapter.replace(list);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
