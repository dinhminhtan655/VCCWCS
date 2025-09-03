package com.wcs.vcc.main;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.vcc.main.capnhatphienban.CapNhatUngDungActivity;
import com.wcs.wcs.BuildConfig;
import com.wcs.wcs.R;
import com.wcs.vcc.api.ChangeStoreParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NotificationParameter;
import com.wcs.wcs.databinding.ActivityMainBinding;
import com.wcs.vcc.login.LoginActivity;
import com.wcs.vcc.main.detailphieu.OrderDetailActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.main.kiemvitri.KiemViTriNoEMDKActivity;
import com.wcs.vcc.main.newscanmasan.itemmasan.XdocPackingScanActivity;
import com.wcs.vcc.main.palletcartonweighting.PalletCartonWeightingActivity;
import com.wcs.vcc.main.services.CheckActive;
import com.wcs.vcc.main.services.NotificationInfo;
import com.wcs.vcc.main.services.NotificationServices;
import com.wcs.vcc.main.services.UpdateLocationServices;
import com.wcs.vcc.main.unitech.UnitechConst;
import com.wcs.vcc.main.vo.Group;
import com.wcs.vcc.main.vo.Menu;
import com.wcs.vcc.main.vo.MenuItem;
import com.wcs.vcc.preferences.DevicePref;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.PalletFromTo;
import com.wcs.vcc.preferences.SettingPref;
import com.wcs.vcc.preferences.SpinCusIdPref;
import com.wcs.vcc.preferences.VersionPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends EmdkActivity {

    private static final String CHANNEL_ID = "VHL";
    private static final int NOTIFICATION_ID = 1106;

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 123, RESOLUTION_RESULT = 124;
    public static AppCompatActivity activity;

    private int posRadioButton;
    private String ip;
    private String userName;
    private MenuAdapter adapter;
    private SwitchCompat switchCompat, switchSettingDeviceMenu;
    private int navigate;
    private boolean isAcceptChangeDateTime;
    public static int storeId;
    private String group;
    private EditText etTargetScan;
    private TextView tvSettingDeviceMenu;
    private ActivityMainBinding binding;
    private String[] listWH =  new String[]{"VCC 1 - Kho lạnh", "VCC 2 - Kho khô"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = LoginPref.getInfoUser(this, LoginPref.POSITION_GROUP);
        String realName = LoginPref.getInfoUser(this, LoginPref.REAL_NAME);
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        storeId = LoginPref.getStoreId(this);

        activity = this;
        getWindow().setBackgroundDrawableResource(R.color.white);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ButterKnife.bind(this);
        startService(new Intent(this, CheckActive.class));

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(realName);

        setSearchLayoutVisibility(group);
        List<MenuItem> menuItemList = Menu.createMenu(storeId, Group.convertGroupStringToInt(group));
        adapter = new MenuAdapter(MainActivity.this);
        adapter.replace(menuItemList);
        binding.recyclerViewMenu.setAdapter(adapter);

        if (!BuildConfig.VERSION_NAME.equalsIgnoreCase(VersionPref.getVersion(this)))
            VersionPref.setVersion(this, BuildConfig.VERSION_NAME);

        etTargetScan = (EditText) findViewById(R.id.et_target_scan);
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
                if (contents.contains("\n")) {
                    onData(contents.replace("\n", "").trim());
                }

            }
        });
