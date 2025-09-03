package com.wcs.vcc.main.scanvinmart;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.XDockVinOutboundPackingSummaryView;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.wcs.vcc.main.scanvinmart.ScanVinmartActivity.DATE_FORMAT2;

public class PackingReviewActivity extends AppCompatActivity {
    public static final String TAG = "PackingReviewActivity";

    @BindView(R.id.btChooseDate)
    Button btChooseDate;
    @BindView(R.id.tvSumBooking)
    TextView tvSumBooking;
    @BindView(R.id.tvSumSlChia)
    TextView tvSumSlChia;
    @BindView(R.id.tvSumSlScan)
    TextView tvSumSlScan;
    @BindView(R.id.rvVinmartPackingSummaryView)
    RecyclerView rvVinmartPackingSummaryView;

    PackingReviewAdapter adapter;
    private Calendar calendar;
    private String reportDate, reportDate2, userName;

    int i = 0;

    public static final String DATE_FORMAT = "yyyy/MM/dd";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_review);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        userName = LoginPref.getUsername(PackingReviewActivity.this);


        calendar = Calendar.getInstance();
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        SimpleDateFormat dateformat2 = new SimpleDateFormat(DATE_FORMAT2);
        reportDate2 = dateformat2.format(calendar.getTime());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));

        loadPackingReviewSummaryView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPackingReviewSummaryView() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("Delivery_Date", reportDate2);
        jsonObject.addProperty("User_Chia_Item", userName);

        MyRetrofit.initRequest(PackingReviewActivity.this).loadXDockVinOutboundPackingSummaryView(jsonObject).enqueue(new Callback<List<XDockVinOutboundPackingSummaryView>>() {
            @Override
            public void onResponse(Response<List<XDockVinOutboundPackingSummaryView>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null){
                    adapter = new PackingReviewAdapter(new RecyclerViewItemListener<XDockVinOutboundPackingSummaryView>() {
                        @Override
                        public void onClick(XDockVinOutboundPackingSummaryView item, int position) {
                            Intent intent = new Intent(PackingReviewActivity.this, PackingReview2Activity.class);
                            intent.putExtra("reportdate", reportDate);
                            intent.putExtra("reportdate2", reportDate2);
                            intent.putExtra("storecode", item.Store_Code);
                            intent.putExtra("palletid", item.Pallet_ID);
                            startActivity(intent);
                        }

                        @Override
                        public void onLongClick(XDockVinOutboundPackingSummaryView item, int position) {

                        }
                    });
                    adapter.replace(response.body());
                    rvVinmartPackingSummaryView.setAdapter(adapter);

                    int sumBooking = 0;
                    int sumSlChia = 0;
                    int sumSLScan = 0;

                    for (XDockVinOutboundPackingSummaryView x : response.body()){
                        sumBooking += (int)x.Booking;
                        sumSlChia += (int)x.SLChia;
                        sumSLScan += Integer.parseInt(x.SLScan);
                        i++;
                    }

                    tvSumBooking.setText(String.valueOf(sumBooking));
                    tvSumSlChia.setText(String.valueOf(sumSlChia));
                    tvSumSlScan.setText(String.valueOf(sumSLScan));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(PackingReviewActivity.this, "Kiểm tra lại mạng!", Toast.LENGTH_SHORT).show();
            }
        });
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
                loadPackingReviewSummaryView();

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
        loadPackingReviewSummaryView();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        loadPackingReviewSummaryView();
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
