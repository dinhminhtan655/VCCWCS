package com.wcs.vcc.main.scanhang;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit2;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.scanbhx.model.ABATimeRaiking;
import com.wcs.vcc.main.scanhang.adapter.PalletCodeAdapter;
import com.wcs.vcc.main.scanhang.model.ItemScan;
import com.wcs.vcc.main.scanhang.model.MinMaxPalletID;
import com.wcs.vcc.main.scanhang.model.ScanPalletCode;
import com.wcs.vcc.main.scanhang.viewmodel.AllViewModel;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.InputFilterMinMax;
import com.wcs.vcc.utilities.UIHelper;
import com.wcs.vcc.utilities.Utilities;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.ZebraImageFactory;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanPalletCodeActivity extends RingScanActivity {

    private static final String PREFS_NAME = "OurSavedAddress";
    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvRegion)
    TextView tvRegion;
    @BindView(R.id.edtPalletID)
    EditText edtPalletID;
    @BindView(R.id.toggleThemBot)
    ToggleButton toggleThemBot;
    @BindView(R.id.et_target_scan)
    EditText et_target_scan;
    @BindView(R.id.tv_prev_barcode)
    TextView tv_prev_barcode;
    @BindView(R.id.rv_addremove)
    RecyclerView rv_addremove;
    @BindView(R.id.tvCountPallet)
    TextView tvCountPallet;
    @BindView(R.id.btnXong)
    Button btnXong;
    @BindView(R.id.flButtonPrint)
    FloatingActionButton flButtonPrint;
    @BindView(R.id.lnTotal)
    LinearLayout lnTotal;
    @BindView(R.id.tvMinMaxPallet)
    TextView tvMinMaxPallet;
    @BindView(R.id.spinType)
    Spinner spinType;

    String[] strType;

    String strDate, strRegion, strCustomerCode, strGroup, strMac, strCurrentPalletID, strUserName, strDocEntry, strId, strFlag, userName;
    int strSoBich, strSLMove, strSoThung, strIToggle;
    int iMin = 0, iMax = 0, iCheck;
    boolean bAddRemove = false; //false = Thêm
    List<ScanPalletCode> list;

    AllViewModel allViewModel;

    PalletCodeAdapter adapter;

    private UIHelper helper = new UIHelper(this);
    LinearLayoutManager layoutManager;
    Vibrator vibrator;
    MediaPlayer mediaPlayer, mediaPlayer2;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_pallet_code);
        ButterKnife.bind(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        userName = LoginPref.getUsername(ScanPalletCodeActivity.this);
        strMac = settings.getString(bluetoothAddressKey, "");

        Bundle b = getIntent().getExtras();
        if (b != null) {
            strDate = b.getString("reportdate");
            strRegion = b.getString("region");
            strCustomerCode = b.getString("customercode");
            bAddRemove = b.getBoolean("bit");
            strGroup = b.getString("group");
            strCurrentPalletID = b.getString("palletid");
            strDocEntry = b.getString("docentry");
            strSoThung = b.getInt("sothung");
            strSLMove = b.getInt("slmove");
            strSoBich = b.getInt("sobich", 0);
            strId = b.getString("id");
            strIToggle = b.getInt("itoggle");
            strFlag = b.getString("flag");
            tvDate.setText(Utilities.formatDate_ddMMyy2(strDate));
            tvRegion.setText(strRegion);
            edtPalletID.setText(strCurrentPalletID);
        } else {
            strDate = "";
            strRegion = "";
            strCustomerCode = "";
            strGroup = "";
            strCurrentPalletID = "";
        }

        list = new ArrayList<>();
        strType = new String[]{"Thịt", "Mát", "Đông"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinType.setAdapter(arrayAdapter);

        if (strFlag != null) {
            if (strFlag.equals("1"))
                spinType.setSelection(0);
            else if (strFlag.equals("2")) {
                spinType.setSelection(1);
            } else {
                spinType.setSelection(2);
            }
        }


        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (strCustomerCode.equals("BHX")) {
                    strFlag = String.valueOf(i + 1);
                } else
                    strFlag = "0";
                if (bAddRemove) {
                    getPopulateList(strCustomerCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        adapter = new PalletCodeAdapter(new RecyclerViewItemOrderListener<ScanPalletCode>() {
            @Override
            public void onClick(ScanPalletCode item, int position, int order) {
                if (!bAddRemove) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanPalletCodeActivity.this);
                    builder.setMessage("Bạn muốn xóa " + item.palletCode + " ?");
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            list.remove(position);
                            adapter.setCustomerScan(list);
                        }
                    }).create().show();

                }
            }

            @Override
            public void onLongClick(ScanPalletCode item, int position, int order) {

            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        strUserName = LoginPref.getUsername(ScanPalletCodeActivity.this);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }


        if (strCustomerCode.equals("BHX")) {
            spinType.setVisibility(View.VISIBLE);
        } else {
            spinType.setVisibility(View.GONE);
        }

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        setUpScan();

        allViewModel = ViewModelProviders.of(ScanPalletCodeActivity.this).get(AllViewModel.class);


        if (bAddRemove) {
            getPopulateList(strCustomerCode);
//            list = populateList(strDate, strRegion, strCustomerCode, strGroup, strFlag);
            toggleThemBot.setChecked(true);
            flButtonPrint.setVisibility(View.GONE);
            btnXong.setVisibility(View.GONE);
            lnTotal.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lnTotal.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lnTotal.setLayoutParams(lp);
        } else {
            toggleThemBot.setChecked(false);
            flButtonPrint.setVisibility(View.VISIBLE);
            btnXong.setVisibility(View.VISIBLE);
            lnTotal.setVisibility(View.GONE);
            adapter.setCustomerScan(list);
            rv_addremove.setAdapter(adapter);
            rv_addremove.invalidate();
//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lnTotal.getLayoutParams();
//            lp.addRule(RelativeLayout.BELOW,R.id.rv_addremove);
//            lnTotal.setLayoutParams(lp);
        }

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()) {
                    case R.id.toggleThemBot:
                        if (b) {
                            list.clear();
                            bAddRemove = true;
                            getPopulateList(strCustomerCode);
//                            list = populateList(strDate, strRegion, strCustomerCode, strGroup, strFlag);
                            flButtonPrint.setVisibility(View.GONE);
                            btnXong.setVisibility(View.GONE);
                            lnTotal.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lnTotal.getLayoutParams();
                            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            lnTotal.setLayoutParams(lp);
                        } else {
                            list.clear();
                            bAddRemove = false;
                            list = new ArrayList<>();
                            adapter.setCustomerScan(list);
                            rv_addremove.setAdapter(adapter);
                            rv_addremove.invalidate();
                            flButtonPrint.setVisibility(View.VISIBLE);
                            btnXong.setVisibility(View.VISIBLE);
                            lnTotal.setVisibility(View.GONE);
//                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lnTotal.getLayoutParams();
//                            lp.addRule(RelativeLayout.BELOW,R.id.rv_addremove);
//                            lnTotal.setLayoutParams(lp);
                        }
//                        Toast.makeText(ScanPalletCodeActivity.this, "" + bAddRemove, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        toggleThemBot.setOnCheckedChangeListener(listener);


        getTimeRaiking();
        getMinMaxPalletId();


    }

    private void getTimeRaiking() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Customer", strCustomerCode);
        MyRetrofit2.initRequest2().GetTimeRaiking(jsonObject).enqueue(new Callback<ABATimeRaiking>() {
            @Override
            public void onResponse(Call<ABATimeRaiking> call, Response<ABATimeRaiking> response) {

            }

            @Override
            public void onFailure(Call<ABATimeRaiking> call, Throwable t) {

            }
        });
    }

    private void getMinMaxPalletId() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("GroupSorting", strGroup);
        jsonObject.addProperty("Delivery_Date", strDate);
        jsonObject.addProperty("Region", strRegion);
        jsonObject.addProperty("CustomerCode", strCustomerCode);
        MyRetrofit2.initRequest2().loadMinMaxPalletId(jsonObject).enqueue(new Callback<MinMaxPalletID>() {
            @Override
            public void onResponse(Call<MinMaxPalletID> call, Response<MinMaxPalletID> response) {
                if (response.isSuccessful() && response.body() != null) {
                    iMin = response.body().mini;
                    iMax = response.body().maxi;
                    tvMinMaxPallet.setText("Có thể Scan từ Pallet: " + iMin + " -> " + iMax);
                }
            }

            @Override
            public void onFailure(Call<MinMaxPalletID> call, Throwable t) {
                Toast.makeText(ScanPalletCodeActivity.this, "Vui lòng kiểm tra internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPopulateList(String customerCode) {
        if (customerCode.equals("BHX"))
            list = populateList(strDate, strRegion, strCustomerCode, strGroup, strFlag);
        else
            list = populateList(strDate, strRegion, strCustomerCode, strGroup, "0");
    }

    private List<ScanPalletCode> populateList(String strDate, String strRegion, String strCustomerCode, String strGroup, String strFlag) {
        List<ScanPalletCode> list = new ArrayList<>();

        allViewModel.getScanPalletCode(ScanPalletCodeActivity.this, strDate, strRegion, strCustomerCode, strGroup, strFlag).observe(this, new Observer<List<ScanPalletCode>>() {
            @Override
            public void onChanged(@Nullable List<ScanPalletCode> scanPalletCodes) {
                adapter.setCustomerScan(scanPalletCodes);
                rv_addremove.setAdapter(adapter);
                rv_addremove.invalidate();

                int s = 0;

                for (int i = 0; i <= scanPalletCodes.size(); i++) {
                    s = i;
                    tvCountPallet.setText("Tổng Khay: " + s);
                }
            }
        });

        return list;
    }


    private void printMultiplePhotoFromExternal(final List<Bitmap> bitmap, Dialog dialog, List<ScanPalletCode> l) {

        MyRetrofit2.initRequest2().AddNewPalletCode(l).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().equals('"' + "Thành công" + '"')) {
                        l.clear();
                        Utilities.thongBaoDialog(ScanPalletCodeActivity.this, "Thêm khay thành công");
                        if (strMac != null && !strMac.equals("")) {
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        Looper.prepare();
                                        Toast.makeText(ScanPalletCodeActivity.this, "Sending image to printer", Toast.LENGTH_SHORT).show();
                                        Connection connection = getZebraPrinterConn();
                                        connection.open();
                                        ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);

                                        for (Bitmap b : bitmap) {
//                            printer.printImage(ZebraImageFactory.getImage(b), (int) (b.getWidth() / 3), b.getHeight(), b.getWidth(), b.getHeight(), false);
                                            printer.printImage(ZebraImageFactory.getImage(b), 0, 0, (int) (b.getWidth() / 1.2f), b.getHeight(), false);
                                        }
                                        connection.close();
                                        dialog.dismiss();

                                    } catch (ConnectionException e) {
                                        helper.showErrorDialogOnGuiThread(e.getMessage());
                                    } catch (ZebraPrinterLanguageUnknownException e) {
                                        helper.showErrorDialogOnGuiThread(e.getMessage());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        helper.dismissLoadingDialog();
                                        Looper.myLooper().quit();
                                    }

                                }
                            }).start();
                        }
                    } else {
                        Utilities.thongBaoDialog(ScanPalletCodeActivity.this, "Hệ thống chưa lưu được khay này.Vui lòng in lại");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utilities.thongBaoDialog(ScanPalletCodeActivity.this, "Kiểm tra kết nối Internet");
            }
        });


    }

    @Override
    public void onData(String data) {
        super.onData(data);
        boolean pa = data.substring(0, 2).equals("PA") && data.contains("-");
        if (!bAddRemove) {
            //Scan Add
            Toast.makeText(this, "Scan Add", Toast.LENGTH_SHORT).show();

            if (pa) {
                String[] strPalletId = data.split("-");
                String[] strPalletId2 = strPalletId[0].split("A");
                String flagUpdate = strPalletId[2].equals("") ? "0" : strPalletId[2];


                if (Integer.parseInt(strPalletId2[1]) >= iMin && Integer.parseInt(strPalletId2[1]) <= iMax) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).palletCode.contains(data)) {
                                iCheck = 1;
                                break;
                            } else {
                                iCheck = 0;
                                continue;
                            }
                        }

                        if (iCheck == 0) {
                            list.add(new ScanPalletCode(Integer.parseInt(strPalletId2[1]), data, strDate, strUserName, true, Integer.parseInt(strGroup), strCustomerCode, strRegion, flagUpdate));
                            adapter.setCustomerScan(list);
                            rv_addremove.setAdapter(adapter);
                            rv_addremove.invalidate();
                        } else {
                            Utilities.speakingSomeThingslow("Đã tồn tại", ScanPalletCodeActivity.this);
                            Utilities.thongBaoDialog(ScanPalletCodeActivity.this, "Đã tồn tại");
                        }

                    } else {
                        list.add(new ScanPalletCode(Integer.parseInt(strPalletId2[1]), data, strDate, strUserName, true, Integer.parseInt(strGroup), strCustomerCode, strRegion, flagUpdate));
                        adapter.setCustomerScan(list);
                        rv_addremove.setAdapter(adapter);
                        rv_addremove.invalidate();
                    }
                } else {
                    Utilities.speakingSomeThingslow("Ngoài giới hạn Pallet của nhóm " + strGroup, ScanPalletCodeActivity.this);
                    Utilities.thongBaoDialog(ScanPalletCodeActivity.this, "Ngoài giới hạn Pallet của nhóm " + strGroup);
                }


            } else {
                Utilities.speakingSomeThingslow("Mã không hợp lệ", ScanPalletCodeActivity.this);
                Utilities.thongBaoDialog(ScanPalletCodeActivity.this, "Mã không hợp lệ");
            }


        } else {
            //Scan Remove
            if (pa) {
                String[] strPalletId = data.split("-");
                String[] strPalletId2 = strPalletId[0].split("A");

                AlertDialog.Builder builder = new AlertDialog.Builder(ScanPalletCodeActivity.this);

                builder.setMessage("Bạn có muốn hủy khay số " + strPalletId[1] + " của Pallet " + strPalletId2[1]);
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ScanPalletCode scanPalletCode;

                        if (strCustomerCode.equals("BHX")) {
                            scanPalletCode = new ScanPalletCode(0, 0, data, strDate, null, null, true, Integer.parseInt(strGroup), strCustomerCode, strRegion, strUserName, null, strFlag);
                        } else {
                            scanPalletCode = new ScanPalletCode(0, 0, data, strDate, null, null, true, Integer.parseInt(strGroup), strCustomerCode, strRegion, strUserName, null, "0");
                        }

                        MyRetrofit2.initRequest2().UpdateInActivePallet(scanPalletCode).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().equals('"' + "Thành công" + '"')) {
                                        Utilities.speakingSomeThingslow("Bỏ khay số " + strPalletId[1] + " của Pallet " + strPalletId2[1] + " thành công", ScanPalletCodeActivity.this);
                                        Utilities.thongBaoDialog(ScanPalletCodeActivity.this, "Bỏ khay số " + strPalletId[1] + " của Pallet " + strPalletId2[1] + " thành công");
//                                        list = populateList(strDate, strRegion, strCustomerCode, strGroup, strFlag);
                                        getPopulateList(strCustomerCode);
                                    } else {
                                        Utilities.speakingSomeThingslow(response.body(), ScanPalletCodeActivity.this);
                                        Utilities.thongBaoDialog(ScanPalletCodeActivity.this, response.body());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Utilities.thongBaoDialog(ScanPalletCodeActivity.this, "Vui lòng kiểm tra Internet");
                            }
                        });

                    }
                });

                Dialog dialog = builder.create();
                dialog.show();
            }


        }
    }

    private Connection getZebraPrinterConn() {
        return new BluetoothConnection(strMac);
    }

    @OnClick(R.id.btnXong)
    public void addPalletCode() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ScanPalletCodeActivity.this);
        builder.setMessage("Bấm đồng ý để gửi thông tin");
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyRetrofit2.initRequest2().AddNewPalletCode(list).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().equals('"' + "Thành công" + '"')) {
                                list.clear();
                                adapter.setCustomerScan(list);

                                JsonObject jsonObject1 = new JsonObject();
                                jsonObject1.addProperty("Doc_Entry", strDocEntry);
                                jsonObject1.addProperty("SLMove", strSLMove);
                                jsonObject1.addProperty("UserName", strGroup);
                                jsonObject1.addProperty("DeviceNumber", strUserName);
                                jsonObject1.addProperty("Region", strRegion);
                                jsonObject1.addProperty("CustomerCode", strCustomerCode);
                                jsonObject1.addProperty("SoThung", strSoThung);

                                MyRetrofit2.initRequest2().updateXDockAllOutboundPackingDetailMove(jsonObject1).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            if (response.body().equals('"' + "OK" + '"')) {
                                                Toast.makeText(ScanPalletCodeActivity.this, "Thêm khay thành công", Toast.LENGTH_SHORT).show();

                                                if (!bAddRemove) {
                                                    ItemScan itemScan = new ItemScan(Integer.parseInt(strCurrentPalletID), strSoBich, strSLMove, 2, strSoThung, strFlag);
                                                    itemScan.setId(Integer.parseInt(strId));
                                                    Outbound2Activity.itemScanViewModel.update(itemScan);

                                                    if (strIToggle == 0) {
                                                        Outbound2Activity.itemScanViewModel.updateConfirmBack1();
                                                        Outbound2Activity.itemScanViewModel.updateConfirmBack0PalletIdDesc();
                                                    } else {
                                                        Outbound2Activity.itemScanViewModel.updateConfirmBack1();
                                                        Outbound2Activity.itemScanViewModel.updateConfirmBack0PalletIdAsc();
                                                    }
                                                }

                                                finish();

                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(ScanPalletCodeActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Utilities.thongBaoDialog(ScanPalletCodeActivity.this, "Thêm khay thất bại");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Utilities.thongBaoDialog(ScanPalletCodeActivity.this, "Kiểm tra kết nối Internet");
                    }
                });
            }
        });


        Dialog d = builder.create();
        d.show();


    }

    @OnClick(R.id.flButtonPrint)
    public void printMultipleTem() {
        final Dialog dialog = new Dialog(ScanPalletCodeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_print_multiple_tem);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView tvText = dialog.findViewById(R.id.tvText);
        EditText edtFromBox = dialog.findViewById(R.id.edtFromBox);
        EditText edtToBox = dialog.findViewById(R.id.edtToBox);
        EditText edtPalletNumber = dialog.findViewById(R.id.edtPalletNumber);
        EditText edtPalletType = dialog.findViewById(R.id.edtPalletType);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnPrint = dialog.findViewById(R.id.btnPrint);

        if (edtPalletID.getText().toString() != null && !edtPalletID.getText().toString().equals("")) {
            tvText.setText("PA");
//            tvText2.setText("-"+soThung);
//            edtFromBox.setText((soThung + 1) + "");
            edtPalletNumber.setText(edtPalletID.getText().toString());
            edtPalletType.setText(strFlag);
        }

        edtToBox.setFilters(new InputFilter[]{new InputFilterMinMax("1", "10")});
        edtPalletType.setFilters(new InputFilter[]{new InputFilterMinMax("1", "3")});


        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!edtToBox.getText().toString().equals("")) {
                int iFromBox = Integer.parseInt(edtFromBox.getText().toString().equals("") ? String.valueOf(1) : edtFromBox.getText().toString());
                int iToBox = Integer.parseInt(edtToBox.getText().toString().equals("") ? String.valueOf(0) : edtToBox.getText().toString());
                List<Bitmap> lBitmap = new ArrayList<>();
                List<ScanPalletCode> l = new ArrayList<>();
                try {
//                        int iPallet = Integer.parseInt(tvPalletCurrent.getText().toString());
                    int iPallet = Integer.parseInt(edtPalletNumber.getText().toString());
                    for (int i2 = iFromBox; i2 <= iToBox + iFromBox; i2++) {
                        int iSoThung = i2;
                        String totalPaBoxWhitoutCode = null;
                        String totalPaBox = null;
                        if (strCustomerCode.equals("BHX")) {
                            strFlag = edtPalletType.getText().toString();
                            if (strFlag != null) {
                                if (strFlag.equals("1")) {
                                    totalPaBoxWhitoutCode = iPallet + "-" + iSoThung + "\n(Thịt)" + "\n"+ strDate;
//                                totalPaBoxWhitoutCode =  "1500" + "-" + iSoThung + " (Thịt)";
                                    totalPaBox = "PA" + iPallet + "-" + iSoThung + "-" + strFlag;
                                } else if (strFlag.equals("2")) {
                                    totalPaBoxWhitoutCode = iPallet + "-" + iSoThung + "\n(Mát)" + "\n"+ strDate;
                                    totalPaBox = "PA" + iPallet + "-" + iSoThung + "-" + strFlag;
                                } else {
                                    totalPaBoxWhitoutCode = iPallet + "-" + iSoThung + "\n(Đông)" + "\n"+ strDate;
                                    totalPaBox = "PA" + iPallet + "-" + iSoThung + "-" + strFlag;
                                }

                            } else {
                                if (edtPalletType.getText().toString().equals("")) {
                                    Utilities.speakingSomeThingslow("Loại hàng không được để trống", ScanPalletCodeActivity.this);
                                } else {
                                    if (edtPalletType.getText().toString().equals("1")) {
                                        totalPaBoxWhitoutCode = iPallet + "-" + iSoThung + "\n(Thịt)" + "\n"+ strDate;
//                                totalPaBoxWhitoutCode =  "1500" + "-" + iSoThung + " (Thịt)";
                                        totalPaBox = "PA" + iPallet + "-" + iSoThung + "-" + edtPalletType.getText().toString();
                                    } else if (edtPalletType.getText().toString().equals("2")) {
                                        totalPaBoxWhitoutCode = iPallet + "-" + iSoThung + "\n(Mát)" + "\n"+ strDate;
                                        totalPaBox = "PA" + iPallet + "-" + iSoThung + "-" + edtPalletType.getText().toString();
                                    } else {
                                        totalPaBoxWhitoutCode = iPallet + "-" + iSoThung + "\n(Đông)" + "\n"+ strDate;
                                        totalPaBox = "PA" + iPallet + "-" + iSoThung + "-" + edtPalletType.getText().toString();
                                    }
                                }

                            }

                        } else {
                            totalPaBoxWhitoutCode = iPallet + "-" + iSoThung + "\n(Thịt)" + "\n"+ strDate;
                            totalPaBox = "PA" + iPallet + "-" + iSoThung;
                        }

                        Bitmap bm = Bitmap.createBitmap(540, 380, Bitmap.Config.ARGB_8888);

                        /*create a canvas object*/
                        Canvas cv = new Canvas(bm);
                        /*create a paint object*/
                        Paint paint = new Paint();


                        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                        Writer codeWriter;
                        codeWriter = new Code39Writer();
                        BitMatrix byteMatrix = null;

                        try {
                            byteMatrix = codeWriter.encode(totalPaBox, BarcodeFormat.CODE_39, 540, 150, hintMap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                        int width = byteMatrix.getWidth();
                        int height = byteMatrix.getHeight();
                        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        for (int i = 0; i < width; i++) {
                            for (int j = 0; j < height; j++) {
                                bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                            }
                        }

                        cv.drawBitmap(bitmap, 0f, 180f, paint);

                        Bitmap bitmap1 = Bitmap.createBitmap(540, 230, Bitmap.Config.ARGB_8888);

                        Canvas canvas = new Canvas(bitmap1);
                        canvas.drawBitmap(bm, 0, 0, null);
                        Paint paint1 = new Paint();

                        Paint.FontMetrics fm = new Paint.FontMetrics();
                        paint1.setColor(Color.WHITE);
                        paint1.getFontMetrics(fm);
                        paint1.setTextSize(80);
                        paint1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        paint1.setTextAlign(Paint.Align.CENTER);
                        int margin = 14;

                        int xPos = (cv.getWidth() / 2);
                        int yPos = (int) (((cv.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) * 0.25);

                        cv.drawRect(0, 0, 540, 200, paint1);
                        paint1.setColor(Color.BLACK);
                        int y = yPos + (margin * 3);
                        int count = 0;
                        if (strCustomerCode.equals("BHX")) {
                            for (String line : totalPaBoxWhitoutCode.split("\n")) {
//                                cv.drawText(line, xPos, y, paint1);
//                                y += paint1.descent() - paint1.ascent();

                                if (count == 2) {
                                    paint1.setTextSize(20);
                                    y += paint1.descent() - paint1.ascent();
                                    cv.drawText(line, xPos, 20, paint1);
                                } else {
                                    cv.drawText(line, xPos, y, paint1);
                                    y += paint1.descent() - paint1.ascent();
                                }

                                count++;

                            }
                        } else {
                            cv.drawText(totalPaBoxWhitoutCode, xPos, y, paint1);
                        }


                        l.add(new ScanPalletCode(Integer.parseInt(totalPaBox.split("-")[0].replace("PA", "")), totalPaBox.trim(), strDate, userName, true, Integer.parseInt(strGroup), strCustomerCode, strRegion, totalPaBox.split("-")[2]));


                        lBitmap.add(bm);
                    }
                    printMultiplePhotoFromExternal(lBitmap, dialog, l);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

//        return true;

    }


    @Override
    protected void onResume() {
        super.onResume();
        Const.isActivating = true;
    }


    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }


}