//        if (item.getName() == R.string.doi_kho) {
//            DialogSpinnerChangeWH changeWH = new DialogSpinnerChangeWH(context);
//            changeWH.show();
//        }

        spinnerWH(binding);


    }


    private void setSearchLayoutVisibility(String group) {

        int groupInt = Group.convertGroupStringToInt(group);

        if (groupInt == Group.LOWER_USER || groupInt == Group.NO_POSITION) {
            findViewById(R.id.ll_search).setVisibility(View.GONE);
        }
    }

    private void getNumberQHSENewAssign() {
        MyRetrofit.initRequest(this).getNotification(new NotificationParameter(userName)).enqueue(new Callback<List<NotificationInfo>>() {
            @Override
            public void onResponse(Response<List<NotificationInfo>> response, Retrofit retrofit) {
                if (Menu.menuItemGiaoViec != null) {
                    Menu.menuItemGiaoViec.setNotification(0);
                }
                if (Menu.menuItemQHSE != null) {
                    Menu.menuItemQHSE.setNotification(0);
                }
                if (Menu.menuItemInfoTruckCont != null) {
                    Menu.menuItemInfoTruckCont.setNotification(0);
                }
                if (Menu.menuItemPhieuCuaToi != null) {
                    Menu.menuItemPhieuCuaToi.setNotification(0);
                }
                if (Menu.menuItemCountTrip != null) {
                    Menu.menuItemCountTrip.setNotification(20);
                }

                List<NotificationInfo> body = response.body();

                if (response.isSuccess() && body != null) {
                    for (NotificationInfo info : body) {
                        short notificationCount = info.getNotiQty();
                        switch (info.getType()) {
                            case "QH":
                                if (Menu.menuItemGiaoViec != null) {
                                    Menu.menuItemGiaoViec.addNotification(notificationCount);
                                }
                                break;
                            case "QHSE":
                                if (Menu.menuItemQHSE != null) {
                                    Menu.menuItemQHSE.addNotification(notificationCount);
                                }
                                break;
                            case "CO":
                            case "TR":
                                if (Menu.menuItemInfoTruckCont != null) {
                                    Menu.menuItemInfoTruckCont.addNotification(notificationCount);
                                }
                                break;
                            case "MT":
                                break;
                            default:
                                if (Menu.menuItemPhieuCuaToi != null) {
                                    Menu.menuItemPhieuCuaToi.addNotification(notificationCount);
                                }
                                if (Menu.menuItemCountTrip != null) {
                                    Menu.menuItemCountTrip.addNotification(notificationCount);
                                }
                        }
                    }
                    if (Menu.menuItemGiaoViec != null) {
                        adapter.notifyItemChanged(Menu.menuItemGiaoViec.getPosition());
                    }
                    if (Menu.menuItemQHSE != null) {
                        adapter.notifyItemChanged(Menu.menuItemQHSE.getPosition());
                    }
                    if (Menu.menuItemInfoTruckCont != null) {
                        adapter.notifyItemChanged(Menu.menuItemInfoTruckCont.getPosition());
                    }
                    if (Menu.menuItemPhieuCuaToi != null) {
                        adapter.notifyItemChanged(Menu.menuItemPhieuCuaToi.getPosition());
                    }
                    if (Menu.menuItemCountTrip != null) {
                        adapter.notifyItemChanged(Menu.menuItemCountTrip.getPosition());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @Override
    public void onData(String data) {
        etTargetScan.setText("");
        etTargetScan.requestFocus();
        if (data.contains("PRINTER")) {
            LoginPref.setPrinter(MainActivity.this, data.replace("PRINTER", ""));
            return;
        }
        if (data.contains("PLO")) {
            String[] listCode = data.split("-");
            if (listCode.length == 4) {
                String ProductID = listCode[0].replaceAll("PLO", "");
                String pickingListOrder = listCode[0].replaceAll("PLO", "");
                String Lot = listCode[1];
                String palletFrom = listCode[2];
                String palletTo = listCode[3];
                String strCustomerID = SpinCusIdPref.LoadCusID(MainActivity.this);


                PalletFromTo.savePalletFrom(MainActivity.this, palletFrom);
                PalletFromTo.savePalletTo(MainActivity.this, palletTo);

                Intent intent = new Intent(MainActivity.this, XdocPackingScanActivity.class);
                intent.putExtra("ProductID", ProductID);
                intent.putExtra("pickingListOrder", pickingListOrder);
                intent.putExtra("curcus", strCustomerID);
                intent.putExtra("itemName", ProductID);
                intent.putExtra("Lot", Lot);
                startActivity(intent);
            } else {
                return;
            }
            return;
        }

        HashMap<String, String> barcodeInfo = Utilities.barcodeInfo(getApplicationContext(), data);
        if (barcodeInfo != null && barcodeInfo.get("type") != null) {
            String type = barcodeInfo.get("type");
            String number = barcodeInfo.get("number");

            Bundle args = new Bundle();
            args.putString("BARCODE", data);
            if (type.equalsIgnoreCase("do") ||
                    type.equalsIgnoreCase("ro") ||
                    type.equalsIgnoreCase("tw") ||
                    type.equalsIgnoreCase("tm")) {

                Intent intent = new Intent(MainActivity.this, OrderDetailActivity.class);
                intent.putExtra(ORDER_NUMBER, String.format(Locale.getDefault(), "%s-%s", type, number));
                intent.putExtra("SCAN_TYPE", 9);
                intent.putExtra("CUSTOMER_TYPE", "");
                intent.putExtra("GO_TO_WORKER", false);
                intent.putExtra("BARCODE", data);
                startActivity(intent);
            } else if (type.equalsIgnoreCase("pi")) {
                Intent intent = new Intent(this, PalletCartonWeightingActivity.class);
                intent.putExtra("PALLET_NUMBER", number);
                startActivity(intent);
            } else if (type.equalsIgnoreCase("lo")) {
                gotoActivity(this, KiemViTriNoEMDKActivity.class, args);
            }
        }
    }

    private void gotoActivity(Context context, Class<?> cls, Bundle args) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("ARGS", args);
        startActivity(intent);
    }

    private void getVersionWHC() {
        MyRetrofit.initRequest(this).getWHCVersion().enqueue(new Callback<List<VersionInfo>>() {
            @Override
            public void onResponse(Response<List<VersionInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    VersionInfo info = response.body().get(0);
                    if (!BuildConfig.VERSION_NAME.equalsIgnoreCase(info.getVersionNo())) {
                        if (Menu.menuItemUpdateVersion != null) {
                            Menu.menuItemUpdateVersion.setNotification(1);
                            String strTitle = "Cập nhật mới";
                            String strContent = "Có bản cập nhật mới " + info.getVersionNo();
                            adapter.notifyItemChanged(Menu.menuItemUpdateVersion.getPosition());
                            Utilities.speakingSomeThingslow("Có bản cập nhật mới", MainActivity.this);
                            Utilities.createNotification(MainActivity.this, CapNhatUngDungActivity.class, CHANNEL_ID, strTitle, strContent, NOTIFICATION_ID, NotificationManager.IMPORTANCE_DEFAULT);

                        }
                    } else {
                        VersionPref.setVersionDate(MainActivity.this, info.getVersionDate());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void getTimeServer() {
        isAcceptChangeDateTime = false;

        MyRetrofit.initRequest(this).getTimeServer().enqueue(new Callback<List<TimeServer>>() {
            @Override
            public void onResponse(Response<List<TimeServer>> response, Retrofit retrofit) {
                List<TimeServer> body = response.body();
                if (response.isSuccess() && body != null && body.size() > 0) {
                    Date date = Calendar.getInstance().getTime();
                    long l = date.getTime() - body.get(0).getTime();
                    if (Math.abs(l) >= 3 * 60 * 1000) {
                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Thời gian trên thiết bị không khớp với thời gian trên hệ thống. Vui lòng cập nhật lại thời gian trên thiết bị.")
                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        isAcceptChangeDateTime = true;
                                        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(intent);
                                        }
                                    }
                                })
                                .create();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (!isAcceptChangeDateTime)
                                    getTimeServer();
                            }
                        });
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_sign_out)
            signOut();
        else if (itemId == R.id.action_setting)
            setNetwork();
        else if (itemId == R.id.device_setting) {
            setDeviceScan();
        } else if (itemId == R.id.devices_type) {
            connectDevices();
        }
        return super.onOptionsItemSelected(item);
    }


    public void signOut() {
        updateSignOut();
        LoginPref.resetInfoUserByUser(this);
        startActivity(new Intent(this, LoginActivity.class));
        navigate = 1;
        stopServiceAndRemoveNotify();
        stopCheckActive();
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS)
            stopService(new Intent(this, UpdateLocationServices.class));

    }

    /* Scan camera */
    // Start
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
    // End

    private void updateSignOut() {
        if (!WifiHelper.isConnected(this))
            return;

        MyRetrofit.initRequest(this).signOut(userName).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


    private void stopCheckActive() {
        stopService(new Intent(this, CheckActive.class));
        Const.isActivating = false;
        Const.timePauseActive = 0;
    }

    private void stopServiceAndRemoveNotify() {
        stopService(new Intent(this, NotificationServices.class));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        for (int id : Const.arrayListIDNotify) {
            notificationManager.cancel(id);
        }
        Const.arrayListIDNotify.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Const.isActivating = true;

        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (manager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            boolean isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled) {
                SettingPref.setAccessLocation(this, false);
                stopService(new Intent(MainActivity.this, UpdateLocationServices.class));
            }
        }

        getNumberQHSENewAssign();
        updateStore();
        getVersionWHC();
        boolean unitech = UnitechConst.isUnitech();
        if (unitech)
            startScanUnitech();

        getTimeServer();
    }

    private void checkAndRequestUpdateGooglePlayService() {
        int gpsResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (gpsResult != ConnectionResult.SUCCESS && gpsResult != ConnectionResult.API_UNAVAILABLE) {
            Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, gpsResult, 1);
            errorDialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        if (navigate == 1)
            finish();
        super.onStop();
    }

    private void startScanUnitech() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("close", true);
        Intent mIntent = new Intent().setAction(UnitechConst.START_SCAN_SERVICE).putExtras(bundle);
        sendBroadcast(mIntent);
    }


    private void setDeviceScan() {
        final View view = View.inflate(this, R.layout.setting_device_menu, null);
        boolean bSwitchDevice = DevicePref.LoadItemDevice(MainActivity.this);
        switchSettingDeviceMenu = view.findViewById(R.id.switchSettingDeviceMenu);
        tvSettingDeviceMenu = view.findViewById(R.id.tvSettingDeviceMenu);
        if (bSwitchDevice) {
            tvSettingDeviceMenu.setText("Handheld");
        } else {
            tvSettingDeviceMenu.setText("Ring");
        }
        switchSettingDeviceMenu.setChecked(bSwitchDevice);
        switchSettingDeviceMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tvSettingDeviceMenu.setText("Handheld");
                    DevicePref.SaveItemDevice(MainActivity.this, true);
                } else {
                    tvSettingDeviceMenu.setText("Ring");
                    DevicePref.SaveItemDevice(MainActivity.this, false);
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        //----------------------------------------------------------


    }


    private void connectDevices() {
        Intent i = new Intent(MainActivity.this, DevicesActivity.class);
        startActivity(i);
    }

    public void setNetwork() {
        final View view = View.inflate(this, R.layout.setting_network_location, null);
        boolean accessLocation = SettingPref.getAccessLocation(this);
        switchCompat = view.findViewById(R.id.sc_access_location);
        switchCompat.setChecked(accessLocation);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
                if (isChecked) {
                    if (code == ConnectionResult.SUCCESS)
                        requestLocation();
                    else {
                        Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, code, 125);
                        if (dialog != null) {
                            dialog.show();
                        }
                    }
                } else {
                    if (code == ConnectionResult.SUCCESS)
                        stopService(new Intent(MainActivity.this, UpdateLocationServices.class));
                }
            }
        });

        final EditText ipManual = (EditText) view.findViewById(R.id.et_manual_ip);
        String[] infoNetwork = SettingPref.getInfoNetwork(this);
        ip = infoNetwork[0];
        posRadioButton = Integer.parseInt(infoNetwork[1]);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rb_group);
        if (infoNetwork[1].equalsIgnoreCase("1"))
            ((RadioButton) radioGroup.findViewById(R.id.rb_global)).setChecked(true);
        else if (infoNetwork[1].equalsIgnoreCase("2")) {
            ((RadioButton) radioGroup.findViewById(R.id.rb_manual)).setChecked(true);
            ipManual.setVisibility(View.VISIBLE);
            ipManual.setText(SettingPref.getInfoNetwork(MainActivity.this)[0]);
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

        View rlStore = view.findViewById(R.id.rl_main_setting_store);
        if (Group.isEqualGroup(group, Group.MANAGER)) {
            rlStore.setVisibility(View.VISIBLE);
            Spinner spinnerPlace = (Spinner) view.findViewById(R.id.spinner_main_setting_places);
            spinnerPlace.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listWH));
            spinnerPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    storeId = position + 1;
                    updateStore();
                    spinnerWH(binding);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinnerPlace.setSelection(storeId - 1);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utilities.hideKeyboard(MainActivity.this);
            }

        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utilities.hideKeyboard(MainActivity.this);
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
                SettingPref.setInfoNetwork(MainActivity.this, ip, posRadioButton);
            }
        }).setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else
            startService(new Intent(this, UpdateLocationServices.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE)
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService(new Intent(this, UpdateLocationServices.class));
            } else {
                switchCompat.setChecked(false);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLUTION_RESULT) {
            stopService(new Intent(this, UpdateLocationServices.class));
            if (resultCode == RESULT_OK) {
                startService(new Intent(this, UpdateLocationServices.class));
            } else
                switchCompat.setChecked(false);
        } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                String barcode = result.getContents();
                onData(barcode);
            }
        }

    }

    private void updateStore() {
        MyRetrofit.initRequest(this).updateStore(new ChangeStoreParameter(userName, storeId)).enqueue(new Callback<List<UpdateStoreResult>>() {
            @Override
            public void onResponse(Response<List<UpdateStoreResult>> response, Retrofit retrofit) {
                List<UpdateStoreResult> body = response.body();
                if (response.isSuccess() && body != null && body.size() > 0) {
                    LoginPref.putStoreId(MainActivity.this, storeId);
                    UpdateStoreResult info = body.get(0);
                    LoginPref.putWarehouseId(MainActivity.this, info.getWarehousId());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void spinnerWH(ActivityMainBinding binding){
        binding.spinnerChangeWH.setAdapter(new ArrayAdapter<>(MainActivity.this,R.layout.spinner_item_change_wh, listWH));
        binding.spinnerChangeWH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.storeId = position + 1;
                updateStore();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinnerChangeWH.setSelection(MainActivity.storeId - 1);
    }


}
