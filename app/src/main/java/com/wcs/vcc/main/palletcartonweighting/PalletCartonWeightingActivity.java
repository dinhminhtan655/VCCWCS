package com.wcs.vcc.main.palletcartonweighting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wcs.vcc.preferences.ShowDialogPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PalletCartonWeightDeleteAlParam;
import com.wcs.vcc.api.PalletCartonWeightDeleteParam;
import com.wcs.vcc.api.PalletCartonWeightInsertParam;
import com.wcs.vcc.api.PalletCartonWeightManualInsertParam;
import com.wcs.vcc.api.PalletCartonWeightingParam;
import com.wcs.vcc.api.PalletCartonWeightingViewParam;
import com.wcs.wcs.databinding.ActivityPalletCartonWeightingBinding;
import com.wcs.wcs.databinding.ItemPalletCartonWeightingBinding;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.DataBoundViewHolder;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PalletCartonWeightingActivity extends EmdkActivity {

    private PalletCartonWeightingAdapter adapter;
    private String username;
    private String androidID;
    private PalletCartonWeighting palletCartonWeighting;
    private ActivityPalletCartonWeightingBinding binding;
    private boolean isKg = true;
    private View currentFocus;
    private int selectedPosition = -1;
    private int storeId;
    private int barcodeType = 0;
    private int palletNumber;
    private boolean isEditable;
    private String DispatchingOrderNumber;
    private UUID DO_DETAIL;
    private UUID PalletCartonID;//buu-2022-10-21
    private List<PalletCartonWeighting> listPallet;

    private int indexItemRV = 0;
    private int topViewRV = 0;
    private int iCheckDeleteOK = 0;
    private int iStatus = 0;

    @BindView(R.id.tv_header_pallet_carton_weighting_sum)
    TextView tv_header_pallet_carton_weighting_sum;


    private boolean isShowedDialog;


//    private RecyclerViewReadyCallback recyclerViewReadyCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.showBackIcon(getSupportActionBar());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pallet_carton_weighting);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        username = LoginPref.getUsername(getApplicationContext());
        storeId = LoginPref.getStoreId(getApplicationContext());
        androidID = Utilities.getAndroidID(getApplicationContext());

        isShowedDialog = false;


        PalletCartonWeightingAdapter.count = 0;

        try {
            DispatchingOrderNumber = getIntent().getStringExtra("DO");
            DO_DETAIL = UUID.fromString(getIntent().getStringExtra("DO_DETAIL"));
            iStatus = getIntent().getIntExtra("STATUS",0);
        } catch (Exception e) {
        }

        if (DispatchingOrderNumber != null && DispatchingOrderNumber.contains("DO") && DispatchingOrderNumber.length() > 0) {
            binding.button3.setVisibility(View.VISIBLE);
            binding.button.setVisibility(View.GONE);
            binding.button2.setVisibility(View.INVISIBLE);
            binding.button2.setEnabled(false);
        } else {
            binding.button3.setVisibility(View.GONE);
        }

        try {
            String strPalletNumber = getIntent().getStringExtra("PALLET_NUMBER");
            palletNumber = Integer.parseInt(strPalletNumber);
            setTitle(getTitle() + " " + palletNumber);
            isEditable = getIntent().getBooleanExtra("IS_EDITABLE", true);
        } catch (Exception ignored) {
        }
        setUpScan();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.rvPalletCartonWeighting.setLayoutManager(layoutManager);

        binding.rvPalletCartonWeighting.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                if (lm != null && lm instanceof LinearLayoutManager) {
                    indexItemRV = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();
                    View v = ((LinearLayoutManager) lm).getChildAt(0);
                    topViewRV = v == null ? 0 : v.getTop() - (binding.rvPalletCartonWeighting.getLayoutManager().getPaddingTop());
                }
            }
        });




    }

    private void scrollToPosition(int position, int offset) {
        RecyclerView.LayoutManager lm = binding.rvPalletCartonWeighting.getLayoutManager();
        if (lm != null && lm instanceof LinearLayoutManager) {
            ((LinearLayoutManager) lm).scrollToPositionWithOffset(position, offset);
            Toast.makeText(this, "" + position + " - " + offset, Toast.LENGTH_SHORT).show();
        }

    }

    private void getPalletCartonWeightingList() {
        PalletCartonWeightingViewParam body = new PalletCartonWeightingViewParam(username, androidID, palletNumber, DispatchingOrderNumber);
        MyRetrofit.initRequest(this).getPalletCartonWeightingList(body).enqueue(new Callback<List<PalletCartonWeighting>>() {
            @Override
            public void onResponse(Response<List<PalletCartonWeighting>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    listPallet = response.body();

                    float weights = 0;
                    float gross = 0;
                    for (PalletCartonWeighting pallet : listPallet) {
                        weights += pallet.CartonWeight;
                        gross += pallet.PalletGrossWeight;
                        pallet.IsRecordNew = false;
                    }
                    binding.setCount(listPallet.size());
                    adapter = new PalletCartonWeightingAdapter(new WeightingItemListener() {
                        @Override
                        public void onClick(PalletCartonWeighting item, int position) {
                            binding.setItem(item);
                            binding.setWeight(String.format(Locale.US, "%.2f", item.CartonWeight));
                            binding.setEditWeight(String.format(Locale.US, "%.2f", item.CartonWeight));
                            palletCartonWeighting = item;
                            selectedPosition = position;
                            currentFocus = getCurrentFocus();


                            if (currentFocus instanceof EditText) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((EditText) currentFocus).selectAll();
                                    }
                                }, 10);
//                                Utilities.showKeyboard(PalletCartonWeightingActivity.this, currentFocus);
                            }
                        }

                        @Override
                        public void onLongClick(PalletCartonWeighting item, int position) {
                            //buu-2021-08-08
                            // if (isEditable) {
                            showDialogConfirmDeleting(item);
                            //} else {
                            //    Toast.makeText(getApplicationContext(), "Bạn không thể xoá trong DO.", Toast.LENGTH_LONG).show();
                            //}
                        }

                        @Override
                        public void onChecked(int sumChecked) {
                            tv_header_pallet_carton_weighting_sum.setText("" + sumChecked);
                        }
                    }, listPallet);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(PalletCartonWeightingActivity.this);
                    binding.rvPalletCartonWeighting.setLayoutManager(layoutManager);
                    binding.rvPalletCartonWeighting.setAdapter(adapter);
                    adapter.replace(listPallet);
                    adapter.notifyDataSetChanged();
                    binding.setWeights(String.format(Locale.US, "%.3f", weights));
                    binding.setGross(String.format(Locale.US, "%.3f", gross));
