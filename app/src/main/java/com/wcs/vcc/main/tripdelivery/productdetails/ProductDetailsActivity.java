package com.wcs.vcc.main.tripdelivery.productdetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.TripDeliveryProductDetailsParams;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ProductDetailsActivity extends ShowHomeButtonActivity {

    private String orderNumber;
    private RecyclerView rv;
    private ProductDetailsAdapter adapter;
    private CheckBox cbAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        rv = findViewById(R.id.lv_trip_product_details);
        adapter = new ProductDetailsAdapter(new RecyclerViewItemListener<TripDeliveryProductDetails>() {
            @Override
            public void onClick(TripDeliveryProductDetails item, int position) {
                Toast.makeText(ProductDetailsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                showDialogUpdateDetail(item);
            }

            @Override
            public void onLongClick(TripDeliveryProductDetails item, int position) {
                Toast.makeText(ProductDetailsActivity.this, "Long Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        rv.setAdapter(adapter);

        orderNumber = getIntent().getStringExtra("ORDER_NUMBER");
        getTripDeliveryProductDetails();
    }

    private void showDialogUpdateDetail(TripDeliveryProductDetails item) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_update_detail_delivery_trip,null);
        TextInputEditText edtSoLuong = alertLayout.findViewById(R.id.edtSoluongUpdateDeltail);
        TextInputEditText edtKhoiLuong = alertLayout.findViewById(R.id.edtKhoiLuongUpdateDeltail);
        TextInputEditText edtGhiChu = alertLayout.findViewById(R.id.edtGhiChuUpdateDeltail);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(item.ProductName);
        builder.setView(alertLayout);
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ProductDetailsActivity.this, "Hủy", Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ProductDetailsActivity.this, "Đồng ý", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void getTripDeliveryProductDetails() {
        TripDeliveryProductDetailsParams params = new TripDeliveryProductDetailsParams(orderNumber);
        MyRetrofit.initRequest(this).getTripDeliveryProductDetails(params).enqueue(new Callback<List<TripDeliveryProductDetails>>() {
            @Override
            public void onResponse(Response<List<TripDeliveryProductDetails>> response, Retrofit retrofit) {
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
