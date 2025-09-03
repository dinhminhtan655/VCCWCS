package com.wcs.vcc.main.scanvinmart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.XDockVinInboundItemScan;
import com.wcs.vcc.api.XDockVinInboundWeightReceiveView;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.wcs.vcc.main.scanvinmart.ScanVinmartActivity.DATE_FORMAT2;

public class ScanVinmartDetailActivity extends RingScanActivity {
    public static final String TAG = "ScanVinmartDetailActivity";

    @BindView(R.id.btChooseDate)
    Button btChooseDate;
    private Calendar calendar;
    private String reportDate, reportDate2;
    private int warehouseId = 0;
    private int storeId;
    private MenuItem item_kho;
    @BindView(R.id.tv_prev_barcode)
    TextView tv_prev_barcode;
    @BindView(R.id.rv_vinmartdetail)
    RecyclerView rv_vinmartdetail;
    @BindView(R.id.tvTotalActual)
    TextView tvTotalActual;
    @BindView(R.id.tvTotalBooking)
    TextView tvTotalBooking;

    ScanVinmartDetailAdapter adapter;

    String loai = "", strSupplierCode, strSupplierName, strItemCode, strItemName, strUoMCode;
    int thucNhan, strBooking, soDu;
    public static final String DATE_FORMAT = "yyyy/MM/dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_vinmart_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }
        setUpScan();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        SimpleDateFormat dateformat2 = new SimpleDateFormat(DATE_FORMAT2);
        reportDate2 = dateformat2.format(calendar.getTime());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));


