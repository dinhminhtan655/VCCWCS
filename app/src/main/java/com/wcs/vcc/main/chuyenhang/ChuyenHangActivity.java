package com.wcs.vcc.main.chuyenhang;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.main.emdk.ScanListener;
import com.wcs.wcs.R;
import com.wcs.vcc.api.ListLocationParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.StockMovementInsertParameter;
import com.wcs.vcc.api.StockMovementParameter;
import com.wcs.vcc.api.StockMovementReversedParameter;

import com.wcs.vcc.main.chuyenhang.lichsu.LichSuChuyenHangActivity;
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

public class ChuyenHangActivity extends BaseActivity implements View.OnFocusChangeListener, TextView.OnEditorActionListener, ScanListener {
    @BindView(R.id.listViewFrom)
    ListView listViewFrom;
    @BindView(R.id.listViewTo)
    ListView listViewTo;
    @BindView(R.id.acs_chuyen_hang_reason)
    AppCompatSpinner acsReason;
    @BindView(R.id.acactv_chuyen_hang_to)
    AppCompatAutoCompleteTextView acactvTo;
    @BindView(R.id.tv_chuyen_hang_original_from)
    TextView tvOriginalFrom;
    @BindView(R.id.tv_chuyen_hang_original_to)
    TextView tvOriginalTo;
    @BindView(R.id.et_target_scan)
    EditText edtTargetScan;

    @BindView(R.id.ivCameraScan)
    ImageView ivCameraScan;
    @BindView(R.id.frameChuyenHangFrom)
    FrameLayout frameChuyenHangFrom;
    @BindView(R.id.frameChuyenHangTo)
    FrameLayout frameChuyenHangTo;
    @BindView(R.id.edt_chuyen_hang_from)
    EditText edtChuyenHanngFrom;

    @BindView(R.id.btnSwitch)
    Button btnSwitch;


    private final String TAG = ChuyenHangActivity.class.getSimpleName();
    private String userName, location, reason = "", stringPalletID;
    private LocationAdapter adapterFrom, adapterTo;
    private boolean bSuccess = false;
    private ListLocationInfo locationInfoFrom, locationInfoTo;
    private int object, adapterFromSize, adapterToSize, funcion;
    private int storeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyen_hang);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