//                    binding.setGross(gross);
                    if (!listPallet.isEmpty()) {
                        binding.setWeight(String.format(Locale.US, "%.2f", listPallet.get(0).CartonWeight));
                        binding.setEditWeight("");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getPalletCartonWeightingListDO() {
        PalletCartonWeightingViewParam body = new PalletCartonWeightingViewParam(username, androidID, palletNumber, DispatchingOrderNumber);
        MyRetrofit.initRequest(this).getSTAndroid_PalletCartonStock(body).enqueue(new Callback<List<PalletCartonWeighting>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Response<List<PalletCartonWeighting>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    listPallet = response.body();
                    listPallet.forEach(x -> {
                        x.IsRecordNew = false;
                    });
                    adapter.replace(listPallet);

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void updatePalletCartonWeight(PalletCartonWeightingParam body) {
        MyRetrofit.initRequest(this).updatePalletCartonWeight(body).enqueue(new Callback<List<PalletCartonWeighting>>() {
            @Override
            public void onResponse(Response<List<PalletCartonWeighting>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<PalletCartonWeighting> body = new ArrayList<>();
                    body.addAll(response.body());
                    adapter.replace(response.body());
                    final int size = body.size();

                    try {
                        selectedPosition++;

                        RecyclerView.ViewHolder view = binding.rvPalletCartonWeighting.findViewHolderForAdapterPosition(selectedPosition);

                        if (view instanceof DataBoundViewHolder) {
                            ItemPalletCartonWeightingBinding bindingItem = ((ItemPalletCartonWeightingBinding) ((DataBoundViewHolder) view).binding);
                            bindingItem.getRoot().performClick();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int position = 0;

                                if ((size - selectedPosition) > 1) {
                                    position = selectedPosition;
                                } else {
                                    position = size - 1;
                                }
                                binding.rvPalletCartonWeighting.smoothScrollToPosition(position);
                            }
                        }, 600);
                    } catch (Exception e) {
                        Log.e("findViewHolder", "", e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    public void updatePalletCarton(View view) {
        if (DispatchingOrderNumber != null && DispatchingOrderNumber.contains("DO")) {
            getPalletCartonWeightingList();
            return;
        }
        if (palletCartonWeighting == null) {
            Toast.makeText(getApplicationContext(), "Bạn cần chọn một pallet/carton muốn update", Toast.LENGTH_LONG).show();
            return;
        }
        double weight = 0, gross = 0, pay = 0;
        int units = 0;
        String stWeight = binding.tvPalletCartonWeightingWeight.getText().toString();
        String stGross = binding.tvPalletCartonWeightingGross.getText().toString();
        String stUnits = binding.tvPalletCartonWeightingUnits.getText().toString();
        String remark = binding.tvPalletCartonWeightingRemark.getText().toString().trim();
        if (stWeight.trim().length() > 0) {
            weight = Double.parseDouble(stWeight);
            if (!isKg) {
                weight *= 0.4535924;
            }
        }
        if (stGross.trim().length() > 0) {
            gross = Double.parseDouble(stGross);
        }
        if (stUnits.trim().length() > 0) {
            units = Integer.parseInt(stUnits);
        }

        PalletCartonWeightingParam body = new PalletCartonWeightingParam(username, androidID);
        body.PalletCartonID = palletCartonWeighting.PalletCartonID;
        body.PalletNumber = palletCartonWeighting.PalletNumber;

        body.CartonWeight = weight;
        body.PalletGrossWeight = gross;
        body.CartonUnits = units;
        body.PalletRemark = remark;

        Utilities.hideKeyboard(PalletCartonWeightingActivity.this);

        updatePalletCartonWeight(body);
    }


    public void inventory(View view) {
        Intent intent = new Intent(this, DispatchingPalletCartonActivity.class);
        intent.putExtra("PALLET_NUMBER", String.valueOf(palletNumber));
        intent.putExtra("DO", DispatchingOrderNumber);
        intent.putExtra("DO_DETAIL", DO_DETAIL.toString());
        startActivity(intent);
    }

    public void addPalletCarton(View view) {
        if (iStatus != 2){
            double weight = 0, gross = 0, pay = 0;
            int units = 0;
            String stWeight = binding.tvPalletCartonWeightingWeight.getText().toString();
            String stGross = binding.tvPalletCartonWeightingGross.getText().toString();
            String stUnits = binding.tvPalletCartonWeightingUnits.getText().toString();
            String remark = binding.tvPalletCartonWeightingRemark.getText().toString().trim();
            if (stWeight.trim().length() > 0) {
                weight = Double.parseDouble(stWeight);
                if (!isKg) {
                    weight *= 0.4535924;
                }
            }
            if (stGross.trim().length() > 0) {
                gross = Double.parseDouble(stGross);
            }
            if (stUnits.trim().length() > 0) {
                units = Integer.parseInt(stUnits);
            }

            PalletCartonWeightManualInsertParam params = new PalletCartonWeightManualInsertParam(
                    username,
                    androidID,
                    palletNumber,
                    weight,
                    pay,
                    gross,
                    units,
                    remark,
                    storeId
            );

            manualInsertPalletCartonWeight(params);

            binding.tvPalletCartonWeightingWeight.setText("");
        }else {
            Utilities.speakingSomeThingslow("Phiếu này đã được xác nhận không thể Scan", PalletCartonWeightingActivity.this);
        }


    }

    private void manualInsertPalletCartonWeight(PalletCartonWeightManualInsertParam params) {
        MyRetrofit.initRequest(this).manualInsertPalletCartonWeight(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    getPalletCartonWeightingList();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void showDialogConfirmDeleting(final PalletCartonWeighting data) {
        View content = View.inflate(this, R.layout.item_dialog_pallet_carton_weighting, null);
        final CheckBox cbDeleteAll = content.findViewById(R.id.cb_detail_phieu_checked);

        final EditText etRemark = content.findViewById(R.id.et_remark);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(content)
                .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utilities.hideKeyboard(PalletCartonWeightingActivity.this);
                    }
                })
                .setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        if (cbDeleteAll.isChecked()) {
                            new AlertDialog.Builder(PalletCartonWeightingActivity.this)
                                    .setMessage("Bạn có chắc muốn xoá tất cả các thùng không?")
                                    .setPositiveButton("Không", null)
                                    .setNegativeButton("Xoá", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            PalletCartonWeightDeleteAlParam params = new PalletCartonWeightDeleteAlParam(
                                                    username,
                                                    androidID,
                                                    palletNumber,
                                                    etRemark.getText().toString(),
                                                    DispatchingOrderNumber
                                            );
                                            deleteAllPalletCartonWeight(params);
                                        }
                                    })
                                    .create()
                                    .show();
                        } else {

                            listPallet.forEach(x -> {
                                if (x.isChecked) {

                                    PalletCartonWeightDeleteParam params = new PalletCartonWeightDeleteParam(
                                            username,
                                            androidID,
                                            x.PalletCartonID,
                                            etRemark.getText().toString(),
                                            DispatchingOrderNumber
                                    );
                                    deletePalletCartonWeight(params);
                                    x.isChecked = false;
                                }
                            });


                        }
                    }
                })
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void deletePalletCartonWeight(PalletCartonWeightDeleteParam params) {
        MyRetrofit.initRequest(this).deletePalletCartonWeight(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    iCheckDeleteOK++;
                    if (iCheckDeleteOK == PalletCartonWeightingAdapter.count) {
                        getPalletCartonWeightingList();
                        PalletCartonWeightingAdapter.count = 0;
                        tv_header_pallet_carton_weighting_sum.setText("0");
                        iCheckDeleteOK = 0;
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void deleteAllPalletCartonWeight(PalletCartonWeightDeleteAlParam params) {
        MyRetrofit.initRequest(this).deleteAllPalletCartonWeight(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    getPalletCartonWeightingList();
                    PalletCartonWeightingAdapter.count = 0;
                    tv_header_pallet_carton_weighting_sum.setText("0");
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
        stopLogoutService();
        Const.timePauseActive = 0;
//        resetLogoutTimer();
        if (iStatus != 2){
            data = data.toLowerCase();

            if (DispatchingOrderNumber != null && DispatchingOrderNumber.contains("DO") && DispatchingOrderNumber.length() > 0)//2022-10-21 buu-new
            {
                Log.d("tessttt", "hahahaha");
                //buu-2022-09-11
                PalletCartonWeightInsertParam params = new PalletCartonWeightInsertParam(
                        username,
                        androidID,
                        data,
                        PalletCartonID,
                        DO_DETAIL
                );
                checkSameCode(data, params, 1);
            } else {
                Log.d("tessttt", "huhuhuhu");
                PalletCartonWeightInsertParam params = new PalletCartonWeightInsertParam(
                        username,
                        androidID,
                        data,
                        palletNumber,
                        storeId,
                        barcodeType
                );
                checkSameCode(data, params, 2);
            }
        }else {
            Utilities.speakingSomeThingslow("Phiếu này đã được xác nhận không thể Scan", PalletCartonWeightingActivity.this);
        }

    }

    private void checkSameCode(String data, PalletCartonWeightInsertParam params, int iCheck) {
        boolean bSameCode = false;
        for (PalletCartonWeighting str : listPallet) {
            if (str.BarcodeString.equals(data)) {
                bSameCode = true;
                break;
            }
        }

        if (bSameCode) {
            Utilities.speakingSomeThingslow("Mã này đã scan", PalletCartonWeightingActivity.this);
            AlertDialog.Builder dialog = new AlertDialog.Builder(PalletCartonWeightingActivity.this);
            dialog.setMessage("Mã này đã scan cần xác thực")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            insertROORDO(iCheck, params);
                            isShowedDialog = false;
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isShowedDialog = false;
                        }
                    }).create();
            dialog.setCancelable(false);
            if (isShowedDialog == false) {
                dialog.show();
                isShowedDialog = true;
            }
        } else {
            insertROORDO(iCheck, params);
        }

    }

    //Save dialog's state is showing
    private void processDialogShow(boolean value) {
        ShowDialogPref.SaveDialogShow(PalletCartonWeightingActivity.this, value);
    }

    private void insertROORDO(int iCheck, PalletCartonWeightInsertParam params) {
        if (iCheck == 1)
            insertPalletCartonWeightDO(params);
        else
            insertPalletCartonWeight(params);
    }

    private void insertPalletCartonWeight(PalletCartonWeightInsertParam params) {
        MyRetrofit.initRequest(this).insertPalletCartonWeight(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    String body = response.body();
                    if (!body.equals("OK")) {
                        Toast.makeText(getApplicationContext(), body, Toast.LENGTH_LONG).show();
                    } else {
                        getPalletCartonWeightingList();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void insertPalletCartonWeightDO(PalletCartonWeightInsertParam params) {
        MyRetrofit.initRequest(this).DispatchingCartonInser(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    String body = response.body();
                    if (!body.equals("OK")) {
                        Toast.makeText(getApplicationContext(), body, Toast.LENGTH_LONG).show();
                    } else {
                        getPalletCartonWeightingList();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        PalletCartonWeightingAdapter.count = 0;
        getPalletCartonWeightingList();
        isShowedDialog = false;
    }

}
