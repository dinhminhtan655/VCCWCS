package com.wcs.vcc.main.kiemqa.metroqacheckinglistproducts;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wcs.wcs.R;import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.kiemqa.metroqacheckingproduct.MetroCheckingProductActivity;
import com.wcs.vcc.api.MetroQACheckingListProductsParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class QACheckingListProductsActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = QACheckingListProductsActivity.class.getSimpleName();
    @BindView(R.id.lvOrderDetail)
    ListView listView;
    @BindView(R.id.tv_metro_suppliers)
    TextView tvSuppliers;
    @BindView(R.id.tv_metro_list_product_order_quantity)
    TextView tvOrderQuantity;
    @BindView(R.id.tv_metro_list_product_actual_quantity)
    TextView tvActualQuantity;
    private View.OnClickListener tryAgain;
    private String reportDate;
    private int supplierID;
    private MetroListProductAdapter adapter;
    private ArrayList<Integer> listID = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_list_product);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        initUI();
    }

    private void initUI() {
        supplierID = getIntent().getIntExtra("SUPPLIER_ID", 0);
        reportDate = getIntent().getStringExtra("REPORT_DATE");
        String supplierName = String.format(Locale.US, "%s - %s", getIntent().getStringExtra("SUPPLIER_NAME"), getIntent().getIntExtra("SUPPLIER_NUMBER", 0));
        tvSuppliers.setText(supplierName);
        adapter = new MetroListProductAdapter(this, new ArrayList<QACheckingListProductsInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        getMetroQACheckingProducts(listView);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMetroQACheckingProducts(listView);
            }
        };
    }

    private void getMetroQACheckingProducts(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dismissDialog(dialog);
        }
        MyRetrofit.initRequest(this).getMetroQACheckingListProducts(new MetroQACheckingListProductsParameter(reportDate, supplierID)).enqueue(new Callback<List<QACheckingListProductsInfo>>() {
            @Override
            public void onResponse(Response<List<QACheckingListProductsInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    listID.clear();
                    float orderQuantity = 0, actualQuantity = 0;
                    for (QACheckingListProductsInfo info : response.body()) {
                        orderQuantity += info.getOrderQuantity();
                        actualQuantity += info.getActualQuantity();
                        listID.add(info.getReceivingOrderDetailID());
                    }
                    tvOrderQuantity.setText(String.format(Locale.getDefault(), "%s", orderQuantity));
                    tvActualQuantity.setText(String.format(Locale.getDefault(), "%s", actualQuantity));
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(QACheckingListProductsActivity.this, t, TAG, view, tryAgain);
                dismissDialog(dialog);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lich_lam_viec, menu);
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
            onBackPressed();
        return true;
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
        Intent intent = new Intent(this, MetroCheckingProductActivity.class);
        intent.putExtra("RO_ID", adapter.getItem(position).getReceivingOrderDetailID());
        intent.putExtra("REPORT_DATE", reportDate.split("T")[0]);
        intent.putIntegerArrayListExtra("LIST_ID", listID);
        startActivity(intent);
    }
}
