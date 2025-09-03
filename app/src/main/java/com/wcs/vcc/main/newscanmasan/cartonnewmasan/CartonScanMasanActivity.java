package com.wcs.vcc.main.newscanmasan.cartonnewmasan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickPackShipCartonDeleteParameter;
import com.wcs.vcc.api.PickPackShipCartonInsertParameter;
import com.wcs.vcc.api.PickPackShipCartonsParameter;
import com.wcs.vcc.api.PickPackShipFinishDOParameter;
import com.wcs.vcc.api.PickPackShipOrdersParameter;
import com.wcs.vcc.api.PickPackShipPackScanCartonParameter;
import com.wcs.vcc.api.PickPackShipPackageTypeUpdateParameter;
import com.wcs.vcc.api.RefeshStatusPackingParam;
import com.wcs.wcs.databinding.ActivityCartonScanMasanBinding;
import com.wcs.vcc.main.BarcodeFuncDef;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.newscanmasan.xongnhacarton.XongNhaCartonActivity;
import com.wcs.vcc.main.packingscan.PickPackShipOrder;
import com.wcs.vcc.main.packingscan.carton.Carton;
import com.wcs.vcc.main.packingscan.carton.CartonAdapter;
import com.wcs.vcc.main.packingscan.carton.PackageType;
import com.wcs.vcc.main.packingscan.carton.PackageTypeListener;
import com.wcs.vcc.main.pickship.CheckStatusPickPackShipActivity;
import com.wcs.vcc.preferences.DatePref;
import com.wcs.vcc.preferences.ItemCodePref;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.wcs.vcc.main.BarcodeFuncDef.CARTON_CLOSE;
import static com.wcs.vcc.main.BarcodeFuncDef.CARTON_NEW;
import static com.wcs.vcc.main.BarcodeFuncDef.CARTON_OPEN;
import static com.wcs.vcc.main.BarcodeFuncDef.CHECK_STATUS;
import static com.wcs.vcc.main.BarcodeFuncDef.GOTO_CARTON;
import static com.wcs.vcc.main.BarcodeFuncDef.STATUS_REFESH;
import static com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.ScanMasanActivity.DATE_FORMAT2;

public class CartonScanMasanActivity extends RingScanActivity {
    private Dialog dialog;
    private CartonAdapter adapter;
    private String username, androidId;
    private String orderNumber;
    private String storeNumber;
    private String nextStoreNumber;
    private String date, barcode;
    private ActivityCartonScanMasanBinding binding;
    private List<PackageType> packageTypes = new ArrayList<>();
    private int countSelected;
    List<Carton> cartons = new ArrayList<>();
    private TextView tvNextStoreNumber;

    private Carton curCarton;

    private int IS_LOAD_DATA = 1;

