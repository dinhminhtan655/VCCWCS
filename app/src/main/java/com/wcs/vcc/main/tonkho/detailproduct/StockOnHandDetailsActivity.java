package com.wcs.vcc.main.tonkho.detailproduct;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import com.wcs.vcc.api.StockOnHandDetailsParameter;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

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

public class StockOnHandDetailsActivity extends BaseActivity {
    public static final String TAG = StockOnHandDetailsActivity.class.getSimpleName();
    @BindView(R.id.lvOrderDetail)
    ListView listView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_productName)
    TextView tvProductName;
    private View.OnClickListener action;
    private StockOnHandDetailAdapter adapter;
    private int a, b, c, d, e;
    private UUID customerID, productID;
    private List<StockOnHandDetailsInfo> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_on_hand_details);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        a = b = c = d = e = 1;
        String customerIDString = getIntent().getStringExtra("customerID");
        customerID = UUID.fromString(customerIDString);

        String strProductName = getIntent().getStringExtra("productName");
        tvProductName.setText(strProductName);

        String productIDString = getIntent().getStringExtra("productID");
        productID = UUID.fromString(productIDString);

        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStockOnHandDetails(listView);
            }
        };
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStockOnHandDetails(listView);

            }
        });
        adapter = new StockOnHandDetailAdapter(this, new ArrayList<StockOnHandDetailsInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        getStockOnHandDetails(listView);
    }

    public void getStockOnHandDetails(final View view) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getStockOnHandDetails(new StockOnHandDetailsParameter(customerID, productID)).enqueue(new Callback<List<StockOnHandDetailsInfo>>() {
            @Override
            public void onResponse(Response<List<StockOnHandDetailsInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    arrayList = response.body();
                    adapter.addAll(arrayList);
                }
                refreshLayout.setRefreshing(false);
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(StockOnHandDetailsActivity.this, t, TAG, view, action);
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

    @OnClick(R.id.tv_stock_on_hand_detail_LocationNumber)
    public void sortLocation() {
        a = -a;
        Collections.sort(arrayList, new StockOnHandDetailComparator("Location", a));
        adapter.clear();
        adapter.addAll(arrayList);
    }

    @OnClick(R.id.tv_stock_on_hand_detail_Status)
    public void sortStatus() {
        b = -b;
        Collections.sort(arrayList, new StockOnHandDetailComparator("Status", b));
        adapter.clear();
        adapter.addAll(arrayList);
    }

    @OnClick(R.id.tv_stock_on_hand_detail_ProductionDate)
    public void sortProductionDate() {
        c = -c;
        Collections.sort(arrayList, new StockOnHandDetailComparator("NSX", c));
        adapter.clear();
        adapter.addAll(arrayList);
    }

    @OnClick(R.id.tv_stock_on_hand_detail_UseByDater)
    public void sortUseByDater() {
        d = -d;
        Collections.sort(arrayList, new StockOnHandDetailComparator("HSD", d));
        adapter.clear();
        adapter.addAll(arrayList);
    }

    @OnClick(R.id.tv_stock_on_hand_detail_ReceivingOrderDate)
    public void sortReceivingOrderDate() {
        e = -e;
        Collections.sort(arrayList, new StockOnHandDetailComparator("RODate", e));
        adapter.clear();
        adapter.addAll(arrayList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        return true;
    }

}
