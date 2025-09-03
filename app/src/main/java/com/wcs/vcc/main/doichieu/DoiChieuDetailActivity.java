package com.wcs.vcc.main.doichieu;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit2;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.doichieu.adapter.DoiChieuDetailAdapter;
import com.wcs.vcc.main.doichieu.model.ShiftConfirmDetail;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewPassTotal;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoiChieuDetailActivity extends EmdkActivity {

    @BindView(R.id.rvDoiChieuDetail)
    RecyclerView rvDoiChieuDetail;
    @BindView(R.id.tvSumViKhoChia)
    TextView tvSumViKhoChia;
    @BindView(R.id.tvSumViNVGN)
    TextView tvSumViNVGN;

    private ProgressDialog progressDialog;

    private DoiChieuDetailAdapter doiChieuDetailAdapter;

    private List<ShiftConfirmDetail> finalShiftConfirmDetails;

    String strDeliveryDate = "", strCustomerCode = "";
    int iPalletID;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_chieu_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        Bundle b = getIntent().getExtras();

        if (b != null) {
            strDeliveryDate = b.getString("DeliveryDate");
            strCustomerCode = b.getString("CustomerID");
            iPalletID = b.getInt("PalletID", 0);

            strDeliveryDate = strDeliveryDate.split("T")[0];
        }


        loadDoiChieuDetail();
    }

    private void loadDoiChieuDetail() {

        progressDialog = Utilities.getProgressDialog(DoiChieuDetailActivity.this, "Đang tải...");
        progressDialog.show();

        if (!WifiHelper.isConnected(DoiChieuDetailActivity.this)) {
            RetrofitError.errorNoAction(DoiChieuDetailActivity.this, new NoInternet(), TAG, view);
            Utilities.dismissDialog(progressDialog);
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("DeliveryDate", strDeliveryDate);
        jsonObject.addProperty("CustomerID", strCustomerCode);
        jsonObject.addProperty("PalletID", iPalletID);

        MyRetrofit2.initRequest2().getDoiChieuDetail(jsonObject).enqueue(new Callback<List<ShiftConfirmDetail>>() {
            @Override
            public void onResponse(Call<List<ShiftConfirmDetail>> call, Response<List<ShiftConfirmDetail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    doiChieuDetailAdapter = new DoiChieuDetailAdapter(DoiChieuDetailActivity.this, response.body(), new RecyclerViewItemOrderListener<ShiftConfirmDetail>() {
                        @Override
                        public void onClick(ShiftConfirmDetail item, int position, int order) {
                            switch (order) {
                                case 0:

                                    break;
                            }
                        }

                        @Override
                        public void onLongClick(ShiftConfirmDetail item, int position, int order) {

                        }
                    }, new RecyclerViewPassTotal<Integer>() {
                        @Override
                        public int totalCount(Integer item) {
                            tvSumViNVGN.setText(String.valueOf(item));
                            return 0;
                        }

                        @Override
                        public int totalCount2(Integer item) {
                            return 0;
                        }
                    });

                    rvDoiChieuDetail.setAdapter(doiChieuDetailAdapter);

                    int sumViKho = 0, sumViNVGN = 0;

                    for (ShiftConfirmDetail s : response.body()) {
                        sumViKho += s.acctualSortQuantity;
                        sumViNVGN += s.qty_ConfirmDriver;
                    }

                    tvSumViKhoChia.setText(String.valueOf(sumViKho));


                    Utilities.dismissDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<List<ShiftConfirmDetail>> call, Throwable t) {
                Toast.makeText(DoiChieuDetailActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                Utilities.dismissDialog(progressDialog);
            }
        });

    }


    @OnClick(R.id.btnUpdateShiftConfirmDetail)
    public void btnUpdateShiftConfirmDetail() {
        if(doiChieuDetailAdapter != null){
            List<ShiftConfirmDetail> shiftConfirmDetails = new ArrayList<>();
            shiftConfirmDetails = doiChieuDetailAdapter.passList();

            finalShiftConfirmDetails = new ArrayList<>();
            for (ShiftConfirmDetail s : shiftConfirmDetails){
                finalShiftConfirmDetails.add(new ShiftConfirmDetail(s.id, s.getQty_ConfirmDriver()));
            }

        }


        progressDialog = Utilities.getProgressDialog(DoiChieuDetailActivity.this, "Đang xử lý...");
        progressDialog.show();

        if (!WifiHelper.isConnected(DoiChieuDetailActivity.this)) {
            RetrofitError.errorNoAction(DoiChieuDetailActivity.this, new NoInternet(), TAG, view);
            Utilities.dismissDialog(progressDialog);
            return;
        }


        MyRetrofit2.initRequest2().UpdateQtyConfirmDriver(finalShiftConfirmDetails).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().equals('"'+"OK"+'"')){
                        Utilities.thongBaoDialog(DoiChieuDetailActivity.this,"Cập nhật thành công");
                    }else {
                        Utilities.thongBaoDialog(DoiChieuDetailActivity.this,response.body());
                    }
                    Utilities.dismissDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(DoiChieuDetailActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                Utilities.dismissDialog(progressDialog);
            }
        });
    }


}