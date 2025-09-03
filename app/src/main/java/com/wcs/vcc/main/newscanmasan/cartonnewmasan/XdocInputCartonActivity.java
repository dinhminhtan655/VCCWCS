package com.wcs.vcc.main.newscanmasan.cartonnewmasan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.ComboCustomerResult;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.xdoc.DeliveryTypeResponse;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.newscanmasan.itemmasan.ItemPickingList;
import com.wcs.vcc.main.newscanmasan.itemmasan.ItemPickingListAdapter;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.CheckTotalInfo;
import com.wcs.vcc.preferences.DatePref;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.SpinCusIdPref;
import com.wcs.vcc.preferences.SpinCusPref;
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

public class XdocInputCartonActivity extends RingScanActivity {
    @BindView(R.id.btChooseDate)
    Button btChooseDate;
    @BindView(R.id.spinScanMasan)
    Spinner spinScanMasan;

    @BindView(R.id.spinCartonList)
    Spinner spinCartonList;


    @BindView(R.id.tv_prev_barcode)
    TextView tv_prev_barcode;

    ArrayList<String> al = new ArrayList<String>();
    private ComboCustomerResult cusSelected;
    private DeliveryTypeResponse deliveryTypeSelected;
    ArrayAdapter<ComboCustomerResult> spineradapter;
    ArrayAdapter<DeliveryTypeResponse> spinerDeliveryType;

    List<ComboCustomerResult> customers;

    List<ItemPickingList> pickingLists = new ArrayList<>();

    ItemPickingListAdapter adapter;

    ItemPickingList curItemPicking;
    String curBarcode20;

    private Calendar calendar;
    private String reportDate, reportDate2, strCustomerID, strCustomerNumber, strCodeItem, strCodeItem2;

    @BindView(R.id.etSoThung)
    TextView etSoThung;

    @BindView(R.id.tvPalletNum)
    TextView tvPalletNum;

    @BindView(R.id.tvPalletID)
    TextView tvPalletID;

    @BindView(R.id.tvQty)
    TextView tvQty;

    @BindView(R.id.tvCartonTotal)
    TextView tvCartonTotal;

    @BindView(R.id.etInner)
    EditText etInner;

    @BindView(R.id.tvPackLe)
    TextView tvPackLe;

