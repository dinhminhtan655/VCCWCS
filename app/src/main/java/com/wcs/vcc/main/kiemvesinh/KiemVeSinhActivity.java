package com.wcs.vcc.main.kiemvesinh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.wcs.R;
import com.wcs.vcc.api.HouseKeepingCheckParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class KiemVeSinhActivity extends EmdkActivity implements AdapterView.OnItemLongClickListener, View.OnClickListener {

    public static final String BARCODE = "BARCODE";
    private ListView listView;
    private EditText etTargetScan;
    private EditText etBarcode;
    private View.OnClickListener action;
    private KVDAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiem_ve_sinh);
        mapView();
        setListenerView();
        initial();
    }

    private void mapView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.lv_kvs);
        etBarcode = (EditText) findViewById(R.id.et_all_barcode);
        etTargetScan = (EditText) findViewById(R.id.et_all_target_scan);
        snackBarView = listView;
    }

    private void setListenerView() {
        listView.setOnItemLongClickListener(this);
        etBarcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    new AsyncUiControlUpdate().execute(etBarcode.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void initial() {
        Utilities.showBackIcon(getSupportActionBar());

        adapter = new KVDAdapter(this, new ArrayList<HouseKeepingCheck>());
        listView.setAdapter(adapter);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHouseKeepingCheck();
            }
        };
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
                if (contents.contains("\n"))
                    new AsyncUiControlUpdate().execute(contents.replace("\n", ""));
            }
        });

    }

    public void getHouseKeepingCheck() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, action);
            return;
        }
        MyRetrofit.initRequest(this).getHouseKeepingCheck(new HouseKeepingCheckParameter(LoginPref.getUsername(this), LoginPref.getStoreId(this))).enqueue(new Callback<List<HouseKeepingCheck>>() {
            @Override
            public void onResponse(Response<List<HouseKeepingCheck>> response, Retrofit retrofit) {
                List<HouseKeepingCheck> body = response.body();
                if (response.isSuccess() && body != null) {
                    adapter.clear();
                    adapter.addAll(body);
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(KiemVeSinhActivity.this, t, TAG, snackBarView, action);
            }
        });
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    public void scanCamera(View view) {
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
        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String contents = result.getContents();
            new AsyncUiControlUpdate().execute(contents);
        }
    }

    @Override
    public void onData(String data) {
        new AsyncUiControlUpdate().execute(data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getHouseKeepingCheck();

    }

    public class AsyncUiControlUpdate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {

            etTargetScan.setText("");
            etBarcode.setText(result);
            Intent intent = new Intent(KiemVeSinhActivity.this, InsertKVSActivity.class);
            intent.putExtra(BARCODE, result);
            startActivity(intent);
        }

    }
}
