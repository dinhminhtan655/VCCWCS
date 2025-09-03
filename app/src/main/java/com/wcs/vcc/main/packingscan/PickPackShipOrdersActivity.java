package com.wcs.vcc.main.packingscan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.ComboCustomerResult;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickPackShipOrdersParameter;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
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

public class PickPackShipOrdersActivity extends EmdkActivity {

    @BindView(R.id.btChooceDate)
    Button btChooseDate;
    @BindView(R.id.rv_pick_pack_ship_order)
    RecyclerView rv;
    @BindView(R.id.et_pps_order_store_number)
    EditText etStoreNumber;

    private Calendar calendar;
    private String reportDate;
    private PickPackShipOrderAdapter adapter;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    private Spinner comboCustomer;
    List<ComboCustomerResult> customers = new ArrayList<>();
    ArrayAdapter<ComboCustomerResult> spineradapter = null;
    private ComboCustomerResult cusSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_pack_ship_orders);
        ButterKnife.bind(this);
        Utilities.showBackIcon(getSupportActionBar());
        setUpScan();
        calendar = Calendar.getInstance();
        comboCustomer = findViewById(R.id.sp_list_id_customer);
        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        reportDate = dateformat.format(calendar.getTime());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getCustomer();

        adapter = new PickPackShipOrderAdapter(new RecyclerViewItemListener<PickPackShipOrder>() {
            @Override
            public void onClick(PickPackShipOrder item, int position) {
                gotoCarton(item);
            }

            @Override
            public void onLongClick(PickPackShipOrder item, int position) {

            }
        });
        rv.setAdapter(adapter);

        pickPackShipOrders();

    }
    private void getCustomer() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("StoreID", LoginPref.getStoreId(PickPackShipOrdersActivity.this));
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
                            pickPackShipOrders();
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
                AlertDialog.Builder alert = new AlertDialog.Builder(PickPackShipOrdersActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không lấy được thông tin khách hàng!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        pickPackShipOrders();
    }

    @Override
    public void onData(String data) {
        super.onData(data);
        try {
            if (adapter.getItems() != null) {
                PickPackShipOrder selected = null;
                for (PickPackShipOrder item : adapter.getItems()) {
                    if (item.StoreNumber_Barcode.equalsIgnoreCase(data)) {
                        selected = item;
                        break;
                    }
                }
                if (selected != null) {
                    gotoCarton(selected);
                } else {
                    Toast.makeText(getApplicationContext(), "Không tìm thấy Order với barcode " + data, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            showMessage(e.getMessage());
        }
    }


    private void pickPackShipOrders() {
        PickPackShipOrdersParameter pickPackShipOrdersParameter = new PickPackShipOrdersParameter();
        pickPackShipOrdersParameter.setVarDate(reportDate);
        if(cusSelected!=null){
            pickPackShipOrdersParameter.setCustomerID(cusSelected.getCustomerID());
        }

        MyRetrofit.initRequest(this).loadOrderByCustomer(pickPackShipOrdersParameter).enqueue(new Callback<List<PickPackShipOrder>>() {
            @Override
            public void onResponse(Response<List<PickPackShipOrder>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    adapter.replace(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @OnClick(R.id.btChooceDate)
    public void chooseDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                pickPackShipOrders();
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
        pickPackShipOrders();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        pickPackShipOrders();
    }

    public void searchStore(View view) {
        try {
            int storeNumber = Integer.parseInt(etStoreNumber.getText().toString());
            if (adapter.getItems() != null) {
                for (PickPackShipOrder item : adapter.getItems()) {
                    if (item.StoreNumber == storeNumber) {
                        gotoCarton(item);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            showMessage(e.getMessage());
        }
    }

    private void gotoCarton(PickPackShipOrder item) {
        Intent intent = new Intent(PickPackShipOrdersActivity.this, PickPackShipOrderByCustomerActivity.class);
        intent.putExtra("DATE", item.DispatchingOrderDate);
        intent.putExtra("STORE_NUMBER", item.StoreNumber);
        startActivity(intent);
    }
}
