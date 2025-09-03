package com.wcs.vcc.main.pickship.order;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickPackShipCartonDeleteParameter;
import com.wcs.vcc.api.PickPackShipPackageTypeUpdateParameter;
import com.wcs.vcc.api.xdoc.request.AssignCartonToPalletRequest;
import com.wcs.vcc.api.xdoc.response.PackingList;
import com.wcs.vcc.api.xdoc.response.PackingListParent;
import com.wcs.vcc.api.xdoc.response.XdocDispatchingOrderResponse;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.main.packingscan.PickPackShipOrder;
import com.wcs.vcc.main.packingscan.PickPackShipOrderAdapter;
import com.wcs.vcc.main.packingscan.carton.Carton;
import com.wcs.vcc.main.packingscan.carton.CartonAdapter;
import com.wcs.vcc.main.packingscan.carton.PackageType;
import com.wcs.vcc.main.packingscan.carton.PackageTypeListener;
import com.wcs.vcc.main.packingscan.pack.PacksActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.SettingPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DispatchingOrderPackingActivity extends EmdkActivity {

    @BindView(R.id.btChooceDate)
    Button btChooseDate;
    @BindView(R.id.rv_pick_pack_ship_order)
    RecyclerView rv;
    @BindView(R.id.tvXdocTitle)
    TextView tvMessage;
    @BindView(R.id.tv_prev_barcode)
    TextView tv_prev_barcode;
    EditText etTargetScanAsignCarton;
    TextView tvCartonLabel;
    TextView tvStoreNumberAssgin;
    TextView tvPrevBarcodeAssignCarton;
    View btCameraScanAssginCarton;
    EditText etInerCarton;
    Button btnAssignCartonToPallet;
    private Calendar calendar;
    private String reportDate, reportDate2;
    private PickPackShipOrderAdapter adapter;
    private CartonAdapter cartonAdapter;
    private DispatchingOrderPackingAdapter dispatchingOrderPackingAdapter;
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    private List<PackageType> packageTypes = new ArrayList<>();
    private List<PackingList> packingLists = new ArrayList<>();
    ArrayList<PickPackShipOrder> xdocOrders = new ArrayList<>();
    private List<Carton> cartons = new ArrayList<>();
    public String strItemCode, strDispatchingOrderNumber;
    public Integer storeNumber = 0;
    private int countSelected;
    public static final String CARTON = "CT";
    public static final String STORE_NUMBER = "ST";
    public static final String ITEM = "ITEM";
    private String barcodeType = "DEFAULT";

    private String strPallerCode;
    TextToSpeech textToSpeech;

    //////////////-------------------------
    EditText edtDiaScanCT, edtDiaSLMove;
    TextView tvDiaPreCT, tvDiaCT, tvDiaPallet;
    Button btnDiaOk, btnDiaHuy;
    private Spinner comboCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatching_order_packing);
        ButterKnife.bind(this);
        Utilities.showBackIcon(getSupportActionBar());
        setUpScan();
        calendar = Calendar.getInstance();
        initCartonAdapter();
        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        reportDate = dateformat.format(calendar.getTime());
        Log.e("e", reportDate);
        comboCustomer = findViewById(R.id.sp_list_id_customer);
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));

        if (barcodeType.equals("DEFAULT")) {
            adapter = new PickPackShipOrderAdapter(new RecyclerViewItemListener<PickPackShipOrder>() {
                @Override
                public void onClick(PickPackShipOrder item, int position) {

                    pickPackShipCartons(storeNumber, reportDate, item.DispatchingOrderNumber);
                }

                @Override
                public void onLongClick(PickPackShipOrder item, int position) {
                }
            });
            rv.setAdapter(adapter);

            pickPackShipOrders();


            dispatchingOrderPackingAdapter = new DispatchingOrderPackingAdapter(new RecyclerViewItemOrderListener() {
                @Override
                public void onClick(Object item, final int position, int order) {
                    switch (order) {
                        case 0:

                            final AlertDialog.Builder builder = new AlertDialog.Builder(DispatchingOrderPackingActivity.this);
                            builder.setCancelable(false);
                            LayoutInflater inflater = getLayoutInflater();
                            @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.dialog_update_dispatching, (ViewGroup) findViewById(R.layout.activity_dispatching_order_packing));
                            edtDiaScanCT = dialogView.findViewById(R.id.edtDiaScanCT);
                            edtDiaSLMove = dialogView.findViewById(R.id.edDiaCartonTotal);
                            tvDiaPreCT = dialogView.findViewById(R.id.tvDiaPreCT);
                            tvDiaCT = dialogView.findViewById(R.id.tvDiaItem);
                            btnDiaOk = dialogView.findViewById(R.id.btnDiaOk);
                            btnDiaHuy = dialogView.findViewById(R.id.btnDiaHuy);
                            tvDiaPallet = dialogView.findViewById(R.id.tvDiaPallet);

                            tvDiaPallet.setText(packingLists.get(position).Pallet_ID);
                            tvDiaCT.setText(CARTON+packingLists.get(0).Doc_Entry);
                            tvDiaPreCT.setText(CARTON+packingLists.get(0).Doc_Entry);

                            builder.setView(dialogView);
                            final Dialog dialog2 = builder.create();

                            btnDiaOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("CartonBarCode", tvDiaCT.getText().toString());
                                    jsonObject.addProperty("ItemBarcode", strItemCode);
                                    jsonObject.addProperty("UserName", LoginPref.getUsername(DispatchingOrderPackingActivity.this));
                                    jsonObject.addProperty("DeviceNumber", SettingPref.getCodeItem(DispatchingOrderPackingActivity.this));
                                    jsonObject.addProperty("OrderDate", reportDate);
                                    jsonObject.addProperty("RequestNumber", edtDiaSLMove.getText().toString());

                                    MyRetrofit.initRequest(DispatchingOrderPackingActivity.this).scanPackingList(jsonObject).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Response<String> response, Retrofit retrofit) {
                                            if (response.body().length() == 0) {
                                                //Toast.makeText(DispatchingOrderPackingActivity.this, "thành công", Toast.LENGTH_SHORT).show();
                                                loadPackingList();
                                                dialog2.dismiss();
                                            } else {
                                                AlertDialog dialog = new AlertDialog.Builder(DispatchingOrderPackingActivity.this)
                                                        .setMessage(response.body())
                                                        .setNegativeButton("OK", null)
                                                        .create();
                                                dialog.show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            Toast.makeText(DispatchingOrderPackingActivity.this, "kiểm tra mạng", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });


                            btnDiaHuy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog2.dismiss();
                                }
                            });


                            dialog2.show();

                            edtDiaScanCT.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    View view = dialog2.getCurrentFocus();
                                    if (view != null) {
                                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 1);
                                    }

                                    String contents2 = editable.toString();
                                    contents2.trim();
                                    if (contents2 != null) {
                                        if (contents2.contains("\n")) {
                                            //onData(contents.replaceAll("\n", "").trim());
                                            if (contents2.substring(0, 2).equals(CARTON)) {
                                                tvDiaCT.setText(contents2.replaceAll("\n", ""));
                                            }
                                            if (edtDiaScanCT != null) {
                                                edtDiaScanCT.setText("");
                                                edtDiaScanCT.requestFocus();
                                            }
                                            if (edtDiaScanCT != null) {
                                                tvDiaPreCT.setText(contents2.replaceAll("\n", ""));
                                            }

                                        }
                                    } else {
                                        AlertDialog dialog = new AlertDialog.Builder(DispatchingOrderPackingActivity.this)
                                                .setMessage("Không được để trống")
                                                .setNegativeButton("OK", null)
                                                .create();
                                        dialog.show();
                                    }

                                }
                            });
                            break;


                        case 1:
                            AlertDialog dialog = new AlertDialog.Builder(DispatchingOrderPackingActivity.this)
                                    .setMessage("Bạn có muốn đónng thùng này không?")
                                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            JsonObject jsonObject = new JsonObject();
                                            jsonObject.addProperty("CartonNumber",packingLists.get(position).Doc_Entry);
                                            jsonObject.addProperty("DispatchingOrderDate", reportDate);

                                            MyRetrofit.initRequest(DispatchingOrderPackingActivity.this).updateDongThung(jsonObject).enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Response<String> response, Retrofit retrofit) {
                                                    if (response.isSuccess()){
                                                        //Toast.makeText(DispatchingOrderPackingActivity.this, "Đóng thùng thành công", Toast.LENGTH_SHORT).show();
                                                        loadPackingList();
                                                    }else {
                                                        AlertDialog dialog = new AlertDialog.Builder(DispatchingOrderPackingActivity.this)
                                                                .setMessage("Đóng thùng thất bại")
                                                                .setNegativeButton("OK", null)
                                                                .create();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    AlertDialog dialog = new AlertDialog.Builder(DispatchingOrderPackingActivity.this)
                                                            .setMessage("Kiểm tra kết nối mạng")
                                                            .setNegativeButton("OK", null)
                                                            .create();
                                                }
                                            });
                                        }
                                    })
                                    .create();
                            dialog.show();
                            break;
                    }
                }

                @Override
                public void onLongClick(Object item, int position, int order) {

                }
            },packingLists);

        }


        tv_prev_barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                strPallerCode = editable.toString();


                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if (packingLists.size() > 0 && strPallerCode.substring(0,2).equals(CARTON)) {
                    if (strPallerCode.length() > 0 && strPallerCode.substring(0,2).equals(CARTON)) {

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("CartonBarCode", strPallerCode);
                        jsonObject.addProperty("ItemBarcode", strItemCode);
                        jsonObject.addProperty("UserName", LoginPref.getUsername(DispatchingOrderPackingActivity.this));
                        jsonObject.addProperty("DeviceNumber", SettingPref.getCodeItem(DispatchingOrderPackingActivity.this));
                        jsonObject.addProperty("OrderDate", reportDate);
                        jsonObject.addProperty("RequestNumber", "");

                        MyRetrofit.initRequest(DispatchingOrderPackingActivity.this).scanPackingList(jsonObject).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Response<String> response, Retrofit retrofit) {
                                if (response.isSuccess() && response.body() != null) {
                                    if (response.body().length() == 0) {
//                                        Toast.makeText(DispatchingOrderPackingActivity.this, "thành công", Toast.LENGTH_SHORT).show();
                                        loadPackingList();

                                    } else {
                                        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @Override
                                            public void onInit(int status) {
                                                textToSpeech.speak("Lỗi rồi!", TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        });
                                        AlertDialog dialog = new AlertDialog.Builder(DispatchingOrderPackingActivity.this)
                                        .setMessage(response.body())
                                        .setNegativeButton("OK", null)
                                        .create();
                                        dialog.show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(DispatchingOrderPackingActivity.this, "kiểm tra mạng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    if (strPallerCode.length() > 0) {
                        strItemCode = tv_prev_barcode.getText().toString();
                        loadPackingList();
                    }
                }
            }
        });

    }

    private void loadPackingList() {
        MyRetrofit.initRequest(DispatchingOrderPackingActivity.this).loadPackingList(strItemCode, reportDate).enqueue(new Callback<PackingListParent>() {
                        @Override
                        public void onResponse(final Response<PackingListParent> response, Retrofit retrofit) {
                            if (response.isSuccess() && response.body() != null) {
                                packingLists.addAll(response.body().PackingList);
                                tvMessage.setText(response.body().PackingList.get(0).Item_Name);
                                textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        if (i == TextToSpeech.SUCCESS) {
                                                String content = response.body().PackingList.get(0).Pallet_ID + "!!" + response.body().PackingList.get(0).SoBich + "Gói";
                                                String content2 = "Mã này đã chia xong !";
                                                String content3 = "Vui lòng thêm thùng vào điểm: ";
                                                textToSpeech.setLanguage(new Locale("vi_VN"));
                                                textToSpeech.setSpeechRate(1.3f);
                                                if (response.body().PackingList.get(0).Doc_Entry.equals("0")
                                                        && !response.body().PackingList.get(0).XacNhan.equals("2")){
                                                    textToSpeech.speak(content3 +response.body().PackingList.get(0).Pallet_ID , TextToSpeech.QUEUE_FLUSH, null);
                                                    showDialogAssginCarton();
                                                }else if(!response.body().PackingList.get(0).Doc_Entry.isEmpty()
                                                        &&!response.body().PackingList.get(0).XacNhan.equals("2") ) {
                                                    textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
                                                }else if (response.body().PackingList.get(0).XacNhan.equals("2")){
                                                    textToSpeech.speak(content2, TextToSpeech.QUEUE_FLUSH, null);
                                                }

                                            }
                                        }
                                    });
                        packingLists = response.body().PackingList;
                        dispatchingOrderPackingAdapter.replace(response.body().PackingList);
                        rv.setAdapter(dispatchingOrderPackingAdapter);



                } else {
                    List<PackingList> packingLists = new ArrayList<>();
                    DispatchingOrderPackingAdapter adapter = new DispatchingOrderPackingAdapter();
                    adapter.replace(packingLists);
                    rv.setAdapter(adapter);
                    tvMessage.setText("Không có dữ liệu");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(DispatchingOrderPackingActivity.this, "kiểm tra mạng", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeType.equals("DEFAULT")) {
            pickPackShipOrders();
        }

    }

    @Override
    public void onData(String data) {
        super.onData(data);
        if (data.length() < 2) {
            tvMessage.setText("Barcode định dạng không đúng");
            return;
        }
        barcodeType = data.substring(0, 2);
        try {
            switch (barcodeType) {
                case "DEFAULT":
                    pickPackShipOrders();
                    break;
                case CARTON:

                    break;
                case ITEM:

                    break;
                case STORE_NUMBER:
                    storeNumber = Integer.parseInt((data.substring(2, data.length())));
                    pickPackShipCartons(storeNumber, reportDate, "");
                    break;
            }

        } catch (Exception e) {
            showMessage(e.getMessage());
        }
    }


    private void pickPackShipOrders() {
        MyRetrofit.initRequest(this)
                .loadDispatchingOrderByDate(
                        LoginPref.getStoreId(this),
                        reportDate
                )
                .enqueue(new Callback<XdocDispatchingOrderResponse>() {
                    @Override
                    public void onResponse(Response<XdocDispatchingOrderResponse> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            xdocOrders =PickPackShipOrder.fromXdocOrder(response.body().getDispatchingOrders());
                            tvMessage.setText(reportDate);
                            adapter.notifyDataSetChanged();
                            adapter.replace(xdocOrders);
                            rv.setAdapter(adapter);
                        } else {
                            tvMessage.setText("Không có dữ liệu");
                        }
                    }

                    @Override
                    public void onFailure(Throwable t){
                        AlertDialog dialog = new AlertDialog.Builder(DispatchingOrderPackingActivity.this)
                                .setMessage("Kiểm tra kết nối mạng")
                                .setNegativeButton("OK", null)
                                .create();
                        tvMessage.setText("Lỗi không xác định");
                    }
                });
    }


    @OnClick(R.id.btn_carton_add)
    public void showDialogAssginCarton() {
        final View view = LayoutInflater
                .from(DispatchingOrderPackingActivity.this)
                .inflate(
                        R.layout.layout_assign_carton,
                        null,
                        false
                );

        final Dialog dialog = new AlertDialog.Builder(DispatchingOrderPackingActivity.this)
                .setView(view)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
        etInerCarton = view.findViewById(R.id.etInerCarton);
        etTargetScanAsignCarton = view.findViewById(R.id.et_target_scan);
        tvPrevBarcodeAssignCarton = view.findViewById(R.id.tv_prev_barcode);
        btCameraScanAssginCarton = view.findViewById(R.id.ivCameraScan_assgin_carton);
        tvCartonLabel = view.findViewById(R.id.tv_carton_barcode_assgin);
        tvStoreNumberAssgin = view.findViewById(R.id.tv_store_lable);
        btnAssignCartonToPallet = view.findViewById(R.id.btn_assign_carton_to_pallet);


        if(packingLists!=null && packingLists.size()>0){
            tvStoreNumberAssgin.setText(STORE_NUMBER+ packingLists.get(0).Pallet_ID);
        }

        btnAssignCartonToPallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                assignCarton(v,dialog.getContext());
            }
        });

        etTargetScanAsignCarton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                View view = dialog.getCurrentFocus();
                if (view != null) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), 1);
                }

                String contents = s.toString();
                contents.trim();
                if (contents.contains("\n")) {
                    if (contents.substring(0, 2).equals(CARTON)) {
                        tvCartonLabel.setText(contents.replaceAll("\n", ""));
                    }
                    if (contents.substring(0, 2).equals(STORE_NUMBER)) {
                        tvStoreNumberAssgin.setText(contents.replaceAll("\n", ""));
                    }
                    if (etTargetScanAsignCarton != null) {
                        etTargetScanAsignCarton.setText("");
                        etTargetScanAsignCarton.requestFocus();
                    }
                    if (etTargetScanAsignCarton != null) {
                        tvPrevBarcodeAssignCarton.setText(contents.replaceAll("\n", ""));
                    }
                    if (contents.contains("ENTER")){
                        assignCarton(view,dialog.getContext());
                    }
                }
            }
        });

        btCameraScanAssginCarton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(DispatchingOrderPackingActivity.this);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setCaptureActivity(ScanCameraPortrait.class);
                integrator.initiateScan();
            }
        });

        dialog.show();
    }

    private void assignCarton(View view, Context context){
        if (tvCartonLabel.getText() == null || tvCartonLabel.getText().length() == 0) {
            textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    textToSpeech.speak("Lỗi rồi!", TextToSpeech.QUEUE_FLUSH, null);
                }
            });
            AlertDialog dialogWaring = new AlertDialog.Builder(context)
                    .setMessage("Vui lòng scan mã thùng mới")
                    .setNegativeButton("OK", null)
                    .create();
            dialogWaring.show();
            return;
        }
        if (tvStoreNumberAssgin.getText() == null || tvStoreNumberAssgin.getText().length() == 0) {
            textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    textToSpeech.speak("Lỗi rồi!", TextToSpeech.QUEUE_FLUSH, null);
                }
            });
            AlertDialog dialogWaring = new AlertDialog.Builder(context)
                    .setMessage("Vui lòng scan mã vạch trên Pallet")
                    .setNegativeButton("OK", null)
                    .create();
            dialogWaring.show();
            return;
        }
        AssignCartonToPalletRequest request = new AssignCartonToPalletRequest();
        request.setUserName(LoginPref.getUsername(DispatchingOrderPackingActivity.this));
        request.setBarcodeString(tvCartonLabel.getText().toString());
        request.setStoreNumber(Integer.parseInt(tvStoreNumberAssgin.getText()
                .toString().substring(2, tvStoreNumberAssgin.getText().length())));
        request.setDeviceNumber(Utilities.getAndroidID(getApplicationContext()));
        request.setOrderDate(reportDate.substring(0, 10));
        if(etInerCarton.getText()!=null){
            request.setInner(Integer.parseInt(etInerCarton.getText().toString()));
        }

        MyRetrofit.initRequest(view.getContext()).assginCartonToPallet(request).enqueue(new Callback<XdocDispatchingOrderResponse>() {
            @Override
            public void onResponse(Response<XdocDispatchingOrderResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipCartons(storeNumber, reportDate, "");
                    showMessage(response.body().getMessage());
                    if(strItemCode!=null && strItemCode.length()>0)
                    {
                        MyRetrofit.initRequest(DispatchingOrderPackingActivity.this).loadPackingList(strItemCode, reportDate)
                                .enqueue(new Callback<PackingListParent>() {
                                    @Override
                                    public void onResponse(Response<PackingListParent> response, Retrofit retrofit) {
                                        packingLists = response.body().PackingList;
                                        dispatchingOrderPackingAdapter.notifyDataSetChanged();
                                        dispatchingOrderPackingAdapter.replace(packingLists);
                                        String content = response.body().PackingList.get(0).Pallet_ID + "!!" + response.body().PackingList.get(0).SoBich + "Gói";
                                        String content2 = "Mã này đã chia xong !";
                                        String content3 = "Vui lòng thêm thùng vào điểm: ";
                                        textToSpeech.setLanguage(new Locale("vi_VN"));
                                        textToSpeech.setSpeechRate(1.3f);
                                        if (response.body().PackingList.get(0).Doc_Entry.equals("0")){
                                            textToSpeech.speak(content3 +response.body().PackingList.get(0).Pallet_ID , TextToSpeech.QUEUE_FLUSH, null);
                                        }else if(!response.body().PackingList.get(0).Doc_Entry.isEmpty()
                                                &&!response.body().PackingList.get(0).XacNhan.equals("2") ) {
                                            textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
                                        }else if (response.body().PackingList.get(0).XacNhan.equals("2")){
                                            textToSpeech.speak(content2, TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                        rv.setAdapter(dispatchingOrderPackingAdapter);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

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


    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        reportDate = Utilities.formatDateTime_yyyyMMddFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        barcodeType = "DEFAULT";
        pickPackShipOrders();
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
        barcodeType = "DEFAULT";
        pickPackShipOrders();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        barcodeType = "DEFAULT";
        pickPackShipOrders();
    }

    private void initCartonAdapter() {
        cartonAdapter = new CartonAdapter(new RecyclerViewItemListener<Carton>() {
            @Override
            public void onClick(Carton item, int position) {
                if (item.PackageType.trim().length() == 0) {
                    showMessage("Bạn phải chọn loại thùng trước khi scan");
                    return;
                }
                Intent intent = new Intent(DispatchingOrderPackingActivity.this, PacksActivity.class);
                intent.putExtra("CARTON_ID", item.DispatchingProductCartonID);
                intent.putExtra("ORDER_NUMBER", item.DispatchingOrderNumber);
                intent.putExtra("CARTON_NUMBER", item.CartonNumber);
                intent.putExtra("COMPLETED", item.Completed);
                startActivity(intent);
            }

            @Override
            public void onLongClick(final Carton item, final int position) {
                strDispatchingOrderNumber = item.DispatchingOrderNumber;
                AlertDialog dialog = new AlertDialog.Builder(DispatchingOrderPackingActivity.this)
                        .setMessage("Bạn có muốn xóa thùng này không hả?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deletePickPackShipCarton(item.DispatchingProductCartonID);

                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .create();
                dialog.show();
            }
        }, new PackageTypeListener() {
            @Override
            public void onClickListen(final Carton item) {
                countSelected = 0;
                View view = LayoutInflater.from(DispatchingOrderPackingActivity.this).inflate(R.layout.layout_update_package_type, null, false);

                Spinner spType = view.findViewById(R.id.sp_ppsc);

                ArrayAdapter<PackageType> adapter = new ArrayAdapter<PackageType>(DispatchingOrderPackingActivity.this, android.R.layout.simple_list_item_1, packageTypes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if (countSelected > 0) {
                            PackageType packageType = packageTypes.get(position);
                            PickPackShipPackageTypeUpdateParameter data = new PickPackShipPackageTypeUpdateParameter(
                                    LoginPref.getUsername(DispatchingOrderPackingActivity.this), SettingPref.getCodeItem(DispatchingOrderPackingActivity.this), item.DispatchingProductCartonID, packageType.Packages, packageType.WeightOfPackage);
                            updatePickPackShipPackageType(data);
                        }
                        countSelected++;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                spType.setAdapter(adapter);

                AlertDialog dialog = new AlertDialog.Builder(DispatchingOrderPackingActivity.this)
                        .setView(view)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }

    private void pickPackShipComboPackageType() {
        MyRetrofit.initRequest(this).pickPackShipComboPackageType().enqueue(new Callback<List<PackageType>>() {
            @Override
            public void onResponse(Response<List<PackageType>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    packageTypes = response.body();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void deletePickPackShipCarton(int cartonId) {
        MyRetrofit.initRequest(this).deletePickPackShipCarton(
                new PickPackShipCartonDeleteParameter(
                        LoginPref.getUsername(DispatchingOrderPackingActivity.this),
                        Utilities.getAndroidID(getApplicationContext()),
                        cartonId)
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    showMessage(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        pickPackShipCartons(storeNumber, reportDate, "");
    }

    private void updatePickPackShipPackageType(PickPackShipPackageTypeUpdateParameter data) {
        MyRetrofit.initRequest(this).updatePickPackShipPackageType(data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipCartons(storeNumber, reportDate, "");

                    showMessage(response.body());

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void pickPackShipCartons(Integer customerNumber, String date, String dispatchingOrderNumber) {
        MyRetrofit.initRequest(this).loadDispatchingCartonByDO(customerNumber, date.substring(0, 10), dispatchingOrderNumber).enqueue(new Callback<XdocDispatchingOrderResponse>() {
            @Override
            public void onResponse(Response<XdocDispatchingOrderResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    cartons = Carton.fromXdocCarton(response.body().getCartons());
                    cartonAdapter.notifyDataSetChanged();
                    cartonAdapter.replace(cartons);
                    rv.setAdapter(cartonAdapter);
                    if (response.body() != null) {
                        tvMessage.setText(response.body().getCartons().get(0).getTitle());
                    }

                } else {
                    cartons = new ArrayList<>();
                    cartonAdapter.notifyDataSetChanged();
                    cartonAdapter.replace(cartons);
                    rv.setAdapter(cartonAdapter);

                }
            }
            @Override
            public void onFailure(Throwable t) {
                tvMessage.setText(t.toString());
            }
        });
    }

}
