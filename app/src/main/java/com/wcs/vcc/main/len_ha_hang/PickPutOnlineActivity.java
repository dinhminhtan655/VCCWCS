package com.wcs.vcc.main.len_ha_hang;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.emdk.ScanListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.PickAndPutAwayLocationScanParam;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.main.len_ha_hang.model.Aisle;
import com.wcs.vcc.main.len_ha_hang.model.Ticket;
import com.wcs.vcc.main.vo.PalletPickPut;
import com.wcs.vcc.main.vo.PickPut;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.RadiLenHaHangPref;
import com.wcs.vcc.preferences.SpinCusPrefVin;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PickPutOnlineActivity extends BaseActivity implements ScanListener {

    private static final String RO = "RO";
    private static final String DO = "DO";

    public static final String TAG = "PickPutActivity";
    private String username, strOrderNumber, strRoomNumber, key, androidID;
    private PickPutAdapter adapter;
    private TextView tvPrevBarcode;
    private EditText etTargetScan;
    private int storeId, flag, strAisle;
    private TextView countRecords;
    private TextView sumCartons, tvRemain;
    private RadioButton radiPriority, radiRemain, radiFinished, radiAisle, radiTicket;
    private SearchableSpinner spinAisle;
    private SearchableSpinner spinTicket;
    private Aisle aisleSelected;
    private Ticket ticketSelected;
    private LinearLayout lnSpin;
    View view;
    private ProgressDialog progressDialog;

    private ArrayAdapter<Aisle> adapterAisle;
    private ArrayAdapter<Ticket> adapterTicket;
    private List<Aisle> aisles = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();
    private RecyclerView lv;

    //Dialog View RO
    private Button btnClose, btnDialogOk;
    private TextView tvResult, tvPltScannedQty, tvPalletNumber, tvLocationNumber, tvProductName, tvPackageQty, tvProductionDate, tvExpDate, tvMsg;
    private EditText et_target_scan_detail;
    private TextView tv_prev_barcode_detail;
    private ImageView ivCameraScan_detail;
    private PalletPickPut pallet;
    private int timeScanToCreateLocation;
    //    private Timer timer;
    private StringBuilder locationBuilder;
    private Dialog dialog2 = null;
    private AlertDialog.Builder builder = null;
    @SuppressLint("ResourceType")
    private View dialogView = null;
    private List<PickPut> pickPutNoDoneList;


    //Dialog View DO

    private Button btnOkDO, btnCloseDO;
    private TextView tvResultDO, tvPltScannedQtyDO, tvPalletNumberDO, tvLocationNumberDO, tvProductNameDO, tvPackageQtyDO, tvProductionDateDO, tvExpDateDO, tv_msgDO;
    private EditText et_slDO;
    //    private EditText et_target_scan_detailDO;
//    private TextView tv_prev_barcode_detailDO;
    private ImageView ivCameraScan_detailDO;
    private Dialog dialogDO = null;
    private AlertDialog.Builder builderDO = null;
    private View dialogViewDO = null;
    private List<PickPut> pickPutList;
    private int iAilse = 0;
    private boolean bCheckExistTicket = false;
    private boolean bCheckExistPalletID = false;
    private int slPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_put_online);
        Utilities.showBackIcon(getSupportActionBar());
        ButterKnife.bind(this);

        androidID = Utilities.getAndroidID(this);
        username = LoginPref.getUsername(this);
        storeId = LoginPref.getStoreId(this);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            key = b.getString("type");
        }


        Utilities.hideKeyboard(this);
        pickPutNoDoneList = new ArrayList<>();
        adapter = new PickPutAdapter(PickPutOnlineActivity.this);
        lv = (RecyclerView) findViewById(R.id.lv_pick_put);
        lv.setAdapter(adapter);

        countRecords = (TextView) findViewById(R.id.tv_pick_put_count);
        sumCartons = (TextView) findViewById(R.id.tv_pick_put_sum);
        tvRemain = (TextView) findViewById(R.id.tvRemain);
        etTargetScan = (EditText) findViewById(R.id.et_target_scan);
        radiPriority = findViewById(R.id.radiPriority);
        radiRemain = findViewById(R.id.radiRemain);
        radiFinished = findViewById(R.id.radiFinished);
        radiAisle = findViewById(R.id.radiAisle);
        radiTicket = findViewById(R.id.radiTicket);
        spinAisle = findViewById(R.id.spinAisle);
        spinTicket = findViewById(R.id.spinTicket);
        lnSpin = findViewById(R.id.lnSpin);

        if (key.equals(RO)) {
            getSupportActionBar().setTitle("Cất hàng");
            radiPriority.setChecked(true);
            RadiLenHaHangPref.saveOrderRadi(PickPutOnlineActivity.this, 1);
        } else {
            getSupportActionBar().setTitle("Soạn hàng");
            radiTicket.setChecked(true);
            RadiLenHaHangPref.saveOrderRadi(PickPutOnlineActivity.this, 5);
        }

        spinAisle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                }
                return false;
            }
        });

        spinTicket.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                }
                return false;
            }
        });


        etTargetScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((dialog2 == null || (dialog2 != null && !dialog2.isShowing()))) {
                    String contents = s.toString();
                    if (contents.contains("\n")) {
                        onData(contents.replaceAll("\n", ""));
                    }
                }

            }
        });


        tvPrevBarcode = (TextView) findViewById(R.id.tv_prev_barcode);

        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_pick_put);


        radiCheckedOrder();


