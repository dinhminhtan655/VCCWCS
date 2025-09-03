package com.wcs.vcc.main.CheckOutTrip;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.checkouttrip.request.ScanCheckOutRequest;
import com.wcs.vcc.api.checkouttrip.response.CheckOutTripResponse;
import com.wcs.vcc.api.checkouttrip.response.TripCarton;
import com.wcs.vcc.main.CheckOutTrip.Adapter.CustomerClientByTripAdapter;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CustomerClientByTripActivity extends EmdkActivity {
    @BindView(R.id.rv_check_out)
    RecyclerView rv;
    CustomerClientByTripAdapter adapter;
    String TripNumber = "",DeliveryDate;

    @BindView(R.id.tvCountOrder)
    TextView tvCountOderl;
    @BindView(R.id.tvCartonTotal1)
    TextView tvCartonTotal1;
    @BindView(R.id.edShipMentID)
    TextView edShipMentID;
    @BindView(R.id.tvCartonTotal2)
    TextView tvCartonTotal2;
    @BindView(R.id.edDriver)
    TextView edDriver;
    @BindView(R.id.edVehicel)
    TextView edVehicel;
    @BindView(R.id.edRemark)
    TextView edRemark;

    @BindView(R.id.btnShip)
    Button btnShip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_client_by_trip);
        ButterKnife.bind(this);
        Utilities.showBackIcon(getSupportActionBar());
        setUpScan();
        TripNumber =  getIntent().getStringExtra("TRIP_NUMBER");
        DeliveryDate =  getIntent().getStringExtra("DeliveryDate");
        adapter = new CustomerClientByTripAdapter(new RecyclerViewItemListener<TripCarton>() {
            @Override
            public void onClick(TripCarton item, int position) {

                Intent intent = new Intent(CustomerClientByTripActivity.this,
                        CartonStatusByCustomerClientActivity.class);
                intent.putExtra("DO_NUMBER", item.getDispatchingOrderNumber());
                startActivity(intent);
            }

            @Override
            public void onLongClick(TripCarton item, int position) {

            }
        });

        getData();
        rv.setAdapter(adapter);
        btnShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ATMShipmentID = "",Vehicle= "",DriverName= "",Remark= "",DeliveryDate= "",UserName= "",_TripNumber= "";
                if(edShipMentID.getText() != null){
                    ATMShipmentID = edShipMentID.getText().toString();
                }
                if(edVehicel.getText() != null){
                    Vehicle = edVehicel.getText().toString();
                }
                if(edDriver.getText()!=null ){
                    DriverName = edDriver.getText().toString();
                }
                if(edRemark.getText()!=null ){
                    Remark = edRemark.getText().toString();
                }

                _TripNumber = TripNumber;

                ScanCheckOutRequest request = new ScanCheckOutRequest();
                request.setATMShipmentID(ATMShipmentID);
                request.setVehicle(Vehicle);
                request.setDriverName(DriverName);
                request.setRemark(Remark);
                request.setTripNumber(_TripNumber);
                request.setUserName(LoginPref.getUsername(CustomerClientByTripActivity.this));
                request.setDeliveryDate(DeliveryDate);
                MyRetrofit.initRequest(CustomerClientByTripActivity.this).scanShipConfirm(_TripNumber,request).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if(response.isSuccess() && response.body().length() == 0){
                            showMessage("Đã ship thành công!");
                        }
                        else{
                            showMessage(response.body());
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showMessage(t.getMessage());
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onData(String data) {
        super.onData(data);
        ScanCheckOutRequest request = new ScanCheckOutRequest();
        request.setCartonBarcode(data);
        request.setTripNumber(TripNumber);
        request.setUserName(LoginPref.getUsername(this));
        request.setDeviceNumber(Utilities.getAndroidID(getApplicationContext()));
        if(edShipMentID.getText() != null){
            request.setATMShipmentID("ABC");
        }

        MyRetrofit.initRequest(this).scanCheckOut(request).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if(response.isSuccess() && response.body().length() == 0){
                    getData();
                }
                else{
                    showMessage(response.body());
                }

            }

            @Override
            public void onFailure(Throwable t) {
                showMessage(t.getMessage());
            }
        });
    }

    private void getData() {
        MyRetrofit.initRequest(this).loadCustomerByTrip(TripNumber).enqueue(new Callback<CheckOutTripResponse>() {
            @Override
            public void onResponse(Response<CheckOutTripResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    ArrayList<TripCarton> rs = response.body().getTripCartonList();
                    if(rs.size()>0){
                        int countOrder = rs.size();
                        int cartonTotal1=0;
                        int cartonTotal2=0;
                        for(int i=0;i<rs.size();i++){
                            cartonTotal1=cartonTotal1+rs.get(i).getTotalPlanCarton();
                            cartonTotal2 =cartonTotal2+rs.get(i).getTotalCartonDispatch();
                        }

                        tvCartonTotal1.setText("Kho Giao: "+ String.valueOf(cartonTotal1));
                        tvCartonTotal2.setText("Thực nhận : " +String.valueOf(cartonTotal2));
                        tvCountOderl.setText("Điểm giao: " + String.valueOf(countOrder));
                    }
                    adapter.replace(response.body().getTripCartonList());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
