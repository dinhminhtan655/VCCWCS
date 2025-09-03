package com.wcs.vcc.main.CheckOutTrip;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.checkouttrip.response.CartonStatus;
import com.wcs.vcc.api.checkouttrip.response.CheckOutTripResponse;
import com.wcs.vcc.main.CheckOutTrip.Adapter.CartonStatusByCustomerClientAdapter;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CartonStatusByCustomerClientActivity extends AppCompatActivity {

    private ArrayList<CartonStatus> cartonStatuses;
    private String DONumber;
    private CartonStatusByCustomerClientAdapter adapter;

    @BindView(R.id.rv_check_out)
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carton_status_by_customer_client);
        ButterKnife.bind(this);
        DONumber =  getIntent().getStringExtra("DO_NUMBER");
        adapter = new CartonStatusByCustomerClientAdapter(new RecyclerViewItemListener<CartonStatus>() {
            @Override
            public void onClick(CartonStatus item, int position) {

            }

            @Override
            public void onLongClick(CartonStatus item, int position) {

            }
        });

        rv.setAdapter(adapter);

        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {

        MyRetrofit.initRequest(this).loadCartonByCustomerClient(DONumber).enqueue(new Callback<CheckOutTripResponse>() {
            @Override
            public void onResponse(Response<CheckOutTripResponse> response, Retrofit retrofit) {
                if(response.isSuccess()&& response.body()!=null){
                    cartonStatuses = response.body().getCartonStatsList();
                    adapter.notifyDataSetChanged();
                    adapter.replace(cartonStatuses);
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
