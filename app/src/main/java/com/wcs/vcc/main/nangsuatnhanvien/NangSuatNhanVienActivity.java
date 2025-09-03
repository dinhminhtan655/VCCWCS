package com.wcs.vcc.main.nangsuatnhanvien;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.api.EmployeeWorkingByDateParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NangSuatNhanVienActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = NangSuatNhanVienActivity.class.getSimpleName();
    private TextView dateTV;
    private AppCompatSpinner departmentSP;
    private AppCompatSpinner shiftSP;
    private AppCompatSpinner positionSP;
    private ListView listView;
    private Calendar calendar;
    private ProgressDialog dialog;
    private NSNVAdapter adapter;
    private DepartmentAdapter deptAdapter;
    private ShiftAdapter shiftAdapter;
    private PositionAdapter positionAdapter;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nang_suat_nhan_vien);

        initial();
    }

    private void initial() {
        mapView();
        setListener();
        snackBarView = listView;
        Utilities.showBackIcon(getSupportActionBar());

        storeId = LoginPref.getStoreId(this);

        calendar = Calendar.getInstance();
        updateMaintenanceDate(calendar.getTimeInMillis());

        adapter = new NSNVAdapter(NangSuatNhanVienActivity.this, new ArrayList<EmployeeWorkingByDate>());
        listView.setAdapter(adapter);

        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        getComboDepartmentID();
    }

    private void mapView() {
        dateTV = findViewById(R.id.nsnv_tv_date);
        departmentSP = findViewById(R.id.nsnv_department_sp);
        shiftSP = findViewById(R.id.nsnv_shift_sp);
        positionSP = findViewById(R.id.nsnv_position_sp);
        listView = findViewById(R.id.lv_nang_suat_nhan_vien);
    }

    private void setListener() {
        dateTV.setOnClickListener(this);
        departmentSP.setOnItemSelectedListener(this);
        shiftSP.setOnItemSelectedListener(this);
        positionSP.setOnItemSelectedListener(this);
    }

    private void getComboDepartmentID() {
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dismissDialog(dialog);
            return;
        }
        MyRetrofit.initRequest(this)
                .getComboDepartmentID()
                .enqueue(new Callback<List<ComboDepartmentID>>() {
                    @Override
                    public void onResponse(Response<List<ComboDepartmentID>> response, Retrofit retrofit) {
                        List<ComboDepartmentID> body = response.body();
                        if (response.isSuccess() && body != null) {
                            deptAdapter = new DepartmentAdapter(NangSuatNhanVienActivity.this, new ArrayList<>(body));
                            departmentSP.setAdapter(deptAdapter);
                            getComboShiftID();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissDialog(dialog);
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void getComboShiftID() {
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dismissDialog(dialog);
            return;
        }
        MyRetrofit.initRequest(this)
                .getComboShiftID()
                .enqueue(new Callback<List<ComboShiftID>>() {
                    @Override
                    public void onResponse(Response<List<ComboShiftID>> response, Retrofit retrofit) {
                        List<ComboShiftID> body = response.body();
                        if (response.isSuccess() && body != null) {
                            shiftAdapter = new ShiftAdapter(NangSuatNhanVienActivity.this, new ArrayList<>(body));
                            shiftSP.setAdapter(shiftAdapter);
                            getComboPositionID();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissDialog(dialog);
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void getComboPositionID() {
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dismissDialog(dialog);
            return;
        }
        MyRetrofit.initRequest(this)
                .getComboPositionID()
                .enqueue(new Callback<List<ComboPositionID>>() {
                    @Override
                    public void onResponse(Response<List<ComboPositionID>> response, Retrofit retrofit) {
                        List<ComboPositionID> body = response.body();
                        if (response.isSuccess() && body != null) {
                            positionAdapter = new PositionAdapter(NangSuatNhanVienActivity.this, new ArrayList<>(body));
                            positionSP.setAdapter(positionAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissDialog(dialog);
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void getEmployeeWorkingByDate(EmployeeWorkingByDateParameter parameter) {
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dismissDialog(dialog);
            return;
        }
        MyRetrofit.initRequest(this)
                .getEmployeeWorkingByDate(parameter)
                .enqueue(new Callback<List<EmployeeWorkingByDate>>() {
                    @Override
                    public void onResponse(Response<List<EmployeeWorkingByDate>> response, Retrofit retrofit) {
                        List<EmployeeWorkingByDate> body = response.body();
                        if (response.isSuccess() && body != null) {
                            adapter.clear();
                            adapter.addAll(body);
                        }
                        dismissDialog(dialog);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissDialog(dialog);
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void pickDate() {
        int yearNow = calendar.get(Calendar.YEAR);
        int monthOfYearNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                updateMaintenanceDate(calendar.getTimeInMillis());

            }
        }, yearNow, monthOfYearNow, dayOfMonthNow);
        datePicker.show();
    }

    private void updateMaintenanceDate(long date) {
        dateTV.setTag(Utilities.formatDateTime_yyyyMMddFromMili(date));
        dateTV.setText(Utilities.formatDate_ddMMyyyy(date));
    }

    @Override
    public void onClick(View v) {
        pickDate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected() returned: ");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void okClick(View view) {
        dialog.show();

        EmployeeWorkingByDateParameter parameter = new EmployeeWorkingByDateParameter(
                dateTV.getTag().toString(),
                deptAdapter.getItem(departmentSP.getSelectedItemPosition()).getId(),
                shiftAdapter.getItem(shiftSP.getSelectedItemPosition()).getName(),
                positionAdapter.getItem(positionSP.getSelectedItemPosition()).getId(),
                storeId
        );
        getEmployeeWorkingByDate(parameter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }
}
