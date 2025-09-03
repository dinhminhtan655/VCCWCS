package com.wcs.vcc.main.bigc;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wcs.wcs.R;import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.ProductsOfStoreParameter;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ProductsOfStoreActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final int RC_UPDATE_PRODUCT = 101;
    public static final String CURRENT_POSITION = "current_position";
    private View.OnClickListener action;

    private ProductsOfStoreAdapter adapter;
    private ListView lv;
    private int purchasingOrderId;
    private int salesOrderId;
    private TextView tvHeader;
    public static List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_of_store);
        Utilities.showBackIcon(getSupportActionBar());

        mapViewAndSetListener();

        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProductsOfStore();
            }
        };

        adapter = new ProductsOfStoreAdapter(this, new ArrayList<Product>());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        String header = getIntent().getStringExtra(SuppliersTodayActivity.HEADER);
        tvHeader.setText(header);
        purchasingOrderId = getIntent().getIntExtra(SuppliersTodayActivity.PURCHASING_ORDER_ID, 0);
        salesOrderId = getIntent().getIntExtra(StoresOfSupplierActivity.SALES_ORDER_ID, 0);
    }

    private void mapViewAndSetListener() {
        lv = (ListView) findViewById(R.id.lv_products_of_store);
        tvHeader = (TextView) findViewById(R.id.tv_products_of_store_header);
        snackBarView = lv;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductsOfStore();

    }

    public void getProductsOfStore() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, action);
            return;
        }
        MyRetrofit.initRequest(this).getProductsOfStore(new ProductsOfStoreParameter(purchasingOrderId, salesOrderId)).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Response<List<Product>> response, Retrofit retrofit) {
                products = response.body();
                if (response.isSuccess() && products != null) {
                    Collections.sort(products);
                    adapter.clear();
                    adapter.addAll(products);
                }

                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(ProductsOfStoreActivity.this, t, TAG, snackBarView, action);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product product = adapter.getItem(position);
        int index = products.indexOf(product);
        if (product != null) {
            Intent intent = new Intent(this, ProductUpdateActivity.class);
            intent.putExtra(SuppliersTodayActivity.NEXT_ACTIVITY_HEADER, getIntent().getStringExtra(SuppliersTodayActivity.NEXT_ACTIVITY_HEADER));
            intent.putExtra(CURRENT_POSITION, index);
            startActivityForResult(intent, RC_UPDATE_PRODUCT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_UPDATE_PRODUCT && resultCode == RESULT_OK) {
            getProductsOfStore();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
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

}
