package com.wcs.vcc.main.technical.schedulejobplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.vo.Group;
import com.wcs.vcc.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.wcs.vcc.main.technical.assign.EmployeePresentAdapter;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.api.EmployeePresentParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.QHSEAssignmentInsertParameter;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UpdateScheduleJobActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = UpdateScheduleJobActivity.class.getSimpleName();
    private String scheduleId;
    private String schedule_info;
    private TextView infoTV;
    private MultiAutoCompleteTextView assignToACTV;
    private EmployeePresentAdapter adapter;
    private String username;
    private boolean haveUpdate = false;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_schedule_job);
        initial();
    }

    private void initial() {
        mapView();
        setListener();
        snackBarView = infoTV;
        username = LoginPref.getUsername(getApplicationContext());
        storeId = LoginPref.getStoreId(this);

        Utilities.showBackIcon(getSupportActionBar());
        scheduleId = getIntent().getStringExtra(ScheduleJobActivity.SCHEDULE_ID);
        schedule_info = getIntent().getStringExtra(ScheduleJobActivity.SCHEDULE_INFO);
        String assign_to = getIntent().getStringExtra(ScheduleJobActivity.ASSIGN_TO);
        infoTV.setText(schedule_info);
        assignToACTV.setText(assign_to);
        adapter = new EmployeePresentAdapter(this, new ArrayList<EmployeeInfo>());
        assignToACTV.setAdapter(adapter);
        assignToACTV.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        getEmployeeID();
    }

    private void mapView() {
        infoTV = (TextView) findViewById(R.id.tv_schedule_job_info);
        assignToACTV = (MultiAutoCompleteTextView) findViewById(R.id.actv_assign_to);
    }

    private void setListener() {
        assignToACTV.setOnClickListener(this);
    }

    public void getEmployeeID() {
        String position;
        int department;
        if (LoginPref.getPositionGroup(getApplicationContext()).equals(Group.TECHNICAL)) {
            position = "2";
            department = 4;
        } else {
            position = "0";
            department = 2;
        }
        MyRetrofit.initRequest(this).getEmployeeID(new EmployeePresentParameter(department, position, storeId)).enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public void updateAssign(View view) {
        String idAssign = assignToACTV.getText().toString().trim();
        if (idAssign.length() > 0) {
            boolean b = idAssign.lastIndexOf(',') == idAssign.length() - 1;
            if (b)
                idAssign = idAssign.substring(0, idAssign.length() - 1);
            QHSEAssignmentInsertParameter pa = new QHSEAssignmentInsertParameter(
                    username, scheduleId, String.format("(%s)", idAssign)
            );
            executeQHSEAssignmentInsert(pa);
        }
    }

    private void executeQHSEAssignmentInsert(QHSEAssignmentInsertParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.saving));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).executeQHSEAssignmentInsert(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                dismissDialog(dialog);
                if (response.isSuccess() && response.body() != null) {
                    haveUpdate = true;
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(UpdateScheduleJobActivity.this, t, TAG, snackBarView);
                dismissDialog(dialog);
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent().putExtra(Intent.EXTRA_TEXT, haveUpdate));
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        assignToACTV.showDropDown();
    }
}
