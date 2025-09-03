package com.wcs.vcc.main.newscanmasan.itemmasan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.ScanMasanActivity;
import com.wcs.vcc.preferences.DatePref;
import com.wcs.vcc.preferences.ItemCodePref;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.SpinCusIdPref;
import com.wcs.vcc.preferences.SpinCusPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Const;
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

public class ItemMasanActivity extends RingScanActivity {

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

    ArrayList<String> al = new ArrayList<String>();
    private ComboCustomerResult cusSelected;
    ArrayAdapter<ComboCustomerResult> spineradapter;

    List<ComboCustomerResult> customers;

    List<ItemPickingList> pickingLists = new ArrayList<>();

    ItemPickingListAdapter adapter;

    ItemPickingList curItemPicking;
    String curBarcode20;
    private Calendar calendar;
    private String reportDate, reportDate2, strCustomerID, strCustomerNumber, strCodeItem, strCodeItem2;

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_masan);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }
        setUpScan();
        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat dateformat2 = new SimpleDateFormat(DATE_FORMAT2);
        calendar = Calendar.getInstance();
        getCustomer();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Utilities.isRunning(ItemMasanActivity.this);
    }



    private void getCustomer() {
        cusSelected = new ComboCustomerResult();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("StoreID", LoginPref.getStoreId(ItemMasanActivity.this));
        MyRetrofit.initRequest(this).loadListCustomer(jsonObject).enqueue(new Callback<List<ComboCustomerResult>>() {
            @Override
            public void onResponse(Response<List<ComboCustomerResult>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {

                    customers = new ArrayList<>();
                    customers = response.body();
                    spineradapter = new ArrayAdapter<ComboCustomerResult>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, customers);
                    spinScanMasan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            SpinCusPref.SaveInt(ItemMasanActivity.this, position);
                            cusSelected = customers.get(position);
                            strCustomerID = String.valueOf(cusSelected.getCustomerID());
                            strCustomerNumber = customers.get(position).getCustomerNumber();
                            SpinCusIdPref.saveCusID(ItemMasanActivity.this,String.valueOf(cusSelected.getCustomerID()));
                            if(cusSelected!=null){
                                loadPickingList(strCustomerID, reportDate2);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinScanMasan.setAdapter(spineradapter);
                    spinScanMasan.setSelection(SpinCusPref.LoadInt(ItemMasanActivity.this));

                }
            }

            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ItemMasanActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không lấy được thông tin khách hàng!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }
    private void loadPickingList(String strCustomerID, String reportDate2) {
        if (strCustomerID==null || reportDate2==null)
        {
            return;
        }

        MyRetrofit.initRequest(this).loadItemPickingList(strCustomerID, reportDate2, LoginPref.getUsername(ItemMasanActivity.this)).enqueue(new Callback<ParentItemPickingList>() {
            @Override
            public void onResponse(Response<ParentItemPickingList> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    pickingLists = response.body().PickingList;
                    if (response.body().PickingList.size() > 0) {
                        adapter = new ItemPickingListAdapter(new RecyclerViewItemOrderListener<ItemPickingList>() {
                            @Override
                            public void onClick(ItemPickingList item, int position, int order) {
                                switch (order) {
                                    case 0:
                                        al.clear();
                                        curBarcode20 = "";
                                        String curBarcodeSplit[] = item.ProductNumber.split("-");
                                        for (String s: curBarcodeSplit){
                                            al.add(s);
                                        }
                                        curBarcode20 = curBarcodeSplit[al.size() - 1];
                                        gotoNext(item,curBarcode20);
                                        break;
                                }
                            }

                            @Override
                            public void onLongClick(ItemPickingList item, int position, int order) {

                            }
                        });

                        int Sl = 0;

                        for (ItemPickingList p : response.body().PickingList) {
                            Sl += p.Quantity;
                        }

                        tvSumQuantity.setText(String.valueOf(Sl));

                        adapter.replace(response.body().PickingList);
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

    @Override
    public void onData(String data) {
        super.onData(data);

        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        if (pickingLists != null && pickingLists.size() > 0) {
            strCodeItem = data;

            if(strCodeItem.substring(0,2).equals("PI")){
                List<ItemPickingList> pickList = new ArrayList<>();

                for (int i = 0; i < pickingLists.size(); i++) {
                    int PalletNumber = Integer.parseInt(data.replace("PI",""));
                    if (PalletNumber == pickingLists.get(i).PalletNumber) {
                        pickList.add(new ItemPickingList(pickingLists.get(i).OperationGroupDefinitionID, pickingLists.get(i).OperationGroupName, pickingLists.get(i).LoginName,
                                pickingLists.get(i).ProductNumber, pickingLists.get(i).ProductName, pickingLists.get(i).Quantity, pickingLists.get(i).NetQty,pickingLists.get(i).IsWeightingRequire,pickingLists.get(i).XdocPickingListID,pickingLists.get(i).PalletNumber));

                    }
                }

                if (pickList.size() == 0) {
                    Utilities.speakingSomeThing("Không phải Lô này!", ItemMasanActivity.this);
                } else if (pickList.size() == 1) {
                    Utilities.speakingSomeThing(pickList.get(0).ProductName + "!!!" + pickList.get(0).Quantity + "gói", ItemMasanActivity.this);
                    curItemPicking = pickList.get(0);
                    strCodeItem2 = data;
                    gotoNext(curItemPicking,strCodeItem2);
                }
            }
            else{
                List<ItemPickingList> pickList = new ArrayList<>();

                for (int i = 0; i < pickingLists.size(); i++) {
                    if (data.contains(pickingLists.get(i).ProductNumber)) {
                        pickList.add(new ItemPickingList(pickingLists.get(i).OperationGroupDefinitionID, pickingLists.get(i).OperationGroupName, pickingLists.get(i).LoginName,
                                pickingLists.get(i).ProductNumber, pickingLists.get(i).ProductName, pickingLists.get(i).Quantity, pickingLists.get(i).NetQty,pickingLists.get(i).IsWeightingRequire,pickingLists.get(i).XdocPickingListID,pickingLists.get(i).PalletNumber));

                    }
                }

                if (pickList.size() == 0) {
                    Utilities.speakingSomeThing("Sản phẩm không tồn tại!", ItemMasanActivity.this);
                } else if (pickList.size() == 1) {
                    Utilities.speakingSomeThing(pickList.get(0).ProductName + "!!!" + pickList.get(0).Quantity + "gói", ItemMasanActivity.this);
                    curItemPicking = pickList.get(0);
                    strCodeItem2 = data;
                    gotoNext(curItemPicking,strCodeItem2);
                }
            }

        }
    }

    private void gotoNext(ItemPickingList curItemPicking, String codeItem) {
        ItemCodePref.SaveItemCode(ItemMasanActivity.this, codeItem);
        Intent intent = new Intent(ItemMasanActivity.this, ScanMasanActivity.class);
        intent.putExtra("itempicking", curItemPicking);
        intent.putExtra("codeItem", codeItem);
        intent.putExtra("itemName", curItemPicking.ProductName);
        intent.putExtra("curcus", strCustomerID);
        intent.putExtra("date", reportDate2);
        intent.putExtra("IsWeightingRequire", curItemPicking.IsWeightingRequire);
        startActivity(intent);
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
                DatePref.SaveOrderDate(ItemMasanActivity.this, Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
                loadPickingList(strCustomerID,reportDate2);

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
        DatePref.SaveOrderDate(ItemMasanActivity.this, Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
        loadPickingList(strCustomerID, reportDate2);
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        DatePref.SaveOrderDate(ItemMasanActivity.this, Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
        loadPickingList(strCustomerID,reportDate2);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Utilities.isRunning(ItemMasanActivity.this);
        Const.isActivating = true;
        loadPickingList(strCustomerID,reportDate2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utilities.isRunning(ItemMasanActivity.this);
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        Utilities.isRunning(ItemMasanActivity.this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
