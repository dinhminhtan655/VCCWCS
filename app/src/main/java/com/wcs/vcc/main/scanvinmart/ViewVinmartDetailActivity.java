package com.wcs.vcc.main.scanvinmart;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.XDockVinInboundWeightReceiveViewDetail;
import com.wcs.vcc.utilities.Utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ViewVinmartDetailActivity extends AppCompatActivity {
    public static final String TAG = "ViewVinmartDetailActivity";


//    @BindView(R.id.btChooseDate)
//    Button btChooseDate;
    private Calendar calendar;
    private String reportDate, reportDate2, strSupplierCode;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    ViewVinmartDetailAdapter adapter;

    @BindView(R.id.tvSupplierCode)
    TextView tvSupplierCode;
    @BindView(R.id.tvSupplierName)
    TextView tvSupplierName;
    @BindView(R.id.tvSumBookingReceiving)
    TextView tvSumBookingReceiving;
    @BindView(R.id.tvSumDuThieuReceiving)
    TextView tvSumDuThieuReceiving;
    @BindView(R.id.tvSumActualReceiving)
    TextView tvSumActualReceiving;
    @BindView(R.id.tvPercentReceiving)
    TextView tvPercentReceiving;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.rv_vinmart)
    RecyclerView rv_vinmart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vinmart_detail);
        ButterKnife.bind(this);
        Utilities.showBackIcon(getSupportActionBar());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }
//        calendar = Calendar.getInstance();
//        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
//        SimpleDateFormat dateformat2 = new SimpleDateFormat(DATE_FORMAT2);
//        reportDate = dateformat.format(calendar.getTime());
//        reportDate2 = dateformat2.format(calendar.getTime());
//        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));


        Intent b = getIntent();
        if (b != null) {
            strSupplierCode = b.getStringExtra("suppliercode");
            tvSupplierCode.setText(strSupplierCode);
            tvSupplierName.setText(b.getStringExtra("suppliername"));
            reportDate2 = b.getStringExtra("date2");
            reportDate = b.getStringExtra("date");
            tvDate.setText(reportDate);
        } else {
            tvSupplierCode.setText(b.getStringExtra("N/A"));
            tvSupplierName.setText(b.getStringExtra("N/A"));
            tvDate.setText("N/A");
        }

        loadSupplierDetails();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadSupplierDetails() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Received_Date", reportDate2);
        jsonObject.addProperty("Supplier_Code", strSupplierCode);

        MyRetrofit.initRequest(ViewVinmartDetailActivity.this).loadXDockVinInboundSupplierDetails(jsonObject).enqueue(new Callback<List<XDockVinInboundWeightReceiveViewDetail>>() {
            @Override
            public void onResponse(Response<List<XDockVinInboundWeightReceiveViewDetail>> response, Retrofit retrofit) {

                double sumBooking = 0;
                double sumActual = 0;
                double sumThieu = 0;
                double sumPercent = 0;

                if (response.isSuccess() && response.body() != null) {
                    adapter = new ViewVinmartDetailAdapter();
                    adapter.replace(response.body());
                    rv_vinmart.setAdapter(adapter);
                    for (int i = 0; i < response.body().size(); i++) {
                        sumBooking += Double.parseDouble(String.valueOf(response.body().get(i).Booking));
                        sumActual += Double.parseDouble(String.valueOf(response.body().get(i).Actual));
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

            }
        });
    }


//    @OnClick(R.id.btChooseDate)
//    public void chooseDate() {
//
//        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                calendar.set(year, monthOfYear, dayOfMonth);
//                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
//                reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
//                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
//                loadSupplierDetails();
//
//            }
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//    }
//
//    @OnClick(R.id.ivArrowLeft)
//    public void previousDay() {
//        calendar.add(Calendar.DATE, -1);
//        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
//        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
//        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
//        loadSupplierDetails();
//    }
//
//    @OnClick(R.id.ivArrowRight)
//    public void nextDay() {
//        calendar.add(Calendar.DATE, 1);
//        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
//        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
//        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
//        loadSupplierDetails();
//    }
}
