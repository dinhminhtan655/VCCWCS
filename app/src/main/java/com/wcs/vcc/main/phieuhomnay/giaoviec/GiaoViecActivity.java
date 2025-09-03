package com.wcs.vcc.main.phieuhomnay.giaoviec;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.wcs.wcs.R;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.api.DeleteEmployeeIDGiaoViecParameter;
import com.wcs.vcc.api.EmployeePresentParameter;
import com.wcs.vcc.api.GiaoViecParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class GiaoViecActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private final String TAG = "GiaoViecActivity";

    @BindView(R.id.lvOrderDetail)
    ListView listView;
    @BindView(R.id.ac_employeeID)
    AppCompatAutoCompleteTextView acEmployeeID;

    private GiaoViecAdapter adapter;

    private View.OnClickListener action;
    private int empID;
    private String userName;
    private ArrayList<String> employeeIDArray = new ArrayList<>();
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_viec);
        ButterKnife.bind(this);
        getEmployeeID();
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        storeId = LoginPref.getStoreId(this);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGiaoViec(listView);
            }
        };
        adapter = new GiaoViecAdapter(this, new ArrayList<GiaoViecInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        getGiaoViec(listView);
    }

    public void getGiaoViec(final View view) {
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
        MyRetrofit.initRequest(this).getGiaoViec(new GiaoViecParameter(empID, getIntent().getStringExtra("order_number"), userName)).enqueue(new Callback<List<GiaoViecInfo>>() {
            @Override
            public void onResponse(Response<List<GiaoViecInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(GiaoViecActivity.this, t, TAG, view, action);
            }
        });
    }

    public void getEmployeeID() {
        MyRetrofit.initRequest(this).getEmployeeID(new EmployeePresentParameter(1, Const.EMPLOYEE_ID, storeId)).enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    for (EmployeeInfo info : response.body())
                        employeeIDArray.add(Integer.toString(info.EmployeeCode));
                    acEmployeeID.setAdapter(new ArrayAdapter<String>(GiaoViecActivity.this, android.R.layout.simple_list_item_1, employeeIDArray));
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

    }

    @OnClick(R.id.bt_send)
    public void send() {
        String sID = acEmployeeID.getText().toString();
        if (sID.length() != 0) {
            empID = Integer.parseInt(sID);
            if (employeeIDArray.contains(Integer.toString(empID))) {
                getGiaoViec(listView);
            } else {
                if (RetrofitError.getSnackbar() != null)
                    RetrofitError.getSnackbar().dismiss();
                Snackbar.make(listView, "Bạn phải chọn EmployeeID có trong danh sách", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            if (RetrofitError.getSnackbar() != null)
                RetrofitError.getSnackbar().dismiss();
            Snackbar.make(listView, "Bạn phải nhập EmployeeID", Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage("Bạn có muốn xóa nhân viên này không?").setNegativeButton("Không", null).setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteEmployeeID(listView, adapter.getItem(position).getEmployeeWorkingID());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void deleteEmployeeID(final View view, int employeeWorkingID) {
        Log.e(TAG, "deleteEmployeeID: " + employeeWorkingID);
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.deleting));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).deleteEmployeeID(new DeleteEmployeeIDGiaoViecParameter(employeeWorkingID, userName)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                dismissDialog(dialog);
                if (response.isSuccess() && response.body().equalsIgnoreCase("")) {
                    empID = 0;
                    getGiaoViec(listView);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(GiaoViecActivity.this, t, TAG, view);
            }
        });

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
