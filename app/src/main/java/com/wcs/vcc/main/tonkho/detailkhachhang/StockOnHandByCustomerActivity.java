package com.wcs.vcc.main.tonkho.detailkhachhang;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.StockOnHandByCustomerParameter;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.tonkho.detailproduct.StockOnHandDetailsActivity;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class StockOnHandByCustomerActivity extends BaseActivity {
    public static final String TAG = StockOnHandByCustomerActivity.class.getSimpleName();
    @BindView(R.id.lvOrderDetail)
    ListView listView;
    @BindView(R.id.tv_customerName)
    TextView cusName;
    @BindView(R.id.tv_stock_on_hand_by_customer_total)
    TextView tvTotal;
    private View.OnClickListener action;
    private SOHByCustomerAdapter adapter;
    private UUID customerID;
    private List<StockOnHandByCustomerInfo> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_on_hand_by_customer);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        cusName.setText(getIntent().getStringExtra("customerName"));

        String customerIDString = getIntent().getStringExtra("customerID");
        customerID = UUID.fromString(customerIDString);

        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStockOnHandByCustomer(listView);
            }
        };
        adapter = new SOHByCustomerAdapter(this, new ArrayList<StockOnHandByCustomerInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StockOnHandByCustomerActivity.this, StockOnHandDetailsActivity.class);
                StockOnHandByCustomerInfo item = adapter.getItem(position);
                intent.putExtra("customerID", customerID.toString());
                intent.putExtra("productID", item.ProductID.toString());
                intent.putExtra("productName", item.getProductName());
                startActivity(intent);
            }
        });
        getStockOnHandByCustomer(listView);
    }



    public void getStockOnHandByCustomer(final View view) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getStockOnHandByCustomer(new StockOnHandByCustomerParameter(customerID)).enqueue(new Callback<List<StockOnHandByCustomerInfo>>() {
            @Override
            public void onResponse(Response<List<StockOnHandByCustomerInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                arrayList.clear();
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    arrayList = response.body();
                    adapter.addAll(arrayList);
                    int total = 0;
                    for (StockOnHandByCustomerInfo info : response.body())
                        total++;
                    tvTotal.setText(NumberFormat.getInstance().format(total));
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(StockOnHandByCustomerActivity.this, t, TAG, view, action);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.giao_ho_so, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doSearch(newText);
                return true;
            }
        });
        return true;
    }

    private void doSearch(String keyword) {
        adapter.getFilter().filter(keyword);
    }

    @OnClick(R.id.tv_stock_on_hand_by_customer_productID)
    public void sortProductID() {
        t = -t;
        if (arrayList.size() > 0) {
            Collections.sort(arrayList, new StockByCustomerComparator("ProductID", t));
            adapter.clear();
            adapter.addAll(arrayList);
            Log.e(TAG, "sortProductID: " + new Gson().toJson(arrayList));
        }
    }

    private int i = 1, t = 1;

    @OnClick(R.id.tv_stock_on_hand_by_customer_productName)
    public void sortProductName() {
        i = -i;
        if (arrayList.size() > 0) {
            Collections.sort(arrayList, new StockByCustomerComparator("ProductName", i));
            adapter.clear();
            adapter.addAll(arrayList);
            Log.e(TAG, "sortProductName: " + new Gson().toJson(arrayList));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        return true;
    }
}
