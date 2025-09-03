package com.wcs.vcc.main.capdau;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.wcs.wcs.R;import com.wcs.vcc.api.BaseParameter;
import com.wcs.vcc.api.DistributionOrderCheckOutParameter;
import com.wcs.vcc.api.DistributionOrderUpdateParameter;
import com.wcs.vcc.api.DistributionOrderViewParameter;
import com.wcs.vcc.api.DriverChangeParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.wcs.databinding.DialogDistributionOrderBinding;
import com.wcs.wcs.databinding.DialogDistributionOrderInsertBinding;
import com.wcs.wcs.databinding.DriverBinding;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.main.giaonhanhoso.SignActivity;
import com.wcs.vcc.main.viewImage.ViewImageActivity;
import com.wcs.vcc.main.vo.Group;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemListener;
import com.wcs.vcc.utilities.Utilities;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DistributionOrderViewActivity extends ShowHomeButtonActivity {

    @BindView(R.id.btChooseDate)
    Button btChooseDate;

    @BindView(R.id.lv_distribution)
    RecyclerView recyclerView;

    public static final int SIGN_ACTIVITY_CODE = 100;
    public static final int SIGN_ACTIVITY_CODE_2 = 200;
    private Calendar calendar;
    private String reportDate;
    private int storeId;
    private DistributionOrderViewAdapter adapter;
    private TruckDriverAdapter truckDriverAdapter;
    private DriverAdapter driverAdapter;
    private String username;
    private int groupIndex, clickPosition, truckDriverPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_order_view);
        ButterKnife.bind(this);

        storeId = LoginPref.getStoreId(getApplicationContext());
        username = LoginPref.getUsername(getApplicationContext());
        String positionGroup = LoginPref.getPositionGroup(getApplicationContext());
        groupIndex = Group.convertGroupStringToInt(positionGroup);

        setTitle(username);

        calendar = Calendar.getInstance();

        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));

        adapter = new DistributionOrderViewAdapter(new RecyclerViewItemListener<DistributionOrderView>() {
            @Override
            public void onClick(final DistributionOrderView data, int position) {
                if (groupIndex == Group.MANAGER || groupIndex == Group.SUPERVISOR) {
                    clickPosition = position;

                    final View view = LayoutInflater.from(DistributionOrderViewActivity.this).inflate(R.layout.dialog_distribution_order, null, false);
                    final DialogDistributionOrderBinding binding = DataBindingUtil.bind(view);
                    if (binding != null) {
                        String checkoutString = data.IsCheckOut ? "Xóa cho ra" : "Cho ra";
                        binding.setData(data);
                        binding.btSign.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(DistributionOrderViewActivity.this, SignActivity.class);
                                intent.putExtra(ORDER_NUMBER, data.DistributionOrderNumber);
                                startActivityForResult(intent, SIGN_ACTIVITY_CODE);
                            }
                        });
                        final AlertDialog dialog = new AlertDialog.Builder(DistributionOrderViewActivity.this)
                                .setView(binding.getRoot())
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("OK", null)
                                .setNeutralButton(checkoutString, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DistributionOrderCheckOutParameter param = new DistributionOrderCheckOutParameter(
                                                data.DistributionOrderID,
                                                !data.IsCheckOut,
                                                binding.tvDistributionOrderRemark.getText().toString(),
                                                username
                                        );
                                        checkoutDistributionOrder(param);
                                    }
                                })
                                .create();

                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                double km = Utilities.getDoubleFromEditText(binding.tvDistributionOrderKilometer);
                                if (km <= data.KilometerLatest) {
                                    Snackbar.make(view, "Bạn phải nhập số Km lớn hơn lần trước", Snackbar.LENGTH_LONG).show();
                                    return;
                                }
                                double quantity = Utilities.getDoubleFromEditText(binding.tvDistributionOrderQuantity);
                                if (quantity <= data.QuantityLatest) {
                                    Snackbar.make(view, "Bạn phải nhập số Lít lớn hơn lần trước", Snackbar.LENGTH_LONG).show();
                                    return;
                                }
                                DistributionOrderUpdateParameter param = new DistributionOrderUpdateParameter(
                                        data.DistributionOrderID,
                                        km,
                                        quantity,
                                        1,
                                        binding.tvDistributionOrderRemark.getText().toString(),
                                        username
                                );
                                updateDistributionOrder(param);
                                dialog.dismiss();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn không có quyền thực hiện chức năng này.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLongClick(DistributionOrderView item, int position) {
                Intent intent = new Intent(DistributionOrderViewActivity.this, ViewImageActivity.class);
                intent.putExtra("src", item.AttachmentFile);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        getDistributionOrderView();

        getTruckDrivers();

        getDrivers();
    }

    @OnClick(R.id.btChooseDate)
    public void chooseDate() {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                getDistributionOrderView();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getDistributionOrderView();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getDistributionOrderView();
    }

    public void getDistributionOrderView() {
        reportDate = reportDate.split("T")[0];

        MyRetrofit.initRequest(this)
                .getDistributionOrderView(new DistributionOrderViewParameter(reportDate, storeId))
                .enqueue(new Callback<List<DistributionOrderView>>() {
                    @Override
                    public void onResponse(Response<List<DistributionOrderView>> response, Retrofit retrofit) {
                        List<DistributionOrderView> data = response.body();
                        if (response.isSuccess() && data != null) {
                            adapter.replace(data);
                            adapter.setOriginalData(data);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }

    public void getTruckDrivers() {

        MyRetrofit.initRequest(this)
                .getTruckDrivers(new BaseParameter(username))
                .enqueue(new Callback<List<TruckDriver>>() {
                    @Override
                    public void onResponse(Response<List<TruckDriver>> response, Retrofit retrofit) {
                        List<TruckDriver> data = response.body();
                        if (response.isSuccess() && data != null) {
                            truckDriverAdapter = new TruckDriverAdapter(DistributionOrderViewActivity.this, data);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }

    public void updateDistributionOrder(DistributionOrderUpdateParameter param) {
        reportDate = reportDate.split("T")[0];

        MyRetrofit.initRequest(this)
                .updateDistributionOrder(param)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            getDistributionOrderView();
                            getTruckDrivers();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cap_dau, menu);
        setupActionSearch(menu, adapter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_distribution_insert) {
            showDistributionInsertDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDistributionInsertDialog() {
        View view = LayoutInflater.from(DistributionOrderViewActivity.this).inflate(R.layout.dialog_distribution_order_insert, null, false);
        final DialogDistributionOrderInsertBinding binding = DataBindingUtil.bind(view);
        if (binding != null) {
            binding.actvDistributionOrderTruckNumber.setAdapter(truckDriverAdapter);
            binding.actvDistributionOrderTruckNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TruckDriver truckDriver = truckDriverAdapter.getItem(i);
                    if (truckDriver != null) {
                        clickPosition = i;
                        binding.setTruckDriver(truckDriver);
                        binding.btDistributionOrderChangeDriver.setEnabled(true);
                    }
                }
            });

            binding.btDistributionOrderChangeDriver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View driversView = LayoutInflater.from(DistributionOrderViewActivity.this).inflate(R.layout.driver, null, false);
                    final AlertDialog driversDialog = new AlertDialog.Builder(DistributionOrderViewActivity.this)
                            .setView(driversView)
                            .create();
                    driversDialog.show();

                    DriverBinding driversBinding = DataBindingUtil.bind(driversView);
                    if (driversBinding != null) {
                        driversBinding.actvDistributionOrderDriver.setAdapter(driverAdapter);
                        driversBinding.actvDistributionOrderDriver.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Driver driver = driverAdapter.getItem(i);
                                TruckDriver truckDriver = binding.getTruckDriver();
                                if (driver != null) {
                                    if (truckDriver != null) {
                                        changeDriver(new DriverChangeParameter(
                                                username,
                                                driver.DriverID,
                                                truckDriver.TruckID,
                                                truckDriver.DistributionOrderID));
                                        binding.tvDistributionOrderDriverName.setText(driver.DriverName);
                                        driversDialog.dismiss();
                                    }
                                }
                            }
                        });
                    }
                }
            });
            binding.tvDistributionOrderKilometer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    String data = textView.getText().toString();
                    if (actionId == EditorInfo.IME_ACTION_DONE && data.length() > 0) {
                        double km = Double.parseDouble(data);
                        double kmLatest = binding.getTruckDriver().KilometerLatest;
                        binding.tvDistributionOrderKilometerQty.setText(String.valueOf(km - kmLatest));
                    }
                    return true;
                }
            });
            binding.tvDistributionOrderQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        String kmString = binding.tvDistributionOrderKilometer.getText().toString();
                        String litString = binding.tvDistributionOrderQuantity.getText().toString();

                        double km = Double.parseDouble(kmString),
                                kmLatest = binding.getTruckDriver().KilometerLatest,
                                lit = Double.parseDouble(litString),
                                kmQty = km - kmLatest,
                                avgQty = 100 * lit / kmQty;
                        binding.tvDistributionOrderKilometerQty.setText(String.valueOf(kmQty));
                        binding.tvDistributionOrderAvgQty.setText(String.format(Locale.getDefault(), "%.2f", avgQty));
                    }
                    return true;
                }
            });

            final AlertDialog dialog = new AlertDialog.Builder(DistributionOrderViewActivity.this)
                    .setView(binding.getRoot())
                    .setNeutralButton("Ký", null)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", null)
                    .create();

            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(22);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String kmString = binding.tvDistributionOrderKilometer.getText().toString();
                    String litString = binding.tvDistributionOrderQuantity.getText().toString();
                    if (binding.getTruckDriver() == null) {
                        Toast.makeText(DistributionOrderViewActivity.this, "Hãy nhập Số xe", Toast.LENGTH_LONG).show();
                    } else if (kmString.length() > 0 && litString.length() > 0) {
                        double km = Double.parseDouble(kmString),
                                kmLatest = binding.getTruckDriver().KilometerLatest,
                                lit = Double.parseDouble(litString),
                                kmQty = km - kmLatest,
                                avgQty = 100 * lit / kmQty;

                        binding.tvDistributionOrderKilometerQty.setText(String.valueOf(kmQty));
                        binding.tvDistributionOrderAvgQty.setText(String.format(Locale.getDefault(), "%.2f", avgQty));

                        if (avgQty > binding.getTruckDriver().NormsWarning) {
                            Toast.makeText(DistributionOrderViewActivity.this, "CẢNH BÁO: ĐỊNH MỨC", Toast.LENGTH_LONG).show();
                        }

                        DistributionOrderUpdateParameter param = new DistributionOrderUpdateParameter(
                                binding.getTruckDriver().DistributionOrderID,
                                km,
                                lit,
                                binding.spDistributionOrderPump.getSelectedItemPosition() + 1,
                                binding.tvDistributionOrderRemark.getText().toString(),
                                username
                        );
                        updateDistributionOrder(param);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(DistributionOrderViewActivity.this, "Km và lít không được để trống", Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TruckDriver truckDriver = binding.getTruckDriver();
                    if (truckDriver != null) {
                        if (truckDriver.AttachmentFile.length() > 0) {
                            Intent intent = new Intent(DistributionOrderViewActivity.this, ViewImageActivity.class);
                            intent.putExtra("src", truckDriver.AttachmentFile);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(DistributionOrderViewActivity.this, SignActivity.class);
                            intent.putExtra(ORDER_NUMBER, "DF-" + binding.getTruckDriver().DistributionOrderID);
                            startActivityForResult(intent, SIGN_ACTIVITY_CODE_2);
                        }
                    }
                }
            });
        }
    }

    public void getDrivers() {

        MyRetrofit.initRequest(this)
                .getDrivers(new BaseParameter(username))
                .enqueue(new Callback<List<Driver>>() {
                    @Override
                    public void onResponse(Response<List<Driver>> response, Retrofit retrofit) {
                        List<Driver> data = response.body();
                        if (response.isSuccess() && data != null) {
                            driverAdapter = new DriverAdapter(DistributionOrderViewActivity.this, data);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }

    public void changeDriver(DriverChangeParameter params) {

        MyRetrofit.initRequest(this)
                .changeDriver(params)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {

                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }

    public void checkoutDistributionOrder(DistributionOrderCheckOutParameter param) {

        MyRetrofit.initRequest(this)
                .checkoutDistributionOrder(param)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            getDistributionOrderView();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_ACTIVITY_CODE && resultCode == RESULT_OK) {
            String fileName = data.getStringExtra("FILE_NAME");
            Log.i("adapter: ", fileName);
            adapter.getItems().get(clickPosition).AttachmentFile = fileName;
        } else if (requestCode == SIGN_ACTIVITY_CODE_2 && resultCode == RESULT_OK) {
            String fileName = data.getStringExtra("FILE_NAME");
            Log.i("truckDriverAdapter: ", fileName);
            truckDriverAdapter.getItem(clickPosition).AttachmentFile = fileName;
        }
    }
}
