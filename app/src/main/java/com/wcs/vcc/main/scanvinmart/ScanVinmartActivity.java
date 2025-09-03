package com.wcs.vcc.main.scanvinmart;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.XDockVinInboundSupplierSummary;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ScanVinmartActivity extends EmdkActivity {
    public static final String TAG = "ScanVinmartActivity";

    @BindView(R.id.btChooseDate)
    Button btChooseDate;
    @BindView(R.id.rv_vinmart)
    RecyclerView rv_vinmart;
    @BindView(R.id.tvSumBookingReceiving)
    TextView tvSumBookingReceiving;
    @BindView(R.id.tvSumActualReceiving)
    TextView tvSumActualReceiving;
    @BindView(R.id.tvSumDuThieuReceiving)
    TextView tvSumDuThieuReceiving;
    @BindView(R.id.tvPercentReceiving)
    TextView tvPercentReceiving;

    private ScanVinmartAdapter adapter;

    private Calendar calendar;
    private String reportDate, reportDate2;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_vinmart);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }
        calendar = Calendar.getInstance();

        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat dateformat2 = new SimpleDateFormat(DATE_FORMAT2);
        reportDate = dateformat.format(calendar.getTime());
        reportDate2 = dateformat2.format(calendar.getTime());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));


        getDataVinMart();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Const.isActivating = true;
        getDataVinMart();
    }

    private void getDataVinMart() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("Received_Date", reportDate2);

        MyRetrofit.initRequest(ScanVinmartActivity.this).loadXDockVinInboundSupplierSummary(jsonObject).enqueue(new Callback<List<XDockVinInboundSupplierSummary>>() {
            @Override
            public void onResponse(final Response<List<XDockVinInboundSupplierSummary>> response, Retrofit retrofit) {
                double sumBooking = 0;
                double sumActual = 0;
                double sumThieu = 0;
                double sumPercent = 0;
                if (response.isSuccess() && response != null) {
                    adapter = new ScanVinmartAdapter(new RecyclerViewItemListener<XDockVinInboundSupplierSummary>() {
                        @Override
                        public void onClick(XDockVinInboundSupplierSummary item, int position) {
                            Intent i = new Intent(ScanVinmartActivity.this, ViewVinmartDetailActivity.class);
                            i.putExtra("suppliercode", response.body().get(position).Supplier_Code);
                            i.putExtra("suppliername", response.body().get(position).Supplier_Name);
                            i.putExtra("date", reportDate);
                            i.putExtra("date2", reportDate2);
                            startActivity(i);
                        }

                        @Override
                        public void onLongClick(XDockVinInboundSupplierSummary item, int position) {

                        }
                    }, response.body());

                    rv_vinmart.setAdapter(adapter);
                    adapter.replace(response.body());
                    for (int i = 0; i < response.body().size(); i++) {
                        sumBooking += Double.parseDouble(response.body().get(i).Booking);
                        sumActual += Double.parseDouble(response.body().get(i).Actual);
                    }

                    sumThieu = sumBooking - sumActual;

                    sumPercent = (sumThieu * 100) / sumBooking;

                    tvSumBookingReceiving.setText(String.valueOf(sumBooking));
                    tvSumActualReceiving.setText(String.valueOf(sumActual));
                    tvSumDuThieuReceiving.setText(String.valueOf(sumThieu));

                    NumberFormat formatter = new DecimalFormat("#0.00");


                    tvPercentReceiving.setText(String.valueOf(formatter.format(sumPercent)) + "%");

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
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
                try {
                    String text = newText;
                    adapter.filter(text);
                } catch (Exception e) {
                    Log.e("er", e + "");
                }
                return false;
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.view_vin_menu, menu);
        setupActionSearch(menu);
        return true;
    }

    @OnClick(R.id.btChooseDate)
    public void chooseDate() {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                getDataVinMart();

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getDataVinMart();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getDataVinMart();
    }

    @Override
    public void onData(String data) {
        super.onData(data);
    }


    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }


}