    private List<Carton> carList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_carton_scan_masan);
        Utilities.showBackIcon(getSupportActionBar());
        tvNextStoreNumber = findViewById(R.id.tvNextStoreNumber);

        setUpScan();


        curCarton = new Carton();


        username = LoginPref.getUsername(this);
        androidId = Utilities.getAndroidID(getApplicationContext());


        storeNumber = getIntent().getStringExtra("STORE_NUMBER");
        nextStoreNumber = getIntent().getStringExtra("NEXT_STORE_NUMBER");
        date =    DatePref.LoadOrderDate(CartonScanMasanActivity.this);
        orderNumber = getIntent().getStringExtra("ORDER_NUMBER");
        String clientName = getIntent().getStringExtra("CLIENT_NAME");
        barcode = ItemCodePref.LoadItemCode(CartonScanMasanActivity.this);

        tvNextStoreNumber.setText(" >> " + nextStoreNumber);


        curCarton.DispatchingOrderNumber = orderNumber;

        binding.setStoreNumber(Integer.parseInt(storeNumber.trim()));
        binding.setClientName(clientName);
        binding.setOrderNumber(orderNumber);


        adapter = new CartonAdapter(new RecyclerViewItemListener<Carton>() {
            @Override
            public void onClick(Carton item, int position) {
                if (item.PackageType.trim().length() == 0) {
                    showMessage("Bạn phải chọn loại thùng trước khi scan");
                    return;
                }
                Intent intent = new Intent(CartonScanMasanActivity.this, XongNhaCartonActivity.class);
                intent.putExtra("CARTON_ID", item.DispatchingProductCartonID);
                intent.putExtra("ORDER_NUMBER", item.DispatchingOrderNumber);
                intent.putExtra("CARTON_NUMBER", item.CartonNumber);
                intent.putExtra("CARTON_NUMBER", item.CartonNumber);
                intent.putExtra("COMPLETED", item.Completed);
                intent.putExtra("BARCODE", barcode);
                intent.putExtra("STORE_NUM", storeNumber);
                intent.putExtra("DATE", date);
                startActivity(intent);
            }

            @Override
            public void onLongClick(final Carton item, final int position) {
                AlertDialog dialog = new AlertDialog.Builder(CartonScanMasanActivity.this)
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
                View view = LayoutInflater.from(CartonScanMasanActivity.this).inflate(R.layout.layout_update_package_type, null, false);

                Spinner spType = view.findViewById(R.id.sp_ppsc);

                ArrayAdapter<PackageType> adapter = new ArrayAdapter<>(CartonScanMasanActivity.this, android.R.layout.simple_list_item_1, packageTypes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if (countSelected > 0) {
                            PackageType packageType = packageTypes.get(position);
                            PickPackShipPackageTypeUpdateParameter data = new PickPackShipPackageTypeUpdateParameter(
                                    username, androidId, item.DispatchingProductCartonID, packageType.Packages, packageType.WeightOfPackage);
                            updatePickPackShipPackageType(data);
                        }
                        countSelected++;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                spType.setAdapter(adapter);

                AlertDialog dialog = new AlertDialog.Builder(CartonScanMasanActivity.this)
                        .setView(view)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();
                dialog.show();
            }
        }, cartons);


        pickPackShipComboPackageType();
        pickPackShipCartons(Integer.parseInt(storeNumber.trim()), date);

    }

    @Override
    protected void onResume() {
        super.onResume();
        pickPackShipCartons(Integer.parseInt(storeNumber.trim()), date);
    }
    @Override
    public void onData(String data) {
        super.onData(data);
        Map<String, String> map = new HashMap<String, String>();
        if(BarcodeFuncDef.NEXT_STORE_NUM.equals(data)){
           // BarcodeFuncDef.execFunc(data,Cả);
        }
        else if (data.equals(""))
            scanCartonPickPackShipPack(data);
        else if (data.equals(CARTON_NEW)) {
            map.put("OrderDate", getIntent().getStringExtra("DATE"));
            map.put("STORENUM", storeNumber);
            map.put("DISHPATCHING", orderNumber);
            BarcodeFuncDef.execFunc(data, CartonScanMasanActivity.this, map);
        } else if (data.equals(STATUS_REFESH)) {
            onRefesh();
        } else if (data.substring(0, 2).equals(BarcodeFuncDef.CT)) {
            if (cartons != null&&cartons.size() > 0 ) {
                int cartonNumber = 0;
                try {
                    cartonNumber = Integer.parseInt(data.substring(2, data.length()));
                } catch (Exception e) {
                    Log.e("a",e.getMessage());
                }

                carList = new ArrayList<>();
                for (int i = 0; i < cartons.size(); i++) {
                    if (cartons.get(i).CartonNumber == cartonNumber) {
                        carList.add(new Carton(cartons.get(i).DispatchingProductCartonID, cartons.get(i).CartonNumber, cartons.get(i).PackageType, cartons.get(i).CartonDescription
                                , cartons.get(i).Quantity, cartons.get(i).StoreNumber, cartons.get(i).CustomerClientName, cartons.get(i).DispatchingOrderNumber, cartons.get(i).CUST_PO_NUMBER
                                , cartons.get(i).WeightOfPackage, cartons.get(i).NetWeight, cartons.get(i).Completed, cartons.get(i).DispatchingOrderID));
                    }
                }

                if (carList.size() == 0) {
                    Utilities.speakingSomeThing("Thùng không tồn tại", CartonScanMasanActivity.this);
                } else if (carList.size() == 1) {
                    Utilities.speakingSomeThing(carList.get(0).DispatchingProductCartonID + "", CartonScanMasanActivity.this);
                    curCarton = carList.get(0);
                    goToInside();
                }

            }
        } else if (!data.equals(CARTON_NEW) || !data.equals(CARTON_OPEN) || !data.equals(CARTON_CLOSE) ||
                !data.equals(STATUS_REFESH) || !data.equals(GOTO_CARTON) || !data.equals(CHECK_STATUS) || !data.substring(0, 2).equals("ST")
                || !data.substring(0, 2).equals(BarcodeFuncDef.CT) || !data.substring(0, 2).equals(BarcodeFuncDef.TW)) {
            finish();
        }

    }


    private void goToInside() {
        Intent intent = new Intent(CartonScanMasanActivity.this, XongNhaCartonActivity.class);
        intent.putExtra("CARTON_ID", curCarton.DispatchingProductCartonID);
        intent.putExtra("ORDER_NUMBER", curCarton.DispatchingOrderNumber);
        intent.putExtra("CARTON_NUMBER", curCarton.CartonNumber);
        intent.putExtra("COMPLETED", curCarton.Completed);
        intent.putExtra("BARCODE", barcode);
        intent.putExtra("STORE_NUM", storeNumber);
        intent.putExtra("DATE", date);
        startActivity(intent);
    }

    private void scanCartonPickPackShipPack(String scanResult) {
        PickPackShipPackScanCartonParameter params = new PickPackShipPackScanCartonParameter(username, androidId, scanResult, orderNumber);
        MyRetrofit.initRequest(this).scanCartonPickPackShipPack(params).enqueue(new Callback<String>() {
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
                    pickPackShipCartons(Integer.parseInt(storeNumber.trim()), date);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void pickPackShipCartons(int storeNumber, String date) {
        MyRetrofit.initRequest(this).pickPackShipCartons(new PickPackShipCartonsParameter(storeNumber, date, barcode, orderNumber)).enqueue(new Callback<List<Carton>>() {
            @Override
            public void onResponse(Response<List<Carton>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    cartons = response.body();
                    if (cartons.size() > 0) {

                        adapter.replace(cartons);
                        binding.rvPickPackShipCarton.setAdapter(adapter);
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
                if(cartons.size()>0&& IS_LOAD_DATA==1){
                    Carton cartonFistLine = cartons.get(0);
                    if(cartonFistLine.Completed){
                        gotoAsignCarton();
                    }
                }else if(IS_LOAD_DATA==1) {
                    gotoAsignCarton();
                }
                IS_LOAD_DATA = 0;
            }

            @Override
            public void onFailure(Throwable t) {
                String message = t.toString();
            }
        });
    }

    private void updatePickPackShipPackageType(PickPackShipPackageTypeUpdateParameter data) {
        MyRetrofit.initRequest(this).updatePickPackShipPackageType(data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipCartons(Integer.parseInt(storeNumber.trim()), date);
                    showMessage(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void gotoAsignCarton() {
//        Intent intent = new Intent(CartonScanMasanActivity.this, AddNewCarton.class);
//        intent.putExtra("OrderDate", getIntent().getStringExtra("DATE"));
//        intent.putExtra("STORENUM", storeNumber);
//        intent.putExtra("DISHPATCHING", curCarton.DispatchingOrderNumber);
//        startActivity(intent);
    }

    private void insertPickPackShipCarton() {
        MyRetrofit.initRequest(this).insertPickPackShipCarton(new PickPackShipCartonInsertParameter(username, androidId, orderNumber)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipCartons(Integer.parseInt(storeNumber.trim()), date);
                    showMessage(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void deletePickPackShipCarton(int cartonId) {
        MyRetrofit.initRequest(this).deletePickPackShipCarton(new PickPackShipCartonDeleteParameter(username, androidId, cartonId)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipCartons(Integer.parseInt(storeNumber.trim()), date);
                    showMessage(response.body());
                } else {
                    binding.rvPickPackShipCarton.setAdapter(new CartonAdapter());
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
                    pickPackShipCartons(Integer.parseInt(storeNumber.trim()), date);
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
        getMenuInflater().inflate(R.menu.pick_pack_ship_carton_new_masan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int itemId = item.getItemId();
        if (itemId == R.id.action_insert)
        {
            //gotoAsignCarton();
            insertPickPackShipCarton();
        }
        else if (itemId == R.id.action_done)
            onFinishDO();
        else if (itemId == R.id.action_refresh)
            onRefesh();
        else if (itemId == R.id.action_check_status)
            onCheckMiss();
        return true;
    }

    public void onFinishDO() {
        Utilities.basicDialog(this, "Bạn muốn chốt đơn?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishPickPackShipDO();
            }
        });
    }

    public void onRefesh() {
        MyRetrofit.initRequest(this).UpdateStatusPacking(new RefeshStatusPackingParam(cartons.get(0).DispatchingOrderID)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    pickPackShipCartons(Integer.parseInt(storeNumber.trim()), date);
                    refeshData();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void refeshData() {
        Calendar calendar = Calendar.getInstance();
        MyRetrofit.initRequest(this).pickPackShipOrders(new PickPackShipOrdersParameter(new SimpleDateFormat(DATE_FORMAT2).format(calendar.getTime()), orderNumber)).enqueue(new Callback<List<PickPackShipOrder>>() {
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

    public void onCheckMiss() {
        Intent intent = new Intent(CartonScanMasanActivity.this, CheckStatusPickPackShipActivity.class);
        intent.putExtra("STORE_NUMBER", storeNumber);
        intent.putExtra("DATE", date);
        startActivity(intent);
    }



}
