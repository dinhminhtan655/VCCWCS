package com.wcs.vcc.main.scanbhx;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit2;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.scanbhx.adapter.SealGroupAdapter;
import com.wcs.vcc.main.scanbhx.model.SealGroup;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ABASealBHXActivity extends RingScanActivity {

    private static final String NO_PALLET = "Scan Pallet để gắn seal";
    @BindView(R.id.tvDateSeal)
    TextView tvDateSeal;
    @BindView(R.id.tv_prev_barcode)
    TextView tv_prev_barcode;
    @BindView(R.id.et_target_scan)
    EditText et_target_scan;
    @BindView(R.id.tvTotalPallet)
    TextView tvTotalPallet;
    @BindView(R.id.edtFrom)
    EditText edtFrom;
    @BindView(R.id.edtTo)
    EditText edtTo;
    @BindView(R.id.tvPalletCurrent)
    TextView tvPalletCurrent;
    @BindView(R.id.rcAddSealPallet)
    RecyclerView rcAddSealPallet;

    View view;

    ProgressDialog progressDialog;

    SealGroupAdapter adapter;

    String strDeliveryDate,nhom;
    String[] strP;
    int iFrom = 0, iTo = 0, iFrom2 = 0, iTo2 = 0;

    List<SealGroup> sealGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_b_a_seal_b_h_x);
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
            strDeliveryDate = b.getString("date");
            nhom = b.getString("nhom");
            tvDateSeal.setText(strDeliveryDate);
        }else {
            tvDateSeal.setText("Lỗi");
            nhom = "0";
        }
        tvPalletCurrent.setText(NO_PALLET);


        MyRetrofit2.initRequest2().GetMinMaxPallet(strDeliveryDate, nhom).enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().size() > 0){
                        tvTotalPallet.setText("Ngày " + strDeliveryDate + " nhóm này hiện có Pallet từ " + response.body().get(0) + " đến " + response.body().get(1) + ". Nếu nhóm bạn có 2 người hoặc hơn để tránh nhầm lẫn khi đi gắn Seal thì hãy điền giới hạn pallet mình sẽ gắn.");
                        iFrom2 = response.body().get(0);
                        iTo2 = response.body().get(1);
                        edtFrom.setText(iFrom2+"");
                        edtTo.setText(iTo2+"");
                    }else {
                        tvTotalPallet.setText("Ngày " + strDeliveryDate + " nhóm " + nhom + " không có Pallet!");
                        iFrom2 = 0;
                        iTo2 = 0;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Toast.makeText(ABASealBHXActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                iFrom2 = 0;
                iTo2 = 0;
            }
        });

        GetSealGroupPallet(strDeliveryDate, nhom,"0");




        setUpScan();
    }

    @Override
    public void onData(String data) {
        super.onData(data);

        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (data.length() > 0){
            if (data.substring(0,2).equals("PA")){
                try{
                    String text = data;
                    strP = text.split("A");
                    if (tvPalletCurrent.getText().equals(NO_PALLET)){
                        if (Integer.parseInt(strP[1]) < iFrom2 || Integer.parseInt(strP[1]) > iTo2){
                            Utilities.speakingSomeThing("Bạn không được scan mã pallet có số nhỏ hơn " + iFrom2 + " hoặc lớn hơn " + iTo2, ABASealBHXActivity.this);
                        }else {
                            Utilities.speakingSomeThing("Bắt đầu gắn seal pallet " + strP[1], ABASealBHXActivity.this);
                            tvPalletCurrent.setText(""+ strP[1]);
                            GetSealGroupPallet(strDeliveryDate, nhom,tvPalletCurrent.getText().toString());
                        }
                    }else if (!tvPalletCurrent.getText().equals(strP[1])){
                        Utilities.speakingSomeThing("Bạn chưa đóng Pallet " + tvPalletCurrent.getText().toString() + " hãy scan lại Pallet hiện tại trên màn hình để đóng", ABASealBHXActivity.this);
                    }else {
                        if ((sealGroups.size() % 2) == 0 || sealGroups.get(0).sealName != null){
                            Utilities.speakingSomeThing("Đã đóng Pallet " + strP[1], ABASealBHXActivity.this);
                            GetSealGroupPallet(strDeliveryDate, nhom,"0");
                            tvPalletCurrent.setText(NO_PALLET);
                        }else {
                            Utilities.speakingSomeThing("Seal hiện tại đang là số lẻ không thể đóng Pallet", ABASealBHXActivity.this);
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Lỗi: " + e, Toast.LENGTH_SHORT).show();
                }
            }else if (data.trim().length() == 8 && !data.matches(".*[a-z].*")){
                if (tvPalletCurrent.getText().toString().equals(NO_PALLET)){
                    Utilities.speakingSomeThing("Bạn chưa scan Pallet vui lòng scan Pallet", ABASealBHXActivity.this);
                }else {
                    MyRetrofit2.initRequest2().AddNewSeal(tvPalletCurrent.getText().toString(), data, tvDateSeal.getText().toString(), LoginPref.getUsername(ABASealBHXActivity.this)).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful() && response.body() != null){
                                if (response.body().equals('"' + "Thành công!"+ '"')){
                                    GetSealGroupPallet(strDeliveryDate, nhom,tvPalletCurrent.getText().toString());
                                    Toast.makeText(ABASealBHXActivity.this, response.body(), Toast.LENGTH_SHORT).show();

                                }else {
                                    Utilities.speakingSomeThing(response.body(), ABASealBHXActivity.this);
                                }
                            }else {
                                Toast.makeText(ABASealBHXActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(ABASealBHXActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }else {
                Utilities.speakingSomeThing("Mã barcode không phù hợp!", ABASealBHXActivity.this);
            }
        }
    }


    public void GetSealGroupPallet(String deliveryDate, String groupSorting, String palletID){
        progressDialog = Utilities.getProgressDialog(ABASealBHXActivity.this, "Đang tải...");
        progressDialog.show();

        if (!WifiHelper.isConnected(ABASealBHXActivity.this)) {
            Utilities.dismissDialog(progressDialog);
            RetrofitError.errorNoAction(ABASealBHXActivity.this, new NoInternet(), TAG, view);
        }


        MyRetrofit2.initRequest2().GetSealGroupPallet(deliveryDate, groupSorting, palletID).enqueue(new Callback<List<SealGroup>>() {
            @Override
            public void onResponse(Call<List<SealGroup>> call, Response<List<SealGroup>> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().size() > 0){
                        sealGroups = new ArrayList<>();
                        sealGroups = response.body();
                        if (Integer.parseInt(palletID.toString()) > 0){
                            Utilities.speakingSomeThing((sealGroups.size()) + "Seal", ABASealBHXActivity.this);
                        }
                        adapter = new SealGroupAdapter(new RecyclerViewItemOrderListener<SealGroup>() {
                            @Override
                            public void onClick(SealGroup item, int position, int order) {
                                switch (order){
                                    case 0:
                                        AlertDialog.Builder b = new AlertDialog.Builder(ABASealBHXActivity.this);
                                        b.setTitle("Xóa Seal");
                                        b.setMessage("Bạn có muốn xóa " + item.sealName + " của pallet " + item.palletID);
                                        b.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                progressDialog = Utilities.getProgressDialog(ABASealBHXActivity.this, "Đang tải...");
//                                                progressDialog.show();
//
//                                                if (!WifiHelper.isConnected(ABASealBHXActivity.this)) {
//                                                    Utilities.dismissDialog(progressDialog);
//                                                    RetrofitError.errorNoAction(ABASealBHXActivity.this, new NoInternet(), TAG, view);
//                                                }

                                                MyRetrofit2.initRequest2().RemoveSeal(item.id).enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, Response<String> response) {
                                                        if (response.isSuccessful() && response.body() != null){
                                                            if (response.body().equals('"'+"Xóa Thành Công!"+'"')){
                                                                Utilities.speakingSomeThing(response.body(), ABASealBHXActivity.this);
                                                                if (!tvPalletCurrent.getText().toString().equals(NO_PALLET)){
                                                                    GetSealGroupPallet(strDeliveryDate, nhom,tvPalletCurrent.getText().toString());
                                                                }else {
                                                                    GetSealGroupPallet(strDeliveryDate, nhom,"0");
                                                                }
                                                            }else {
                                                                Utilities.speakingSomeThing(response.body(), ABASealBHXActivity.this);
                                                            }
//                                                            Utilities.dismissDialog(progressDialog);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<String> call, Throwable t) {
//                                                        Utilities.dismissDialog(progressDialog);
                                                        RetrofitError.errorNoAction(ABASealBHXActivity.this, new NoInternet(), TAG, view);
                                                    }
                                                });
                                            }
                                        });
                                        Dialog d = b.create();
                                        d.show();
                                        break;
                                }
                            }

                            @Override
                            public void onLongClick(SealGroup item, int position, int order) {

                            }
                        });

                        adapter.replace(response.body());
                        rcAddSealPallet.setAdapter(adapter);
                    }else {
                        Toast.makeText(ABASealBHXActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                    Utilities.dismissDialog(progressDialog);

                }
            }

            @Override
            public void onFailure(Call<List<SealGroup>> call, Throwable t) {
                Utilities.dismissDialog(progressDialog);
                RetrofitError.errorNoAction(ABASealBHXActivity.this, new NoInternet(), TAG, view);
            }
        });
    }
}