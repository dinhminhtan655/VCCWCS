package com.wcs.vcc.main.palletcartonweighting;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wcs.vcc.utilities.Const;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PalletCartonWeightInsertParam;
import com.wcs.vcc.api.PalletCartonWeightingViewParam;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DispatchingPalletCartonActivity extends EmdkActivity {
    private PalletCartonWeightingAdapter adapter;
    private String username;
    private String androidID;
    private int storeId;
    private int barcodeType = 0;
    private UUID PalletCartonID;
    private UUID DispatchingOrderDetailID;
    private int palletNumber;
    private boolean isEditable;
    private String DispatchingOrderNumber;
    private UUID DO_DETAIL;
    private String scanResult;
    private List<PalletCartonWeighting> listPallet;
    private boolean isSuccess = false;

    @BindView(R.id.tv_header_pallet_carton_weighting_sum)
    TextView tv_header_pallet_carton_weighting_sum;

    @BindView(R.id.rv_pallet_carton)
    RecyclerView rv_pallet_carton;

    @BindView(R.id.btnSearch)
    Button btnSearch;
    @BindView(R.id.btnClearAll)
    Button btnClearAll;

    @BindView(R.id.btnDispat)
    Button btnDispat;
    @BindView(R.id.btnSelectAll)
    Button btnSelectAll;

    @BindView(R.id.edSearh)
    EditText edSearch;

    private boolean isSelectAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatching_pallet_carton);
        Utilities.showBackIcon(getSupportActionBar());
        ButterKnife.bind(this);
        username = LoginPref.getUsername(getApplicationContext());
        storeId = LoginPref.getStoreId(getApplicationContext());
        androidID = Utilities.getAndroidID(getApplicationContext());
//        listPallet = new ArrayList<>();
        setUpScan();


        String strPalletNumber = getIntent().getStringExtra("PALLET_NUMBER");
        palletNumber = Integer.parseInt(strPalletNumber);
        setTitle(getTitle() + " PI: " + palletNumber);
        DispatchingOrderNumber = getIntent().getStringExtra("DO");
        DO_DETAIL = UUID.fromString(getIntent().getStringExtra("DO_DETAIL"));

        PalletCartonWeightingAdapter.count = 0;

        getPalletCartonWeightingList();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (edSearch.getText() != null) {

                    String strCartonWeight = edSearch.getText().toString();
                    if (strCartonWeight.isEmpty()) {
                        getPalletCartonWeightingList();
                    } else {
                        PalletCartonWeightingViewParam body = new PalletCartonWeightingViewParam(username, androidID, palletNumber, DispatchingOrderNumber, strCartonWeight);
                        MyRetrofit.initRequest(DispatchingPalletCartonActivity.this).getSTAndroid_PalletCartonStockSearch(body).enqueue(new Callback<List<PalletCartonWeighting>>() {
                            @Override
                            public void onResponse(Response<List<PalletCartonWeighting>> response, Retrofit retrofit) {
                                listPallet = response.body();
//                    listPallet.forEach(x->{
//                        x.IsRecordNew=false;
//                    });
                                for (PalletCartonWeighting pallet : listPallet) {
                                    pallet.IsRecordNew = false;
                                }
                                adapter = new PalletCartonWeightingAdapter(new WeightingItemListener() {
                                    @Override
                                    public void onClick(PalletCartonWeighting item, int position) {

//                            listPallet.get(position).IsRecordNew = !listPallet.get(position).IsRecordNew;
                                        edSearch.setText(String.valueOf(listPallet.get(position).CartonWeight));
                                    }

                                    @Override
                                    public void onLongClick(PalletCartonWeighting item, int position) {

                                    }

                                    @Override
                                    public void onChecked(int sumChecked) {
                                        tv_header_pallet_carton_weighting_sum.setText("" + sumChecked);
                                    }
                                }, listPallet);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(DispatchingPalletCartonActivity.this);
                                rv_pallet_carton.setLayoutManager(layoutManager);
                                rv_pallet_carton.setAdapter(adapter);
                                adapter.replace(listPallet);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    }


                }


            }
        });


        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPalletCartonWeightingList();
            }
        });

        btnDispat.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                listPallet.forEach(x -> {
                    if (x.isChecked) {
                        PalletCartonWeightInsertParam params = new PalletCartonWeightInsertParam(username, androidID, scanResult, x.PalletCartonID, DO_DETAIL);
                        insertPalletCartonWeight(params);
                        x.isChecked = false;

                    }
                });
                getPalletCartonWeightingList();
            }
        });

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                listPallet.forEach(x -> {
                    x.isChecked = !isSelectAll;
                });
                adapter.notifyDataSetChanged();
                isSelectAll = !isSelectAll;
            }
        });
    }

    @Override
    public void onData(String data) {
        super.onData(data);
        stopLogoutService();
        Const.timePauseActive = 0;
        data = data.toLowerCase();//buu-2022-09-29
        //buu-2022-09-11
        PalletCartonWeightInsertParam params = new PalletCartonWeightInsertParam(
                username,
                androidID,
                data,
                PalletCartonID,
                DO_DETAIL
        );
        insertPalletCartonWeight(params);
        //buu-2022-09-11

        if (data.contains("PI") == false)
            return;
        String strPalletNumber = getIntent().getStringExtra("PALLET_NUMBER");
        palletNumber = Integer.parseInt(strPalletNumber);
        setTitle(getTitle() + " PI: " + palletNumber);
        getPalletCartonWeightingList();
    }

    private void getPalletCartonWeightingList() {
        PalletCartonWeightingViewParam body = new PalletCartonWeightingViewParam(username, androidID, palletNumber, DispatchingOrderNumber);
        MyRetrofit.initRequest(this).getSTAndroid_PalletCartonStock(body).enqueue(new Callback<List<PalletCartonWeighting>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Response<List<PalletCartonWeighting>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    listPallet = response.body();
//                    listPallet.forEach(x->{
//                        x.IsRecordNew=false;
//                    });
                    for (PalletCartonWeighting pallet : listPallet) {
                        pallet.IsRecordNew = false;
                    }
                    adapter = new PalletCartonWeightingAdapter(new WeightingItemListener() {
                        @Override
                        public void onClick(PalletCartonWeighting item, int position) {

//                            listPallet.get(position).IsRecordNew = !listPallet.get(position).IsRecordNew;
                            edSearch.setText(String.valueOf(listPallet.get(position).CartonWeight));
                        }

                        @Override
                        public void onLongClick(PalletCartonWeighting item, int position) {

                        }

                        @Override
                        public void onChecked(int sumChecked) {
                            if (isSuccess) {
                                isSuccess = false;
                                PalletCartonWeightingAdapter.count = 1;
                                sumChecked = PalletCartonWeightingAdapter.count;
                            }
                            tv_header_pallet_carton_weighting_sum.setText("" + sumChecked);
                        }
                    }, listPallet);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(DispatchingPalletCartonActivity.this);
                    rv_pallet_carton.setLayoutManager(layoutManager);
                    rv_pallet_carton.setAdapter(adapter);
                    adapter.replace(listPallet);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void insertPalletCartonWeight(PalletCartonWeightInsertParam params) {
        MyRetrofit.initRequest(this).DispatchingCartonInser(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    String body = response.body();
                    if (!body.equals("OK")) {
                        Toast.makeText(getApplicationContext(), body, Toast.LENGTH_LONG).show();
                    } else {
                        getPalletCartonWeightingList();
                        isSuccess = true;
                        tv_header_pallet_carton_weighting_sum.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


}