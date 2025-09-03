package com.wcs.vcc.main.tripdelivery.orderlist;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.main.nhapphi.DanhSachPhiActivity;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.main.tripdelivery.deliverydetail.DeliveryDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

//import com.wcs.wcs.DanhSachPhiACtivity;

public class DeliveryOrderListActivity extends ShowHomeButtonActivity {


    @BindView(R.id.rv_trip_delivery_detail)
    RecyclerView rv;



    private TripDeliveryDetailAdapter adapter;

    @BindView(R.id.btn_DS_Phi_Da_Gui)
    Button btn_DanhSachPhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_list);




        ButterKnife.bind(this);
        adapter = new TripDeliveryDetailAdapter(new ItemTripDeliveryDetailListener() {
            @Override
            public void onClick(TripDeliveryDetail item, int position) {
                Intent intent = new Intent(DeliveryOrderListActivity.this, DeliveryDetailActivity.class);
                intent.putExtra("ORDER_NUMBER", item.OrderNumber);
                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);

        String tripID = getIntent().getStringExtra("TRIP_ID");
        tripDeliveryDetail(tripID);

        Danhsachphi();
    }


    private void tripDeliveryDetail(String tripID) {

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("TripID=%s", tripID));

        MyRetrofit.initRequest(this).tripDeliveryDetail(body).enqueue(new Callback<List<TripDeliveryDetail>>() {
            @Override
            public void onResponse(Response<List<TripDeliveryDetail>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    adapter.replace(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void Danhsachphi(){

        btn_DanhSachPhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(DeliveryOrderListActivity.this, DanhSachPhiActivity.class);
                startActivity(intent);
            }
        });
    }

}