//        setUpScan();
        initUI();
    }

    private void initUI() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.reason));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acsReason.setAdapter(adapter);
        object = 1;
        hilightList(object);
        edtChuyenHanngFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getStockMovement();
            }
        });

        acactvTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getStockMovement();
            }
        });

        edtTargetScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if ((dialog2 == null || (dialog2 != null && !dialog2.isShowing()))) {
                String contents = s.toString();
                if (contents.contains("\n")) {
                    onData(contents.replaceAll("\n", ""));
                }
//                }
            }
        });


        acactvTo.setOnEditorActionListener(this);
        edtChuyenHanngFrom.setOnFocusChangeListener(this);
        acactvTo.setOnFocusChangeListener(this);

        userName = LoginPref.getUsername(this);
        storeId = LoginPref.getStoreId(this);

        adapterFrom = new LocationAdapter(this, new ArrayList<LocationInfo>());
        listViewFrom.setAdapter(adapterFrom);
        adapterTo = new LocationAdapter(this, new ArrayList<LocationInfo>());
        listViewTo.setAdapter(adapterTo);
    }


    private void hilightList(int object) {
        if (object == 1) {
            frameChuyenHangFrom.setBackgroundResource(R.drawable.outline_blue);
            frameChuyenHangTo.setBackgroundResource(0);
        } else {
            frameChuyenHangFrom.setBackgroundResource(0);
            frameChuyenHangTo.setBackgroundResource(R.drawable.outline_blue);
        }
    }

    public void getLocation() {
        MyRetrofit.initRequest(this).getLocation(new ListLocationParameter(location, storeId)).enqueue(new Callback<List<ListLocationInfo>>() {
            @Override
            public void onResponse(Response<List<ListLocationInfo>> response, Retrofit retrofit) {
                List<ListLocationInfo> body = response.body();
                if (response.isSuccess() && body != null) {
                    if (body.size() > 0) {
                        if (object == 1) {
                            locationInfoFrom = body.get(0);
                            tvOriginalFrom.setText(locationInfoFrom.getLocationNumber());
                            edtTargetScan.requestFocus();
                        } else if (object == 2) {
                            locationInfoTo = body.get(0);
                            tvOriginalTo.setText(locationInfoTo.getLocationNumber());
                            if (funcion == 1) {
                                int amountFrom = 0;
                                StringBuilder builderFrom = new StringBuilder("(");
                                for (int i = 0; i < adapterFromSize; i++) {
                                    LocationInfo info = adapterFrom.getItem(i);
                                    if (info.isChecked()) {
                                        builderFrom.append(info.PalletID);
                                        builderFrom.append(",");
                                        amountFrom++;
                                    }
                                }
                                builderFrom.deleteCharAt(builderFrom.length() - 1);
                                builderFrom.append(")");
                                stringPalletID = builderFrom.toString();
                                reason = "Reversed";
                                if (amountFrom == 0) {
                                    Snackbar.make(listViewFrom, "Không có Pallet nào được chọn", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    executeStockMovementReversed(listViewFrom);

                                }
                            }
                        }

                    } else {
                        if (object == 1) {
                            edtTargetScan.requestFocus();

                        } else if (object == 2) {
                            edtTargetScan.requestFocus();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


    private void getStockMovement(final View view) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dismissDialog(dialog);
        }
        StockMovementParameter parameter = new StockMovementParameter(location, userName, storeId);
        MyRetrofit.initRequest(this).getStockMovement(parameter).enqueue(new Callback<List<LocationInfo>>() {
            @Override
            public void onResponse(Response<List<LocationInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    if (object == 1) {
                        adapterFrom.clear();
                        adapterFrom.addAll(response.body());
                        adapterFromSize = response.body().size();
                        adapterTo.clear();
                        adapterTo.addAll(new ArrayList<>());
                        tvOriginalTo.setText("");
                        if (response.body().size() > 0) {
                            if (response.body().get(0).getCheckAllowcate() == 0) {
                                Utilities.speakingSomeThingslow("Pallet này đang trong đơn hàng, không thể chuyển", ChuyenHangActivity.this);
                            }
                        }
                        if (adapterFromSize == 0 && object == 1) {
                            object = 1;
                            hilightList(object);
                            if (!bSuccess) {
                                Snackbar.make(view, "Không có hàng để chuyển vui lòng Scan vị trí khác", Snackbar.LENGTH_SHORT).show();
                            }
                            edtTargetScan.requestFocus();

                        } else {
                            object = 2;
                            hilightList(object);
                        }
                        if (funcion == 2) {
                            object = 2;
                            hilightList(object);
                            location = acactvTo.getText().toString();
                            getStockMovement(listViewFrom);
                        }


                    } else if (object == 2) {
                        adapterTo.clear();
                        adapterTo.addAll(response.body());
                        adapterToSize = response.body().size();
                        object = 1;
                        hilightList(object);
                        funcion = 0;
                        edtTargetScan.requestFocus();
                        chuyenHang(view);
                    }
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(ChuyenHangActivity.this, t, TAG, view);
                dismissDialog(dialog);

            }
        });
    }

    private void executeStockMovementInsert(final View view) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang chuyển...");
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dismissDialog(dialog);
        }
        String strScanResult = edtChuyenHanngFrom.getText().toString();
        StockMovementInsertParameter parameter = new StockMovementInsertParameter(
                locationInfoTo.LocationID,
                locationInfoTo.getLocationNumber(),
                reason,
                locationInfoFrom.LocationID,
                userName,
                stringPalletID,
                strScanResult

        );
        MyRetrofit.initRequest(this).executeStockMovementInsert(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    funcion = 2;
                    object = 1;
                    bSuccess = true;
//                    location = edtChuyenHanngFrom.getText().toString();
                    location = "";
                    tvOriginalFrom.setText("");
                    hilightList(object);
                    getStockMovement(listViewFrom);
                    if (response.body().equalsIgnoreCase("OK")) {
                        Snackbar.make(view, "Chuyển thành công", Snackbar.LENGTH_LONG).show();
                        Utilities.speakingSomeThingslow("Chuyển thành công", ChuyenHangActivity.this);
                    } else {
                        Snackbar.make(view, "Pallet này đang trong đơn hàng, không thể chuyển", Snackbar.LENGTH_LONG).show();
                        Utilities.speakingSomeThingslow("Pallet này đang trong đơn hàng, không thể chuyển", ChuyenHangActivity.this);
                    }
                } else {
                    bSuccess = false;
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                bSuccess = false;
                RetrofitError.errorNoAction(ChuyenHangActivity.this, t, TAG, view);
                dismissDialog(dialog);
            }
        });
    }

    private void executeStockMovementReversed(final View view) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang đảo...");
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dismissDialog(dialog);
        }
        StockMovementReversedParameter parameter = new StockMovementReversedParameter(
                locationInfoTo.LocationID,
                locationInfoTo.getLocationNumber(),
                0,
                reason,
                locationInfoFrom.LocationID,
                locationInfoFrom.getLocationNumber(),
                userName
        );
        MyRetrofit.initRequest(this).executeStockMovementReversed(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    funcion = 2;
                    object = 1;
//                    location = acactvFrom.getText().toString();
                    location = edtChuyenHanngFrom.getText().toString();
                    getStockMovement(listViewFrom);

                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(ChuyenHangActivity.this, t, TAG, view);
                dismissDialog(dialog);

            }
        });
    }

    @OnClick(R.id.btnSwitch)
    public void switchFromTo(View view) {
        if (object == 1) {
            object = 2;
            hilightList(object);
        } else {
            object = 1;
            hilightList(object);
        }
    }

    @OnClick(R.id.bt_chuyen_hang_history)
    public void history(View view) {
        funcion = 0;
        startActivity(new Intent(this, LichSuChuyenHangActivity.class));
    }

    private void chuyenHang(View view) {
        funcion = 0;
        if (tvOriginalFrom.getText().toString().equals("")) {
            if (!bSuccess) {
                Snackbar.make(view, "Vui lòng chọn vị trí ban đầu", Snackbar.LENGTH_SHORT).show();
            }
            object = 1;
            hilightList(object);
            edtTargetScan.requestFocus();
            return;
        }
        if (tvOriginalTo.getText().toString().equals("")) {
            Snackbar.make(view, "Vui lòng chọn vị trí đến", Snackbar.LENGTH_SHORT).show();
            object = 2;
            hilightList(object);
            edtTargetScan.requestFocus();
            return;
        }

        int amountFrom = 0;
        StringBuilder builderFrom = new StringBuilder("(");
        for (int i = 0; i < adapterFromSize; i++) {
            LocationInfo info = adapterFrom.getItem(i);
            if (info.isChecked()) {
                builderFrom.append(info.PalletNumber);
                builderFrom.append(",");
                amountFrom++;
            }
        }
        builderFrom.deleteCharAt(builderFrom.length() - 1);
        builderFrom.append(")");
        stringPalletID = builderFrom.toString();
        reason = ((TextView) acsReason.getSelectedView()).getText().toString();

        if (amountFrom == 0) {
            Snackbar.make(view, "Không có Pallet nào được chọn", Snackbar.LENGTH_SHORT).show();
        } else {
//            if (adapterFrom.getItem(0).getCurrentQuantity() > adapterFrom.getItem(0).getAfterDPQuantity())
//                Utilities.speakingSomeThingslow("Hàng này đã có đơn hàng. Không thể di chuyển", ChuyenHangActivity.this);
//            else {
            Utilities.basicDialog(this, String.format("Bạn có muốn chuyển đến vị trí %s không?", location), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    executeStockMovementInsert(listViewFrom);
                }
            });