//        tv_prev_barcode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//
////                b.getWindow().setLayout((7*width)/7,(3*height)/5);
//
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.scan_vin_menu, menu);

        item_kho = menu.findItem(R.id.action_menu);

        if (warehouseId == 0) {
            menu.findItem(R.id.vin_all).setChecked(true);
            item_kho.setTitle("All");
            loai = item_kho.getTitle().toString();
        } else if (warehouseId == 1) {
            menu.findItem(R.id.vin_hcm).setChecked(true);
            item_kho.setTitle("HCM");
            loai = item_kho.getTitle().toString();
        } else if (warehouseId == 2) {
            menu.findItem(R.id.vin_tinh).setChecked(true);
            item_kho.setTitle("Tinh");
            loai = item_kho.getTitle().toString();
        }
        setupActionSearch(menu);
        return true;
    }


    private void setupActionSearch(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    String text = newText;
                    adapter.filter(text);
                } catch (Exception e) {
                    Log.e("er", e + "");
                }
                return true;
            }
        });
    }




    public void loadDataScanning() {
        final EditText[] edtDiaThucNhanUpdate = new EditText[1];
        final TextView[] tvDiaTenNCCUpdate = new TextView[1];
        final TextView[] tvDiaTenHangUpdate = new TextView[1];
        final TextView[] tvDiaLoaiNhanUpdate = new TextView[1];
        final TextView[] tvDiaSoBookingUpdate = new TextView[1];
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("Delivery_Date", reportDate2);
        jsonObject2.addProperty("UserName", LoginPref.getUsername(ScanVinmartDetailActivity.this));
        MyRetrofit.initRequest(ScanVinmartDetailActivity.this).loadXDockVinInboundWeightReceiveView(jsonObject2).enqueue(new Callback<List<XDockVinInboundWeightReceiveView>>() {
            @Override
            public void onResponse(Response<List<XDockVinInboundWeightReceiveView>> response, Retrofit retrofit) {
                if (response.body() != null && response.isSuccess()) {
                    adapter = new ScanVinmartDetailAdapter(new RecyclerViewItemOrderListener<XDockVinInboundWeightReceiveView>() {
                        @Override
                        public void onClick(final XDockVinInboundWeightReceiveView item, int position, int order) {
                            switch (order) {
                                case 0:
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(ScanVinmartDetailActivity.this);
                                    LayoutInflater inflater = getLayoutInflater();
                                    @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.dialog_insert_vinmart, (ViewGroup) findViewById(R.layout.activity_scan_vinmart_detail));
                                    edtDiaThucNhanUpdate[0] = dialogView.findViewById(R.id.edtDiaThucNhan);
                                    tvDiaTenNCCUpdate[0] = dialogView.findViewById(R.id.tvDiaTenNCC);
                                    tvDiaTenHangUpdate[0] = dialogView.findViewById(R.id.tvDiaTenHang);
                                    tvDiaLoaiNhanUpdate[0] = dialogView.findViewById(R.id.tvDiaLoaiNhan);
                                    tvDiaSoBookingUpdate[0] = dialogView.findViewById(R.id.tvDiaSoBooking);


                                    tvDiaTenNCCUpdate[0].setText(item.Supplier_Name);
                                    tvDiaTenHangUpdate[0].setText(item.Item_Name);
                                    tvDiaLoaiNhanUpdate[0].setText(item.LoaiCan);
                                    tvDiaSoBookingUpdate[0].setText(String.valueOf(item.Booking));
                                    edtDiaThucNhanUpdate[0].setText(String.valueOf(item.Actual));


                                    builder.setCancelable(false).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).setPositiveButton("Cập Nhật", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            final JsonObject jsonObject = new JsonObject();

                                            jsonObject.addProperty("Doc_Number", item.Doc_Number);
                                            jsonObject.addProperty("SoBichNhan", edtDiaThucNhanUpdate[0].getText().toString());
                                            jsonObject.addProperty("UserName", LoginPref.getUsername(ScanVinmartDetailActivity.this));
                                            jsonObject.addProperty("DeviceNumber", Utilities.getAndroidID(ScanVinmartDetailActivity.this));

                                            if (Integer.parseInt(edtDiaThucNhanUpdate[0].getText().toString()) > item.Booking) {
                                                Toast.makeText(ScanVinmartDetailActivity.this, "Số thực nhận không thể lớn hơn số Booking", Toast.LENGTH_SHORT).show();

                                            } else {
                                                MyRetrofit.initRequest(ScanVinmartDetailActivity.this).editXDockVinInboundWeightReceive(jsonObject).enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Response<String> response, Retrofit retrofit) {
                                                        if (response.isSuccess() && response.body() != null) {
                                                            if (response.body().equals("OK")) {
                                                                loadDataScanning();
                                                                Toast.makeText(ScanVinmartDetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(ScanVinmartDetailActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {
                                                        Toast.makeText(ScanVinmartDetailActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                            }
                                        }
                                    });


                                    builder.setView(dialogView);
                                    AlertDialog b = builder.create();
                                    b.show();
                                    break;

                                case 1:
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ScanVinmartDetailActivity.this);
                                    builder2.setCancelable(false);
                                    builder2.setTitle("Xóa Mã Hàng " + item.Item_Name);
                                    builder2.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            JsonObject jsonObject = new JsonObject();

                                            jsonObject.addProperty("Doc_Number", item.Doc_Number);
                                            jsonObject.addProperty("UserName", LoginPref.getUsername(ScanVinmartDetailActivity.this));
                                            jsonObject.addProperty("DeviceNumber", Utilities.getAndroidID(ScanVinmartDetailActivity.this));

                                            MyRetrofit.initRequest(ScanVinmartDetailActivity.this).deleteXDockVinInboundWeightReceive(jsonObject).enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Response<String> response, Retrofit retrofit) {
                                                    if (response.isSuccess() && response.body() != null) {
                                                        if (response.body().equals("OK")) {
                                                            loadDataScanning();
                                                            Toast.makeText(ScanVinmartDetailActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(ScanVinmartDetailActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    Toast.makeText(ScanVinmartDetailActivity.this, "Kiểm tra lại kết nối mạng!", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });

                                    Dialog dialog = builder2.create();
                                    dialog.show();
                                    break;
                            }


                        }

                        @Override
                        public void onLongClick(final XDockVinInboundWeightReceiveView item, int position, int order) {


                        }
                    },response.body());
                    adapter.replace(response.body());
                    int t = 0;
                    int t2 = 0;
                    for (int i = 0; i < response.body().size(); i++) {
                        t = t + response.body().get(i).Actual;
                        t2 = t2 + response.body().get(i).Booking;
                        tvTotalActual.setText(String.valueOf(t));
                        tvTotalBooking.setText(String.valueOf(t2));
                    }
                    rv_vinmartdetail.setAdapter(adapter);
                }


            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(ScanVinmartDetailActivity.this, "Kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.vin_all) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_kho.setTitle("All");
                loai = item_kho.getTitle().toString();
            }
        } else if (itemId == R.id.vin_hcm) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_kho.setTitle("HCM");
                loai = item_kho.getTitle().toString();
            }
        } else if (itemId == R.id.vin_tinh) {
            if (!item.isChecked()) {
                item.setChecked(true);
                item_kho.setTitle("Tinh");
                loai = item_kho.getTitle().toString();
            }
        }
        return true;
    }

    @Override
    public void onData(String data) {
        super.onData(data);
        View view =  getCurrentFocus();
        if (view != null){
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

        final EditText edtDiaThucNhan;
        final TextView tvDiaTenNCC, tvDiaTenHang, tvDiaLoaiNhan, tvDiaSoBooking;
        final AlertDialog.Builder builder = new AlertDialog.Builder(ScanVinmartDetailActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.dialog_insert_vinmart, (ViewGroup) findViewById(R.layout.activity_scan_vinmart_detail));
        edtDiaThucNhan = dialogView.findViewById(R.id.edtDiaThucNhan);
        tvDiaTenNCC = dialogView.findViewById(R.id.tvDiaTenNCC);
        tvDiaTenHang = dialogView.findViewById(R.id.tvDiaTenHang);
        tvDiaLoaiNhan = dialogView.findViewById(R.id.tvDiaLoaiNhan);
        tvDiaSoBooking = dialogView.findViewById(R.id.tvDiaSoBooking);

        JsonObject jsonObject = new JsonObject();

        if (data.length() >= 13) {
            jsonObject.addProperty("Item_Code", "123");
            jsonObject.addProperty("Barcode", tv_prev_barcode.getText().toString());
            jsonObject.addProperty("Delivery_Date", reportDate2);
            jsonObject.addProperty("Is_Tinh", loai);
            jsonObject.addProperty("strItemBarCode", "BCODE");


        } else {
            jsonObject.addProperty("Item_Code", tv_prev_barcode.getText().toString());
            jsonObject.addProperty("Barcode", "123");
            jsonObject.addProperty("Delivery_Date", reportDate2);
            jsonObject.addProperty("Is_Tinh", loai);
            jsonObject.addProperty("strItemBarCode", "ICODE");





        }

        MyRetrofit.initRequest(ScanVinmartDetailActivity.this).loadXDockVinInboundItemScan(jsonObject).enqueue(new Callback<List<XDockVinInboundItemScan>>() {
            @Override
            public void onResponse(Response<List<XDockVinInboundItemScan>> response, Retrofit retrofit) {
                if (response.isSuccess() && response != null) {
                    if (response.body().size() != 0) {
                        strSupplierCode = response.body().get(0).Supplier_Code;
                        strSupplierName = response.body().get(0).Supplier_Name;
                        strItemCode = response.body().get(0).Item_Code;
                        strItemName = response.body().get(0).Item_Name;
                        strBooking = response.body().get(0).Booking;
                        strUoMCode = response.body().get(0).UoM_Code;
                        tvDiaTenNCC.setText(response.body().get(0).Supplier_Name);
                        tvDiaTenHang.setText(response.body().get(0).Item_Name);
                        tvDiaSoBooking.setText(String.valueOf(response.body().get(0).Booking));
                    } else {
                        tvDiaTenNCC.setText("Không xác định");
                        tvDiaTenHang.setText("Không xác định");
                        tvDiaSoBooking.setText("Không xác định");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        loadDataScanning();

        tvDiaLoaiNhan.setText(loai);
        builder.setCancelable(false);
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (edtDiaThucNhan.getText().toString().equals("")) {
                    Toast.makeText(ScanVinmartDetailActivity.this, "Thực nhận không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    thucNhan = Integer.parseInt(edtDiaThucNhan.getText().toString());

                    if (thucNhan > strBooking) {
                        Toast.makeText(ScanVinmartDetailActivity.this, "Số thực nhận không thể lớn hơn số booking", Toast.LENGTH_SHORT).show();
                    } else {
                        soDu = strBooking - thucNhan;

                        JsonObject jsonObject1 = new JsonObject();
                        jsonObject1.addProperty("Received_Date", reportDate2);
                        jsonObject1.addProperty("Supplier_Code", strSupplierCode);
                        jsonObject1.addProperty("Supplier_Name", strSupplierName);
                        jsonObject1.addProperty("Item_Code", strItemCode);
                        jsonObject1.addProperty("Item_Name", strItemName);
                        jsonObject1.addProperty("Order_Qty", strBooking);
                        jsonObject1.addProperty("Diff_Qty", soDu);
                        jsonObject1.addProperty("SoBichNhan", thucNhan);
                        jsonObject1.addProperty("BichViMang", strUoMCode);
                        jsonObject1.addProperty("UserName", LoginPref.getUsername(ScanVinmartDetailActivity.this));
                        jsonObject1.addProperty("LoaiCan", loai);

                        MyRetrofit.initRequest(ScanVinmartDetailActivity.this).insertXDockVinInboundWeightReceive(jsonObject1).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Response<String> response, Retrofit retrofit) {
                                if (response.isSuccess() && response != null) {
                                    if (response.body().equals("OK")) {
                                        Toast.makeText(ScanVinmartDetailActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        loadDataScanning();
                                    } else if (response.body().equals("FAILED")) {
                                        Toast.makeText(ScanVinmartDetailActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();

                                    } else if (response.body().equals("EXISTED")) {
                                        Toast.makeText(ScanVinmartDetailActivity.this, "Đã tồn tại", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(ScanVinmartDetailActivity.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        builder.setView(dialogView);
        AlertDialog b = builder.create();
        b.show();

        Display display = ((WindowManager) getSystemService(ScanVinmartDetailActivity.this.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        Log.v("width", width + "");
    }

    @OnClick(R.id.btChooseDate)
    public void chooseDate() {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                loadDataScanning();

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        loadDataScanning();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        loadDataScanning();
    }


    @Override
    protected void onResume() {
        Const.isActivating = true;
        super.onResume();
    }


    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }
}
