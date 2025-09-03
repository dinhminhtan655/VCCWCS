package com.wcs.vcc.main.resetpassword;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.ResetPasswordParameter;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class resetpasswordActivity extends ShowHomeButtonActivity {
    private final String TAG = resetpasswordActivity.class.getSimpleName();
    @BindView(R.id.tv_resetPassword_userName)
    TextView tvUserName;
    @BindView(R.id.tv_resetPassword_realName)
    TextView tvRealName;
    @BindView(R.id.et_resetPassword_newPassword)
    EditText etresetNewPass;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        ButterKnife.bind(this);

        initial();
    }
    private void initial() {
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        tvUserName.setText(String.format("User Name: %s", userName));
        String realName = LoginPref.getInfoUser(this, LoginPref.REAL_NAME);
        tvRealName.setText(realName);
    }

    @OnClick(R.id.bt_resetPassword_send)
    public void send(View view) {
        if (isEmpty(etresetNewPass))
            return;
        ResetPasswordParameter parameter = new ResetPasswordParameter(userName, etresetNewPass.getText().toString());
        resetPassword(view, parameter);
    }

    public void resetPassword(final View view, ResetPasswordParameter parameter) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang gửi ...");
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).resetPassword(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    Toast.makeText(resetpasswordActivity.this, response.body(), Toast.LENGTH_LONG).show();
                    finish();
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(resetpasswordActivity.this, t, TAG, view);
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
