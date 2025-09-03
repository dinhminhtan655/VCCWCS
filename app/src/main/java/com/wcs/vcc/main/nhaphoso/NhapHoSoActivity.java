package com.wcs.vcc.main.nhaphoso;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.ReceivingOrderDetailParameter;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.UpdateLocationReceivingOrder;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.preferences.LoginPref;
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
import timber.log.Timber;

public class NhapHoSoActivity extends EmdkActivity {
    private static final String TAG = "NhapHoSoActivity";
    @BindView(R.id.lvOrderDetail)
    ListView listView;
    @BindView(R.id.etScanResult)
    EditText etScanResult;
    @BindView(R.id.tvNhsType)
    TextView tvType;
    @BindView(R.id.tvNhsCartonIDSelect)
    TextView tvCartonIDSelect;
    @BindView(R.id.tvNhsOrderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.tvNhsRD)
    TextView tvRD;
    @BindView(R.id.tvNhsTotalSelect)
    TextView tvTotalSelect;
    @BindView(R.id.etTakeScannerResult)
    EditText etTakeScannerResult;

    private ReceivingOrderDetailParameter parameter;
    private ReceivingOrderDetailsAdapter adapter;
    private String userName;
    private ArrayList<Integer> cartonSelectIDs = new ArrayList<>();
    private int eventKeycode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_ho_so);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        etScanResult.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    onData(etScanResult.getText().toString());
                    return true;
                }
                return false;
            }
        });
        adapter = new ReceivingOrderDetailsAdapter(this, new ArrayList<ReceivingOrderDetailsInfo>());
        listView.setAdapter(adapter);
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        etTakeScannerResult.addTextChangedListener(new TextWatcher() {
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
                    Timber.d("TextChanged: " + contents);
                    onData(contents.replaceAll("\n", ""));
                }
            }
        });
    }

    @OnClick(R.id.ivCameraScan)
    public void cameraScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(ScanCameraPortrait.class);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                String data = result.getContents();
                onData(data);
            }
        }
    }

    @Override
    public void onData(String data) {
        Timber.d(data);
        new AsyncDataUpdate().execute(data);
    }

    private class AsyncDataUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            return params[0];
        }

        protected void onPostExecute(String data) {
            etTakeScannerResult.setText("");
            etScanResult.setText(data);
            String type = data.substring(0, 2);
            int number = Integer.parseInt(data.substring(2));
            tvOrderNumber.setText(String.format("%d", number));
            if (type.equalsIgnoreCase("LO")) {
                if (tvType.getText().toString().equalsIgnoreCase("CT")
                        || tvType.getText().toString().equalsIgnoreCase("PI")) {
                    showSettingsAlert(number, tvType.getText().toString());
                } else {
                    tvOrderNumber.setText(String.format("%d", number));
                    tvType.setText(type);
                }

            } else if (type.equalsIgnoreCase("RD")) {
                tvType.setText(type);
                parameter = new ReceivingOrderDetailParameter(data);
                getReceivingOrderDetails(listView, parameter);
            } else if (type.equalsIgnoreCase("CT")) {
                tvType.setText(type);
                if (tvRD.getText().toString().trim().length() > 0) {
                    byte TotalCarton = Byte.parseByte(tvTotalSelect.getText().toString());
                    if (TotalCarton < 12) {
                        parameter = new ReceivingOrderDetailParameter(data, Integer.parseInt(tvRD.getText().toString()));
                        getReceivingOrderDetails(listView, parameter);
                    } else
                        Snackbar.make(listView, "Bạn đã chọn 12 thùng, vui lòng chọn vị trí", Snackbar.LENGTH_LONG).show();
                } else {
                    parameter = new ReceivingOrderDetailParameter(data);
                    getReceivingOrderDetails(listView, parameter);
                }
            }
        }
    }

    public void getReceivingOrderDetails(final View view, ReceivingOrderDetailParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).getReceivingOrderDetails(parameter).enqueue(new Callback<List<ReceivingOrderDetailsInfo>>() {

            @Override
            public void onResponse(Response<List<ReceivingOrderDetailsInfo>> response, Retrofit retrofit) {
                cartonSelectIDs.clear();
                if (response.isSuccess() && response.body() != null) {
                    if (response.body().size() > 0)
                        tvRD.setText(String.format("%s", response.body().get(response.body().size() - 1).getDSROID()));
                    int totalSelect = 0;
                    for (ReceivingOrderDetailsInfo info : response.body()) {
                        if (info.isRecordFirst()) {
                            totalSelect++;
                            cartonSelectIDs.add(info.getPalletID());
                        }
                    }
                    tvTotalSelect.setText(String.format("%d", totalSelect));
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(NhapHoSoActivity.this, t, TAG, view);
            }
        });
    }

    public void showSettingsAlert(final int locationNumber,
                                  final String codeType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (codeType.equals("CT")) {
            builder.setMessage("Bạn có muốn cập nhật carton " + cartonSelectIDs.toString()
                    + " này vào vị trí " + locationNumber + " hay không?");
        } else if (codeType.equals("PI")) {
            builder.setMessage("Bạn có muốn cập nhật pallet " + cartonSelectIDs.toString()
                    + " này vào vị trí " + locationNumber + " hay không?");
        }
        builder.setPositiveButton("Cập nhật",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for (int cartonID : cartonSelectIDs) {
                            UpdateLocationReceivingOrder parameterUpdate = new UpdateLocationReceivingOrder(
                                    cartonID, Integer.toString(locationNumber), userName
                            );
                            updateLocation(listView, parameterUpdate);
                        }

                        switch (tvType.getText().toString()) {
                            case "CT":
                                parameter = new ReceivingOrderDetailParameter(String.format("RD0%s", tvRD.getText().toString()));
                                getReceivingOrderDetails(listView, parameter);
                                break;
                            case "PI":
                                parameter = new ReceivingOrderDetailParameter(String.format("PI0%s", tvRD.getText().toString()));
                                getReceivingOrderDetails(listView, parameter);
                                break;
                        }

                    }
                });
        builder.setNegativeButton("Hủy", null);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void updateLocation(final View view, UpdateLocationReceivingOrder parameter) {
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).updateLocationReceivingOrderDetails(parameter).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    Snackbar.make(view, response.body(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(NhapHoSoActivity.this, t, TAG, view);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Timber.d("KeyDown " + keyCode);
        Utilities.hideKeyboard(this);
        if (eventKeycode == 0 && (keyCode == 245 || keyCode == 242)) {
            eventKeycode++;
            etTakeScannerResult.requestFocus();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Timber.d("KeyUp " + keyCode);
        if (keyCode == 245 || keyCode == 242)
            eventKeycode = 0;

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Const.isActivating = true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        Const.isActivating = false;
    }
}
