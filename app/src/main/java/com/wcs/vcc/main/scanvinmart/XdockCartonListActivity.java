package com.wcs.vcc.main.scanvinmart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickPackShipCartonInsertParameter;
import com.wcs.vcc.api.PickPackShipFinishDOParameter;
import com.wcs.vcc.api.PickPackShipOrdersParameter;
import com.wcs.vcc.api.PickPackShipPackageTypeUpdateParameter;
import com.wcs.vcc.api.RefeshStatusPackingParam;
import com.wcs.wcs.databinding.ActivityXdockCartonListBinding;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.packingscan.PickPackShipOrder;
import com.wcs.vcc.main.packingscan.carton.Carton;
import com.wcs.vcc.main.packingscan.carton.CartonAdapter;
import com.wcs.vcc.main.packingscan.carton.PackageType;
import com.wcs.vcc.main.packingscan.carton.PackageTypeListener;
import com.wcs.vcc.main.pickship.CheckStatusPickPackShipActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class XdockCartonListActivity  extends EmdkActivity {

    private CartonAdapter adapter;
    private String username, androidId;
    private String orderNumber;
    private int storeNumber;
    private String date, barcode;
    private ActivityXdockCartonListBinding binding;
    private List<PackageType> packageTypes = new ArrayList<>();
    private int countSelected;
    List<Carton> cartons = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xdock_carton_list);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_xdock_carton_list);
        Utilities.showBackIcon(getSupportActionBar());
        setUpScan();

        username = LoginPref.getUsername(this);
        androidId = Utilities.getAndroidID(getApplicationContext());

        storeNumber = getIntent().getIntExtra("STORE_NUMBER", 1);
        date = getIntent().getStringExtra("DATE");
