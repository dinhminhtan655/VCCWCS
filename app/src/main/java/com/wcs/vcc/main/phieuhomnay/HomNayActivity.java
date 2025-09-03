package com.wcs.vcc.main.phieuhomnay;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.MainActivity;
import com.wcs.vcc.main.emdk.ScanListener;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.wcs.R;
import com.wcs.vcc.api.InOutToDayInfo;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.detailphieu.OrderDetailActivity;
import com.wcs.vcc.main.phieuhomnay.giaoviec.GiaoViecActivity;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class HomNayActivity extends EmdkActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, ScanListener {

    private static final String RO = "RO";
    private static final String DO = "DO";

    public static final String TAG = "HomNayActivity";

    @BindView(R.id.lvOrderDetail)
    ListView listView;
//    @BindView(R.id.tv_customerName)
//    TextView cusName;
    @BindView(R.id.btChooseDate)
    Button btChooseDate;
    @BindView(R.id.et_target_scan)
    EditText etTargetScan;
    @BindView(R.id.tv_prev_barcode)
    TextView tvPrevBarcode;
    @BindView(R.id.ivCameraScan)
    ImageView ivCameraScan;
    private View.OnClickListener action;
    private HomNayAdapter adapter;
    private Calendar calendar;
    private String reportDate;
    private String customerID;
    private List<HomNayInfo> list;

    private String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom_nay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        Bundle b = getIntent().getExtras();
        if (b != null) {
            key = b.getString("type");
        }


        customerID = getIntent().getStringExtra(KhachHangActivity.CUSTOMER_ID);

        ButterKnife.bind(this);

//        cusName.setText(getIntent().getStringExtra(KhachHangActivity.CUSTOMER_NAME));
        long date = getIntent().getLongExtra(KhachHangActivity.DATE, 0);

        calendar = Calendar.getInstance();
        if (key == null) {
            calendar.setTimeInMillis(date);
        }
//        calendar.setTimeInMillis(date);

        list = new ArrayList<>();

        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));

        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhieuHomNay(listView);
            }
        };
        adapter = new HomNayAdapter(this, new ArrayList<HomNayInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        getPhieuHomNay(listView);

        ivCameraScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.ScanByCamera(HomNayActivity.this);
            }
        });

        etTargetScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String contents = s.toString();
                if (contents.contains("\n")) {
                    onData(contents.replaceAll("\t\t\n", ""));
                }
            }
        });
    }

    public void getPhieuHomNay(final View view) {
        String strCusID;
        if (key != null) {
            strCusID = "00000000-0000-0000-0000-000000000000";
        } else {
            strCusID = customerID;
            key = "SO";
        }
        reportDate = reportDate.split("T")[0];
        adapter.clear();
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getPhieuHomNay(new InOutToDayInfo(UUID.fromString(strCusID), reportDate,key , MainActivity.storeId)).enqueue(new Callback<List<HomNayInfo>>() {
            @Override
            public void onResponse(Response<List<HomNayInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
//                    tvOrderDate.setText(response.body().get(0).getOrderDate());
                    list.addAll(response.body());
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(HomNayActivity.this, t, TAG, view, action);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doSearch(newText);
                return true;
            }
        });
        return true;
    }

    private void doSearch(String keyword) {
        adapter.getFilter().filter(keyword);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        return true;
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, GiaoViecActivity.class);
        HomNayInfo item = adapter.getItem(position);
        if (item != null) {
            intent.putExtra("order_number", item.getOrderNumber());
        }
        startActivity(intent);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HomNayInfo item = adapter.getItem(position);
        if (item != null) {
            Intent intent = new Intent(HomNayActivity.this, OrderDetailActivity.class);
            intent.putExtra(ORDER_NUMBER, item.getOrderNumber());
            intent.putExtra("SCAN_TYPE", item.getScannedType());
            intent.putExtra("CUSTOMER_TYPE", item.getCustomerType());
            intent.putExtra("CUSTOMER_NUMBER", item.getCustomerNumber());
            intent.putExtra("DISPATCHING_ID", item.getDispatchingOrderID());
            startActivity(intent);
        }
    }

    @OnClick(R.id.btChooseDate)
    public void chooseDate() {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                getPhieuHomNay(listView);
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
        getPhieuHomNay(listView);
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getPhieuHomNay(listView);
    }

    @Override
    public void onData(String data) {
        super.onData(data);
        data = data.trim();
        tvPrevBarcode.setText(data);
        etTargetScan.getText().clear();
        boolean bExitst = false;
        for (HomNayInfo hn : list) {
            HashMap<String, String> barcodeInfo = Utilities.barcodeInfo(getApplicationContext(), data);
            if (barcodeInfo != null) {
                String type = barcodeInfo.get("type");
                String number = barcodeInfo.get("number");
                Bundle args = new Bundle();
                args.putString("BARCODE", data);
                if (type.equalsIgnoreCase("do") ||
                        type.equalsIgnoreCase("ro")) {
                    Log.d("compare", "" + Integer.parseInt(number) +" && " + Integer.parseInt(hn.getOrderNumber().split("-")[1]));
                    if (Integer.parseInt(number) == Integer.parseInt(hn.getOrderNumber().split("-")[1])) {
                        bExitst = true;
                        Intent intent = new Intent(HomNayActivity.this, OrderDetailActivity.class);
                        intent.putExtra(ORDER_NUMBER, hn.getOrderNumber());
                        intent.putExtra("SCAN_TYPE", hn.getScannedType());
                        intent.putExtra("CUSTOMER_TYPE", hn.getCustomerType());
                        startActivity(intent);
                        break;
                    }else {
                        bExitst = false;
                    }
                }
            }
        }
        if(!bExitst) {
            Utilities.speakingSomeThingslow("Không tồn tại!", HomNayActivity.this);
            Toast.makeText(this, "Không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String dataScan = result.getContents();
                onData(dataScan.trim());
            }
        }
    }
}