    private CheckTotalInfo checkTotalInfo;
    private Integer pallet;

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    List<DeliveryTypeResponse> deliveryTypeResponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xdoc_input_carton);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbarXdocInputCarton);
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
        chooseDate();
        deliveryTypeSelected = new DeliveryTypeResponse();
        getCustomer();
        getDeliveryType();
    }

    @Override
    public void onData(final String data) {
        super.onData(data);

        if (data.contains("PA")) {
            pallet = Integer.parseInt(data.replaceAll("PA", ""));
        }
        tvPalletNum.setText(data);
        loadData(strCustomerID,reportDate,pallet,deliveryTypeSelected.PackagedItem,deliveryTypeSelected.DeliveryType);


    }
    private void getDeliveryType(){
        MyRetrofit.initRequest(this).APIDeliveryType(strCustomerID,reportDate).enqueue(new Callback<List<DeliveryTypeResponse>>() {
            @Override
            public void onResponse(Response<List<DeliveryTypeResponse>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    deliveryTypeResponses = response.body();
                    spinerDeliveryType = new ArrayAdapter<DeliveryTypeResponse>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, deliveryTypeResponses);
                    spinCartonList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            deliveryTypeSelected = deliveryTypeResponses.get(position);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinCartonList.setAdapter(spinerDeliveryType);
                    spinCartonList.setSelection(0);

                }
            }

            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(XdocInputCartonActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không lấy được thông tin khách hàng!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }
    private void getCustomer() {
        cusSelected = new ComboCustomerResult();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("StoreID", LoginPref.getStoreId(XdocInputCartonActivity.this));
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
                            SpinCusPref.SaveInt(XdocInputCartonActivity.this, position);
                            cusSelected = customers.get(position);
                            strCustomerID = String.valueOf(cusSelected.getCustomerID());
                            getDeliveryType();
                            strCustomerNumber = customers.get(position).getCustomerNumber();
                            SpinCusIdPref.saveCusID(XdocInputCartonActivity.this,String.valueOf(cusSelected.getCustomerID()));
                            if(cusSelected!=null){
                                loadData(strCustomerID, reportDate2,pallet,deliveryTypeSelected.getPackagedItem(),deliveryTypeSelected.getDeliveryType());
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinScanMasan.setAdapter(spineradapter);

                }
            }

            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(XdocInputCartonActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không lấy được thông tin khách hàng!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }

    public void onXong(View view) {
        if(checkTotalInfo==null) return;;
        JsonObject object = new JsonObject();
        object.addProperty("UserName",LoginPref.getUsername(this));
        object.addProperty("CartonTotal",Integer.parseInt(etSoThung.getText().toString()));
        if(etSoThung.getText()==null && etSoThung.getText().length()==0){return;}
        MyRetrofit.initRequest(this).saveCarton(String.valueOf(checkTotalInfo.getDispatchingOrderID()),object).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    loadData(strCustomerID,reportDate,pallet,deliveryTypeSelected.getPackagedItem(),deliveryTypeSelected.getDeliveryType());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(XdocInputCartonActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Lỗi!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });


    }
    public void onIncre(View view) {
        if(checkTotalInfo==null) return;;
        if(etSoThung.getText()==null && etSoThung.getText().length()==0){return;}
        Integer soThung = Integer.parseInt(etSoThung.getText().toString())+1;
        if(soThung<0) soThung = 0;
        etSoThung.setText(soThung+"");
        Integer inner = Integer.parseInt(etInner.getText().toString());
        tvPackLe.setText((checkTotalInfo.getQty()-inner*soThung)+"");
//        JsonObject object = new JsonObject();
//        object.addProperty("UserName","hai");
//        object.addProperty("CartonTotal",Integer.parseInt(etSoThung.getText().toString()));
//        MyRetrofit.initRequest(this).saveCarton(String.valueOf(checkTotalInfo.getDispatchingOrderID()),object).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Response<String> response, Retrofit retrofit) {
//                if (response.isSuccess() && response.body() != null) {
//                    loadData(strCustomerID,reportDate,pallet);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                AlertDialog.Builder alert = new AlertDialog.Builder(XdocInputCartonActivity.this);
//                alert.setTitle("Thông Báo");
//                alert.setMessage("Lỗi!");
//                alert.setPositiveButton("OK", null);
//                alert.show();
//            }
//        });

    }
    public void OnDecre(View view) {
        if(checkTotalInfo==null) return;

        if(etSoThung.getText()==null && etSoThung.getText().length()==0){return;}
        Integer soThung = Integer.parseInt(etSoThung.getText().toString())  -1;
        if(soThung<0) soThung = 0;
        etSoThung.setText(soThung+"");
        Integer inner = Integer.parseInt(etInner.getText().toString());
        tvPackLe.setText((checkTotalInfo.getQty()-inner*soThung)+"");
//        JsonObject object = new JsonObject();
//        object.addProperty("UserName","hai");
//        object.addProperty("CartonTotal",Integer.parseInt(etSoThung.getText().toString()));
//        MyRetrofit.initRequest(this).saveCarton(String.valueOf(checkTotalInfo.getDispatchingOrderID()),object).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Response<String> response, Retrofit retrofit) {
//                if (response.isSuccess() && response.body() != null) {
//                    loadData(strCustomerID,reportDate,pallet);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                AlertDialog.Builder alert = new AlertDialog.Builder(XdocInputCartonActivity.this);
//                alert.setTitle("Thông Báo");
//                alert.setMessage("Lỗi!");
//                alert.setPositiveButton("OK", null);
//                alert.show();
//            }
//        });

    }
    private void loadData(String strCustomerID, String date, Integer pallet,String PackagedItem, String DeliveryType) {

        Integer inner = Integer.parseInt(etInner.getText().toString());

        MyRetrofit.initRequest(this).pickPackShipLoadTotalCarton(strCustomerID,date,pallet,PackagedItem,DeliveryType).enqueue(new Callback<CheckTotalInfo>() {
            @Override
            public void onResponse(Response<CheckTotalInfo> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    checkTotalInfo= response.body();
                    if(checkTotalInfo!=null){
                        tvPalletNum.setText(checkTotalInfo.getPalletNumber().toString());
                        tvPalletID.setText(checkTotalInfo.getPalletNumber().toString());
                        tvQty.setText(checkTotalInfo.getQty().toString());
                        tvCartonTotal.setText(checkTotalInfo.getTotalCarton().toString());
                        if(checkTotalInfo.getTotalCarton()>0){
                            etSoThung.setText(checkTotalInfo.getTotalCarton().toString());
                            tvPackLe.setText(checkTotalInfo.getQty()-inner*checkTotalInfo.getTotalCarton()+"");
                        }else{
                            etSoThung.setText((int) Math.ceil((double )checkTotalInfo.getQty()/inner)+"");
                            tvPackLe.setText(checkTotalInfo.getQty()-inner*(int) Math.ceil((double )checkTotalInfo.getQty()/inner)+"");
                        }
                        Utilities.speakingSomeThing(checkTotalInfo.getQty().toString() + " Gói " + checkTotalInfo.getTotalCarton() + " Thùng" , XdocInputCartonActivity.this);
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(XdocInputCartonActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Lỗi!");
                alert.setPositiveButton("OK", null);
                alert.show();
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
                DatePref.SaveOrderDate(XdocInputCartonActivity.this, Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));

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
        DatePref.SaveOrderDate(XdocInputCartonActivity.this, Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        DatePref.SaveOrderDate(XdocInputCartonActivity.this, Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
    }
}
