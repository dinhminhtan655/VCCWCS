package com.wcs.vcc.main.logrecords;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ActivityLogRecordsBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LogRecordsActivity extends BaseActivity {

    ActivityLogRecordsBinding binding;
    private String userName, dateTime;

    private Calendar calendar;
    private String reportDate;

    private LogRecordAdapter adapter;
    private List<LogRecord> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogRecordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        calendar = Calendar.getInstance();
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());

        adapter = new LogRecordAdapter();
        binding.rcLogRecords.setAdapter(adapter);

        binding.tvDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));

        binding.btnSearch.setOnClickListener(v -> getLogRecords());

        binding.tvDate.setOnClickListener(v -> chooseDate());

    }


    private void getLogRecords() {

        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        userName = binding.edtUsername.getText().toString();
        dateTime = Utilities.formatDate_MMddyyyy4(binding.tvDate.getText().toString());

        if (userName.length() > 0) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Username", userName);
            jsonObject.addProperty("FromDate", dateTime);

            MyRetrofit.initRequest(LogRecordsActivity.this).getViewLogRecords(jsonObject).enqueue(new Callback<List<LogRecord>>() {
                @Override
                public void onResponse(Response<List<LogRecord>> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        if (response.body().size() > 0) {
                            list.clear();
                            list.addAll(response.body());
                            adapter.replace(list);
                            binding.rcLogRecords.setAdapter(adapter);
                        } else {
                            Toast.makeText(LogRecordsActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                        dismissDialog(dialog);
                    } else {
                        dismissDialog(dialog);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    dismissDialog(dialog);
                }
            });
        } else {
            Toast.makeText(this, "Vui lòng nhập tên tài khoản", Toast.LENGTH_SHORT).show();
            Utilities.speakingSomeThingslow("Vui lòng nhập tên tài khoản", this);
        }


    }

    private void chooseDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                binding.tvDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        return true;
    }
}