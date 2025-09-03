package com.wcs.vcc.main.newscanmasan.pickpackshippickinglist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickPackShipPackScanParameter;
import com.wcs.vcc.main.BarcodeFuncDef;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.newscanmasan.cartonnewmasan.CartonScanMasanActivity;
import com.wcs.vcc.main.newscanmasan.itemmasan.ItemPickingList;
import com.wcs.vcc.preferences.DatePref;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.PalletFromTo;
import com.wcs.vcc.preferences.SpinCusIdPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ScanMasanActivity extends RingScanActivity {
    @BindView(R.id.rvScanMasan)
    RecyclerView rvScanMasan;
    @BindView(R.id.tv_prev_barcode)
    TextView tv_prev_barcode;
    @BindView(R.id.et_target_scan)
    EditText et_target_scan;
    @BindView(R.id.tvSumSLOrder)
    TextView tvSumSLOrder;
    @BindView(R.id.tvSumTLOrder)
    TextView tvSumTLOrder;
    @BindView(R.id.tvSumSLScan)
    TextView tvSumSLScan;
    @BindView(R.id.tvSumTLScan)
    TextView tvSumTLScan;
    @BindView(R.id.tvPalletNum)
    TextView tvPalletNum;
//    @BindView(R.id.tvMiss)
//    TextView tvMiss;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvItemInfor)
    TextView tvItemInfor;
    //etPalletFrom
    @BindView(R.id.etPalletFrom)
    EditText etPalletFrom;
    //etPalletTo
    @BindView(R.id.etPalletTo)
    EditText etPalletTo;

    @BindView(R.id.btnSaveCarton)
    Button btnChangeType;

    private Calendar calendar;
    private String  reportDate, strCustomerID;
    private String username, androidId, barcode = "";
    private int isLoad = 1;

    ItemPickingList itemPickingList;

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    private PickingListAdapter pickingListAdapter;

    public List<PickingList> pickingLists = new ArrayList<>();
    private List<PickingList> curentList = new ArrayList<>();
    private PickingList curentStore;

    public String curBarcode, codeItem;

    private  String TypeScan = "+1";

    private Integer palletNumber = 0;

    Boolean  IsWeightingRequire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_masan);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }
        setUpScan();
        itemPickingList = (ItemPickingList) getIntent().getSerializableExtra("itempicking");

        strCustomerID = String.valueOf(SpinCusIdPref.LoadCusID(ScanMasanActivity.this));

        codeItem  = getIntent().getStringExtra("codeItem");

        reportDate = DatePref.LoadOrderDate(ScanMasanActivity.this);

        IsWeightingRequire =getIntent().getBooleanExtra("IsWeightingRequire",false);
        tvPalletNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        curentList = new ArrayList<>();

        username = LoginPref.getUsername(this);
        androidId = Utilities.getAndroidID(getApplicationContext());
        etPalletFrom.setText(PalletFromTo.loadPalletFrom(ScanMasanActivity.this));
        etPalletTo.setText(PalletFromTo.loadPalletTo(ScanMasanActivity.this));


        getPickingList(strCustomerID, reportDate, codeItem);
    }


    @Override
    public void onData(final String data) {
        super.onData(data);

        tvMessage.setText(data);

        View view = getCurrentFocus();

        Integer qty = 1;

        if(btnChangeType.getText().equals(TypeScan)){
            qty=1;
        }else{
            qty =-1;
        }

//        if(data.contains("PA") && IsWeightingRequire==true){
//            palletNumber = Integer.parseInt(data.replaceAll("PA",""));
//            tvPalletNum.setText(palletNumber.toString());
//            Utilities.speakingSomeThing("Pallet: " + palletNumber,ScanMasanActivity.this);
//            return;
//        }
//        if(IsWeightingRequire==false){
//            palletNumber = Integer.parseInt(data.replaceAll("PA",""));
//            tvPalletNum.setText(palletNumber.toString());
//            Utilities.speakingSomeThing("Pallet: " + palletNumber,ScanMasanActivity.this);
//            qty = pickingLists.get(0).Quantity;
//        }

        if(data.contains("PA") ){
            palletNumber = Integer.parseInt(data.replaceAll("PA",""));
            tvPalletNum.setText(palletNumber.toString());
            Utilities.speakingSomeThing("Pallet: " + palletNumber,ScanMasanActivity.this);
            return;
        }

        if(pickingLists.size()>0&&palletNumber.toString().equals(pickingLists.get(0).StoreNumber)==false){
            tvMessage.setText("Vui Lòng scan palelt mới!");
            AlertDialog.Builder alert = new AlertDialog.Builder(ScanMasanActivity.this);
            alert.setTitle("Thông Báo");
            alert.setMessage("Sai Pallet rồi");
            alert.setPositiveButton("OK", null);
            alert.show();
            Utilities.speakingSomeThing("Sai Pallet rồi!",ScanMasanActivity.this);
            return;
        }

        MyRetrofit.initRequest(this).XdocPackingScan(
                new PickPackShipPackScanParameter(username, androidId, data, 0,qty,pickingLists.get(0).XdocPickingListID))
                .enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                   // showpacMessage(response.body());
                    if(response.body().length()==0){
                        getPickingList(strCustomerID, reportDate, codeItem);

                    }else{
                        tvMessage.setText(response.body());
                        AlertDialog.Builder alert = new AlertDialog.Builder(ScanMasanActivity.this);
                        alert.setTitle("Thông Báo");
                        alert.setMessage(response.body());
                        alert.setPositiveButton("OK", null);
                        alert.show();
                        Utilities.speakingSomeThing(response.body(),ScanMasanActivity.this);
                        getPickingList(strCustomerID, reportDate, codeItem);
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {

            }
        });
        /*
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        if (data.substring(0, 2).equals(BarcodeFuncDef.ST)) {
            if (pickingLists != null && pickingLists.size() > 0) {

                String storeNumber = null;
                try {
                    storeNumber = String.valueOf(Integer.parseInt(data.substring(2, data.length())));
                } catch (Exception e) {
                    Log.e("eee", e.getMessage());
                }

                List<PickingList> pickList = new ArrayList<>();

                for (int i = 0; i < pickingLists.size(); i++) {
                    if (pickingLists.get(i).StoreNumber.replace(" ", "").equals(storeNumber)) {
                        pickList.add(new PickingList(pickingLists.get(i).OrderDate, pickingLists.get(i).ProductNumber, pickingLists.get(i).ProductName, pickingLists.get(i).StoreNumber
                                , pickingLists.get(i).Quantity, pickingLists.get(i).CustomerNumber, pickingLists.get(i).CustomerName, pickingLists.get(i).CustomerRef2, pickingLists.get(i).CustomerRef, pickingLists.get(i).CustomerClientName
                                , pickingLists.get(i).DispatchingOrderNumber, pickingLists.get(i).Weights, pickingLists.get(i).RouteCode, pickingLists.get(i).NetQty, pickingLists.get(i).NetWeight));
                    }
                }


                if (pickList.size() == 0) {
                    Utilities.speakingSomeThing("Pallet Không tồn tại", ScanMasanActivity.this);
                } else if (pickList.size() == 1) {
                    if (pickList.get(0).CustomerRef.length() > 0) {
                        Utilities.speakingSomeThing(pickList.get(0).StoreNumber + "!!!" + pickList.get(0).Quantity + "gói" + "!!!" + pickList.get(0).CustomerRef, ScanMasanActivity.this);
                        curentStore = pickList.get(0);
                        gotoCarton();
                    } else {
                        Utilities.speakingSomeThing(pickList.get(0).StoreNumber + "!!!" + pickList.get(0).Quantity + "gói", ScanMasanActivity.this);
                        curentStore = pickList.get(0);
                        gotoCarton();
                    }
                    curentStore = pickList.get(0);
                } else if (pickList.size() >= 2) {
                    Utilities.speakingSomeThing("Vui lòng chọn bằng tay", ScanMasanActivity.this);
                    pickingListAdapter.filter(tv_prev_barcode.getText().toString().substring(2, tv_prev_barcode.getText().length()).trim());
                }


            }
        } else if (data.equals(BarcodeFuncDef.CARTON_NEW) || data.equals(BarcodeFuncDef.CARTON_OPEN) ||
                data.equals(BarcodeFuncDef.CARTON_CLOSE) || data.equals(BarcodeFuncDef.STATUS_REFESH) ||
                data.equals(BarcodeFuncDef.GOTO_CARTON) || data.equals(BarcodeFuncDef.CHECK_STATUS) ||
                data.equals(BarcodeFuncDef.ENTER) || data.equals(BarcodeFuncDef.PRINT) ||
                data.equals(BarcodeFuncDef.NEXT_STORE_NUM) || data.substring(0, 2).equals(BarcodeFuncDef.TW) ||
                data.substring(0, 2).equals(BarcodeFuncDef.CT) || data.equals(BarcodeFuncDef.ITEM)) {
            Utilities.speakingSomeThing("Mã không phù hợp", ScanMasanActivity.this);
        } else {
            ItemCodePref.SaveItemCode(ScanMasanActivity.this,data);
            getPickingList(strCustomerID, reportDate, data);
        }

        */
    }

    public  void onChangeType(View view)
    {
        if(btnChangeType.getText().equals(TypeScan))
        {
            btnChangeType.setText("-1");
        }else{
            btnChangeType.setText("+1");
        }
        getPickingList(strCustomerID, reportDate, codeItem);
    }
    public void onXong(View view) {
        et_target_scan.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_target_scan, InputMethodManager.SHOW_IMPLICIT);
        getPickingList(strCustomerID, reportDate, codeItem);
    }

    private void getPickingList(String strCustomerID, String reportDate, String data) {
        Integer palletFrom = 0;
        Integer palletTo = 0;
        try {
            palletFrom = Integer.parseInt( etPalletFrom.getText().toString());
            PalletFromTo.savePalletFrom(ScanMasanActivity.this,palletFrom.toString());
            palletTo = Integer.parseInt( etPalletTo.getText().toString());
            PalletFromTo.savePalletTo(ScanMasanActivity.this,palletTo.toString());
        }catch (Exception e){}

        MyRetrofit.initRequest(ScanMasanActivity.this).loadPickingList(strCustomerID, reportDate, data,palletFrom,palletTo).enqueue(new Callback<PickingListParent>() {
            @Override
            public void onResponse(Response<PickingListParent> response, Retrofit retrofit) {

                if (response.isSuccess() && response != null) {


                    if (response.body().PickingList.size() > 0) {

                        curBarcode = data;
                        pickingLists = response.body().PickingList;
//                        Integer palletFrom = 0;
//                        Integer palletTo = 0;
//                        try {
//                             palletFrom = Integer.parseInt( etPalletFrom.getText().toString());
//                             palletTo = Integer.parseInt( etPalletTo.getText().toString());
//                        }catch (Exception e){}
//
//                        for (int i =0 ;i<response.body().PickingList.size();i++){
//                            if(Integer.parseInt(response.body().PickingList.get(i).StoreNumber)>=palletFrom
//                                    &&Integer.parseInt(response.body().PickingList.get(i).StoreNumber)<=palletTo){
//                                pickingLists.add(response.body().PickingList.get(i));
//                            }
//                        }

                        if(pickingLists.size()==0){
                            return;
                        }
                        Integer qtyMiss = pickingLists.get(0).Quantity - pickingLists.get(0).NetQty;

                        if ( (isLoad == 1  || pickingLists.get(0).NetQty==0 )&& qtyMiss>0){
                            Utilities.speakingSomeThing(pickingLists.get(0).StoreNumber + "!!!" + qtyMiss , ScanMasanActivity.this);
                            isLoad = 0;
                        }


//                        tvMiss.setText(qtyMiss.toString());

                        curentList = pickingLists;
                        pickingListAdapter = new PickingListAdapter(new RecyclerViewItemOrderListener<PickingList>() {
                            @Override
                            public void onClick(PickingList item, int position, int order) {
                                curentStore = item;
                               // switch (order) {
                                etPalletFrom.setText(item.StoreNumber);
                                getPickingList(strCustomerID, reportDate, codeItem);
//                                    case 0:
//                                        Intent intent = new Intent(ScanMasanActivity.this, CartonScanMasanActivity.class);
//                                        intent.putExtra("STORE_NUMBER", item.StoreNumber);
//                                        if (pickingLists.size() > 1 && position != pickingLists.size() - 1) {
//                                            intent.putExtra("NEXT_STORE_NUMBER", pickingLists.get(position + 1).StoreNumber);
//                                        } else {
//                                            intent.putExtra("NEXT_STORE_NUMBER", "0");
//                                        }
//                                        intent.putExtra("DATE", item.OrderDate);
//                                        intent.putExtra("ORDER_NUMBER", item.DispatchingOrderNumber);
//                                        intent.putExtra("CLIENT_NAME", item.CustomerClientName);
//                                        intent.putExtra("BARCODE", BarcodeFuncDef.getItemCode_MASAN(curBarcode));
//                                        startActivity(intent);
//                                        break;
//                          }
                            }

                            @Override
                            public void onLongClick(PickingList item, int position, int order) {

                            }
                        }, curentList);
                        pickingListAdapter.replace(pickingLists);
                        rvScanMasan.setAdapter(pickingListAdapter);
                        tvItemInfor.setText(pickingLists.get(0).ProductName);

                        int iSLO = 0, iSLScan = 0;
                        int dTLO = 0, dTLScan = 0;

                        for (PickingList p : pickingLists) {
                            iSLO += p.Quantity;
                            dTLO += p.Weights;
                            iSLScan += p.NetQty;
                            dTLScan += p.Quantity-p.NetQty;
                        }

                        NumberFormat formatter = new DecimalFormat("#0.00");

                        tvSumSLOrder.setText(String.valueOf(iSLO));
                        tvSumTLOrder.setText(String.valueOf(formatter.format(dTLO)));
                        tvSumSLScan.setText(String.valueOf(iSLScan));
                        tvSumTLScan.setText(String.valueOf(dTLScan));
                        if(qtyMiss==0){
                            Utilities.speakingSomeThing("Đã chia xong",ScanMasanActivity.this);
                            return;
                        }

                        tvPalletNum.setText(pickingLists.get(0).StoreNumber+ "-" +( pickingLists.get(0).Quantity- pickingLists.get(0).NetQty));

                    }

                } else {
                    rvScanMasan.setAdapter(new PickingListAdapter());
                    showMessage("Không thấy data");
                }


            }

            @Override
            public void onFailure(Throwable t) {
                showMessage("Kiểm tra kết nối mạng");
            }
        });
    }

    private void gotoCarton() {
        Intent intent = new Intent(ScanMasanActivity.this, CartonScanMasanActivity.class);
        intent.putExtra("STORE_NUMBER", curentStore.StoreNumber);
        intent.putExtra("DATE", curentStore.OrderDate);
        intent.putExtra("ORDER_NUMBER", curentStore.DispatchingOrderNumber);
        intent.putExtra("CLIENT_NAME", curentStore.CustomerClientName);
        intent.putExtra("BARCODE", BarcodeFuncDef.getItemCode_MASAN(curBarcode));

        for (int i = 0; i < pickingLists.size(); i++) {
            if (pickingLists.get(i).StoreNumber.equals(curentStore.StoreNumber)) {
                if (pickingLists.size() > 1 && pickingLists.size() - 1 > i) {
                    curentStore.setNextStore(pickingLists.get(i + 1).StoreNumber);
                } else {
                    curentStore.setNextStore("0");
                }


            }
        }

        intent.putExtra("NEXT_STORE_NUMBER", curentStore.getNextStore());

        startActivity(intent);
    }




    @Override
    protected void onResume() {
        super.onResume();
        Const.isActivating = true;
       // getPickingList(strCustomerID, reportDate, codeItem);
    }


    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
