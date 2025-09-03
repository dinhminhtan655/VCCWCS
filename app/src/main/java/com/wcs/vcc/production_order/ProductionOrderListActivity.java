package com.wcs.vcc.production_order;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.ComboCustomerResult;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.production_order.ProductionOrderResponse;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.SpinCusIdPref;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ProductionOrderListActivity extends AppCompatActivity {
    private ComboCustomerResult cusSelected;
    ArrayAdapter<ComboCustomerResult> spineradapter = null;
    List<ComboCustomerResult> customers = new ArrayList<>();
    ProductionOrderAdapter adateper;
    private Spinner comboCustomer;
    @BindView(R.id.rv_production_order)
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_production_order_list);

        ButterKnife.bind(this);

        comboCustomer = findViewById(R.id.sp_list_id_customer);

//        adateper = new ProductionOrderAdapter(new RecyclerViewItemListener<ProductionOrder>() {
//            @Override
//            public void onClick(ProductionOrder item, int position) {
//                Intent intent = new Intent(ProductionOrderListActivity.this, SavePackageHNActivity.class);
//                intent.putExtra("ProductionOrderNumber",item.getProductionOrderNumber());
//                intent.putExtra("Planned_Unit",item.getPlanned_Unit());
//                intent.putExtra("CustomerID",cusSelected.getCustomerID());
//                startActivity(intent);
//            }
//
//            @Override
//            public void onLongClick(ProductionOrder item, int position) {
//
//            }
//        });
        adateper = new ProductionOrderAdapter();
        getCustomer();

    }

    private void loadData(){
        MyRetrofit.initRequest(this).loadProductionOrder( cusSelected.getCustomerID()).enqueue(new Callback<ProductionOrderResponse>() {
            @Override
            public void onResponse(Response<ProductionOrderResponse> response, Retrofit retrofit) {
                adateper.notifyDataSetChanged();
                if(response.body()!=null){
                    adateper.replace(response.body().getProductionOrderList());
                    rv.setAdapter(adateper);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProductionOrderListActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không có data!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }
    private void getCustomer() {
        //khoi tao doi tuong jsonObject
        JsonObject jsonObject = new JsonObject();
        //dua vao kieu tra ve
        jsonObject.addProperty("StoreID", LoginPref.getStoreId(this));
        //goi API
        MyRetrofit.initRequest(this).loadListCustomer(jsonObject).enqueue(new Callback<List<ComboCustomerResult>>() {
            @Override
            public void onResponse(Response<List<ComboCustomerResult>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    customers = response.body();
                    spineradapter = new ArrayAdapter<ComboCustomerResult>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, customers);
                    comboCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            cusSelected = customers.get(position);
                            SpinCusIdPref.saveCusID(ProductionOrderListActivity.this,String.valueOf(cusSelected.getCustomerID()));
                            loadData();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    comboCustomer.setAdapter(spineradapter);
                }
            }


            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProductionOrderListActivity.this);
                alert.setTitle("Thông Báo");
                alert.setMessage("Không lấy được thông tin khách hàng!");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });
    }

}
