package com.wcs.vcc.main.containerandtruckinfor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.containerandtruckinfor.model.ContainerAndTruckInfo;
import com.wcs.vcc.main.containerandtruckinfor.model.InfoSignedup;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.main.emdk.ScanListener;
import com.wcs.wcs.R;
import com.wcs.vcc.api.ContainerAndTruckInfoParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ContainerAndTruckInfoActivity extends BaseActivity implements ScanListener{
    public static final String TAG = ContainerAndTruckInfoActivity.class.getSimpleName();
    private View.OnClickListener tryAgain;
    private MenuItem item_gate;
    private int gate;
    private Toolbar toolbar;
    private ContainerAndTruckAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private ListView listView;
    private FloatingActionButton floatBtnAdd;
    private ImageView ivCameraScan;
    private EditText et_target_scan, edtSearchCarNumber;
    private TextView tv_prev_barcode, tvFromDate, tvToDate;
    private ContainerAndTruckInfo info;
    private List<ContainerAndTruckInfo> infoList;
    private Set<String> carNumberSet = new HashSet<>();
    private Set<String> containerNumSet = new HashSet<>();
    private Set<String> sealNumSet = new HashSet<>();
    private Set<String> driverNameSet = new HashSet<>();
    private Set<String> identitySet = new HashSet<>();
    private Set<String> phoneNumberrSet = new HashSet<>();
    private ArrayList<InfoSignedup> contList = new ArrayList<>();
    private ArrayList<InfoSignedup> contList2 = new ArrayList<>();


    private RadioGroup radiGroupCurHis;
    private RadioButton btnRadiCur, btnRadiHis;
    private LinearLayout lnDate;

    //Dialog checkout
    private FragmentManager fm;

    private Calendar calendarFrom, calendarTo;
    private String strDateFrom, strDateTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_and_truck_infor);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatBtnAdd = findViewById(R.id.floatBtnAdd);
        ivCameraScan = findViewById(R.id.ivCameraScan);
        et_target_scan = findViewById(R.id.et_target_scan);
        edtSearchCarNumber = findViewById(R.id.edtSearchCarNumber);
        tv_prev_barcode = findViewById(R.id.tv_prev_barcode);
        tvFromDate = findViewById(R.id.tvFromDate);
        tvToDate = findViewById(R.id.tvToDate);
        radiGroupCurHis = findViewById(R.id.radiGroupCurHis);
        btnRadiCur = findViewById(R.id.btnRadiCur);
        btnRadiHis = findViewById(R.id.btnRadiHis);
        lnDate = findViewById(R.id.lnDate);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        fm = getSupportFragmentManager();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        assert refreshLayout != null;
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkSwithRadio();
            }
        });
        infoList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lvOrderDetail);
        assert listView != null;

//        listView.setAdapter(adapter);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSwithRadio();
            }
        };

        showHideWhenScroll();

        calendarFrom = Calendar.getInstance();
        calendarTo = Calendar.getInstance();

        strDateFrom = Utilities.formatDateTime_MMddyyyyHHmmssFromMili(calendarFrom.getTimeInMillis());
        strDateTo = Utilities.formatDateTime_MMddyyyyHHmmssFromMili(calendarTo.getTimeInMillis());

        Utilities.setUnderLine(tvFromDate, Utilities.formatDate_ddMMyyyy4(strDateFrom));
        Utilities.setUnderLine(tvToDate, Utilities.formatDate_ddMMyyyy4(strDateTo));

        floatBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContainerAndTruckInfoActivity.this, SignUpContInOutActivity.class);
                Bundle bundle = new Bundle();
                ArrayList<String> carNumberList = new ArrayList<>(carNumberSet);
                ArrayList<String> containerNumList = new ArrayList<>(containerNumSet);
                ArrayList<String> sealNumList = new ArrayList<>(sealNumSet);
                ArrayList<String> driverNameList = new ArrayList<>(driverNameSet);
                ArrayList<String> identityList = new ArrayList<>(identitySet);
                ArrayList<String> phoneNumberList = new ArrayList<>(phoneNumberrSet);
                bundle.putSerializable("conset", contList);
                intent.putStringArrayListExtra("containerset", carNumberList);
                intent.putStringArrayListExtra("containernumset", containerNumList);
                intent.putStringArrayListExtra("sealnumset", sealNumList);
                intent.putStringArrayListExtra("drivernameset", driverNameList);
                intent.putStringArrayListExtra("identityset", identityList);
                intent.putStringArrayListExtra("phonenumberset", phoneNumberList);
                intent.putExtra("consetbun", bundle);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });

        ivCameraScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ContainerAndTruckInfoActivity.this);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setCaptureActivity(ScanCameraPortrait.class);
                integrator.initiateScan();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                info = adapter.getItem(position);
