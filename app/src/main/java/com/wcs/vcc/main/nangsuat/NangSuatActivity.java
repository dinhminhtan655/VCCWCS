package com.wcs.vcc.main.nangsuat;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.wcs.wcs.R;import com.wcs.vcc.api.EmployeePerformanceParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NangSuatActivity extends BaseActivity {
    public static final String TAG = NangSuatActivity.class.getSimpleName();
    @BindView(R.id.lvOrderDetail)
    ExpandableListView listView;
    @BindView(R.id.tv_performance_all_total)
    TextView tvTotal;
    @BindView(R.id.tv_performance_id)
    TextView tvID;
    @BindView(R.id.tv_performance_name)
    TextView tvName;
    private View.OnClickListener tryAgain;
    private String userName, payrollDateInit = "";
    private ArrayList<GroupInfo> arrayGroup = new ArrayList<>();
    private HashMap<Integer, List<ChildInfo>> arrayChild = new HashMap<>();
    private NangSuatAdapter adapter;
    private float total;
    private String employeeID = "00000000-0000-0000-0000-000000000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nang_suat);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        initUI();
    }

    private void initUI() {
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        employeeID = LoginPref.getEmployeeId(this);

        if (getIntent().hasExtra("EMPLOYEE_ID"))
            employeeID = getIntent().getStringExtra("EMPLOYEE_ID");

        if (getIntent().hasExtra(LoginPref.USERNAME))
            tryAgain = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNangSuat(listView);
                }
            };

        adapter = new NangSuatAdapter(this, arrayGroup, arrayChild);
        listView.setAdapter(adapter);
        getNangSuat(listView);
    }

    private void getNangSuat(final View view) {
        total = 0;
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(),
                    TAG, view, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).getEmployeePerformance(new EmployeePerformanceParameter(UUID.fromString(employeeID))).enqueue(new Callback<List<EmployeePerformanceInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeePerformanceInfo>> response, Retrofit retrofit) {
                arrayGroup.clear();
                arrayChild.clear();
                List<ChildInfo> child = new ArrayList<>();
                if (response.isSuccess() && response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        ChildInfo childInfo = new ChildInfo();
                        EmployeePerformanceInfo info = response.body().get(i);
                        String payrollDate = info.getPayrollDate();
                        total += info.getTOTAL();
                        if (!payrollDateInit.equalsIgnoreCase(payrollDate)) {
                            if (i != 0) {
                                arrayGroup.add(new GroupInfo(
                                        info.getPayrollDate(),
                                        info.getTimeKeepingStatus(),
                                        info.getTimeWork(),
                                        info.isNightShift()
                                ));
                                arrayChild.put(arrayGroup.size() - 1, child);
                            }
                            payrollDateInit = payrollDate;
                            child = new ArrayList<>();
                            childInfo.setEndTime(info.getEndTime());
                            childInfo.setOrderNumber(info.getOrderNumber());
                            childInfo.setStartTime(info.getStartTime());
                            childInfo.setTOTAL(info.getTOTAL());
                            child.add(childInfo);
                        } else {
                            childInfo.setEndTime(info.getEndTime());
                            childInfo.setOrderNumber(info.getOrderNumber());
                            childInfo.setStartTime(info.getStartTime());
                            childInfo.setTOTAL(info.getTOTAL());
                            child.add(childInfo);
                        }
                        if (i == response.body().size() - 1) {
                            arrayGroup.add(new GroupInfo(
                                    info.getPayrollDate(),
                                    info.getTimeKeepingStatus(),
                                    info.getTimeWork(),
                                    info.isNightShift()
                            ));
                            arrayChild.put(arrayGroup.size() - 1, child);

                            tvID.setText(String.valueOf(info.EmployeeCode));
                            tvName.setText(info.getVietnamName());
                        }
                    }
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < adapter.getGroupCount(); i++) {
                        listView.expandGroup(i);
                    }
                    tvTotal.setText(String.format("Tổng: %s", NumberFormat.getNumberInstance().format(total)));
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(NangSuatActivity.this, t, TAG, view, tryAgain);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
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
}
