package com.wcs.vcc.main.tonkho.khachhang;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.tonkho.detailkhachhang.StockOnHandByCustomerActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class StockOnHandActivity extends BaseActivity {
    @BindView(R.id.lvOrderDetail)
    ListView listView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;

    public static final String TAG = StockOnHandActivity.class.getSimpleName();
    private View.OnClickListener action;
    private StockOnHandAdapter adapter;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_on_hand);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStockOnHand(listView);
            }
        };
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStockOnHand(listView);

            }
        });
        adapter = new StockOnHandAdapter(this, new ArrayList<StockOnHandInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StockOnHandActivity.this, StockOnHandByCustomerActivity.class);
                StockOnHandInfo item = adapter.getItem(position);
                intent.putExtra("customerID", item.CustomerID.toString());
                intent.putExtra("customerName", String.format("%s - %s", item.getCustomerNumber(), item.getCustomerName()));
                startActivity(intent);
            }
        });
        storeId = LoginPref.getStoreId(this);

        getStockOnHand(listView);
    }

    public void getStockOnHand(final View view) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getStockOnHand(storeId).enqueue(new Callback<List<StockOnHandInfo>>() {
            @Override
            public void onResponse(Response<List<StockOnHandInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    int TotalCurrentCtns = 0;
                    int TotalAfterDPCtns = 0;
                    float TotalWeight = 0;
                    int TotalLocation = 0;
                    int TotalPallet = 0;
                    int TotalExported = 0;
                    int TotalHold = 0;
                    int TotalPicked = 0;
                    for (StockOnHandInfo stockOnHandInfo : response.body()) {
                        TotalCurrentCtns += stockOnHandInfo.getTotalCurrentCtns();
                        TotalAfterDPCtns += stockOnHandInfo.getTotalAfterDPCtns();
                        TotalWeight += stockOnHandInfo.getTotalWeight();
                        TotalLocation += stockOnHandInfo.getTotalLocation();
                        TotalPallet += stockOnHandInfo.getTotalPallet();
                        TotalExported += stockOnHandInfo.getTotalExported();
                        TotalHold += stockOnHandInfo.getTotalHold();
                        TotalPicked += stockOnHandInfo.getTotalPicked();
                    }
                    ((TextView) findViewById(R.id.tv_stock_on_hand_TotalAfter)).setText(Utilities.formatNumber(TotalAfterDPCtns));
                    ((TextView) findViewById(R.id.tv_stock_on_hand_TotalCurrent)).setText(Utilities.formatNumber(TotalCurrentCtns));
                    ((TextView) findViewById(R.id.tv_stock_on_hand_TotalExported)).setText(Utilities.formatNumber(TotalExported));
                    ((TextView) findViewById(R.id.tv_stock_on_hand_TotalLocation)).setText(Utilities.formatNumber(TotalLocation));
                    ((TextView) findViewById(R.id.tv_stock_on_hand_TotalPallet)).setText(Utilities.formatNumber(TotalPallet));
                    ((TextView) findViewById(R.id.tv_stock_on_hand_TotalWeight)).setText(Utilities.formatNumber(TotalWeight));
                    ((TextView) findViewById(R.id.tv_stock_on_hand_TotalHold)).setText(Utilities.formatNumber(TotalHold));
                    ((TextView) findViewById(R.id.tv_stock_on_hand_TotalPicked)).setText(Utilities.formatNumber(TotalPicked));
                }
                refreshLayout.setRefreshing(false);
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(StockOnHandActivity.this, t, TAG, view, action);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        return true;
    }
}
