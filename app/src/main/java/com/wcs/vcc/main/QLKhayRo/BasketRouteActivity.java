package com.wcs.vcc.main.QLKhayRo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.main.QLKhayRo.adapter.BasketCCDCApdater;
import com.wcs.vcc.main.QLKhayRo.adapter.BasketRouteAdapter;
import com.wcs.vcc.main.QLKhayRo.adapter.KhayRoAdapter;
import com.wcs.vcc.main.QLKhayRo.models.BasketCCDCRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketCCDCResponse;
import com.wcs.vcc.main.QLKhayRo.models.BasketConfirmRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketCustomer;
import com.wcs.vcc.main.QLKhayRo.models.BasketCustomerRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketDefaultRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketDefaultResponse;
import com.wcs.vcc.main.QLKhayRo.models.BasketRouteRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketRouteResponse;
import com.wcs.vcc.main.QLKhayRo.models.KhayRoRequest;
import com.wcs.vcc.main.QLKhayRo.models.KhayRoResponse;
import com.wcs.vcc.main.QLKhayRo.models.UpdateKhayRoRequest;
import com.wcs.vcc.main.QLKhayRo.models.UpdateKhayRoResponse;
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

public class BasketRouteActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btChooseDate)
    Button btnChooseDate;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spinnerCustomerCode)
    Spinner spCustomer;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spinnerBasketName)
    Spinner spBasketName;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rvBasketRoute)
    RecyclerView rvBasketRoute;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvSumPack)
    TextView tvSumPack;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvSumBasket)
    TextView tvSumBasket;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvSumBasketNhan)
    TextView tvSumBasketNhan;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvSumCCDC)
    TextView tvSumCCDC;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvSumCCDCNhan)
    TextView tvSumCCDCNhan;

    Calendar calendar;
    String reportDate1, reportDate2;
    String customerCode;
    String basketName;
    int basketId;
    public static final String DATE_FORMAT1 = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    List<BasketCustomer> customerList;
    List<String> customerListString;
    ArrayAdapter<String> customerAdapter;

    List<BasketDefaultResponse> basketList;
    List<String> basketListString;
    ArrayAdapter<String> basketApdater;

    List<BasketRouteResponse> basketRouteResponseList;
    BasketRouteAdapter basketRouteAdapter;

    int tongPack = 0, tongBasket = 0, tongBasketNhan = 0, tongCCDC = 0, tongCCDCNhan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_route);
        ButterKnife.bind(this);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat1 = new SimpleDateFormat(DATE_FORMAT1);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat2 = new SimpleDateFormat(DATE_FORMAT2);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        reportDate1 = dateFormat1.format(calendar.getTime());
        reportDate2 = dateFormat2.format(calendar.getTime());
        btnChooseDate.setText(reportDate1);
        getCustomerList();
        if (customerCode != null) {
            getBasketNameList(customerCode);
        }
        getRouteList(customerCode, reportDate2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getRouteList(customerCode, reportDate2);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btChooseDate)
    public void chooseDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(year, monthOfYear, dayOfMonth);
            reportDate1 = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
            reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
            btnChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate1));
            // Call Api
            getRouteList(customerCode, reportDate2);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ivArrowLeft)
    public void previousDate() {
        calendar.add(Calendar.DATE, -1);
        reportDate1 = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btnChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate1));
        // Call Api
        getRouteList(customerCode, reportDate2);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ivArrowRight)
    public void nextDate() {
        calendar.add(Calendar.DATE, 1);
        reportDate1 = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btnChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate1));
        // Call Api
        getRouteList(customerCode, reportDate2);
    }

    public void getCustomerList() {
        customerList = new ArrayList<>();
        customerListString = new ArrayList<>();

        BasketCustomerRequest request = new BasketCustomerRequest();
        request.setStoreID(LoginPref.getStoreId(BasketRouteActivity.this));

        MyRetrofit.initRequest(this).getCustomerList(request).enqueue(new Callback<List<BasketCustomer>>() {
            @Override
            public void onResponse(Response<List<BasketCustomer>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    customerList = response.body();
                    if (customerList.size() > 0) {
                        for (int i = 0; i < customerList.size(); i++) {
                            customerListString.add(customerList.get(i).getName());
                        }

                        customerAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, customerListString);

                        customerCode = customerList.get(0).getCode();

                        spCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                BasketCustomer basketCustomer;
                                basketCustomer = customerList.get(i);

                                if (basketCustomer != null) {
                                    Log.d("DUONG", basketCustomer.getCode());
                                    customerCode = basketCustomer.getCode();
                                    // Call Api
                                    getRouteList(customerCode, reportDate2);
                                    getBasketNameList(customerCode);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }

                    spCustomer.setAdapter(customerAdapter);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("DUONG", t.getMessage());
                Toast.makeText(BasketRouteActivity.this, "Đã có lỗi, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getBasketNameList(String customerCode) {
        basketList = new ArrayList<>();
        basketListString = new ArrayList<>();

        BasketDefaultRequest request = new BasketDefaultRequest();
        request.setCustomerCode(customerCode);

        MyRetrofit.initRequest(this).getBasketName(request).enqueue(new Callback<List<BasketDefaultResponse>>() {
            @Override
            public void onResponse(Response<List<BasketDefaultResponse>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    basketList = response.body();

                    if (basketList.size() > 0) {
                        for (int i = 0; i < basketList.size(); i++) {
                            basketListString.add(basketList.get(i).getBasketName());
                        }

                        basketApdater = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, basketListString);

                        //basketName = basketList.get(0).getCode();

                        spBasketName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                BasketDefaultResponse basketDefaultResponse;
                                basketDefaultResponse = basketList.get(i);
//
                                if (basketDefaultResponse != null) {
                                    basketName = basketDefaultResponse.getBasketName();
                                    basketId = basketDefaultResponse.getBasketID();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        basketApdater = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, basketListString);
                    }

                    spBasketName.setAdapter(basketApdater);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("DUONG", t.getMessage());
                Toast.makeText(BasketRouteActivity.this, "Đã có lỗi, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getRouteList(String customerCode, String date) {
        BasketRouteRequest basketRouteRequest = new BasketRouteRequest();
        basketRouteRequest.setCustomerCode(customerCode);
        basketRouteRequest.setDate(date);

        MyRetrofit.initRequest(this).getRouteList(basketRouteRequest).enqueue(new Callback<List<BasketRouteResponse>>() {
            @Override
            public void onResponse(Response<List<BasketRouteResponse>> response, Retrofit retrofit) {
                rvBasketRoute.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BasketRouteActivity.this, LinearLayoutManager.VERTICAL, false);
                rvBasketRoute.setLayoutManager(linearLayoutManager);

                basketRouteResponseList = new ArrayList<>();
                basketRouteResponseList = response.body();
                basketRouteAdapter = new BasketRouteAdapter(BasketRouteActivity.this, basketRouteResponseList);

                rvBasketRoute.setAdapter(basketRouteAdapter);

                if (basketRouteResponseList.size() > 0) {
                    tongPack = 0;
                    tongBasket = 0;
                    tongBasketNhan = 0;
                    tongCCDC = 0;
                    tongCCDCNhan = 0;
                    for (int i = 0; i < basketRouteResponseList.size(); i++) {
                        tongPack = tongPack + basketRouteResponseList.get(i).getPackTotal();
                        tongBasket = tongBasket + basketRouteResponseList.get(i).getBasketTotal();
                        tongBasketNhan = tongBasketNhan + basketRouteResponseList.get(i).getBasketTotalReceived();
                        tongCCDC = tongCCDC + basketRouteResponseList.get(i).getTotalCCDC();
                        tongCCDCNhan = tongCCDCNhan + basketRouteResponseList.get(i).getCcdcTotalReceived();
                    }

                    tvSumPack.setText(String.valueOf(tongPack));
                    tvSumBasket.setText(String.valueOf(tongBasket));
                    tvSumBasketNhan.setText(String.valueOf(tongBasketNhan));
                    tvSumCCDC.setText(String.valueOf(tongCCDC));
                    tvSumCCDCNhan.setText(String.valueOf(tongCCDCNhan));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("DUONG", t.getMessage());
                Toast.makeText(BasketRouteActivity.this, "Có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getBasketCCDCDetail(String routeNo) {
        BasketCCDCRequest request = new BasketCCDCRequest();
        request.setDate(reportDate2);
        request.setCustomerCode(customerCode);
        request.setRouteNo(routeNo);

        MyRetrofit.initRequest(this).getBasketCCDC(request).enqueue(new Callback<List<BasketCCDCResponse>>() {
            @Override
            public void onResponse(Response<List<BasketCCDCResponse>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    List<BasketCCDCResponse> CCDCResponseList = new ArrayList<>();

                    Dialog dialog = new Dialog(BasketRouteActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_update_ccdc);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    dialog.getWindow().setAttributes(lp);

                    RecyclerView rvUpdateHang = dialog.findViewById(R.id.rvListCCDC);
                    Button btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                    Button btnUpdate = dialog.findViewById(R.id.btnUpdateCCDC);

                    rvUpdateHang.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BasketRouteActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvUpdateHang.setLayoutManager(linearLayoutManager);

                    CCDCResponseList = response.body();
                    BasketCCDCApdater mAdapter = new BasketCCDCApdater(BasketRouteActivity.this, CCDCResponseList);

                    rvUpdateHang.setAdapter(mAdapter);

                    List<BasketCCDCResponse> finalCCDCResponseList = CCDCResponseList;
                    btnUpdate.setOnClickListener(view -> {
                        if (finalCCDCResponseList.size() > 0) {
                            for (int i = 0; i < finalCCDCResponseList.size(); i++) {
                                Log.d("DUONG", "Index" + i);
                                UpdateKhayRoRequest request = new UpdateKhayRoRequest();
                                request.setDate(finalCCDCResponseList.get(i).getDate().substring(0, 10));
                                request.setCustomerCode(finalCCDCResponseList.get(i).getCustomerCode());
                                request.setRouteNo(finalCCDCResponseList.get(i).getRouteNo());
                                request.setPalletId(null);
                                request.setBasketId(finalCCDCResponseList.get(i).getBasketId());
                                request.setQuantity(finalCCDCResponseList.get(i).getQuantity());
                                request.setQuantityConfirmReceived(finalCCDCResponseList.get(i).getQuantityConfirmReceived());

                                MyRetrofit.initRequest(BasketRouteActivity.this).updateKhayRo(request).enqueue(new Callback<UpdateKhayRoResponse>() {
                                    @Override
                                    public void onResponse(Response<UpdateKhayRoResponse> response, Retrofit retrofit) {
                                        if (response.isSuccess() && response.body() != null) {
                                            Log.d("DUONG", response.body().getMessage());
                                            Log.d("DUONG", String.valueOf(response.code()));
                                            Toast.makeText(BasketRouteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Log.d("DUONG", t.getMessage());
                                        Toast.makeText(BasketRouteActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            getRouteList(customerCode, reportDate2);
                            //Toast.makeText(BasketRouteActivity.this, "Cập nhật CCDC thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    btnCloseDialog.setOnClickListener(view -> {
                        dialog.dismiss();
                        getRouteList(customerCode, reportDate2);
                    });

                    dialog.show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(BasketRouteActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getBasketReceivedDetail(String routeNo) {
        KhayRoRequest request = new KhayRoRequest();
        request.setDate(reportDate2);
        request.setCustomerCode(customerCode);
        //request.setPalletId(null);
        request.setType(1);
        request.setRouteNo(routeNo);

        Log.d("reportDate2", reportDate2);

        MyRetrofit.initRequest(BasketRouteActivity.this).getKhayRo(request).enqueue(new Callback<List<KhayRoResponse>>() {
            @Override
            public void onResponse(Response<List<KhayRoResponse>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    List<KhayRoResponse> khayRoNhanResponseList = new ArrayList<>();

                    Dialog dialog = new Dialog(BasketRouteActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_load_khay_ro_nhan);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    dialog.getWindow().setAttributes(lp);

                    RecyclerView rvUpdateHang = dialog.findViewById(R.id.rvListKhayRoNhan);
                    Button btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                    Button btnUpdate = dialog.findViewById(R.id.btnUpdateKhayRoNhan);

                    rvUpdateHang.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BasketRouteActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvUpdateHang.setLayoutManager(linearLayoutManager);

                    khayRoNhanResponseList = response.body();
                    KhayRoAdapter mAdapter = new KhayRoAdapter(BasketRouteActivity.this, khayRoNhanResponseList, 1);

                    rvUpdateHang.setAdapter(mAdapter);

                    List<KhayRoResponse> finalKhayRoNhanResponseList = khayRoNhanResponseList;
                    btnUpdate.setOnClickListener(view -> {
                        if (finalKhayRoNhanResponseList.size() >= 0) {
                            for (int i = 0; i < finalKhayRoNhanResponseList.size(); i++) {
                                UpdateKhayRoRequest request = new UpdateKhayRoRequest();
                                request.setDate(reportDate2);
                                request.setCustomerCode(customerCode);
                                request.setRouteNo(routeNo);
                                request.setPalletId(null);
                                request.setBasketId(finalKhayRoNhanResponseList.get(i).getBasketId());
                                request.setQuantity(Integer.parseInt(finalKhayRoNhanResponseList.get(i).getQuantity() == null ? "0" : finalKhayRoNhanResponseList.get(i).getQuantity()));
                                request.setQuantityConfirmReceived(Integer.parseInt(finalKhayRoNhanResponseList.get(i).getQuantityConfirmReceived() == null ? "0" : finalKhayRoNhanResponseList.get(i).getQuantityConfirmReceived()));

                                MyRetrofit.initRequest(BasketRouteActivity.this).updateKhayRo(request).enqueue(new Callback<UpdateKhayRoResponse>() {
                                    @Override
                                    public void onResponse(Response<UpdateKhayRoResponse> response, Retrofit retrofit) {
                                        if (response.isSuccess() && response.body().getCode() == 0) {
                                            Toast.makeText(BasketRouteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(BasketRouteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Toast.makeText(BasketRouteActivity.this, "Đã có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            dialog.dismiss();
                            getRouteList(customerCode, reportDate2);
                            //Toast.makeText(BasketRouteActivity.this, "Cập nhật khay rổ nhận thành công.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    btnCloseDialog.setOnClickListener(view -> {
                        dialog.dismiss();
                        getRouteList(customerCode, reportDate2);
                    });

                    dialog.show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(BasketRouteActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void routeNoItemClick(String routeNo) {
        Dialog dialog = new Dialog(BasketRouteActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_routeno_item_click);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        dialog.setCanceledOnTouchOutside(true);

        Button btnXacnhan1 = dialog.findViewById(R.id.btn01);
        Button btnXacnhan2 = dialog.findViewById(R.id.btn02);

        BasketConfirmRequest request = new BasketConfirmRequest();
        request.setDate(reportDate2);
        request.setCustomerCode(customerCode);
        request.setRouteNo(routeNo);
        request.setLoginName(LoginPref.getInfoUser(BasketRouteActivity.this, LoginPref.USERNAME));

        btnXacnhan1.setOnClickListener(view -> {
            MyRetrofit.initRequest(BasketRouteActivity.this).basketConfirm(request).enqueue(new Callback<UpdateKhayRoResponse>() {
                @Override
                public void onResponse(Response<UpdateKhayRoResponse> response, Retrofit retrofit) {
                    if (response.isSuccess() && response.body().getMessage().contains("thành công")) {
                        Toast.makeText(BasketRouteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        getRouteList(customerCode, reportDate2);
                    } else {
                        Toast.makeText(BasketRouteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(BasketRouteActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnXacnhan2.setOnClickListener(view -> {
            MyRetrofit.initRequest(BasketRouteActivity.this).basketConfirmReceived(request).enqueue(new Callback<UpdateKhayRoResponse>() {
                @Override
                public void onResponse(Response<UpdateKhayRoResponse> response, Retrofit retrofit) {
                    if (response.isSuccess() && response.body().getMessage().contains("thành công")) {
                        Toast.makeText(BasketRouteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        getRouteList(customerCode, reportDate2);
                    } else {
                        Toast.makeText(BasketRouteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(BasketRouteActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

    public void gotoDetail(String routeNo) {
        Intent intent = new Intent(BasketRouteActivity.this, BasketRouteDetailActivity.class);
        intent.putExtra("Date", reportDate2);
        intent.putExtra("CustomerCode", customerCode);
        intent.putExtra("RouteNo", routeNo);
        intent.putExtra("ddMMyyyy", reportDate1);
        intent.putExtra("BasketName", basketName);
        intent.putExtra("BasketId", basketId);

        startActivity(intent);
    }

}