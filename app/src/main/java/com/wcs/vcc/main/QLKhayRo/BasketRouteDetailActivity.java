package com.wcs.vcc.main.QLKhayRo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.main.QLKhayRo.adapter.BasketPackAdapter;
import com.wcs.vcc.main.QLKhayRo.adapter.BasketRouteDetailAdapter;
import com.wcs.vcc.main.QLKhayRo.adapter.KhayRoAdapter;
import com.wcs.vcc.main.QLKhayRo.models.BasketPackRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketPackResponse;
import com.wcs.vcc.main.QLKhayRo.models.BasketRouteDetailRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketRouteDetailResponse;
import com.wcs.vcc.main.QLKhayRo.models.KhayRoRequest;
import com.wcs.vcc.main.QLKhayRo.models.KhayRoResponse;
import com.wcs.vcc.main.QLKhayRo.models.UpdateBasketPackRequest;
import com.wcs.vcc.main.QLKhayRo.models.UpdateKhayRoRequest;
import com.wcs.vcc.main.QLKhayRo.models.UpdateKhayRoResponse;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BasketRouteDetailActivity extends RingScanActivity {

    String getDateFromIntent, getCustomerCodeFromIntent, getRouteNoFromIntent, getDDMMYYYYFromIntent,
            palletFrom, palletTo, basketNameFromIntent;

    int basketIDFromIntent;

    int getPalletIDFromScan;

    int sumHang = 0, sumKhayRo = 0, sumXacNhan = 0;

    @BindView(R.id.mTvRouteNo)
    TextView tvRouteNo;

    @BindView(R.id.rvBasketRouteDetail)
    RecyclerView rvBasketRouteDetail;

    @BindView(R.id.edtPalletFrom)
    EditText edtPalletFrom;

    @BindView(R.id.edtPalletTo)
    EditText edtPalletTo;

    @BindView(R.id.sumHang)
    TextView tvSumHang;

    @BindView(R.id.sumXacNhan)
    TextView tvSumXacNhan;

    @BindView(R.id.sumKhayRo)
    TextView tvSumKhayRo;

    @BindView(R.id.btnTim)
    Button btnTim;

    @BindView(R.id.mPalletId)
    TextView tvPalletID;

    List<BasketRouteDetailResponse> responseList;
    BasketRouteDetailAdapter routeDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_route_detail);
        ButterKnife.bind(this);

        setUpScan();

        ActionBar supportActionBar = getSupportActionBar();

        Intent intent = getIntent();
        if (intent != null) {
            getDateFromIntent = intent.getStringExtra("Date");
            getCustomerCodeFromIntent = intent.getStringExtra("CustomerCode");
            getRouteNoFromIntent = intent.getStringExtra("RouteNo");
            getDDMMYYYYFromIntent = intent.getStringExtra("ddMMyyyy");
            basketNameFromIntent = intent.getStringExtra("BasketName");
            basketIDFromIntent = intent.getIntExtra("BasketId", 0);

            tvRouteNo.setText(getRouteNoFromIntent);
        }

        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(true);
            Utilities.showBackIcon(supportActionBar);
            supportActionBar.setTitle(basketNameFromIntent);
        }

        loadRoutePallet(getDateFromIntent, getCustomerCodeFromIntent, getRouteNoFromIntent, palletFrom, palletTo);

        btnTim.setOnClickListener(view -> {
            palletFrom = edtPalletFrom.getText().toString();
            palletTo = edtPalletTo.getText().toString();
            loadRoutePallet(getDateFromIntent, getCustomerCodeFromIntent, getRouteNoFromIntent, palletFrom, palletTo);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update_sl_khayro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_update_khayro:
                upateSlKhayRo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadRoutePallet(String date, String customerCode, String routeNo, String palletFrom, String palletTo) {
        BasketRouteDetailRequest request = new BasketRouteDetailRequest();
        request.setDate(date);
        request.setCustomerCode(customerCode);
        request.setRouteNo(routeNo);
        request.setPalletFrom(palletFrom);
        request.setPalletTo(palletTo);
        request.setBasketId(basketIDFromIntent);

        MyRetrofit.initRequest(this).getRoutePallet(request).enqueue(new Callback<List<BasketRouteDetailResponse>>() {
            @Override
            public void onResponse(Response<List<BasketRouteDetailResponse>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    rvBasketRouteDetail.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BasketRouteDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvBasketRouteDetail.setLayoutManager(linearLayoutManager);

                    responseList = new ArrayList<>();
                    responseList = response.body();
                    routeDetailAdapter = new BasketRouteDetailAdapter(BasketRouteDetailActivity.this, responseList);

                    sumHang = 0;
                    sumXacNhan = 0;
                    sumKhayRo = 0;

                    rvBasketRouteDetail.setAdapter(routeDetailAdapter);

                    if (responseList.size() > 0) {
                        sumHang = 0;
                        sumXacNhan = 0;
                        sumKhayRo = 0;
                        for (int i = 0; i < responseList.size(); i++) {
                            sumHang = sumHang + responseList.get(i).getPackTotal();
                            sumXacNhan = sumXacNhan + responseList.get(i).getPackConfirmTotal();
                            sumKhayRo = sumKhayRo + responseList.get(i).getBasketTotal();
                        }

                        tvSumHang.setText(String.valueOf(sumHang));
                        tvSumKhayRo.setText(String.valueOf(sumKhayRo));
                        tvSumXacNhan.setText(String.valueOf(sumXacNhan));

                        tvPalletID.setText(String.valueOf(responseList.get(0).getPalletID()));

                        Utilities.speakingSomeThing("Pallet hiện tại " + String.valueOf(responseList.get(0).getPalletID()), BasketRouteDetailActivity.this);
                    } else {
                        Utilities.speakingSomeThing("Không tìm thấy pallet", BasketRouteDetailActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("DUONG", t.getMessage());
                Toast.makeText(BasketRouteDetailActivity.this, "Đã có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showDialogUpdateXacNhan(String palletId) {

        BasketPackRequest request = new BasketPackRequest();
        request.setDate(getDateFromIntent);
        request.setCustomerCode(getCustomerCodeFromIntent);
        request.setRouteNo(getRouteNoFromIntent);
        request.setPalletId(Integer.parseInt(palletId));

        MyRetrofit.initRequest(this).loadDataBasketPack(request).enqueue(new Callback<List<BasketPackResponse>>() {
            @Override
            public void onResponse(Response<List<BasketPackResponse>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    List<BasketPackResponse> responseList;
                    BasketPackAdapter mAdapter;

                    Dialog dialog = new Dialog(BasketRouteDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_update_xacnhan);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    dialog.getWindow().setAttributes(lp);

                    RecyclerView rvUpdateHang = dialog.findViewById(R.id.rvUpdateHang);
                    Button btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                    Button btnUpdate = dialog.findViewById(R.id.btnUpdateBasketPack);

                    rvUpdateHang.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BasketRouteDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvUpdateHang.setLayoutManager(linearLayoutManager);

                    responseList = response.body();
                    mAdapter = new BasketPackAdapter(BasketRouteDetailActivity.this, responseList);

                    rvUpdateHang.setAdapter(mAdapter);

                    btnUpdate.setOnClickListener(view -> {

                        if (responseList.size() > 0) {

                            for (int i = 0; i < responseList.size(); i++) {
                                UpdateBasketPackRequest request = new UpdateBasketPackRequest();
                                request.setDate(responseList.get(i).getDate());
                                request.setCustomerCode(responseList.get(i).getCustomerCode());
                                request.setRouteNo(responseList.get(i).getRouteNo());
                                request.setPalletId(responseList.get(i).getPalletId());
                                request.setItemID(responseList.get(i).getItemID());
                                request.setQuantity(responseList.get(i).getQuantity());

                                MyRetrofit.initRequest(BasketRouteDetailActivity.this).updateDataBasketPack(request).enqueue(new Callback<UpdateKhayRoResponse>() {
                                    @Override
                                    public void onResponse(Response<UpdateKhayRoResponse> response, Retrofit retrofit) {
                                        if (response.isSuccess() && response.body().getCode() == 0) {
                                            Toast.makeText(BasketRouteDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Log.d("DUONG", t.getMessage());
                                        Toast.makeText(BasketRouteDetailActivity.this, "Đã có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            dialog.dismiss();
                            loadRoutePallet(getDateFromIntent, getCustomerCodeFromIntent, getRouteNoFromIntent, palletFrom, palletTo);
                            Toast.makeText(BasketRouteDetailActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    btnCloseDialog.setOnClickListener(view -> {
                        dialog.dismiss();
                        loadRoutePallet(getDateFromIntent, getCustomerCodeFromIntent, getRouteNoFromIntent, palletFrom, palletTo);
                    });

                    dialog.show();

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("DUONG", t.getMessage());
                Toast.makeText(BasketRouteDetailActivity.this, "Đã có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialogLoadKhayRo(String palletId) {
        KhayRoRequest request = new KhayRoRequest();
        request.setDate(getDateFromIntent);
        request.setCustomerCode(getCustomerCodeFromIntent);
        request.setRouteNo(getRouteNoFromIntent);
        request.setPalletId(palletId);
        request.setType(1);

        MyRetrofit.initRequest(this).getKhayRo(request).enqueue(new Callback<List<KhayRoResponse>>() {
            @Override
            public void onResponse(Response<List<KhayRoResponse>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    List<KhayRoResponse> khayRoresponseList;
                    KhayRoAdapter mAdaper;

                    Dialog dialog = new Dialog(BasketRouteDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_update_khayro);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    dialog.getWindow().setAttributes(lp);

                    RecyclerView rvUpdateKhayRo = dialog.findViewById(R.id.rvUpdateKhayRo);
                    Button btnCloseDialog = dialog.findViewById(R.id.btnCloseDialogKhayRo);

                    rvUpdateKhayRo.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BasketRouteDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvUpdateKhayRo.setLayoutManager(linearLayoutManager);

                    khayRoresponseList = response.body();
                    mAdaper = new KhayRoAdapter(BasketRouteDetailActivity.this, khayRoresponseList, 2);

                    rvUpdateKhayRo.setAdapter(mAdaper);

                    btnCloseDialog.setOnClickListener(view -> {
                        dialog.dismiss();
                        loadRoutePallet(getDateFromIntent, getCustomerCodeFromIntent, getRouteNoFromIntent, palletFrom, palletTo);
                    });

                    dialog.show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("DUONG", t.getMessage());
                Toast.makeText(BasketRouteDetailActivity.this, "Đã có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onData(String data) {
        super.onData(data);
        Log.d("DUONG", "onData(): " + data);

        if (ValidateTypeLabel(data) == 1) {
            getPalletIDFromScan = Integer.parseInt(data.substring(0, data.indexOf("ID")).replace("PA", ""));

            edtPalletFrom.setText(String.valueOf(getPalletIDFromScan));
            edtPalletTo.setText(String.valueOf(getPalletIDFromScan));

            palletFrom = String.valueOf(getPalletIDFromScan);
            palletTo = String.valueOf(getPalletIDFromScan);

            loadRoutePallet(getDateFromIntent, getCustomerCodeFromIntent, getRouteNoFromIntent, palletFrom, palletTo);

//            Log.d("DUONG", String.valueOf(getPalletIDFromScan));
//            Log.d("DUONG", String.valueOf(responseList.get(0).getPalletID()));
        } else if (data.contains("ID")) {
            getPalletIDFromScan = Integer.parseInt(data.substring(0, data.indexOf("ID")).replace("PA", ""));

            edtPalletFrom.setText(String.valueOf(getPalletIDFromScan));
            edtPalletTo.setText(String.valueOf(getPalletIDFromScan));

            palletFrom = String.valueOf(getPalletIDFromScan);
            palletTo = String.valueOf(getPalletIDFromScan);

            loadRoutePallet(getDateFromIntent, getCustomerCodeFromIntent, getRouteNoFromIntent, palletFrom, palletTo);

//            Log.d("DUONG", String.valueOf(getPalletIDFromScan));
//            Log.d("DUONG", String.valueOf(responseList.get(0).getPalletID()));
        } else {
            getPalletIDFromScan = Integer.parseInt(data.replace("PA", ""));

            edtPalletFrom.setText(String.valueOf(getPalletIDFromScan));
            edtPalletTo.setText(String.valueOf(getPalletIDFromScan));

            palletFrom = String.valueOf(getPalletIDFromScan);
            palletTo = String.valueOf(getPalletIDFromScan);

            loadRoutePallet(getDateFromIntent, getCustomerCodeFromIntent, getRouteNoFromIntent, palletFrom, palletTo);

//            Log.d("DUONG", String.valueOf(getPalletIDFromScan));
//            Log.d("DUONG", String.valueOf(responseList.get(0).getPalletID()));
        }
    }

    public int ValidateTypeLabel(String data) {
        if (data.contains("PA") && data.contains("ID") && data.contains("PGR")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void upateSlKhayRo() {
        if (responseList.size() > 0) {
            for (int i = 0; i < responseList.size(); i++) {
                UpdateKhayRoRequest request = new UpdateKhayRoRequest();
                request.setDate(getDateFromIntent);
                request.setCustomerCode(getCustomerCodeFromIntent);
                request.setRouteNo(getRouteNoFromIntent);
                request.setPalletId(String.valueOf(responseList.get(i).getPalletID()));
                request.setBasketId(basketIDFromIntent);
                request.setQuantity(responseList.get(i).getBasketTotal());
                request.setQuantityConfirmReceived(0);

                MyRetrofit.initRequest(BasketRouteDetailActivity.this).updateKhayRo(request).enqueue(new Callback<UpdateKhayRoResponse>() {
                    @Override
                    public void onResponse(Response<UpdateKhayRoResponse> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            Toast.makeText(BasketRouteDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(BasketRouteDetailActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("DUONG_ERROR", t.getMessage());
                    }
                });
            }

            loadRoutePallet(getDateFromIntent, getCustomerCodeFromIntent, getRouteNoFromIntent, palletFrom, palletTo);
            Toast.makeText(BasketRouteDetailActivity.this, "Cập nhật số lượng thành công", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateOneByIndex(BasketRouteDetailResponse item, int index) {

        item.setBasketTotal(responseList.get(index).getBasketTotal() + 1);
        responseList.set(index, item);

        Log.d("DUONG", "Index "+index);

        if (routeDetailAdapter != null) {
            routeDetailAdapter.notifyDataSetChanged();
        }
    }
}