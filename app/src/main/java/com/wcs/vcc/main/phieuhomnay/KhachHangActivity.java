package com.wcs.vcc.main.phieuhomnay;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.InOutToDayUnfinishedParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.vo.Group;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class KhachHangActivity extends BaseActivity {
    public static final String TAG = KhachHangActivity.class.getSimpleName();
    public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    public static final String DATE = "date";
    @BindView(R.id.lvOrderDetail)
    ListView listView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_total_weight)
    TextView tvTotalWeight;
    @BindView(R.id.btChooseDate)
    Button btChooseDate;
    private String userName;
    private View.OnClickListener action;
    private KhachHangAdapter adapter;
    private int warehouseId;
    private MenuItem item_kho;
    private Calendar calendar;
    private String reportDate;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        warehouseId = LoginPref.getWarehouseID(this);
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        storeId = LoginPref.getStoreId(this);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhieuHomNayByCustomer(listView, warehouseId);
            }
        };

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPhieuHomNayByCustomer(listView, warehouseId);
            }
        });

        adapter = new KhachHangAdapter(this, new ArrayList<InOutToDayUnfinishedInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KhachHangActivity.this, HomNayActivity.class);
                InOutToDayUnfinishedInfo item = adapter.getItem(position);
                if (item != null) {
                    intent.putExtra(CUSTOMER_ID, item.getCustomerID().toString());
                    intent.putExtra(CUSTOMER_NAME, String.format("%s  -  %s", item.getCustomerName(), item.getCustomerNumber()));
                    intent.putExtra(DATE, calendar.getTimeInMillis());
                    startActivity(intent);
                }
            }
        });

        boolean groupDocument = Group.isEqualGroup(LoginPref.getPositionGroup(getApplicationContext()), Group.DOCUMENT);
        if (groupDocument) {
            warehouseId = 4;
        }
        calendar = Calendar.getInstance();
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));

        getPhieuHomNayByCustomer(listView, warehouseId);
    }

    public void getPhieuHomNayByCustomer(final View view, int wareHouseID) {
        reportDate = reportDate.split("T")[0];
        adapter.clear();
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        tvTotalWeight.setText("0");
        refreshLayout.setRefreshing(false);

        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getPhieuCustomer(new InOutToDayUnfinishedParameter(wareHouseID, userName, reportDate, storeId)).enqueue(new Callback<List<InOutToDayUnfinishedInfo>>() {
            @Override
            public void onResponse(Response<List<InOutToDayUnfinishedInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    float totalWeight = 0;
                    for (InOutToDayUnfinishedInfo info : response.body())
                        totalWeight += info.getTotalWeight();
                    tvTotalWeight.setText(NumberFormat.getInstance().format(totalWeight));
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(KhachHangActivity.this, t, TAG, view, action);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (storeId == com.wcs.vcc.main.vo.Store.ABA_SAI_GON) {
            getMenuInflater().inflate(R.menu.aba_sai_gon_today, menu);

        } else {
            getMenuInflater().inflate(R.menu.aba_ha_noi_today, menu);
        }
        item_kho = menu.findItem(R.id.action_menu);
        if (warehouseId == 0) {
            menu.findItem(R.id.action_all).setChecked(true);
            item_kho.setTitle(R.string.tat_ca);
        } else if (warehouseId == 1) {
            menu.findItem(R.id.action_123).setChecked(true);
            item_kho.setTitle(R.string.warehouse_123);
        } else if (warehouseId == 3) {
            menu.findItem(R.id.action_ha_noi).setChecked(true);
            item_kho.setTitle(R.string.ha_noi);
        }
        setupActionSearch(menu);

        return true;
    }

    private void setupActionSearch(Menu menu) {
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
    }

    private void doSearch(String keyword) {
        adapter.getFilter().filter(keyword);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        else if (itemId == R.id.action_all) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getPhieuHomNayByCustomer(listView, warehouseId = 0);
                item_kho.setTitle(getResources().getString(R.string.tat_ca));
            }
        } else if (itemId == R.id.action_123) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getPhieuHomNayByCustomer(listView, warehouseId = 1);
                item_kho.setTitle(R.string.warehouse_123);

            }
        } else if (itemId == R.id.action_ha_noi) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getPhieuHomNayByCustomer(listView, warehouseId = 3);
                item_kho.setTitle(R.string.ha_noi);
            }
        }
//        else if (itemId == R.id.action_bigc) {
//            if (!item.isChecked()) {
//                item.setChecked(true);
//                getPhieuHomNayByCustomer(listView, warehouseId = 5);
//                item_kho.setTitle(getResources().getString(R.string.bigc));
//            }
//        } else if (itemId == R.id.action_vinmark) {
//            if (!item.isChecked()) {
//                item.setChecked(true);
//                getPhieuHomNayByCustomer(listView, warehouseId = 7);
//                item_kho.setTitle(getResources().getString(R.string.vinmart));
//            }
//        }
        return true;
    }

    @OnClick(R.id.btChooseDate)
    public void chooseDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                getPhieuHomNayByCustomer(listView, warehouseId);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getPhieuHomNayByCustomer(listView, warehouseId);
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getPhieuHomNayByCustomer(listView, warehouseId);
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


}
