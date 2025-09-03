package com.wcs.vcc.main.register;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.wcs.wcs.R;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RegisterParameter;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RegisterActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText userNameET;
    private EditText passwordET;
    private EditText confirmPasswordET;
    private AppCompatSpinner warehouseIdSpinner;
    private int warehouseId = -1;
    private String username;
    private AppCompatCheckBox outsideCB;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initial();
    }

    private void mapView() {
        userNameET = (EditText) findViewById(R.id.register_new_username);
        passwordET = (EditText) findViewById(R.id.register_password);
        confirmPasswordET = (EditText) findViewById(R.id.register_confirm_password);
        warehouseIdSpinner = (AppCompatSpinner) findViewById(R.id.register_warehouse_id);
        outsideCB = (AppCompatCheckBox) findViewById(R.id.register_is_allow_outside);
    }

    private void setListener() {
        warehouseIdSpinner.setOnItemSelectedListener(this);
    }

    private void initial() {
        mapView();
        setListener();
        Utilities.showBackIcon(getSupportActionBar());
        snackBarView = userNameET;
        username = LoginPref.getUsername(this);
        storeId = LoginPref.getStoreId(this);
    }

    public void createClick(View view) {
        if (isEmpty(userNameET)) {
            Toast.makeText(this, String.format(getString(R.string.field_not_empty), getString(R.string.label_username)), Toast.LENGTH_LONG).show();
            return;
        }
        if (isEmpty(passwordET)) {
            Toast.makeText(this, String.format(getString(R.string.field_not_empty), getString(R.string.password)), Toast.LENGTH_LONG).show();
            return;
        }
        if (isEmpty(confirmPasswordET)) {
            Toast.makeText(this, String.format(getString(R.string.field_not_empty), getString(R.string.label_confirm_password)), Toast.LENGTH_LONG).show();
            return;
        }
        if (warehouseId == -1) {
            warehouseIdSpinner.performClick();
            return;
        }
        Utilities.hideKeyboard(this);
        RegisterParameter parameter = new RegisterParameter(userNameET.getText().toString(),
                passwordET.getText().toString(),
                confirmPasswordET.getText().toString(),
                warehouseId,
                outsideCB.isChecked(),
                username,
                storeId
        );
        register(parameter);
    }

    private void register(RegisterParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.saving));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dismissDialog(dialog);
            return;
        }
        MyRetrofit.initRequest(this).register(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    Toast.makeText(getApplicationContext(), response.body(), Toast.LENGTH_LONG).show();
                    userNameET.setText("");
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(RegisterActivity.this, t, TAG, snackBarView);
                dismissDialog(dialog);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                warehouseId = -1;
                break;
            case 1:
                warehouseId = 1;
                break;
            case 2:
                warehouseId = 2;
                break;
            case 3:
                warehouseId = 3;
                break;
            case 4:
                warehouseId = 0;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