//        getAisle();
//        getTicket();

        if (radiPriority.isChecked()) {
            spinAisle.setEnabled(false);
            spinTicket.setEnabled(false);
            lnSpin.setVisibility(View.GONE);
            RadiLenHaHangPref.saveOrderRadi(PickPutOnlineActivity.this, 1);
            loadPickAndPutAwayView(username, 1, null, -1, key);
        }

        if (radiTicket.isChecked()) {
            spinAisle.setEnabled(false);
            spinAisle.setVisibility(View.GONE);
            spinTicket.setEnabled(true);
            getTicket();
        }


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiPriority:
                        doAllThingOnRadio(false, false, View.GONE, View.GONE, View.GONE, 1, username, 1,
                                null, -1, R.id.radiPriority);
                        break;
                    case R.id.radiRemain:
                        doAllThingOnRadio(false, false, View.GONE, View.GONE, View.GONE, 2, username, 2,
                                null, -1, R.id.radiRemain);
                        break;
                    case R.id.radiFinished:
                        doAllThingOnRadio(false, false, View.GONE, View.GONE, View.GONE, 3, username, 3,
                                null, -1, R.id.radiFinished);
                        break;
                    case R.id.radiAisle:
                        doAllThingOnRadio(true, false, View.VISIBLE, View.VISIBLE, View.GONE, 4, username, 4,
                                null, strAisle, R.id.radiAisle);
                        break;
                    case R.id.radiTicket:
                        doAllThingOnRadio(false, true, View.VISIBLE, View.GONE, View.VISIBLE, 5, username, 5,
                                strOrderNumber, -1, R.id.radiTicket);
                        break;
                }
            }
        });
    }


    private void doAllThingOnRadio(boolean bEnableAisle, boolean bEnaleTicket, int iVisibleLn, int iVisibleAisle, int iVisibleTicket,
                                   int iValue, String strUsername, int iFlag, String strOrderNumber, int strAisle, int view) {
        spinAisle.setEnabled(bEnableAisle);
        spinTicket.setEnabled(bEnaleTicket);
        lnSpin.setVisibility(iVisibleLn);
        if (view == R.id.radiAisle || view == R.id.radiTicket) {
            spinAisle.setVisibility(iVisibleAisle);
            spinTicket.setVisibility(iVisibleTicket);
        }
        RadiLenHaHangPref.saveOrderRadi(PickPutOnlineActivity.this, iValue);
        if (view != R.id.radiAisle && view != R.id.radiTicket)
            loadPickAndPutAwayView(strUsername, iFlag, strOrderNumber, strAisle, key);
        else if (view == R.id.radiAisle) {
            getAisle();
        } else if (view == R.id.radiTicket) {
            getTicket();
        }
    }

    private void loadPickAndPutAwayView(String username, int flag, String strOrderNumber, int strAisle, String orderType) {

        progressDialog = Utilities.getProgressDialog(PickPutOnlineActivity.this, "Đang tải...");
        progressDialog.show();

        if (!WifiHelper.isConnected(PickPutOnlineActivity.this)) {
            RetrofitError.errorNoAction(PickPutOnlineActivity.this, new NoInternet(), TAG, view);
            Utilities.dismissDialog(progressDialog);
            return;
        }

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("UserName", username);
        jsonObject1.addProperty("StoreID", storeId);
        jsonObject1.addProperty("OrderNumber", strOrderNumber);
        jsonObject1.addProperty("Aisle", strAisle);
        jsonObject1.addProperty("Flag", flag);
        jsonObject1.addProperty("OrderType", orderType);
        MyRetrofit.initRequest(this).loadPickAndPutAwayView(jsonObject1).enqueue(new Callback<List<PickPut>>() {
            @Override
            public void onResponse(Response<List<PickPut>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {

                    pickPutNoDoneList.clear();
                    pickPutList = response.body();
//                    if (pickPutList.size() > 0) {


                    int totalQty = 0, totalRemain = 0;
                    for (PickPut pickPut : pickPutList) {
                        if (pickPut.scannedBy.equals("")) {
                            if (flag == 5 || flag == 4)
                                pickPutNoDoneList.add(pickPut);
                            totalRemain++;
                            totalQty += pickPut.cartons;
                        }
                    }

                    countRecords.setText(String.valueOf(flag == 5 || flag == 4 ? pickPutNoDoneList.size() : pickPutList.size()));
                    sumCartons.setText(String.valueOf(totalQty));
                    tvRemain.setText(String.valueOf(totalRemain));
                    adapter.replace(flag == 5 || flag == 4 ? pickPutNoDoneList : pickPutList);
                    adapter.notifyDataSetChanged();
//                    lv.setAdapter(adapter);
//                    }

                }
                Utilities.dismissDialog(progressDialog);
            }

            @Override
            public void onFailure(Throwable t) {
                Utilities.dismissDialog(progressDialog);
            }
        });
    }

    private void getAisle() {
        //khoi tao doi tuong jsonObject
        JsonObject jsonObject = new JsonObject();
        //dua vao kieu tra ve
        jsonObject.addProperty("StoreID", storeId);
        jsonObject.addProperty("UserName", username);
        jsonObject.addProperty("OrderType", key);
        //goi API
        MyRetrofit.initRequest(this).loadPickAndPutAwayByAisleView(jsonObject).enqueue(new Callback<List<Aisle>>() {
            @Override
            public void onResponse(Response<List<Aisle>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    aisles = response.body();
                    adapterAisle = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, aisles);
                    spinAisle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            aisleSelected = aisles.get(i);
                            strAisle = aisleSelected.Aisle;
                            strRoomNumber = aisleSelected.RoomNumber;
                            SpinCusPrefVin.SaveIntVin(PickPutOnlineActivity.this, i);
                            if (radiAisle.isChecked()) {
                                loadPickAndPutAwayView(username, 4, null, strAisle, key);
                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinAisle.setAdapter(adapterAisle);

                    if (SpinCusPrefVin.LoadInt(PickPutOnlineActivity.this) >= aisles.size()) {
                        spinAisle.setSelection(0);
                    } else {
                        spinAisle.setSelection(SpinCusPrefVin.LoadInt(PickPutOnlineActivity.this));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(PickPutOnlineActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không lấy được thông tin dãy!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }


    private void getTicket() {
        //khoi tao doi tuong jsonObject
        JsonObject jsonObject = new JsonObject();
        //dua vao kieu tra ve
        jsonObject.addProperty("StoreID", storeId);
        jsonObject.addProperty("UserName", username);
        jsonObject.addProperty("OrderType", key);
        //goi API
        MyRetrofit.initRequest(this).loadPickAndPutAwayByOrderView(jsonObject).enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Response<List<Ticket>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    if (response.body().size() > 0) {
                        tickets = response.body();

                        adapterTicket = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, tickets);
                        spinTicket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                ticketSelected = tickets.get(i);
                                strOrderNumber = ticketSelected.OrderNumber;
                                SpinCusPrefVin.SaveIntProduct(PickPutOnlineActivity.this, i);
                                if (radiTicket.isChecked()) {
                                    loadPickAndPutAwayView(username, 5, strOrderNumber, 0, key);
                                    bCheckExistTicket = true;
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        spinTicket.setAdapter(adapterTicket);

                        if (SpinCusPrefVin.LoadIntProduct(PickPutOnlineActivity.this) >= tickets.size()) {
                            spinTicket.setSelection(0);
                        } else {
                            spinTicket.setSelection(SpinCusPrefVin.LoadIntProduct(PickPutOnlineActivity.this));
                        }
                    } else {
                        loadPickAndPutAwayView(username, 5, strOrderNumber, 0, key);
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(PickPutOnlineActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không lấy được thông tin khách hàng!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }


    @Override
    public void onData(String data) {
        Const.timePauseActive = 0;
        stopLogoutService();
//        resetLogoutTimer();
        data = data.trim();
        if (!data.equals("") && !data.equals("\t\t")) {
            if (key.equals(RO)) {
                if (dialog2 != null && dialog2.isShowing()) {
                    if (data.startsWith("PI") || data.startsWith("pi")) {
                        etTargetScan.clearFocus();
                        et_target_scan_detail.requestFocus();
                        tv_prev_barcode_detail.setText(data);
                        et_target_scan_detail.getText().clear();
                        navigateToDetail(data, 1, key);
                    } else {
                        etTargetScan.clearFocus();
                        et_target_scan_detail.requestFocus();
                        tv_prev_barcode_detail.setText(data);
                        et_target_scan_detail.getText().clear();
                        navigateToDetail(data, 2, key);
                    }

                } else if (dialog2 == null || (dialog2 != null && !dialog2.isShowing())) {
                    if (data.startsWith("PI") || data.startsWith("pi")) {
                        navigateToDetail(data, 1, key);
                        etTargetScan.getText().clear();
                        tvPrevBarcode.setText(data);
                    } else {
                        Utilities.speakingSomeThingslow("Vui lòng Scan Pallet", PickPutOnlineActivity.this);
                        etTargetScan.getText().clear();
                    }
                }
            } else if (key.equals(DO)) {
                iAilse = 0;
                if (dialogDO != null && dialogDO.isShowing()) {
                    etTargetScan.clearFocus();
                    navigateToDetail(data, 2, key);
                } else if (dialogDO == null || (dialogDO != null && !dialogDO.isShowing())) {
                    if (data.startsWith("DO")) {

                        etTargetScan.getText().clear();
                        tvPrevBarcode.setText(data);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            String finalData = data;
                            tickets.stream().filter(x -> {
                                if (x.OrderNumber.equalsIgnoreCase(finalData)) {
                                    spinTicket.setSelection(iAilse);
                                    SpinCusPrefVin.SaveIntProduct(PickPutOnlineActivity.this, iAilse);
                                    bCheckExistTicket = true;
                                } else {
                                    bCheckExistTicket = false;
                                }
                                iAilse++;
                                return bCheckExistTicket;
                            }).findFirst();

                            if (!bCheckExistTicket) {
                                Utilities.speakingSomeThingslow("Mã Phiếu này không tồn tại", PickPutOnlineActivity.this);
                            }

                        }

                    } else if (data.startsWith("PI") && bCheckExistTicket || data.startsWith("pi") && bCheckExistTicket) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            String finalPIData = data;
                            pickPutList.stream().filter(x -> {
                                if (x.getPalletNumberPI().equalsIgnoreCase(finalPIData)) {
                                    etTargetScan.getText().clear();
                                    etTargetScan.requestFocus();
                                    navigateToDetail(finalPIData, 1, key);
                                    bCheckExistPalletID = true;
                                } else {
                                    etTargetScan.getText().clear();
                                    etTargetScan.requestFocus();
                                    bCheckExistPalletID = false;
                                }
                                return bCheckExistPalletID;
                            }).findFirst();

                            if (!bCheckExistPalletID) {
                                Utilities.speakingSomeThingslow("Pallet ID không tồn tại", PickPutOnlineActivity.this);
                            }
                        }
                    } else {
                        Utilities.speakingSomeThingslow("Vui lòng Scan Phiếu", PickPutOnlineActivity.this);
                        etTargetScan.getText().clear();
                    }
                }
            }

        }
    }

    private void navigateToDetail(String data, int i, String key) {
        if (key.equals(RO)) {
            if (i == 1) {
                showDialogRO();
                secondScan(data, key);
            } else {
                secondScan(data, key);
            }
        } else if (key.equals(DO)) {
            if (i == 1) {
                showDialogDO();
                secondScan(data, key);
            } else {
                secondScan(data, key);
            }
        }

    }

    @OnClick(R.id.ivCameraScan)
    public void cameraScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(ScanCameraPortrait.class);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String data = result.getContents();
                onData(data);
            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }


    private void radiCheckedOrder() {
        if (RadiLenHaHangPref.loadOrderRadi(PickPutOnlineActivity.this) == 1)
            radiPriority.setChecked(true);
        else if (RadiLenHaHangPref.loadOrderRadi(PickPutOnlineActivity.this) == 2)
            radiRemain.setChecked(true);
        else if (RadiLenHaHangPref.loadOrderRadi(PickPutOnlineActivity.this) == 3)
            radiFinished.setChecked(true);
        else if (RadiLenHaHangPref.loadOrderRadi(PickPutOnlineActivity.this) == 4)
            radiAisle.setChecked(true);
        else if (RadiLenHaHangPref.loadOrderRadi(PickPutOnlineActivity.this) == 5)
            radiTicket.setChecked(true);
    }


    @SuppressLint("ResourceType")
    private void showDialogRO() {
        if (dialog2 == null) {
            builder = new AlertDialog.Builder(PickPutOnlineActivity.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
            LayoutInflater inflater = getLayoutInflater();
            dialogView = inflater.inflate(R.layout.layout_pick_put_detail, (ViewGroup) findViewById(R.layout.activity_dispatching_order_packing));
            builder.setView(dialogView);
            builder.setCancelable(false);
            dialog2 = builder.create();
        }


        btnClose = dialogView.findViewById(R.id.btnClose);
        btnDialogOk = dialogView.findViewById(R.id.btnDialogOk);
        tvResult = dialogView.findViewById(R.id.tvResult);
        tvPltScannedQty = dialogView.findViewById(R.id.tvPltScannedQty);
        tvPalletNumber = dialogView.findViewById(R.id.tvPalletNumber);
        tvLocationNumber = dialogView.findViewById(R.id.tvLocationNumber);
        tvProductName = dialogView.findViewById(R.id.tvProductName);
        tvPackageQty = dialogView.findViewById(R.id.tvPackageQty);
        tvProductionDate = dialogView.findViewById(R.id.tvProductionDate);
        tvExpDate = dialogView.findViewById(R.id.tvExpDate);
        et_target_scan_detail = dialogView.findViewById(R.id.et_target_scan_detail);
        tv_prev_barcode_detail = dialogView.findViewById(R.id.tv_prev_barcode_detail);
        tvMsg = dialogView.findViewById(R.id.tv_msg);
        ivCameraScan_detail = dialogView.findViewById(R.id.ivCameraScan_detail);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvResult.setText("");
                tvPltScannedQty.setText("");
                tvPalletNumber.setText("");
                tvLocationNumber.setText("");
                tvProductName.setText("");
                tvPackageQty.setText("");
                tvProductionDate.setText("");
                tvExpDate.setText("");
//                if (timer != null)
//                    timer.cancel();
                dialog2.dismiss();

            }
        });

        btnDialogOk.setVisibility(View.GONE);
//


        View view = dialog2.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 1);
        }

        et_target_scan_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (dialog2.isShowing()) {
                    String contents = s.toString();
                    if (contents.contains("\n")) {
                        onData(contents.replaceAll("\n", ""));
                    }
                }

            }
        });

        ivCameraScan_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(PickPutOnlineActivity.this);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setCaptureActivity(ScanCameraPortrait.class);
                integrator.initiateScan();
            }
        });
        if (!dialog2.isShowing() && dialog2 != null) {
            dialog2.show();
        }

    }

    private void secondScan(String edtData, String key) {
        locationBuilder = new StringBuilder();
        String data = edtData;
        data.trim();
        String type = null;
        if (data.length() > 2) {
            if (data.startsWith("N")) {
                checkTypeSeccondScan(locationBuilder, data, "N", 1);
            } else {
                type = data.substring(0, 2);
                if (type.equalsIgnoreCase("LO")) {
                    checkTypeSeccondScan(locationBuilder, data, "LO000", 2);
                } else if (type.equalsIgnoreCase("PI")) {
                    if (key.equals(RO)) {
                        locationBuilder.append(data.substring(2));
                        tvMsg.setText(R.string.pick_put_updating);
                        loadPickAndPutAwayPalletScan(data);
                    } else if (key.equals(DO)) {
                        locationBuilder.append(data.substring(2));
                        loadPickAndPutAwayPickingPalletScan(data);
                    }

                } else if (type.equalsIgnoreCase("1C")) {
                    checkTypeSeccondScan(locationBuilder, data, "1C", 2);
                } else if (type.equalsIgnoreCase("2C")) {
                    checkTypeSeccondScan(locationBuilder, data, "2C", 2);
                } else {
                    String stringNumber = data.substring(2);
                    if (type.equalsIgnoreCase("RK")) {
                        if (timeScanToCreateLocation == 0) {
                            locationBuilder.append(stringNumber);
                        } else {
                            locationBuilder = new StringBuilder();
                            locationBuilder.append(stringNumber);
                            timeScanToCreateLocation = 0;
                        }
                        tvMsg.setText(String.format(getString(R.string.pick_put_rk), stringNumber));
                        timeScanToCreateLocation++;
                    } else if (type.equalsIgnoreCase("HI")) {
                        if (timeScanToCreateLocation == 1) {
                            checkTypeSeccondScan(locationBuilder, stringNumber, "LO000", 2);
                            timeScanToCreateLocation = 0;
                            locationBuilder = new StringBuilder();
                        }
                    }
                }
            }
        }


    }

    private void checkTypeSeccondScan(StringBuilder locationBuilder, String data, String charBarcode, int iSubString) {
        locationBuilder.append(data.substring(iSubString));
        if (key.equals(RO)) {
            tvMsg.setText(R.string.pick_put_updating);
//            if (timer != null) {
//                timer.cancel();
//            }
//            if (charBarcode.equalsIgnoreCase(pallet.LocationNumber))
            loadPickAndPutAwayLocationScan(charBarcode + locationBuilder);
        } else if (key.equals(DO)) {

        }
    }

    // Xử lý scan DO
    @SuppressLint("ResourceType")
    private void showDialogDO() {
        if (dialogDO == null) {
            builderDO = new AlertDialog.Builder(PickPutOnlineActivity.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
            LayoutInflater inflater = getLayoutInflater();
            dialogViewDO = inflater.inflate(R.layout.layout_ha_hang_detail, (ViewGroup) findViewById(R.layout.activity_pick_put_online));
            builderDO.setView(dialogViewDO);
            builderDO.setCancelable(false);
            dialogDO = builderDO.create();

        }


        btnCloseDO = dialogViewDO.findViewById(R.id.btnCloseDO);
        btnOkDO = dialogViewDO.findViewById(R.id.btnOkDO);
        tvResultDO = dialogViewDO.findViewById(R.id.tvResultDO);
        tvPltScannedQtyDO = dialogViewDO.findViewById(R.id.tvPltScannedQtyDO);
        tvPalletNumberDO = dialogViewDO.findViewById(R.id.tvPalletNumberDO);
        tvLocationNumberDO = dialogViewDO.findViewById(R.id.tvLocationNumberDO);
        tvProductNameDO = dialogViewDO.findViewById(R.id.tvProductNameDO);
        tvPackageQtyDO = dialogViewDO.findViewById(R.id.tvPackageQtyDO);
        tvProductionDateDO = dialogViewDO.findViewById(R.id.tvProductionDateDO);
        tvExpDateDO = dialogViewDO.findViewById(R.id.tvExpDateDO);
        et_slDO = dialogViewDO.findViewById(R.id.et_slDO);
        tv_msgDO = dialogViewDO.findViewById(R.id.tv_msgDO);
        ivCameraScan_detailDO = dialogViewDO.findViewById(R.id.ivCameraScan_detailDO);

        btnCloseDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvResultDO.setText("");
                tvPltScannedQtyDO.setText("");
                tvPalletNumberDO.setText("");
                tvLocationNumberDO.setText("");
                tvProductNameDO.setText("");
                tvPackageQtyDO.setText("");
                tvProductionDateDO.setText("");
                tvExpDateDO.setText("");
//                if (timer != null)
//                    timer.cancel();
                dialogDO.dismiss();
                etTargetScan.requestFocus();

            }
        });

        View view = dialogDO.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 1);
        }

        ivCameraScan_detailDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(PickPutOnlineActivity.this);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setCaptureActivity(ScanCameraPortrait.class);
                integrator.initiateScan();
            }
        });
        if (!dialogDO.isShowing() && dialogDO != null) {
            dialogDO.show();
        }
    }

    private void loadPickAndPutAwayPickingPalletScan(String barcode) {
        PickAndPutAwayLocationScanParam param = new PickAndPutAwayLocationScanParam(barcode, username, androidID);
        MyRetrofit.initRequest(this).loadPickAndPutAwayPickingPalletScan(param).enqueue(new Callback<List<PalletPickPut>>() {
            @Override
            public void onResponse(Response<List<PalletPickPut>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<PalletPickPut> data = response.body();
                    if (data != null && data.size() > 0) {
                        pallet = data.get(0);
                        tv_msgDO.setText("");
                        tvResultDO.setText(pallet.returnResult);
                        tvPltScannedQtyDO.setText(pallet.PltScannedQty);
                        tvPalletNumberDO.setText(pallet.PalletNumber + "");
                        tvLocationNumberDO.setText(pallet.LocationNumber);
                        tvProductNameDO.setText(pallet.ProductNumber + "~~" + pallet.ProductName);
                        tvPackageQtyDO.setText(pallet.Cartons + "");
                        tvProductionDateDO.setText(pallet.getProductionDate());
                        tvExpDateDO.setText(pallet.getUseByDate());
                        et_slDO.setText(pallet.Cartons + "");



                        if (!pallet.ConfirmStatus.equalsIgnoreCase("OK!"))
                            tv_msgDO.setText(pallet.ConfirmStatus);

                        if (pallet.returnResult.equalsIgnoreCase("NO") || pallet.ConfirmStatus.equalsIgnoreCase("DO này đã confirm !")) {
                            btnOkDO.setVisibility(View.GONE);
                        } else {
                            btnOkDO.setVisibility(View.VISIBLE);
                        }

                        btnOkDO.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                slPick = Integer.valueOf(et_slDO.getText().toString().length() == 0 ? "0" : et_slDO.getText().toString());
                                if (et_slDO.length() == 0) {
                                    Utilities.speakingSomeThingslow("Vui lòng nhập số lượng", PickPutOnlineActivity.this);
                                } else if (slPick > pallet.Cartons) {
                                    Utilities.speakingSomeThingslow("Số lượng nhập không lớn hơn thực tế", PickPutOnlineActivity.this);
                                } else if (slPick == 0) {
                                    Utilities.speakingSomeThingslow("Số lượng nhập phải lớn hơn 0", PickPutOnlineActivity.this);
                                } else
                                    updatePickAndPutAwayPickingPallet(pallet.DispatchingOrderDetailID, pallet.PalletID, slPick);
                            }
                        });


                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void updatePickAndPutAwayPickingPallet(UUID strDispatchingOrderDetailID, UUID strPalletID, int iPickingQuantity) {
        PickAndPutAwayLocationScanParam param = new PickAndPutAwayLocationScanParam(strPalletID, username, androidID, strDispatchingOrderDetailID, iPickingQuantity);
        MyRetrofit.initRequest(PickPutOnlineActivity.this).updatePickAndPutAwayPickingPallet(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    if (response.body().equalsIgnoreCase("OK")) {
                        if (dialogDO.isShowing() && dialogDO != null) {
                            dialogDO.dismiss();
                        }
                        etTargetScan.requestFocus();
                        Utilities.speakingSomeThingslow("Thành công", PickPutOnlineActivity.this);

                        if (radiPriority.isChecked()) {
                            doAllThingOnRadio(false, false, View.GONE, View.GONE, View.GONE, 1, username, 1,
                                    null, -1, R.id.radiPriority);
                        } else if (radiRemain.isChecked()) {
                            doAllThingOnRadio(false, false, View.GONE, View.GONE, View.GONE, 2, username, 2,
                                    null, -1, R.id.radiRemain);
                        } else if (radiFinished.isChecked()) {
                            doAllThingOnRadio(false, false, View.GONE, View.GONE, View.GONE, 3, username, 3,
                                    null, -1, R.id.radiFinished);
                        } else if (radiAisle.isChecked()) {
                            doAllThingOnRadio(true, false, View.VISIBLE, View.VISIBLE, View.GONE, 4, username, 4,
                                    null, strAisle, R.id.radiAisle);
                        } else if (radiTicket.isChecked()) {
                            doAllThingOnRadio(false, true, View.VISIBLE, View.GONE, View.VISIBLE, 5, username, 5,
                                    strOrderNumber, -1, R.id.radiTicket);
                        }
                    } else {
                        Toast.makeText(PickPutOnlineActivity.this, "Thất bại có thể do DO này đã confirm", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    //Load infomation RO by pallet ID
    private void loadPickAndPutAwayPalletScan(String barcode) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("UserName=%s&ScanResult=%s&DeviceNumber=%s", username, barcode, androidID));
        MyRetrofit.initRequest(this).loadPickAndPutAwayPalletScan(body).enqueue(new Callback<List<PalletPickPut>>() {
            @Override
            public void onResponse(Response<List<PalletPickPut>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<PalletPickPut> data = response.body();
                    if (data != null && data.size() > 0) {
                        pallet = data.get(0);
                        tvResult.setText(pallet.returnResult);
                        tvPltScannedQty.setText(pallet.PltScannedQty);
                        tvPalletNumber.setText(pallet.PalletNumber + "");
                        tvLocationNumber.setText(pallet.LocationNumber);
                        tvProductName.setText(pallet.ProductNumber + "~~" + pallet.ProductName);
                        tvPackageQty.setText(pallet.Cartons + "");
                        tvProductionDate.setText(pallet.getProductionDate());
                        tvExpDate.setText(pallet.getUseByDate());
                        if (data.get(0).CanMove == 0){
                            Utilities.speakingSomeThingslow("Pallet này đang trong đơn hàng, không thể chuyển", PickPutOnlineActivity.this);
                        }
                    } else {
                        pallet = new PalletPickPut();
                        Utilities.speakingSomeThingslow("Pallet Không tồn tại! hoặc đã tạo ĐI Ô", PickPutOnlineActivity.this);
                        data.clear();
                        tvPltScannedQty.setText("");
                        tvPalletNumber.setText("");
                        tvLocationNumber.setText("");
                        tvProductName.setText("");
                        tvPackageQty.setText("0");
                        tvProductionDate.setText("");
                        tvExpDate.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void confirmPickAndPutAwayLocationScanConfirm(String strLocation, UUID uPalletID) {

        PickAndPutAwayLocationScanParam param = new PickAndPutAwayLocationScanParam(strLocation, uPalletID, username, androidID);

        MyRetrofit.initRequest(PickPutOnlineActivity.this).confirPickAndPutAwayLocationScanConfirm(param).enqueue(new Callback<List<PalletPickPut>>() {
            @Override
            public void onResponse(Response<List<PalletPickPut>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<PalletPickPut> data = response.body();
                    if (data != null && data.size() > 0) {
                        pallet = data.get(0);

                        tvResult.setText(pallet.returnResult);
                        tvPltScannedQty.setText(pallet.PltScannedQty);
                        tvPalletNumber.setText(pallet.PalletNumber + "");
                        tvLocationNumber.setText(pallet.LocationNumber);
                        tvProductName.setText(pallet.ProductName);
                        tvPackageQty.setText(pallet.Cartons + "");
                        tvProductionDate.setText(pallet.getProductionDate());
                        tvExpDate.setText(pallet.getUseByDate());

                        btnDialogOk.setVisibility(View.GONE);
                        Utilities.speakingSomeThingslow(pallet.returnStatus.equalsIgnoreCase("OK") ? "Cập nhật thành công" : "Pallet này đang trong đơn hàng, không thể chuyển", PickPutOnlineActivity.this);
                        updateMsgDone("", "", pallet.returnStatus.equalsIgnoreCase("OK") ? "Cập nhật thành công" : "Pallet này đang trong đơn hàng, không thể chuyển");
                        reloadAfterUpdateROLocation();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }


    private void loadPickAndPutAwayLocationScan(String barcode) {

        tv_prev_barcode_detail.setText("");
        tv_prev_barcode_detail.setText(barcode);

        if (pallet == null) {
            return;
        }
        final String oldLocation = pallet.LocationNumber;
//        if (barcode.equals(oldLocation)) {
//            Utilities.speakingSomeThingslow("Đang cùng vị trí", PickPutOnlineActivity.this);
//        } else {
        final PickAndPutAwayLocationScanParam param = new PickAndPutAwayLocationScanParam(barcode, pallet.PalletID, pallet.OrderID, pallet.OrderNumber, username, Utilities.getAndroidID(this), storeId);
        MyRetrofit.initRequest(this).loadPickAndPutAwayLocationScan(param).enqueue(new Callback<List<PalletPickPut>>() {
            @Override
            public void onResponse(Response<List<PalletPickPut>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<PalletPickPut> data = response.body();
                    if (data != null && data.size() > 0) {
                        pallet = data.get(0);
                        tvResult.setText(pallet.returnResult);
                        tvPltScannedQty.setText(pallet.PltScannedQty);
                        tvPalletNumber.setText(pallet.PalletNumber + "");
                        tvLocationNumber.setText(pallet.LocationNumber);
                        tvProductName.setText(pallet.ProductName);
                        tvPackageQty.setText(pallet.Cartons + "");
                        tvProductionDate.setText(pallet.getProductionDate());
                        tvExpDate.setText(pallet.getUseByDate());

                        if (pallet.AllowUpdate == 0) {
                            Utilities.speakingSomeThingslow(pallet.returnStatus, PickPutOnlineActivity.this);
                            updateMsgDone(oldLocation, pallet.LocationNumber, pallet.returnStatus);
                            btnDialogOk.setVisibility(View.VISIBLE);

                            btnDialogOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    if (pallet.)
                                    confirmPickAndPutAwayLocationScanConfirm(tv_prev_barcode_detail.getText().toString(), pallet.PalletID);
                                }
                            });

                        } else if (pallet.AllowUpdate == 1) {
                            Utilities.speakingSomeThingslow("Thành công", PickPutOnlineActivity.this);
                            updateMsgDone("", "", "Cập nhật thành công");
                            reloadAfterUpdateROLocation();
                        } else {
                            btnDialogOk.setVisibility(View.GONE);
                            Utilities.speakingSomeThingslow(pallet.returnStatus, PickPutOnlineActivity.this);
                            updateMsgDone("", "", pallet.returnStatus);
                        }
                    } else {
                        tvMsg.setText(R.string.pick_put_update_failed);
                    }
                } else {
                    tvMsg.setText(R.string.pick_put_update_failed);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                tvMsg.setText(R.string.pick_put_update_failed);
            }
        });
//        }

    }

    private void updateMsgDone(String oldLocation, String newLocation, String returnStatus) {
//        String msg = String.format(getString(R.string.pick_put_ab_hi), oldLocation, newLocation, returnStatus);
        String msg = String.format("%s", returnStatus);
        int start1 = msg.indexOf(oldLocation);
        int start2 = msg.indexOf(newLocation);
        int start3 = msg.indexOf(returnStatus);

        SpannableString span = new SpannableString(msg);
//        span.setSpan(new ForegroundColorSpan(Color.RED), start1, start1 + oldLocation.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//        span.setSpan(new ForegroundColorSpan(Color.RED), start2, start2 + newLocation.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.RED), start3, start3 + returnStatus.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//        Utilities.speakingSomeThingslow(returnStatus, PickPutOnlineActivity.this);

        tvMsg.setText(span);
    }

    private void reloadAfterUpdateROLocation() {
        btnDialogOk.setVisibility(View.GONE);
        if (radiPriority.isChecked()) {
            doAllThingOnRadio(false, false, View.GONE, View.GONE, View.GONE, 1, username, 1,
                    null, -1, R.id.radiPriority);
        } else if (radiRemain.isChecked()) {
            doAllThingOnRadio(false, false, View.GONE, View.GONE, View.GONE, 2, username, 2,
                    null, -1, R.id.radiRemain);
        } else if (radiFinished.isChecked()) {
            doAllThingOnRadio(false, false, View.GONE, View.GONE, View.GONE, 3, username, 3,
                    null, -1, R.id.radiFinished);
        } else if (radiAisle.isChecked()) {
            doAllThingOnRadio(true, false, View.VISIBLE, View.VISIBLE, View.GONE, 4, username, 4,
                    null, strAisle, R.id.radiAisle);
        } else if (radiTicket.isChecked()) {
            doAllThingOnRadio(false, true, View.VISIBLE, View.GONE, View.VISIBLE, 5, username, 5,
                    strOrderNumber, -1, R.id.radiTicket);
        }


//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (dialog2 != null && dialog2.isShowing()) {
//                    tv_prev_barcode_detail.setText("");
//                    dialog2.dismiss();
//                }
//
//
//            }
//        }, 15000);
    }
}