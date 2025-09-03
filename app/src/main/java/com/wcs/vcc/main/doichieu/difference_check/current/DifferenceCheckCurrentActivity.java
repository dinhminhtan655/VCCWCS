package com.wcs.vcc.main.doichieu.difference_check.current;

import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.wcs.wcs.R;
import com.wcs.vcc.api.DifferenceCheckCurrentParams;
import com.wcs.vcc.api.DifferenceCheckPalletUpdateParams;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.wcs.databinding.ActivityDifferenceCheckCurrentBinding;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;
import java.util.UUID;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DifferenceCheckCurrentActivity extends ShowHomeButtonActivity {

    private RecyclerView rv;
    private DifferenceCheckCurrentAdapter adapter;
    private UUID productId;
    private ActivityDifferenceCheckCurrentBinding binding;
    private String username;
    private String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_difference_check_current);
        username = LoginPref.getUsername(this);
        androidID = Utilities.getAndroidID(this);

        rv = findViewById(R.id.lv_difference_check_current);
        adapter = new DifferenceCheckCurrentAdapter(new RecyclerViewItemListener<DifferenceCheckCurrent>() {
            @Override
            public void onClick(DifferenceCheckCurrent item, int position) {

            }

            @Override
            public void onLongClick(DifferenceCheckCurrent item, int position) {

            }
        }, new IEventHandler<DifferenceCheckCurrent>() {
            @Override
            public void onAfterActualClick(DifferenceCheckCurrent item) {
                Log.i(TAG, "onAfterActualClick");
                updatePallet(item, 1);
            }

            @Override
            public void onCurrentActualClick(DifferenceCheckCurrent item) {
                Log.i(TAG, "onCurrentActualClick");
                updatePallet(item, 0);
            }
        });
        rv.setAdapter(adapter);

        String productIdString = getIntent().getStringExtra("PRODUCT_ID");
        productId = UUID.fromString(productIdString);
        String productNumber = getIntent().getStringExtra("PRODUCT_NUMBER");
        String productName = getIntent().getStringExtra("PRODUCT_NAME");

        binding.setProductNumber(productNumber);
        binding.setProductName(productName);

        getDifferenceCheckCurrent();
    }

    private void updatePallet(final DifferenceCheckCurrent item, final int flag) {
        String msg = "Are you sure to connect Current Quantity?";
        int quantity = item.CurrentActual;
        if (flag == 1) {
            msg = "Are you sure to connect After Quantity?";
            quantity = item.AfterDPActual;
        }
        final int finalQuantity = quantity;
        AlertDialog dialog = new AlertDialog.Builder(DifferenceCheckCurrentActivity.this)
                .setMessage(msg)
                .setPositiveButton("No", null)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateDifferenceCheckPallet(item.PalletID, finalQuantity, flag);
                    }
                })
                .create();
        dialog.show();
    }

    private void updateDifferenceCheckPallet(UUID palletId, int quantity, int flag) {
        DifferenceCheckPalletUpdateParams params = new DifferenceCheckPalletUpdateParams(
                username,
                androidID,
                palletId,
                quantity,
                flag
        );
        MyRetrofit.initRequest(this).updateDifferenceCheckPallet(params).enqueue(
                new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            boolean showed = showMessage(response.body());
                            if (!showed) {
                                Toast.makeText(getApplicationContext(), "Thành công.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }

    private void getDifferenceCheckCurrent() {
        DifferenceCheckCurrentParams params = new DifferenceCheckCurrentParams(productId);
        MyRetrofit.initRequest(this).getDifferenceCheckCurrent(params).enqueue(
                new Callback<List<DifferenceCheckCurrent>>() {
                    @Override
                    public void onResponse(Response<List<DifferenceCheckCurrent>> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            adapter.replace(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }
}
