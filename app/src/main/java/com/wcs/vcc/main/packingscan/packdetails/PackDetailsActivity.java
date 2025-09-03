package com.wcs.vcc.main.packingscan.packdetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickPackShipPackDeleteParameter;
import com.wcs.vcc.api.PickPackShipPackDetailsParameter;
import com.wcs.wcs.databinding.ActivityPackDetailsBinding;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PackDetailsActivity extends ShowHomeButtonActivity {

    private ActivityPackDetailsBinding binding;
    private PackDetailsAdapter adapter;
    private String productId;
    private int cartonId;
    private String username, androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pack_details);
        Utilities.showBackIcon(getSupportActionBar());

        username = LoginPref.getUsername(this);
        androidId = Utilities.getAndroidID(getApplicationContext());

        adapter = new PackDetailsAdapter(new RecyclerViewItemListener<PackDetail>() {
            @Override
            public void onClick(PackDetail item, int position) {
            }

            @Override
            public void onLongClick(final PackDetail item, int position) {
                AlertDialog dialog = new AlertDialog.Builder(PackDetailsActivity.this)
                        .setMessage("Bạn có muốn xóa Pack này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deletePickPackShipPack(item.DispatchingProductPackID);
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .create();
                dialog.show();
            }
        });
        binding.rvPackDetails.setAdapter(adapter);

        productId = getIntent().getStringExtra("PRODUCT_ID");
        cartonId = getIntent().getIntExtra("CARTON_ID", 0);
        String productName = getIntent().getStringExtra("PRODUCT_NAME");
        String productNumber = getIntent().getStringExtra("PRODUCT_NUMBER");

        binding.setProductName(productName);
        binding.setProductNumber(productNumber);

        getPackDetails();
    }

    private void getPackDetails() {
        MyRetrofit.initRequest(this).getPackDetails(new PickPackShipPackDetailsParameter(UUID.fromString(productId), cartonId)).enqueue(new Callback<List<PackDetail>>() {
            @Override
            public void onResponse(Response<List<PackDetail>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<PackDetail> detailList = response.body();
                    adapter.replace(detailList);

                    double weight = 0;
                    for (PackDetail detail : detailList) {
                        weight += detail.NetWeight;
                    }
                    binding.setNWeight(NumberFormat.getInstance().format(weight));
                    binding.setNPacks(detailList.size());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void deletePickPackShipPack(int packId) {
        MyRetrofit.initRequest(this).deletePickPackShipPack(new PickPackShipPackDeleteParameter(username, androidId, packId)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    getPackDetails();
                    showMessage(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
