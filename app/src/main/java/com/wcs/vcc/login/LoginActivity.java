package com.wcs.vcc.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;

import com.google.android.material.snackbar.Snackbar;
import com.wcs.wcs.R;
import com.wcs.vcc.api.LoginParam;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.MainActivity;
import com.wcs.vcc.main.services.CheckActive;
import com.wcs.vcc.main.services.NotificationServices;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.SettingPref;
import com.wcs.vcc.preferences.VersionPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class LoginActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback {
    public static final String TAG = "LoginActivity";
    @BindView(R.id.username)
    EditText etUsername;
    @BindView(R.id.password)
    EditText etPassword;
    @BindView(R.id.acb_auto_sign_out)
    AppCompatCheckBox acbAutoSignOut;
    @BindView(R.id.acb_remember_sign_in_info)
    AppCompatCheckBox acbRememberSignInInfo;

    private int navigate;
    private int posRadioButton;
    private String ip = MyRetrofit.IP;
    private String[] infoNetwork;
    private String mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LoginPref.getInfoUser(this, LoginPref.USERNAME).equalsIgnoreCase("-1")) {
            if (getIntent().getStringExtra("some_data") == null){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                navigate = 1;
                return;
            }

        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(String.format("%s - %s", VersionPref.getVersion(LoginActivity.this), VersionPref.getVersionDate(LoginActivity.this)));
        }
        infoNetwork = SettingPref.getInfoNetwork(this);
        ip = infoNetwork[0];
        posRadioButton = Integer.parseInt(infoNetwork[1]);
        boolean autoSignOut = LoginPref.isAutoSignOut(this);
        acbAutoSignOut.setChecked(autoSignOut);

        mac = Utilities.getWiFiMac();
    }

    @OnClick(R.id.sign_in)
    public void signIn(final View view) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang đăng nhập...");
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            Utilities.dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }

        LoginParam info = new LoginParam(mac, etUsername.getText().toString(), etPassword.getText().toString());

        MyRetrofit.initRequest(this).signIn(info).enqueue(new Callback<LoginInfo>() {
            @Override
            public void onResponse(Response<LoginInfo> response, Retrofit retrofit) {
                LoginInfo userInfo = response.body();
                if (response.isSuccess() && userInfo != null) {
                    if (userInfo.getStatus().equalsIgnoreCase("OK")) {
                        String username = userInfo.getUsername();
                        String positionGroup = userInfo.getPositionGroup();
                        String realName = userInfo.getRealName();
                        int warehouseID = userInfo.getWarehouseID();
                        int storeId = userInfo.getStoreID();
                        String DeviceNumber = userInfo.getDeviceNumber();
                        UUID employeeID = userInfo.EmployeeID;
                        boolean isOutSide = userInfo.isAllowOutside();
                        if (isOutSide || posRadioButton == 0) {
                            LoginPref.putInfoUser(LoginActivity.this, username, positionGroup, realName, warehouseID, acbAutoSignOut.isChecked(), storeId, employeeID,DeviceNumber);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            navigate = 1;
                            startService(new Intent(LoginActivity.this, NotificationServices.class));
                            if (acbAutoSignOut.isChecked())
                                startService(new Intent(LoginActivity.this, CheckActive.class));
                        } else
                            Snackbar.make(view, "Vui lòng sử dụng mạng nội bộ", Snackbar.LENGTH_LONG).show();
                    } else
                        Snackbar.make(view, userInfo.getStatus(), Snackbar.LENGTH_LONG).show();
                }
                Utilities.dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                Utilities.dismissDialog(dialog);
                RetrofitError.errorNoAction(LoginActivity.this, t, TAG, view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting)
            setNetwork();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        if (navigate == 1)
            finish();
        super.onStop();
    }

    public void setNetwork() {
        infoNetwork = SettingPref.getInfoNetwork(this);
        ip = infoNetwork[0];
        posRadioButton = Integer.parseInt(infoNetwork[1]);

        final View view = View.inflate(this, R.layout.setting_network, null);
        final EditText ipManual = (EditText) view.findViewById(R.id.et_manual_ip);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rb_group);
        if (infoNetwork[1].equalsIgnoreCase("1"))
            ((RadioButton) radioGroup.findViewById(R.id.rb_global)).setChecked(true);
        else if (infoNetwork[1].equalsIgnoreCase("2")) {
            ((RadioButton) radioGroup.findViewById(R.id.rb_manual)).setChecked(true);
            ipManual.setVisibility(View.VISIBLE);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_manual)
                    ipManual.setVisibility(View.VISIBLE);
                else
                    ipManual.setVisibility(View.GONE);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utilities.hideKeyboard(LoginActivity.this);
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utilities.hideKeyboard(LoginActivity.this);
                if (radioGroup.getCheckedRadioButtonId() == R.id.rb_local) {
                    posRadioButton = 0;
                    ip = MyRetrofit.IP;
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rb_global) {
                    posRadioButton = 1;
                    ip = MyRetrofit.IP;
                } else {
                    String ipM = ipManual.getText().toString().trim();
                    if (ipM.length() > 0) {
                        posRadioButton = 2;
                        ip = ipM;
                    } else {
                        Snackbar.make(view, "Bạn phải điền một địa chỉ IP", Snackbar.LENGTH_LONG).show();
                    }
                }
                SettingPref.setInfoNetwork(LoginActivity.this, ip, posRadioButton);
            }
        }).setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}
