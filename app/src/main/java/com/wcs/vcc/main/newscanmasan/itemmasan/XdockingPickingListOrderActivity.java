package com.wcs.vcc.main.newscanmasan.itemmasan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.ComboCustomerResult;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.xdoc.response.PaletInfor;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.PalletFromTo;
import com.wcs.vcc.preferences.SpinCusIdPref;
import com.wcs.vcc.preferences.SpinCusPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
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

public class XdockingPickingListOrderActivity extends RingScanActivity {

    @BindView(R.id.btChooseDate)
    Button btChooseDate;
    @BindView(R.id.spinScanMasan)
    Spinner spinScanMasan;
    @BindView(R.id.rvItemMasan)
    RecyclerView rvItemMasan;
    @BindView(R.id.tvSumQuantity)
    TextView tvSumQuantity;
    @BindView(R.id.tv_prev_barcode)
    TextView tv_prev_barcode;
    PaletInfor  paletInfor ;

    ArrayList<String> al = new ArrayList<String>();
    private ComboCustomerResult cusSelected;
    ArrayAdapter<ComboCustomerResult> spineradapter;

    List<ComboCustomerResult> customers;

    List<XdocPackingPickingListOrder> pickingLists = new ArrayList<>();

    ItemXdocPackingPickingListOrderAdapter adapter;

