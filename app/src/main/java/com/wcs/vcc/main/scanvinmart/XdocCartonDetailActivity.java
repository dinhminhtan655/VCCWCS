package com.wcs.vcc.main.scanvinmart;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.atalay.bluetoothhelper.Model.BluetoothCallback;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.wcs.databinding.ActivityXdocCartonDetailBinding;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.packingscan.pack.Pack;
import com.wcs.vcc.main.packingscan.pack.PackAdapter;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class XdocCartonDetailActivity extends RingScanActivity implements BluetoothCallback {
    TextView tv_prev_barcode;
    EditText etTargetScanAsignCarton;
    TextView tvCartonLabel;
    TextView tvStoreNumberAssgin;
    TextView tvPrevBarcodeAssignCarton;
    View btCameraScanAssginCarton;
    EditText etInerCarton;
    Button btnAssignCartonToPallet;


    private ActivityXdocCartonDetailBinding binding;
    private PackAdapter adapter;
    private String username, androidId, barcode = "";
    private int cartonId;
    private boolean isCompleted;
    private String orderNumber;
    private int orderQuantity = 0, quantity = 0;
    private double orderNetWeight = 0, netWeight = 0,grossWeight = 0;
    private List<Pack> packList;
    private  String date;
    private  Integer storeNumber;

    private RadioButton raIncre;
    private RadioButton raDecre;
    private RadioGroup rdGroupType;
    private EditText inerQty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xdoc_carton_detail);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_xdoc_carton_detail);

        Utilities.showBackIcon(getSupportActionBar());
        setUpScan();

        username = LoginPref.getUsername(this);
        androidId = Utilities.getAndroidID(getApplicationContext());
        packList = new ArrayList<>();
        packList = new ArrayList<>();
//        raIncre = this.findViewById(R.id.raIncre);
//        raDecre = this.findViewById(R.id.raDecre);
        inerQty = this.findViewById(R.id.edQtyInner);
        rdGroupType = this.findViewById(R.id.rdGroupTypeProduct);
        adapter = new PackAdapter(new RecyclerViewItemListener<Pack>() {
            @Override
            public void onClick(Pack item, int position) {

            }

            @Override
            public void onLongClick(final Pack item, int position) {

            }
        });

        cartonId = getIntent().getIntExtra("CARTON_ID", 1);
        int cartonNumber = getIntent().getIntExtra("CARTON_NUMBER", 1);
        isCompleted = getIntent().getBooleanExtra("COMPLETED", false);
        orderNumber = getIntent().getStringExtra("ORDER_NUMBER");
        date =   getIntent().getStringExtra("ORDER_DATE");
        storeNumber =  getIntent().getIntExtra("STORE_NUMBER",0);

        binding.rvPickPackShipPack.setAdapter(adapter);
        binding.setCartonId(cartonId);
        binding.setCartonNumber(cartonNumber);
        binding.setIsCompleted(isCompleted);

        pickPackShipPacks();
    }

    private void pickPackShipPacks() {
        MyRetrofit.initRequest(this).LoadPackInCarton(cartonId).enqueue(new Callback<LoadPackInCartonResponse>() {
            @Override
            public void onResponse(Response<LoadPackInCartonResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    packList = PackInCartonInfo.toListPackModel(response.body().getPacks());
                    adapter.replace(packList);

                    orderQuantity = 0;
                    quantity = 0;
                    orderNetWeight = 0;
                    netWeight = 0;
                    grossWeight = 0;

                    for (Pack pack : packList) {
                        orderQuantity += pack.OrderQuantity;
                        quantity += pack.Quantity;
                        orderNetWeight += pack.OrderNetWeight;
                        netWeight += pack.NetWeight;
                        grossWeight += pack.GrossWeight;
                    }

                    binding.setOrderQuantity(orderQuantity);
                    binding.setQuantity(quantity);
                    binding.setOrderNetWeight(NumberFormat.getInstance().format(orderNetWeight));
                    binding.setNetWeight(NumberFormat.getInstance().format(netWeight));
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onBegin() {

    }

    @Override
    public void onErrorImportant(String s) {

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onSuccessful() {

    }
}
