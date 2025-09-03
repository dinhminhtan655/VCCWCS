package com.wcs.vcc.main.scanvinmart;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class XdocBookingListActivity extends EmdkActivity {
    @BindView(R.id.btChooceDate)
    Button btChooseDate;
    @BindView(R.id.rv_pick_pack_ship_order)
    RecyclerView rv;
    @BindView(R.id.et_pps_order_store_number)
    EditText etStoreNumber;

    private Calendar calendar;
    private String reportDate;
    private ItemPOAdapter adapter;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_xdoc_booking_list);
        ButterKnife.bind(this);
        Utilities.showBackIcon(getSupportActionBar());
        setUpScan();
        calendar = Calendar.getInstance();

        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        reportDate = dateformat.format(calendar.getTime());
        adapter = new ItemPOAdapter(new RecyclerViewItemListener<ItemPoInfor>() {
            @Override
            public void onClick(ItemPoInfor item, int position) {
                gotoCarton(item);
            }

            @Override
            public void onLongClick(ItemPoInfor item, int position) {

            }
        });
        rv.setAdapter(adapter);

        pickPackShipOrders();

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
                ItemPoInfor selected = null;
                for (ItemPoInfor item : adapter.getItems()) {
                    if (item.getPalletID() == Integer.parseInt(data.substring(2,data.length()))) {
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
        MyRetrofit.initRequest(this).POListByDate(reportDate).enqueue(new Callback<Xdoc_Vin_POListResponse>() {
            @Override
            public void onResponse(Response<Xdoc_Vin_POListResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    adapter.replace(response.body().getOrder());
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
            Integer storeNumber =Integer.parseInt(etStoreNumber.getText().toString());
            if (adapter.getItems() != null) {
                for (ItemPoInfor item : adapter.getItems()) {
                    if (item.getPalletID() == storeNumber) {
                        gotoCarton(item);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            showMessage(e.getMessage());
        }
    }

    private void gotoCarton(ItemPoInfor item) {
        Intent intent = new Intent(XdocBookingListActivity.this, XdockCartonListActivity.class);
        intent.putExtra("DATE", reportDate);
        intent.putExtra("STORE_NUMBER", item.getPalletID());
        startActivity(intent);
    }
}