    ItemPickingList curItemPicking;
    String curBarcode20;
    private Calendar calendar;
    private String reportDate, reportDate2, strCustomerID, strCustomerNumber, strCodeItem, strCodeItem2;

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xdocking_picking_list_order);
        ButterKnife.bind(this);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }
        setUpScan();

        adapter = new ItemXdocPackingPickingListOrderAdapter(new RecyclerViewItemOrderListener<XdocPackingPickingListOrder>() {
            @Override
            public void onClick(XdocPackingPickingListOrder item, int position, int order) {
                gotoNext(item);
            }

            @Override
            public void onLongClick(XdocPackingPickingListOrder item, int position, int order) {

            }
        });


        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat dateformat2 = new SimpleDateFormat(DATE_FORMAT2);
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        reportDate = dateformat.format(calendar.getTime());
        reportDate2=dateformat2.format(calendar.getTime());
        btChooseDate.setText(reportDate);
        getCustomer();
        loadPickingList(strCustomerID,reportDate2,0,"");
    }

    @Override
    public void onData(final String data) {
        super.onData(data);
        if(data.contains("PLO")){
            String[] listCode = data.split("-");
            if(listCode.length==4){
                String ProductID = listCode[0].replaceAll("PLO","");
                String pickingListOrder = listCode[0].replaceAll("PLO","");
                String Lot = listCode[1];
                String palletFrom = listCode[2];
                String palletTo = listCode[3];

                PalletFromTo.savePalletFrom(XdockingPickingListOrderActivity.this,palletFrom);
                PalletFromTo.savePalletTo(XdockingPickingListOrderActivity.this,palletTo);


                Intent intent = new Intent(XdockingPickingListOrderActivity.this, XdocPackingScanActivity.class);
                intent.putExtra("ProductID", ProductID);
                intent.putExtra("pickingListOrder", pickingListOrder);
                intent.putExtra("curcus", strCustomerID);
                intent.putExtra("itemName", ProductID);
                intent.putExtra("Lot", Lot);
                startActivity(intent);

            }else{
                return;
            }
        }else if(data.length()>2 && data.substring(0,2).equals("PI")){
            Integer PalletIDWH = 0;
            PalletIDWH =Integer.parseInt(data.replace("PI",""));

            Intent intent = new Intent(XdockingPickingListOrderActivity.this, XdocPackingScanByLotActivity.class);
            intent.putExtra("ProductID", "");
            intent.putExtra("pickingListOrder", "");
            intent.putExtra("curcus", strCustomerID);
            intent.putExtra("itemName", "");
            intent.putExtra("Lot", "");
            intent.putExtra("PalletIDWH", PalletIDWH);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utilities.isRunning(XdockingPickingListOrderActivity.this);
    }



    private void getCustomer() {
        cusSelected = new ComboCustomerResult();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("StoreID", LoginPref.getStoreId(XdockingPickingListOrderActivity.this));
        MyRetrofit.initRequest(this).loadListCustomer(jsonObject).enqueue(new Callback<List<ComboCustomerResult>>() {
            @Override
            public void onResponse(Response<List<ComboCustomerResult>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {

                    customers = new ArrayList<>();
                    customers = response.body();
                    spineradapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, customers);
                    spinScanMasan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            SpinCusPref.SaveInt(XdockingPickingListOrderActivity.this, position);
                            cusSelected = customers.get(position);
                            strCustomerID = String.valueOf(cusSelected.getCustomerID());
                            Log.d("DUONG", strCustomerID);
                            strCustomerNumber = customers.get(position).getCustomerNumber();
                            SpinCusIdPref.saveCusID(XdockingPickingListOrderActivity.this,String.valueOf(cusSelected.getCustomerID()));
                            if(cusSelected!=null){
                                loadPickingList(strCustomerID, reportDate2,0,"");
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinScanMasan.setAdapter(spineradapter);
                    spinScanMasan.setSelection(SpinCusPref.LoadInt(XdockingPickingListOrderActivity.this));

                }
            }

            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(XdockingPickingListOrderActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không lấy được thông tin khách hàng!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });


    }
    private void loadPickingList(String strCustomerID, String reportDate, int PalletNumber,String data) {

        if (strCustomerID==null)
        {
            return;
        }

        MyRetrofit.initRequest(this).loadItemPickingListOrder(strCustomerID, reportDate, PalletNumber,data).enqueue(new Callback<XdocPackingPickingListOrderResponse>() {
            @Override
            public void onResponse(Response<XdocPackingPickingListOrderResponse> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    pickingLists = response.body().PickingListOrder;
                    if (response.body().PickingListOrder!=null && response.body().PickingListOrder.size() > 0) {
                        adapter = new ItemXdocPackingPickingListOrderAdapter(new RecyclerViewItemOrderListener<XdocPackingPickingListOrder>() {
                            @Override
                            public void onClick(XdocPackingPickingListOrder item, int position, int order) {
                                gotoNext(item);
                            }

                            @Override
                            public void onLongClick(XdocPackingPickingListOrder item, int position, int order) {

                            }
                        });

                        int Sl = 0;

                        for (XdocPackingPickingListOrder p : response.body().PickingListOrder) {
                            Sl += p.getQty();
                        }

                        tvSumQuantity.setText(String.valueOf(Sl));

                        adapter.replace(response.body().PickingListOrder);
                        rvItemMasan.setAdapter(adapter);
                    } else {
                        adapter.replace(response.body().PickingListOrder);
                        rvItemMasan.setAdapter(adapter);
                    }

                } else {
                    rvItemMasan.setAdapter(new ItemPickingListAdapter());
                }
            }

            @Override
            public void onFailure(Throwable t) {

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
                Log.d("DUONG_NE", reportDate);
                loadPickingList(strCustomerID,reportDate2,0,"");

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
        loadPickingList(strCustomerID, reportDate2,0,"");
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        loadPickingList(strCustomerID,reportDate2,0,"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPickingList(strCustomerID,reportDate2,0,"");
    }

    private void gotoNext(XdocPackingPickingListOrder curItemPicking) {
        Intent intent = new Intent(XdockingPickingListOrderActivity.this, XdocPackingScanActivity.class);
        intent.putExtra("ProductID", curItemPicking.getProductID().toString());
        intent.putExtra("pickingListOrder", curItemPicking.getPickingListOrderNumber());
        intent.putExtra("curcus", strCustomerID);
        intent.putExtra("itemName", curItemPicking.getProductName());
        intent.putExtra("Lot", curItemPicking.getLotNumber());
        intent.putExtra("reportDate", reportDate2);
        Log.d("DUONG_NE", reportDate2);
        startActivity(intent);
    }
}
