package com.wcs.vcc.main.doichieu;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit2;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.doichieu.adapter.DoiChieuAdapter;
import com.wcs.vcc.main.doichieu.model.ShiftConfirm;
import com.wcs.vcc.main.scanhang.model.CustomerScan;
import com.wcs.vcc.main.scanhang.viewmodel.AllViewModel;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewPassTotal;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoiChieuActivity extends EmdkActivity {

    @BindView(R.id.spinCustomer)
    Spinner spinCustomer;
    @BindView(R.id.ivArrowLeft)
    ImageView ivArrowLeft;
    @BindView(R.id.btChooseDate)
    Button btChooseDate;
    @BindView(R.id.ivArrowRight)
    ImageView ivArrowRight;
    @BindView(R.id.edtSoXe)
    EditText edtSoXe;
    @BindView(R.id.btnCheck)
    ImageButton btnCheck;
    @BindView(R.id.rvDoiChieu)
    RecyclerView rvDoiChieu;
    @BindView(R.id.tvSumKhay)
    TextView tvSumKhay;
    @BindView(R.id.tvSumVi)
    TextView tvSumVi;
    @BindView(R.id.tvSumPallet)
    TextView tvSumPallet;

    List<ShiftConfirm> finalShiftConfirmList;
    List<CustomerScan> customerScanList;
    AllViewModel allViewModel;

    DoiChieuAdapter doiChieuAdapter;

    private Calendar calendar;
    private String reportDate, reportDate2;

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    CustomerScan customerScan;

    private ProgressDialog progressDialog;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_chieu);
        ButterKnife.bind(this);
        Pattern pattern = Pattern.compile("\\d{2}[A-Z]{1}[-][0-9]{4,5}");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }


        allViewModel = ViewModelProviders.of(DoiChieuActivity.this).get(AllViewModel.class);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat dateformat2 = new SimpleDateFormat(DATE_FORMAT2);
        reportDate = dateformat.format(calendar.getTime());
        reportDate2 = dateformat2.format(calendar.getTime());
        btChooseDate.setText(reportDate);

        customerScanList = populateList();


        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = pattern.matcher(edtSoXe.getText().toString()).matches();
                if (b) {
//                    Toast.makeText(DoiChieuActivity.this, "correct", Toast.LENGTH_SHORT).show();
                    loadDoiChieu();

                } else {
                    Toast.makeText(DoiChieuActivity.this, "not correct", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private List<CustomerScan> populateList() {

        List<CustomerScan> list = new ArrayList<>();

        allViewModel.getAllCustomerScan(DoiChieuActivity.this).observe(this, new Observer<List<CustomerScan>>() {
            @Override
            public void onChanged(@Nullable List<CustomerScan> customerScans) {

                ArrayAdapter spinnerAdapter = new ArrayAdapter<CustomerScan>(DoiChieuActivity.this, R.layout.support_simple_spinner_dropdown_item, customerScans);

                spinCustomer.setAdapter(spinnerAdapter);

                spinCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        customerScan = customerScans.get(i);
                        Toast.makeText(DoiChieuActivity.this, customerScan.customerCode, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });
        return list;

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
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
    }


    private void loadDoiChieu() {

        progressDialog = Utilities.getProgressDialog(DoiChieuActivity.this, "Đang tải...");
        progressDialog.show();

        if (!WifiHelper.isConnected(DoiChieuActivity.this)) {
            RetrofitError.errorNoAction(DoiChieuActivity.this, new NoInternet(), TAG, view);
            Utilities.dismissDialog(progressDialog);
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("StartTime", reportDate2);
        jsonObject.addProperty("CustomerCode", customerScan.customerCode);
        jsonObject.addProperty("PowerUnitGID", edtSoXe.getText().toString());

        MyRetrofit2.initRequest2().getDoiChieu(jsonObject).enqueue(new Callback<List<ShiftConfirm>>() {
            @Override
            public void onResponse(Call<List<ShiftConfirm>> call, Response<List<ShiftConfirm>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    doiChieuAdapter = new DoiChieuAdapter(DoiChieuActivity.this, response.body(), new RecyclerViewItemOrderListener<ShiftConfirm>() {
                        @Override
                        public void onClick(ShiftConfirm item, int position, int order) {
                            switch (order) {
                                case 0:
                                    Intent intent = new Intent(DoiChieuActivity.this, DoiChieuDetailActivity.class);
                                    intent.putExtra("DeliveryDate", item.deliveryDate);
                                    intent.putExtra("CustomerID", item.customerCode);
                                    intent.putExtra("PalletID", item.palletID);
                                    startActivity(intent);
                                    break;
                            }

                        }

                        @Override
                        public void onLongClick(ShiftConfirm item, int position, int order) {

                        }
                    }, new RecyclerViewPassTotal<Integer>() {
                        @Override
                        public int totalCount(Integer item) {
                            tvSumKhay.setText(String.valueOf(item));
                            return 0;
                        }

                        @Override
                        public int totalCount2(Integer item) {
                            tvSumVi.setText(String.valueOf(item));
                            return 0;
                        }
                    });
                    rvDoiChieu.setAdapter(doiChieuAdapter);

                    int sumKhay = 0, sumVi = 0, count = 0;

                    for (ShiftConfirm s : response.body()) {
                        sumKhay += s.box_ConfirmDriver;
                        sumVi += s.qty_ConfirmDriver;
                        count++;
                    }

                    tvSumKhay.setText(String.valueOf(sumKhay));
                    tvSumVi.setText(String.valueOf(sumVi));
                    tvSumPallet.setText(String.valueOf(count));

                    Utilities.dismissDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<List<ShiftConfirm>> call, Throwable t) {
                Toast.makeText(DoiChieuActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                Utilities.dismissDialog(progressDialog);
            }
        });

    }


    @OnClick(R.id.btnConfirm)
    public void Update(View view) {

        if (doiChieuAdapter != null) {
            List<ShiftConfirm> shiftConfirmList = new ArrayList<>();
            shiftConfirmList = doiChieuAdapter.passList();
            int i = 0;
            finalShiftConfirmList = new ArrayList<>();
            for (ShiftConfirm s : shiftConfirmList) {
                finalShiftConfirmList.add(
                        new ShiftConfirm(
                                s.atM_OrderreleaseID,
                                s.routeNo,
                                s.palletID,
                                s.shipToName,
                                s.deliveryDate,
                                s.customerCode,
                                s.getBox_ConfirmDriver(),
                                s.getTongBich()
                        )
                );
                i++;

            }

            progressDialog = Utilities.getProgressDialog(DoiChieuActivity.this, "Đang xử lý...");
            progressDialog.show();

            if (!WifiHelper.isConnected(DoiChieuActivity.this)) {
                RetrofitError.errorNoAction(DoiChieuActivity.this, new NoInternet(), TAG, view);
                Utilities.dismissDialog(progressDialog);
                return;
            }

            MyRetrofit2.initRequest2().UpdateBoxConfirmDriver(finalShiftConfirmList).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().equals('"' + "OK" + '"')) {
                            Utilities.thongBaoDialog(DoiChieuActivity.this, "Cập nhật thành công");
                        } else {
                            Utilities.thongBaoDialog(DoiChieuActivity.this, response.body());
                        }
                        Utilities.dismissDialog(progressDialog);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(DoiChieuActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                    Utilities.dismissDialog(progressDialog);
                }
            });


        }

    }


}