//            }
        }
    }

    @OnClick(R.id.bt_chuyen_hang_move)
    public void move(View view) {
        chuyenHang(view);
    }

    @OnClick(R.id.bt_chuyen_hang_reverse)
    public void reverse(View view) {
        funcion = 0;
        if (tvOriginalFrom.getText().toString().equals("")) {

            Snackbar.make(view, "Bạn chưa chọn vị trí ban đầu", Snackbar.LENGTH_SHORT).show();
            object = 1;
            edtTargetScan.requestFocus();
            return;
        }
        if (tvOriginalTo.getText().toString().equals("")) {
            Snackbar.make(view, "Bạn chưa chọn vị trí đích", Snackbar.LENGTH_SHORT).show();
            object = 2;
            edtTargetScan.requestFocus();
            return;
        }
        Utilities.basicDialog(this, "Bạn có muốn đảo không?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                executeStockMovementReversed(listViewFrom);
            }
        });
    }

    @OnClick(R.id.bt_chuyen_hang_one_to_two)
    public void oneToTwo(View view) {
        funcion = 1;
        if (tvOriginalFrom.getText().toString().equals("")) {
            Snackbar.make(view, "Bạn chưa chọn vị trí ban đầu", Snackbar.LENGTH_SHORT).show();
            edtChuyenHanngFrom.requestFocus();
            object = 1;
            edtTargetScan.requestFocus();
            return;
        }
        String location = edtChuyenHanngFrom.getText().toString();
        if (location.length() == 6) {
            if (location.substring(5).equals("1")) {
                acactvTo.requestFocus();
                acactvTo.setText(String.format("%s2", location.substring(0, 5)));
                getStockMovement();
            } else if (location.substring(5).equals("2")) {
                acactvTo.requestFocus();
                acactvTo.setText(String.format("%s1", location.substring(0, 5)));
                getStockMovement();
            } else {
                Snackbar.make(view, "Chỉ đảo vị trí 1 <-> 2", Snackbar.LENGTH_LONG).show();
            }
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
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


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            funcion = 0;
            getStockMovement();
            return true;
        }
        return false;
    }

    private void getStockMovement() {
        if (object == 1) {
            location = edtChuyenHanngFrom.getText().toString();
        } else if (object == 2) {
            location = acactvTo.getText().toString();
        }
        getLocation();
        getStockMovement(listViewFrom);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v == edtChuyenHanngFrom)
                object = 1;
            else if (v == acactvTo)
                object = 2;
        }
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
    public void onData(String data) {
        Const.timePauseActive = 0;
        stopLogoutService();
//        resetLogoutTimer();
        data = data.trim();
        if (!data.equals("") && !data.equals("\t\t")) {
            if (edtChuyenHanngFrom != null && edtChuyenHanngFrom.length() == 0 || edtChuyenHanngFrom != null && object == 1) {
                object = 1;
                edtChuyenHanngFrom.setText(data);
                edtTargetScan.setText("");
                edtTargetScan.requestFocus();
                hilightList(object);
                bSuccess = false;
                return;
            } else if (acactvTo != null && acactvTo.length() == 0 || acactvTo != null && object == 2) {
                if (!data.contains("PI")) {
                    if (!tvOriginalFrom.getText().toString().equalsIgnoreCase(data)) {
                        object = 2;
                        acactvTo.setText(data);
                        edtTargetScan.setText("");
                        edtTargetScan.requestFocus();
                        hilightList(object);
                        bSuccess = false;
                    } else {
                        Utilities.speakingSomeThingslow("Đang cùng vị trí", ChuyenHangActivity.this);
                        edtTargetScan.getText().clear();
                    }
                    return;
                } else {
                    Snackbar.make(acactvTo, "Vị trí đến không thể là Pallet", Snackbar.LENGTH_LONG).show();
                    edtTargetScan.setText("");
                    edtTargetScan.requestFocus();
                    bSuccess = false;
                }

            }
        }
    }

//    @Override
//    public void onUserInteraction() {
//        super.onUserInteraction();
//        Const.timePauseActive = 0;
//    }
}
