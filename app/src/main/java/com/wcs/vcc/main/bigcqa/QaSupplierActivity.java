package com.wcs.vcc.main.bigcqa;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;

import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.SuppliersTodayParameter;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.bigc.Supplier;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.wcs.vcc.main.bigcqa.QaPoProductActivity.PO_DATE;
import static com.wcs.vcc.main.bigcqa.QaPoProductActivity.PO_ID;
import static com.wcs.vcc.main.bigcqa.QaPoProductActivity.PO_NUMBER;

public class QaSupplierActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private View.OnClickListener action;

    private QaSupplierAdapter adapter;
    private ListView lv;
    private Calendar calendar;
    private Button btChooseDate;
    private ImageView ivPreDate;
    private ImageView ivNextDate;
    private ArrayList<Supplier> suppliers;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers_today);
        Utilities.showBackIcon(getSupportActionBar());
        mapViewAndSetListener();


        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSuppliersToday();
            }
        };
        username = LoginPref.getUsername(this);

        calendar = Calendar.getInstance();
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(calendar.getTime()));

        suppliers = new ArrayList<>();
        adapter = new QaSupplierAdapter(this, suppliers);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        getSuppliersToday();
    }

    private void mapViewAndSetListener() {
        lv = (ListView) findViewById(R.id.lv_suppliers_today);
        btChooseDate = (Button) findViewById(R.id.bt_all_choose_date);
        ivPreDate = (ImageView) findViewById(R.id.iv_all_pre_day);
        ivNextDate = (ImageView) findViewById(R.id.iv_all_next_date);
        btChooseDate.setOnClickListener(this);
        ivPreDate.setOnClickListener(this);
        ivNextDate.setOnClickListener(this);
        snackBarView = lv;
    }


    public void getSuppliersToday() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, action);
            return;
        }
        String requestDate = Utilities.formatDateTime_yyyyMMddFromMili(calendar.getTimeInMillis());
        MyRetrofit.initRequest(this).getSuppliersToday(SuppliersTodayParameter.date(requestDate)).enqueue(new Callback<List<Supplier>>() {
            @Override
            public void onResponse(Response<List<Supplier>> response, Retrofit retrofit) {
                List<Supplier> body = response.body();
                if (response.isSuccess() && body != null) {
                    Collections.sort(body);
                    suppliers.clear();
                    suppliers.addAll(body);
                    adapter.notifyDataSetChanged();
                }

                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(QaSupplierActivity.this, t, TAG, snackBarView, action);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Supplier supplier = adapter.getItem(position);
        if (supplier != null) {
            int purchasingOrderId = supplier.getId();
            Intent intent = new Intent(this, QaPoProductActivity.class);
            intent.putExtra(PO_ID, purchasingOrderId);
            intent.putExtra(PO_NUMBER, supplier.getPurchasingOrderNumber());
            intent.putExtra(PO_DATE, btChooseDate.getText());
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btChooseDate) {
            chooseDate();
        } else if (v == ivNextDate) {
            calendar.add(Calendar.DATE, 1);
            updateUI();
        } else if (v == ivPreDate) {
            calendar.add(Calendar.DATE, -1);
            updateUI();
        }
    }

    private void chooseDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                updateUI();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void updateUI() {
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(calendar.getTimeInMillis()));
        getSuppliersToday();
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
