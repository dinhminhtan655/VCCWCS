package com.wcs.vcc.main.giaonhanhoso;

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

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.NotificationParameter;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class GiaoHoSoActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final String TAG = GiaoHoSoActivity.class.getSimpleName();

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.lvOrderDetail)
    ListView listView;
    private View.OnClickListener tryAgain;
    private String userName;
    private GiaoHoSoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_ho_so);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDSDispatchingOrders(listView);
            }
        });
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDSDispatchingOrders(listView);
            }
        };
        adapter = new GiaoHoSoAdapter(this, new ArrayList<DSDispatchingOrdersInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        getDSDispatchingOrders(listView);
    }

    private void getDSDispatchingOrders(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dismissDialog(dialog);
            return;
        }
        MyRetrofit.initRequest(this).getDSDispatchingOrders(new NotificationParameter(userName)).enqueue(new Callback<List<DSDispatchingOrdersInfo>>() {
            @Override
            public void onResponse(Response<List<DSDispatchingOrdersInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dismissDialog(dialog);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(GiaoHoSoActivity.this, t, TAG, view, tryAgain);
            }
        });
    }


    @Override
    protected void onResume() {
        Const.isActivating = true;
        super.onResume();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
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
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, GiaoHoSoDetailActivity.class);
        intent.putExtra("orderID", adapter.getItem(position).getDispatchingOrderNumber());
        intent.putExtra("CUSTOMER_ID", adapter.getItem(position).getCustomerNumber().split("-")[1]);
        intent.putExtra("ORDER_TYPE", adapter.getItem(position).getOrderType());
        startActivity(intent);

    }
}
