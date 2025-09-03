package com.wcs.vcc.main.scanhang.viewmodel;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.wcs.vcc.api.MyRetrofit2;
import com.wcs.vcc.main.scanhang.model.CustomerScan;
import com.wcs.vcc.main.scanhang.model.ScanPalletCode;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private static final String TAG = "Repository";
    private List<CustomerScan> customerScan = new ArrayList<>();
    private MutableLiveData<List<CustomerScan>> mutableLiveData = new MutableLiveData<>();

    private MutableLiveData<List<ScanPalletCode>> mutableLiveDataScanPalletCode = new MutableLiveData<>();

    public Repository() {
    }

    public MutableLiveData<List<CustomerScan>> getCustomerScan(Context context) {
        MyRetrofit2.initRequest2().LoadCustomerScan().enqueue(new Callback<List<CustomerScan>>() {
            @Override
            public void onResponse(Call<List<CustomerScan>> call, Response<List<CustomerScan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().size() > 0) {
                        mutableLiveData.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CustomerScan>> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }


    public MutableLiveData<List<ScanPalletCode>> getScanPalletCode(Context context, String deliveryDate, String region, String customerCode, String groupSorting, String flag) {

        MyRetrofit2.initRequest2().GetItemPalletCode(deliveryDate, region, customerCode, groupSorting, flag).enqueue(new Callback<List<ScanPalletCode>>() {
            @Override
            public void onResponse(Call<List<ScanPalletCode>> call, Response<List<ScanPalletCode>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().size() > 0) {
                        mutableLiveDataScanPalletCode.setValue(response.body());
                    } else
                        mutableLiveDataScanPalletCode.setValue(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<ScanPalletCode>> call, Throwable t) {
                Utilities.thongBaoDialog(context, "Kiểm tra lại kết nối Internet");
            }
        });

        return mutableLiveDataScanPalletCode;
    }


}