//        orderNumber = getIntent().getStringExtra("ORDER_NUMBER");
//        String clientName = getIntent().getStringExtra("CLIENT_NAME");
//        barcode = getIntent().getStringExtra("BARCODE");

        adapter = new CartonAdapter(new RecyclerViewItemListener<Carton>() {
            @Override
            public void onClick(Carton item, int position) {
                if (item.PackageType.trim().length() == 0) {
                    showMessage("Bạn phải chọn loại thùng trước khi scan");
                    return;
                }
                Intent intent = new Intent(XdockCartonListActivity.this, XdocCartonDetailActivity.class);
                intent.putExtra("CARTON_ID", item.DispatchingProductCartonID);
                intent.putExtra("ORDER_NUMBER", item.DispatchingOrderNumber);
                intent.putExtra("CARTON_NUMBER", item.CartonNumber);
                intent.putExtra("COMPLETED", item.Completed);
                intent.putExtra("ORDER_DATE", date);
                intent.putExtra("STORE_NUMBER",storeNumber);
                startActivity(intent);
            }

            @Override
            public void onLongClick(final Carton item, final int position) {
                AlertDialog dialog = new AlertDialog.Builder(XdockCartonListActivity.this)
                        .setMessage("Bạn có muốn xóa thùng này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deletePickPackShipCarton(item.DispatchingProductCartonID);
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .create();
                dialog.show();
            }
        }, new PackageTypeListener() {
            @Override
            public void onClickListen(final Carton item) {
                countSelected = 0;
                View view = LayoutInflater.from(XdockCartonListActivity.this).inflate(R.layout.layout_update_package_type, null, false);

                Spinner spType = view.findViewById(R.id.sp_ppsc);

                ArrayAdapter<PackageType> adapter = new ArrayAdapter<>(XdockCartonListActivity.this, android.R.layout.simple_list_item_1, packageTypes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if (countSelected > 0) {
//                            PackageType packageType = packageTypes.get(position);
//                            PickPackShipPackageTypeUpdateParameter data = new PickPackShipPackageTypeUpdateParameter(
//                                    username, androidId, item.DispatchingProductCartonID, packageType.Packages, packageType.WeightOfPackage);
//                            updatePickPackShipPackageType(data);
                        }
                        countSelected++;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                spType.setAdapter(adapter);

                AlertDialog dialog = new AlertDialog.Builder(XdockCartonListActivity.this)
                        .setView(view)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();
                dialog.show();
            }
        });



        binding.rvPickPackShipCarton.setAdapter(adapter);
        binding.setStoreNumber(storeNumber);
        binding.setOrderNumber(orderNumber);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pickPackShipComboPackageType();
    }

    @Override
    public void onData(String data) {
        super.onData(data);
        scanCartonPickPackShipPack(data);
    }

    private void scanCartonPickPackShipPack(String scanResult) {
        XdocScanCartonParam params = new XdocScanCartonParam(storeNumber,scanResult,date, username);
        MyRetrofit.initRequest(this).XdocScanCartonDispatching(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipComboPackageType();
                    showMessage(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void pickPackShipComboPackageType() {
        MyRetrofit.initRequest(this).pickPackShipComboPackageType().enqueue(new Callback<List<PackageType>>() {
            @Override
            public void onResponse(Response<List<PackageType>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    packageTypes = response.body();
                    pickPackShipCartons(storeNumber, date);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void pickPackShipCartons(int storeNumber, String date) {
        MyRetrofit.initRequest(this).CartonListByPO(date, storeNumber).enqueue(new Callback<CartonListResponse>() {
            @Override
            public void onResponse(Response<CartonListResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    cartons = CartonInfor.toListCartonModel(response.body().getCartons());
                    if (cartons.size() > 0) {
                        adapter.replace(cartons);

                        double weight = 0;
                        int packs = 0;
                        int count = 0;

                        for (Carton carton : cartons) {
                            weight += carton.NetWeight;
                            packs += carton.Quantity;
                            count = cartons.size();
                        }

                        binding.setPoNumber(cartons.get(0).CUST_PO_NUMBER);
                        binding.setNWeight(NumberFormat.getInstance().format(weight));
                        binding.setNPacks(packs);
                        binding.setCountPacks(count);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void updatePickPackShipPackageType(PickPackShipPackageTypeUpdateParameter data) {
        MyRetrofit.initRequest(this).updatePickPackShipPackageType(data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipCartons(storeNumber, date);
                    showMessage(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void insertPickPackShipCarton() {
        MyRetrofit.initRequest(this).insertPickPackShipCarton(new PickPackShipCartonInsertParameter(username, androidId, orderNumber)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipCartons(storeNumber, date);
                    showMessage(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void deletePickPackShipCarton(int cartonId) {
        MyRetrofit.initRequest(this).deleteXdocPickPackShipCarton(new XDockDeleteCartonDispatchingParam(cartonId,username)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipCartons(storeNumber, date);
                    showMessage(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void finishPickPackShipDO() {
        MyRetrofit.initRequest(this).finishPickPackShipDO(new PickPackShipFinishDOParameter(username, androidId, orderNumber)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipCartons(storeNumber, date);
                    showMessage(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pick_pack_ship_carton, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int itemId = item.getItemId();
        if (itemId == R.id.action_insert)
            insertPickPackShipCarton();
        return true;
    }

    public void onFinishDO(View view) {
        Utilities.basicDialog(this, "Bạn muốn chốt đơn?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishPickPackShipDO();
            }
        });
    }

    public  void  onRefesh(View view){
        MyRetrofit.initRequest(this).UpdateStatusPacking(new RefeshStatusPackingParam( cartons.get(0).DispatchingOrderID)).enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipCartons(storeNumber, date);
                    refeshData();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refeshData(){
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        LocalDate localDate = LocalDate.now();
        MyRetrofit.initRequest(this).pickPackShipOrders(new PickPackShipOrdersParameter(DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate).toString(),orderNumber)).enqueue(new Callback<List<PickPackShipOrder>>() {
            @Override
            public void onResponse(Response<List<PickPackShipOrder>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    binding.setClientName(response.body().get(0).CustomerClientName);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public  void onCheckMiss(View view){
        Intent intent = new Intent(XdockCartonListActivity.this, CheckStatusPickPackShipActivity.class);
        intent.putExtra("STORE_NUMBER", storeNumber);
        intent.putExtra("DATE", date);
        startActivity(intent);
    }
}