package com.wcs.vcc.main.pickship;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;import com.wcs.vcc.api.ComboCustomerResult;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.xdoc.response.CheckStatusPickPackResponse;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

    public class CheckStatusPickPackShipActivity extends EmdkActivity {
    private Spinner comboCustomer;
    private ComboCustomerResult cusSelected;
    ArrayAdapter<ComboCustomerResult> spineradapter = null;
    private String reportDate;
    List<ComboCustomerResult> customers = new ArrayList<>();
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    @BindView(R.id.btChooceDate)
    Button btChooseDate;
    private Calendar calendar;
    @BindView(R.id.rv_pick_pack_ship_order)
    RecyclerView rv;
    private PackingStatusAdateper adateper;
    private  Integer StoreNumber;
    private String ProductNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status_pick_pack_ship);
        ButterKnife.bind(this);
        Utilities.showBackIcon(getSupportActionBar());
        setUpScan();
        calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        reportDate = dateformat.format(calendar.getTime());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy2(reportDate));
        comboCustomer = findViewById(R.id.sp_list_id_customer);

        StoreNumber = getIntent().getIntExtra("STORE_NUMBER", 0);
        if(getIntent().getStringExtra("DATE")!=null)
        {
            reportDate = getIntent().getStringExtra("DATE");
        }

        getCustomer();

        adateper = new PackingStatusAdateper();

        rv.setAdapter(adateper);
    }

    private void getCustomer() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("StoreID", LoginPref.getStoreId(CheckStatusPickPackShipActivity.this));
        MyRetrofit.initRequest(this).loadListCustomer(jsonObject).enqueue(new Callback<List<ComboCustomerResult>>() {
            @Override
            public void onResponse(Response<List<ComboCustomerResult>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    customers = response.body();
                    spineradapter = new ArrayAdapter<ComboCustomerResult>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, customers);
                    comboCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            cusSelected = customers.get(position);
                            loadData();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    comboCustomer.setAdapter(spineradapter);
                }
            }
            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CheckStatusPickPackShipActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không lấy được thông tin khách hàng!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }

    @Override
    public void onData(String data) {
        super.onData(data);
        String barcodeType = "";
        if(data.length()>2){
            barcodeType = data.substring(0,2);
        }
        if(barcodeType.equals("ST")){
            StoreNumber = Integer.parseInt(data.substring(2,data.length()));
            ProductNumber = "";
            loadData();
        }
        else{
            ProductNumber = data;
            StoreNumber = 0;
            loadData();
        }
    }

    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        reportDate = Utilities.formatDateTime_yyyyMMddFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy2(reportDate));
        loadData();
    }

    @OnClick(R.id.btChooceDate)
    public void chooseDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy2(reportDate));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        loadData();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy2(reportDate));
        loadData();
    }

    private void loadData(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("varDate",reportDate.substring(0,10));
        jsonObject.addProperty("StoreID",  LoginPref.getStoreId(this));
        jsonObject.addProperty("ProductNumber",ProductNumber);
        jsonObject.addProperty("StoreNumber",StoreNumber);
        if(cusSelected!=null){
            jsonObject.addProperty("CustomerID", cusSelected.getCustomerID().toString());
        }

        MyRetrofit.initRequest(this).packingStatus(jsonObject).enqueue(new Callback<ArrayList<CheckStatusPickPackResponse>>() {
            @Override
            public void onResponse(Response<ArrayList<CheckStatusPickPackResponse>> response, Retrofit retrofit) {
                adateper.notifyDataSetChanged();
                adateper.replace(response.body());
                rv.setAdapter(adateper);
            }

            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CheckStatusPickPackShipActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không có data!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }
}