//                ContainerAndTruckDialog.newInstance(info, false).show(getSupportFragmentManager(), "ContainerAndTruckBottomSheet");
                Intent intent = new Intent(ContainerAndTruckInfoActivity.this, SignUpContInOutActivity.class);
                intent.putExtra("infocont", info);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });

        et_target_scan.addTextChangedListener(new TextWatcher() {
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
                    onData(contents.replaceAll("\n", ""));
                }
            }
        });

        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(ContainerAndTruckInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendarFrom.set(year, monthOfYear, dayOfMonth);
                        strDateFrom = Utilities.formatDateTime_MMddyyyyHHmmssFromMili(calendarFrom.getTimeInMillis());
                        tvFromDate.setText(Utilities.formatDate_ddMMyyyy4(strDateFrom));
                        getContainerAndTruckInfo(tvFromDate, strDateFrom, strDateTo);

                    }
                }, calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), calendarFrom.get(Calendar.DAY_OF_MONTH));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(ContainerAndTruckInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendarTo.set(year, monthOfYear, dayOfMonth);
                        strDateTo = Utilities.formatDateTime_MMddyyyyHHmmssFromMili(calendarTo.getTimeInMillis());
                        tvToDate.setText(Utilities.formatDate_ddMMyyyy4(strDateTo));
                        getContainerAndTruckInfo(tvToDate, strDateFrom, strDateTo);
                    }
                }, calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        btnRadiCur.setChecked(true);
        if (btnRadiCur.isChecked()) {
            lnDate.setVisibility(View.GONE);
//            getContainerAndTruckInfo(radiGroupCurHis, "","");
        }

        radiGroupCurHis.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup groupRadi, int checkedId) {
                switch (checkedId) {
                    case R.id.btnRadiCur:
                        floatBtnAdd.setVisibility(View.VISIBLE);
                        btnRadiCur.setChecked(true);
                        btnRadiHis.setChecked(false);
                        lnDate.setVisibility(View.GONE);
                        getContainerAndTruckInfo(radiGroupCurHis, "", "");
                        break;
                    case R.id.btnRadiHis:
                        floatBtnAdd.setVisibility(View.GONE);
                        btnRadiCur.setChecked(false);
                        btnRadiHis.setChecked(true);
                        lnDate.setVisibility(View.VISIBLE);
                        getContainerAndTruckInfo(radiGroupCurHis, strDateFrom, strDateTo);
                        break;
                }
            }
        });

        edtSearchCarNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showHideWhenScroll() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mLastFirstVisibleItem < firstVisibleItem) {
                    floatBtnAdd.hide();

                }
                if (mLastFirstVisibleItem > firstVisibleItem) {

                    floatBtnAdd.show();
                }

                mLastFirstVisibleItem = firstVisibleItem;
            }
        });

    }

    public void getContainerAndTruckInfo(final View view, String strDateFrom, String strDateTo) {
        final ProgressDialog bar = Utilities.getProgressDialog(this, "Đang tải dữ liệu..");
        bar.show();
        refreshLayout.setRefreshing(false);

        if (!WifiHelper.isConnected(getApplicationContext())) {
            RetrofitError.errorWithAction(getApplicationContext(), new NoInternet(), TAG, view, tryAgain);
            bar.dismiss();
            return;
        }
        MyRetrofit.initRequest(getApplicationContext()).getContainerAndTruckInfo(new ContainerAndTruckInfoParameter(gate, LoginPref.getStoreId(this), strDateFrom, strDateTo)).enqueue(new Callback<List<ContainerAndTruckInfo>>() {
            @Override
            public void onResponse(Response<List<ContainerAndTruckInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter = new ContainerAndTruckAdapter(ContainerAndTruckInfoActivity.this, new LinkedList<ContainerAndTruckInfo>());
                    infoList.clear();
                    adapter.clear();
                    if (btnRadiCur.isChecked()) {
                        for (ContainerAndTruckInfo con : response.body()) {
                            if (!con.CheckOut) {
                                infoList.add(con);
                            }
                        }
                    } else {
                        infoList.addAll(response.body());
                    }
                    adapter.addAll(infoList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < response.body().size(); i++) {
                        carNumberSet.add(response.body().get(i).ContainerNum);
                        containerNumSet.add(response.body().get(i).TruckIn);
                        sealNumSet.add(response.body().get(i).SealNumber);
                        phoneNumberrSet.add(String.valueOf(response.body().get(i).DriverMobilePhone));
                        driverNameSet.add(response.body().get(i).DriverName);
                        identitySet.add(response.body().get(i).DriverIDCardNo);
                        contList2.add(new InfoSignedup(response.body().get(i).ContainerNum, String.valueOf(response.body().get(i).DriverMobilePhone), response.body().get(i).DriverName, response.body().get(i).DriverIDCardNo));
                    }

                    Collection<InfoSignedup> list = contList2;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        contList = (ArrayList<InfoSignedup>) list.stream().distinct().collect(Collectors.toList());
                    }

                    edtSearchCarNumber.setText(edtSearchCarNumber.getText().toString());
                }
                bar.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(getApplicationContext(), t, TAG, view, tryAgain);
                bar.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.container_checking, menu);
        item_gate = menu.findItem(R.id.action_menu);
        if (gate == 0) {
            menu.findItem(R.id.action_all).setChecked(true);
            item_gate.setTitle(getString(R.string.tat_ca));
        } else if (gate == 1) {
            menu.findItem(R.id.action_g1).setChecked(true);
            item_gate.setTitle(getString(R.string.gate_1));
        } else if (gate == 2) {
            menu.findItem(R.id.action_g2).setChecked(true);
            item_gate.setTitle(getString(R.string.gate_2));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            onBackPressed();
        else if (itemId == R.id.action_all) {
            if (!item.isChecked()) {
                item.setChecked(true);
                gate = 0;
                checkSwithRadio();
                item_gate.setTitle(getString(R.string.tat_ca));
            }
        } else if (itemId == R.id.action_g1) {
            if (!item.isChecked()) {
                item.setChecked(true);
                gate = 1;
                checkSwithRadio();
                item_gate.setTitle(getString(R.string.gate_1));
            }
        } else if (itemId == R.id.action_g2) {
            if (!item.isChecked()) {
                item.setChecked(true);
                gate = 2;
                checkSwithRadio();
                item_gate.setTitle(getString(R.string.gate_2));
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkSwithRadio();
    }

    @Override
    public void onData(String data) {
        data = data.trim();
        et_target_scan.setText("");
        tv_prev_barcode.setText(data);

        boolean isExits = true;

        for (ContainerAndTruckInfo con : infoList) {
            if (con.OrderNumber.equalsIgnoreCase(data)) {
                if (!con.CheckOut) {

                } else {


                    String finalData = data;

                }
                isExits = true;
                break;
            } else {
                isExits = false;
            }
        }

        if (!isExits) {
            Utilities.speakingSomeThingslow("Không tồn tại hoặc đã checkout", ContainerAndTruckInfoActivity.this);
            Toast.makeText(this, "Không tồn tại hoặc đã checkout", Toast.LENGTH_SHORT).show();
        }


    }

//    private void showDialogCheckOut

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String dataScan = result.getContents();
                onData(dataScan);
            }
        }

//        if (requestCode == CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
//
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        contList.clear();
    }

    private void checkSwithRadio() {
        if (btnRadiCur.isChecked()) {
            getContainerAndTruckInfo(toolbar, "", "");
        } else {
            getContainerAndTruckInfo(toolbar, strDateFrom, strDateTo);
        }

    }




    //    @Override
//    public void sendInput(boolean success) {
//        if (success)
//            getContainerAndTruckInfo(toolbar);
//    }
}
