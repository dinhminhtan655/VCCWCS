package com.wcs.vcc.main.phieucuatoi;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wcs.wcs.R;
import com.wcs.vcc.api.InOutAvailableForSupervisorParameter;
import com.wcs.vcc.api.MyOrderInfo;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.detailphieu.OrderDetailActivity;
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

public class PhieuCuaToiActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final String TAG = "PhieuCuaToiActivity";
    @BindView(R.id.lvOrderDetail)
    ListView listView;
    @BindView(R.id.tv_customerName)
    TextView tvCustomerName;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    private int function;
    private View.OnClickListener action;
    private PhieuCuaToiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_cua_toi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        ButterKnife.bind(this);
        if (getIntent() != null) {
            function = getIntent().getIntExtra(com.wcs.vcc.main.vo.MenuItem.MENU_ITEM_NAME, 0) == R.string.phieu_cua_toi ? 0 : 1;
        }
        if (function == 1 && getSupportActionBar() != null)
            getSupportActionBar().setTitle("Phiếu Đã Xong");
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPhieuCuaToi(listView);
            }
        });
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhieuCuaToi(listView);
            }
        };
        adapter = new PhieuCuaToiAdapter(this, new ArrayList<PhieuCuaToiInfo>());
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        if (function == 0)
            getPhieuCuaToi(listView);
        else
            getInOutAvailableForSupervisor(listView);
    }

    public void getPhieuCuaToi(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        refreshLayout.setRefreshing(false);
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getPhieuCuaToi(new MyOrderInfo(LoginPref.getInfoUser(this, LoginPref.USERNAME))).enqueue(new Callback<List<PhieuCuaToiInfo>>() {
            @Override
            public void onResponse(Response<List<PhieuCuaToiInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    tvCustomerName.setText(response.body().get(0).getCustomerName());
                }

                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(PhieuCuaToiActivity.this, t, TAG, view, action);
            }
        });
    }

    public void getInOutAvailableForSupervisor(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        refreshLayout.setRefreshing(false);
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getInOutAvailableForSupervisor(new InOutAvailableForSupervisorParameter(LoginPref.getUsername(this))).enqueue(new Callback<List<PhieuCuaToiInfo>>() {
            @Override
            public void onResponse(Response<List<PhieuCuaToiInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    tvCustomerName.setText(response.body().get(0).getCustomerName());
                }

                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(PhieuCuaToiActivity.this, t, TAG, view, action);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phieu_cua_toi, menu);
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
        return super.onOptionsItemSelected(item);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PhieuCuaToiInfo item = adapter.getItem(position);
        if (item != null) {

            Intent intent = new Intent(PhieuCuaToiActivity.this, OrderDetailActivity.class);
            intent.putExtra(ORDER_NUMBER, item.getOrderNumber());
            intent.putExtra("SCAN_TYPE", item.getScannedType());
            intent.putExtra("CUSTOMER_TYPE", item.getCustomerType());
            startActivity(intent);
        }
    }
}
