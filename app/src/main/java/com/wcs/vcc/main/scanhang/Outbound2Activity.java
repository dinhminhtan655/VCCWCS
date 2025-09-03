package com.wcs.vcc.main.scanhang;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.wcs.vcc.main.scanhang.adapter.ItemScanAdapter;
import com.wcs.vcc.main.scanhang.model.ItemScan;
import com.wcs.vcc.main.scanhang.model.ScanPalletCode;
import com.wcs.vcc.main.scanhang.viewmodel.ItemScanViewModel;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Const;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Outbound2Activity extends RingScanActivity implements ABAListIItemAllFragment.ABAOnInputListener2 {

    private static final String TAG = "Outbound2Activity";

    private static final String PREFS_NAME = "OurSavedAddress";
    private static final String bluetoothAddressKey = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";
    private static final String bluetoothName = "ZEBRA_DEMO_BLUETOOTH_NAME";

    private final int REQ_CODE_SPEECH_INPUT = 100;
    @BindView(R.id.btChooseDate)
    Button btChooseDate;
    @BindView(R.id.tv_prev_barcode)
    TextView tv_prev_barcode;
    @BindView(R.id.et_target_scan)
    EditText et_target_scan;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvSumSLChia)
    TextView tvSumSLChia;
    @BindView(R.id.tvSumMove)
    TextView tvSumMove;
    @BindView(R.id.tvSumDieuChinh)
    TextView tvSumDieuChinh;
    @BindView(R.id.tvSumConfirm)
    TextView tvSumConfirm;
    @BindView(R.id.tvSumUpDown)
    TextView tvSumUpDown;
    @BindView(R.id.tvItemName)
    TextView tvItemName;
    @BindView(R.id.tvPalletCurrent)
    TextView tvPalletCurrent;
    @BindView(R.id.tvSTCurrent)
    TextView tvSTCurrent;
    @BindView(R.id.rv_vinmartdetail)
    RecyclerView rv_vinmartdetail;
    @BindView(R.id.btnXong)
    Button btnXong;
    @BindView(R.id.btnAutoIn)
    ImageButton btnAutoIn;
    @BindView(R.id.toggleToiLui)
    ToggleButton toggleToiLui;
    @BindView(R.id.imgList)
    ImageView imgList;
    @BindView(R.id.imgMultipleScan)
    ImageView imgMultipleScan;
    @BindView(R.id.flButtonPrint)
    FloatingActionButton flButtonPrint;

    private UIHelper helper = new UIHelper(this);

    private Calendar calendar;
    private String reportDate, reportDate2, userName, strDocEntry, strDevice, strSLScan, strCode, strItemCode, strSupplierID, strMac, strSoBich, strId, strFlag;
    LinearLayoutManager layoutManager;
    Vibrator vibrator;
    MediaPlayer mediaPlayer, mediaPlayer2;

    TextToSpeech textToSpeech;

    String[] strP, strT;

    int centerItemPosition;

    ItemScanAdapter adapter;

    public static ItemScanViewModel itemScanViewModel;

    int ii = 0, pos = 0, soThung;

    String currentItemCode = "";

    List<ItemScan> list;
    //    ArrayList<Integer> listSoThung;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    int iToggle = 0;
    String region, nhom, customer;
    boolean scanMulti = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbound2);

        ButterKnife.bind(this);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        strMac = settings.getString(bluetoothAddressKey, "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        Bundle b = getIntent().getExtras();

        if (b != null) {
            customer = b.getString("customer");
            region = b.getString("region");
            nhom = b.getString("nhom");
        } else {
            region = "";
            nhom = "";
        }


        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(Outbound2Activity.this, R.raw.sairoi2);
        mediaPlayer2 = MediaPlayer.create(Outbound2Activity.this, R.raw.nhammaroi);
        list = new ArrayList<ItemScan>();

        userName = LoginPref.getUsername(Outbound2Activity.this);
        strDevice = Utilities.getAndroidID(Outbound2Activity.this);
        tvUsername.setText(userName + "/" + customer + "/N" + nhom + "/" + region);

        setUpScan();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat dateformat2 = new SimpleDateFormat(DATE_FORMAT2);
        reportDate = dateformat.format(calendar.getTime());
        reportDate2 = dateformat2.format(calendar.getTime());
        btChooseDate.setText(reportDate);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        int totalVisibleItems = layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition();
        centerItemPosition = totalVisibleItems / 2;

        toggleToiLui.setChecked(false);

        adapter = new ItemScanAdapter();
        rv_vinmartdetail.setAdapter(adapter);

        itemScanViewModel = ViewModelProviders.of(this).get(ItemScanViewModel.class);
        itemScanViewModel.deleteAllItemScan();


        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()) {
                    case R.id.toggleToiLui:
                        if (b) {
                            iToggle = 1;
                        } else {
                            iToggle = 0;
                        }
//                        Toast.makeText(Outbound2Activity.this, "" + iToggle, Toast.LENGTH_SHORT).show();
                        loadPackingView();
                        break;
                }
            }
        };

        toggleToiLui.setOnCheckedChangeListener(listener);

        imgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FragmentManager fm = getSupportFragmentManager();
                    ABAListIItemAllFragment listItemFragment = ABAListIItemAllFragment.ABAnewInstance(reportDate2, nhom, region, customer);
                    listItemFragment.show(fm, null);
            }
        });

        imgMultipleScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Outbound2Activity.this, ScanPalletCodeActivity.class);
                intent.putExtra("reportdate", reportDate2);
                intent.putExtra("region", region);
                intent.putExtra("customercode", customer);
                intent.putExtra("group", nhom);
                intent.putExtra("bit", true);
                intent.putExtra("palletid", tvPalletCurrent.getText().toString());
                startActivity(intent);
            }
        });

        btnXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Outbound2Activity.this);
                builder.setTitle("Hoàn Thành");
                builder.setMessage("Bạn có muốn hoàn thành để Scan mã hàng khác?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tv_prev_barcode.setText("");
                        currentItemCode = "";
                        if (list.size() > 0) {
                            list.clear();
                            itemScanViewModel.deleteAllItemScan();
                        }
                    }
                });

                Dialog dialog = builder.create();
                dialog.show();

            }
        });

        et_target_scan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                promptSpeechInput();
                return false;
            }
        });


    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et_target_scan.setText(result.get(0).trim().replace(" ", ""));
                }
                break;
            }

        }

    }

    private void loadPackingView() {

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ItemCode", tv_prev_barcode.getText().toString());
        jsonObject.addProperty("Delivery_Date", reportDate2);
        jsonObject.addProperty("User_Chia_Item", nhom);
        jsonObject.addProperty("CustomerID", customer);
        jsonObject.addProperty("SupplierID", strSupplierID);
        jsonObject.addProperty("Region", region);
        jsonObject.addProperty("sort", iToggle);

        MyRetrofit2.initRequest2().loadXDockAllOutboundPackingView2(jsonObject).enqueue(new Callback<List<ItemScan>>() {
            @Override
            public void onResponse(Call<List<ItemScan>> call, Response<List<ItemScan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (list.size() > 0) {
                        list.clear();
                    } else if (list.size() == 0) {
                        currentItemCode = tv_prev_barcode.getText().toString();
                    }

                    itemScanViewModel.deleteAllItemScan();

                    itemScanViewModel.insert(response.body());

                    itemScanViewModel.getAllItemScanDesc(iToggle).observe(Outbound2Activity.this, new Observer<List<ItemScan>>() {
                        @Override
                        public void onChanged(@Nullable List<ItemScan> itemScanList) {
                            list.clear();
                            list.addAll(itemScanList);
                            adapter.submitList(itemScanList);
                            adapter.setOnItemClickListener(new RecyclerViewItemOrderListener<ItemScan>() {
                                @Override
                                public void onClick(ItemScan item, int position, int order) {
                                    switch (order) {
                                        case 0:
//                                            Log.e("aaa", item.getPalletID() + "/" + item.getId());
                                            strDocEntry = String.valueOf(item.getId());
                                            strSLScan = String.valueOf(item.getQuantityScan());
                                            strSoBich = String.valueOf(item.getSoBich());
                                            strId = String.valueOf(item.getId());
                                            strFlag = item.getFlag();
                                            pos = position;
                                            break;
                                    }
                                }

                                @Override
                                public void onLongClick(ItemScan item, int position, int order) {

                                }
                            });
                            int sumSLChia = 0, sumSLScan = 0, sumMove = 0, sumDieuChinh = 0, sumConfirm = 0, sumUpDown = 0;


                            for (ItemScan s : list) {
                                sumSLChia += (int) s.getSoBich();
                                sumSLScan += s.getQuantityScan();
                                sumMove += s.getQuantityMove();
                                sumDieuChinh += s.getQuantityModify();
                                sumConfirm += Integer.parseInt(s.getConfirmQuantity());
                                sumUpDown += Integer.parseInt(s.getThieuDu());
                                ii++;
                            }

                            tvSumSLChia.setText(String.valueOf(sumSLChia));
                            tvSumMove.setText(String.valueOf(sumMove));
                            tvSumDieuChinh.setText(String.valueOf(sumDieuChinh));
                            tvSumConfirm.setText(String.valueOf(sumConfirm));
                            tvSumUpDown.setText(String.valueOf(sumUpDown));

                            if (list.size() > 0) {
                                tvPalletCurrent.setText(String.valueOf(list.get(0).getPalletID()));
                                tvItemName.setText(String.valueOf(list.get(0).getItemName()));
                                tvItemName.setText(list.get(0).getItemName());

                                textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        if (i == TextToSpeech.SUCCESS) {
                                            if (pos >= 0 && list.size() > 0) {
                                                String content = list.get(pos).getPalletID() + " !! " + (int) list.get(pos).getSoBich();
                                                String content2 = "Đã hoàn thành";
                                                textToSpeech.setLanguage(new Locale("vi_VN"));
                                                textToSpeech.setSpeechRate(2.5f);
                                                if (list.get(pos).getConfirm() == 0) {
                                                    textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
                                                } else if (list.get(pos).getConfirm() == 2) {
                                                    textToSpeech.speak(content2, TextToSpeech.QUEUE_FLUSH, null);
                                                }
                                            }

                                        }
                                    }
                                });

                                final JsonObject jsonObject3 = new JsonObject();
                                jsonObject3.addProperty("PalletId", list.get(pos).getPalletID());
                                jsonObject3.addProperty("Delivery_Date", reportDate2);
                                jsonObject3.addProperty("Region", region);
                                jsonObject3.addProperty("CustomerCode", customer);
                                jsonObject3.addProperty("Flag", Integer.parseInt(list.get(pos).getFlag()));

                                MyRetrofit2.initRequest2().loadCurrentBoxNumberABAAll(jsonObject3).enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            soThung = response.body();
                                            tvSTCurrent.setText("/STHT: " + soThung);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {
                                        Toast.makeText(Outbound2Activity.this, "Không lấy được số thùng", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                tvPalletCurrent.setText("");
                                tvSTCurrent.setText("");
                                tvItemName.setText("NaN");
                            }

                        }
                    });


                    if (response.body().size() > 0) {
                        final JsonObject jsonObject3 = new JsonObject();
                        jsonObject3.addProperty("PalletId", response.body().get(0).getPalletID());
                        jsonObject3.addProperty("Delivery_Date", reportDate2);
                        jsonObject3.addProperty("Region", region);
                        jsonObject3.addProperty("CustomerCode", customer);
                        jsonObject3.addProperty("Flag", Integer.parseInt(response.body().get(0).getFlag()));

                        MyRetrofit2.initRequest2().loadCurrentBoxNumberABAAll(jsonObject3).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    soThung = response.body();
                                    tvSTCurrent.setText("/STHT: " + soThung);
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Toast.makeText(Outbound2Activity.this, "Không lấy được số thùng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    if (list.size() > 0) {

                        tvItemName.setText(list.get(0).getItemName());

                        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int i) {
                                if (i == TextToSpeech.SUCCESS) {
                                    String content = list.get(pos).getPalletID() + " !! " + (int) list.get(pos).getSoBich();
                                    String content2 = "Đã hoàn thành";
                                    textToSpeech.setLanguage(new Locale("vi_VN"));
                                    textToSpeech.setSpeechRate(2.5f);
                                    if (list.get(pos).getConfirm() == 0) {
                                        textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
                                    } else if (list.get(pos).getConfirm() == 2) {
                                        textToSpeech.speak(content2, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                }
                            }
                        });
                    } else {
                        tvItemName.setText("NaN");
                    }


                }
            }

            @Override
            public void onFailure(Call<List<ItemScan>> call, Throwable t) {
                Toast.makeText(Outbound2Activity.this, "Vui lòng kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @OnClick(R.id.btChooseDate)
    public void chooseDate() {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                loadPackingView();

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        loadPackingView();
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        loadPackingView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onData(String data) {
        super.onData(data);
        loadDataByBarCodeAndItemCode(data, "");
    }

    @SuppressLint("LongLogTag")
    private void loadDataByBarCodeAndItemCode(String data, String itemName) {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (data.length() > 0) {
            //Check FormatBarcode
            if (data.contains("PA")) {
                try {
                    String text = data;
                    strP = text.split("A");
//                            adapter.filter(strP[1]);
                    strT = strP[1].split("-");
                } catch (Exception e) {
                    Log.e("er", e + "");
                }

                if (customer.equals("BHX")) {
                    if (list.size() > 0) {
                        try {
                            if (Integer.parseInt(strT[0]) != Integer.parseInt(tvPalletCurrent.getText().toString())) {
                                Toast.makeText(this, "Bạn chưa thêm hàng xong ở Pallet thứ: " + tvPalletCurrent.getText().toString(), Toast.LENGTH_SHORT).show();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                } else {
                                    //deprecated in API 26
                                    vibrator.vibrate(500);
                                }
                                mediaPlayer.start();
                            } else {
                                if (!strT[2].equals(strFlag)) {
                                    if (strT[2].equals("1")) {
                                        Utilities.speakingSomeThingslow("Nhầm rồi! Đây là khay hàng thịt", Outbound2Activity.this);
                                    } else if (strT[2].equals("2")) {
                                        Utilities.speakingSomeThingslow("Nhầm rồi! Đây là khay hàng mát", Outbound2Activity.this);
                                    } else if (strT[2].equals("3")) {
                                        Utilities.speakingSomeThingslow("Nhầm rồi! Đây là khay hàng đông", Outbound2Activity.this);
                                    } else {
                                        Utilities.speakingSomeThingslow("Vui lòng scan lại", Outbound2Activity.this);
                                    }
                                } else {
//                                    if (Integer.parseInt(strT[1]) >= soThung) {
                                        if (list.get(pos).getConfirm() == 0) {
                                            int sLMove = (int) list.get(pos).getSoBich() - list.get(pos).getQuantityModify();
//                                            if (list.get(pos).getSoBich() <= 10) {

                                            JsonObject jsonObject1 = new JsonObject();
                                            jsonObject1.addProperty("Doc_Entry", strDocEntry);
                                            jsonObject1.addProperty("SLMove", sLMove);
                                            jsonObject1.addProperty("UserName", nhom);
                                            jsonObject1.addProperty("DeviceNumber", userName);
                                            jsonObject1.addProperty("Region", region);
                                            jsonObject1.addProperty("CustomerCode", customer);
                                            jsonObject1.addProperty("SoThung", strT[1]);

//                                                Log.e(TAG, strDocEntry + " : " + userName + " : " + strDevice + " : " + sLMove);
                                            MyRetrofit2.initRequest2().updateXDockAllOutboundPackingDetailMove(jsonObject1).enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    if (response.isSuccessful() && response.body() != null) {
                                                        if (response.body().equals('"' + "OK" + '"')) {
                                                            Toast.makeText(Outbound2Activity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                                            tv_prev_barcode.setText(currentItemCode);
                                                            String[] strPalletId = data.trim().split("-");
                                                            String[] strPalletId2 = strPalletId[0].split("A");
                                                            List<ScanPalletCode> l = new ArrayList<>();
                                                            l.add(new ScanPalletCode(Integer.parseInt(strPalletId2[1]), data.trim(), reportDate2, userName, true, Integer.parseInt(nhom), customer, region, strFlag));
                                                            MyRetrofit2.initRequest2().AddNewPalletCode(l).enqueue(new Callback<String>() {
                                                                @Override
                                                                public void onResponse(Call<String> call, Response<String> response) {
                                                                    if (response.isSuccessful() && response.body() != null) {
                                                                        if (response.body().equals('"' + "Thành công" + '"')) {
                                                                            l.clear();
                                                                        } else {
                                                                            Utilities.thongBaoDialog(Outbound2Activity.this, "Thêm khay thất bại");
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<String> call, Throwable t) {
                                                                    Utilities.thongBaoDialog(Outbound2Activity.this, "Kiểm tra kết nối Internet");
                                                                }
                                                            });

                                                            ItemScan itemScan = new ItemScan(list.get(pos).getPalletID(), list.get(pos).getSoBich(), sLMove, 2, Integer.parseInt(strT[1]), list.get(pos).getFlag());
                                                            itemScan.setId(list.get(pos).getId());
                                                            itemScanViewModel.update(itemScan);
                                                            if (iToggle == 0) {
                                                                itemScanViewModel.updateConfirmBack1();
                                                                itemScanViewModel.updateConfirmBack0PalletIdDesc();
                                                            } else {
                                                                itemScanViewModel.updateConfirmBack1();
                                                                itemScanViewModel.updateConfirmBack0PalletIdAsc();
                                                            }

                                                        } else {
                                                            Toast.makeText(Outbound2Activity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<String> call, Throwable t) {
                                                    Toast.makeText(Outbound2Activity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                                }
                                            });
//                                            } else {
//                                                Utilities.speakingSomeThingslow("Số vỉ Pallet " + list.get(pos).getPalletID() + " lớn hơn 10 bạn có cần chia nhiều khay", Outbound2Activity.this);
//                                                AlertDialog.Builder builder = new AlertDialog.Builder(Outbound2Activity.this);
//                                                builder.setMessage("Số vỉ Pallet " + list.get(pos).getPalletID() + " lớn hơn 10 bạn có cần chia nhiều khay");
//                                                builder.setNeutralButton("Không cần", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        dialogInterface.dismiss();
//                                                        JsonObject jsonObject1 = new JsonObject();
//                                                        jsonObject1.addProperty("Doc_Entry", strDocEntry);
//                                                        jsonObject1.addProperty("SLMove", sLMove);
//                                                        jsonObject1.addProperty("UserName", nhom);
//                                                        jsonObject1.addProperty("DeviceNumber", userName);
//                                                        jsonObject1.addProperty("Region", region);
//                                                        jsonObject1.addProperty("CustomerCode", customer);
//                                                        jsonObject1.addProperty("SoThung", strT[1]);
//
//                                                        Log.e(TAG, strDocEntry + " : " + userName + " : " + strDevice + " : " + sLMove);
//                                                        MyRetrofit2.initRequest2().updateXDockAllOutboundPackingDetailMove(jsonObject1).enqueue(new retrofit2.Callback<String>() {
//                                                            @Override
//                                                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                                                                if (response.isSuccessful() && response.body() != null) {
//                                                                    if (response.body().equals('"' + "OK" + '"')) {
//                                                                        Toast.makeText(Outbound2Activity.this, "Thành công", Toast.LENGTH_SHORT).show();
//                                                                        tv_prev_barcode.setText(currentItemCode);
//                                                                        String[] strPalletId = data.split("-");
//                                                                        String[] strPalletId2 = strPalletId[0].split("A");
//                                                                        List<ScanPalletCode> l = new ArrayList<>();
//                                                                        l.add(new ScanPalletCode(Integer.parseInt(strPalletId2[1]), data, reportDate2, userName, true, Integer.parseInt(nhom), customer, region, strFlag));
//                                                                        MyRetrofit2.initRequest2().AddNewPalletCode(l).enqueue(new retrofit2.Callback<String>() {
//                                                                            @Override
//                                                                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                                                                                if (response.isSuccessful() && response.body() != null) {
//                                                                                    if (response.body().equals('"' + "Thành công" + '"')) {
//                                                                                        l.clear();
//                                                                                    } else {
//                                                                                        Utilities.thongBaoDialog(Outbound2Activity.this, "Thêm khay thất bại");
//                                                                                    }
//                                                                                }
//                                                                            }
//
//                                                                            @Override
//                                                                            public void onFailure(Call<String> call, Throwable t) {
//                                                                                Utilities.thongBaoDialog(Outbound2Activity.this, "Kiểm tra kết nối Internet");
//                                                                            }
//                                                                        });
//
//                                                                        ItemScan itemScan = new ItemScan(list.get(pos).getPalletID(), list.get(pos).getSoBich(), sLMove, 2, Integer.parseInt(strT[1]), list.get(pos).getFlag());
//                                                                        itemScan.setId(list.get(pos).getId());
//                                                                        itemScanViewModel.update(itemScan);
//                                                                        if (iToggle == 0) {
//                                                                            itemScanViewModel.updateConfirmBack1();
//                                                                            itemScanViewModel.updateConfirmBack0PalletIdDesc();
//                                                                        } else {
//                                                                            itemScanViewModel.updateConfirmBack1();
//                                                                            itemScanViewModel.updateConfirmBack0PalletIdAsc();
//                                                                        }
//
//                                                                    } else {
//                                                                        Toast.makeText(Outbound2Activity.this, "Thất bại", Toast.LENGTH_SHORT).show();
//                                                                    }
//
//                                                                }
//                                                            }
//
//                                                            @Override
//                                                            public void onFailure(Call<String> call, Throwable t) {
//                                                                Toast.makeText(Outbound2Activity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        });
//                                                    }
//                                                });
//                                                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        dialogInterface.dismiss();
//                                                    }
//                                                }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        Intent intent = new Intent(Outbound2Activity.this, ScanPalletCodeActivity.class);
//                                                        intent.putExtra("reportdate", reportDate2);
//                                                        intent.putExtra("region", region);
//                                                        intent.putExtra("customercode", customer);
//                                                        intent.putExtra("group", nhom);
//                                                        intent.putExtra("bit", false);
//                                                        intent.putExtra("palletid", tvPalletCurrent.getText().toString());
//                                                        intent.putExtra("docentry", strDocEntry);
//                                                        intent.putExtra("sothung", strT[1]);
//                                                        intent.putExtra("slmove", sLMove);
//                                                        intent.putExtra("sobich", strSoBich);
//                                                        intent.putExtra("id", strId);
//                                                        intent.putExtra("itoggle", iToggle);
//                                                        intent.putExtra("flag", strFlag);
//                                                        startActivity(intent);
//                                                    }
//                                                }).create().show();

//                                            }


                                        }
//                                    } else {
//                                        Utilities.speakingSomeThingslow("Số thùng/Khay của Pallet " + tvPalletCurrent.getText().toString() + " cao nhất hiện tại là " + soThung, Outbound2Activity.this);
//                                    }
                                }
                            }


                        } catch (Exception e) {
                            Log.e("er", e + "");
                        }
                    }
                } else {
                    if (list.size() > 0) {
                        try {
                            if (Integer.parseInt(strT[0]) != Integer.parseInt(tvPalletCurrent.getText().toString())) {
                                Toast.makeText(this, "Bạn chưa thêm hàng xong ở Pallet thứ: " + tvPalletCurrent.getText().toString(), Toast.LENGTH_SHORT).show();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                } else {
                                    //deprecated in API 26
                                    vibrator.vibrate(500);
                                }
                                mediaPlayer.start();
                            } else {
                                Log.d("DUONG", strDocEntry);
//                                if (Integer.parseInt(strT[1]) >= soThung) {
                                    if (list.get(pos).getConfirm() == 0) {
                                        String logData = strDocEntry;
                                        int sLMove = (int) list.get(pos).getSoBich() - list.get(pos).getQuantityModify();
//                                        if (list.get(pos).getSoBich() <= 10) {

                                            JsonObject jsonObject1 = new JsonObject();
                                            jsonObject1.addProperty("Doc_Entry", strDocEntry);
                                            jsonObject1.addProperty("SLMove", sLMove);
                                            jsonObject1.addProperty("UserName", nhom);
                                            jsonObject1.addProperty("DeviceNumber", userName);
                                            jsonObject1.addProperty("Region", region);
                                            jsonObject1.addProperty("CustomerCode", customer);
                                            jsonObject1.addProperty("SoThung", strT[1]);

                                            Log.e(TAG, strDocEntry + " : " + userName + " : " + strDevice + " : " + sLMove);
                                            MyRetrofit2.initRequest2().updateXDockAllOutboundPackingDetailMove(jsonObject1).enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    if (response.isSuccessful() && response.body() != null) {
                                                        if (response.body().equals(""+"OK"+"")) {
                                                            Toast.makeText(Outbound2Activity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                                            tv_prev_barcode.setText(currentItemCode);
                                                            String[] strPalletId = data.split("-");
                                                            String[] strPalletId2 = strPalletId[0].split("A");
                                                            List<ScanPalletCode> l = new ArrayList<>();
                                                            l.add(new ScanPalletCode(Integer.parseInt(strPalletId2[1]), data, reportDate2, userName, true, Integer.parseInt(nhom), customer, region, strFlag));
                                                            MyRetrofit2.initRequest2().AddNewPalletCode(l).enqueue(new Callback<String>() {
                                                                @Override
                                                                public void onResponse(Call<String> call, Response<String> response) {
                                                                    if (response.isSuccessful() && response.body() != null) {
                                                                        if (response.body().equals('"' + "Thành công" + '"')) {
                                                                            l.clear();
                                                                        } else {
                                                                            Utilities.thongBaoDialog(Outbound2Activity.this, "Thêm khay thất bại");
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<String> call, Throwable t) {
                                                                    Utilities.thongBaoDialog(Outbound2Activity.this, "Kiểm tra kết nối Internet");
                                                                }
                                                            });

                                                            ItemScan itemScan = new ItemScan(list.get(pos).getPalletID(), list.get(pos).getSoBich(), sLMove, 2, Integer.parseInt(strT[1]), list.get(pos).getFlag());
                                                            itemScan.setId(list.get(pos).getId());
                                                            itemScanViewModel.update(itemScan);
                                                            if (iToggle == 0) {
                                                                itemScanViewModel.updateConfirmBack1();
                                                                itemScanViewModel.updateConfirmBack0PalletIdDesc();
                                                            } else {
                                                                itemScanViewModel.updateConfirmBack1();
                                                                itemScanViewModel.updateConfirmBack0PalletIdAsc();
                                                            }

                                                        } else {
                                                            Toast.makeText(Outbound2Activity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<String> call, Throwable t) {
                                                    Toast.makeText(Outbound2Activity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                                }
                                            });
//                                        } else {
//                                            Utilities.speakingSomeThingslow("Số vỉ Pallet " + list.get(pos).getPalletID() + " lớn hơn 10 bạn có cần chia nhiều khay", Outbound2Activity.this);
//                                            AlertDialog.Builder builder = new AlertDialog.Builder(Outbound2Activity.this);
//                                            builder.setMessage("Số vỉ Pallet " + list.get(pos).getPalletID() + " lớn hơn 10 bạn có cần chia nhiều khay");
//                                            builder.setNeutralButton("Không cần", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    dialogInterface.dismiss();
//                                                    JsonObject jsonObject1 = new JsonObject();
//                                                    jsonObject1.addProperty("Doc_Entry", strDocEntry);
//                                                    jsonObject1.addProperty("SLMove", sLMove);
//                                                    jsonObject1.addProperty("UserName", nhom);
//                                                    jsonObject1.addProperty("DeviceNumber", userName);
//                                                    jsonObject1.addProperty("Region", region);
//                                                    jsonObject1.addProperty("CustomerCode", customer);
//                                                    jsonObject1.addProperty("SoThung", strT[1]);
//
//                                                    Log.e(TAG, strDocEntry + " : " + userName + " : " + strDevice + " : " + sLMove);
//                                                    MyRetrofit2.initRequest2().updateXDockAllOutboundPackingDetailMove(jsonObject1).enqueue(new retrofit2.Callback<String>() {
//                                                        @Override
//                                                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                                                            if (response.isSuccessful() && response.body() != null) {
//                                                                if (response.body().equals('"' + "OK" + '"')) {
//                                                                    Toast.makeText(Outbound2Activity.this, "Thành công", Toast.LENGTH_SHORT).show();
//                                                                    tv_prev_barcode.setText(currentItemCode);
//                                                                    String[] strPalletId = data.split("-");
//                                                                    String[] strPalletId2 = strPalletId[0].split("A");
//                                                                    List<ScanPalletCode> l = new ArrayList<>();
//                                                                    l.add(new ScanPalletCode(Integer.parseInt(strPalletId2[1]), data, reportDate2, userName, true, Integer.parseInt(nhom), customer, region, strFlag));
//                                                                    MyRetrofit2.initRequest2().AddNewPalletCode(l).enqueue(new retrofit2.Callback<String>() {
//                                                                        @Override
//                                                                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                                                                            if (response.isSuccessful() && response.body() != null) {
//                                                                                if (response.body().equals('"' + "Thành công" + '"')) {
//                                                                                    l.clear();
//                                                                                } else {
//                                                                                    Utilities.thongBaoDialog(Outbound2Activity.this, "Thêm khay thất bại");
//                                                                                }
//                                                                            }
//                                                                        }
//
//                                                                        @Override
//                                                                        public void onFailure(Call<String> call, Throwable t) {
//                                                                            Utilities.thongBaoDialog(Outbound2Activity.this, "Kiểm tra kết nối Internet");
//                                                                        }
//                                                                    });
//
//                                                                    ItemScan itemScan = new ItemScan(list.get(pos).getPalletID(), list.get(pos).getSoBich(), sLMove, 2, Integer.parseInt(strT[1]), list.get(pos).getFlag());
//                                                                    itemScan.setId(list.get(pos).getId());
//                                                                    itemScanViewModel.update(itemScan);
//                                                                    if (iToggle == 0) {
//                                                                        itemScanViewModel.updateConfirmBack1();
//                                                                        itemScanViewModel.updateConfirmBack0PalletIdDesc();
//                                                                    } else {
//                                                                        itemScanViewModel.updateConfirmBack1();
//                                                                        itemScanViewModel.updateConfirmBack0PalletIdAsc();
//                                                                    }
//
//                                                                } else {
//                                                                    Toast.makeText(Outbound2Activity.this, "Thất bại", Toast.LENGTH_SHORT).show();
//                                                                }
//
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onFailure(Call<String> call, Throwable t) {
//                                                            Toast.makeText(Outbound2Activity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
//                                                }
//                                            });
//                                            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    dialogInterface.dismiss();
//                                                }
//                                            }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    Intent intent = new Intent(Outbound2Activity.this, ScanPalletCodeActivity.class);
//                                                    intent.putExtra("reportdate", reportDate2);
//                                                    intent.putExtra("region", region);
//                                                    intent.putExtra("customercode", customer);
//                                                    intent.putExtra("group", nhom);
//                                                    intent.putExtra("bit", false);
//                                                    intent.putExtra("palletid", tvPalletCurrent.getText().toString());
//                                                    intent.putExtra("docentry", strDocEntry);
//                                                    intent.putExtra("sothung", strT[1]);
//                                                    intent.putExtra("slmove", sLMove);
//                                                    intent.putExtra("sobich", strSoBich);
//                                                    intent.putExtra("id", strId);
//                                                    intent.putExtra("itoggle", iToggle);
//                                                    intent.putExtra("flag", strFlag);
//                                                    startActivity(intent);
//                                                }
//                                            }).create().show();
//
//                                        }


                                    }
//                                } else {
//                                    Utilities.speakingSomeThingslow("Số thùng/Khay của Pallet " + tvPalletCurrent.getText().toString() + " cao nhất hiện tại là " + soThung, Outbound2Activity.this);
//                                }

                            }

                        } catch (Exception e) {
                            Log.e("er", e + "");
                        }

                    }
                }


            } else {
                if (list.size() == 0) {
                    Utilities.speakingSomeThingslow(itemName, Outbound2Activity.this);
                    loadPackingView();
                } else if (list.size() > 0) {

                    if (tv_prev_barcode.getText().toString().equals(currentItemCode)) {
                        loadPackingView();
                    } else {
                        Toast.makeText(this, "Mã không khớp vui lòng thử lại! Hãy bấm Xong nếu bạn muốn Scan hàng khác", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(500);
                        }
                        mediaPlayer2.start();
//
                    }
                }
            }

        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void sendInput(String input, String itemName, String supplierID) {
        strItemCode = input;
        strSupplierID = supplierID;
        tv_prev_barcode.setText(strItemCode);
        loadDataByBarCodeAndItemCode(strItemCode, itemName);
    }


    @OnClick(R.id.flButtonPrint)
    public void printTem() {
        if (list != null && list.size() > 0) {
            if (list.get(0).getConfirm() != 2) {
                try {
                    int iPallet = list.get(0).getPalletID();
                    int iSoThung = soThung + 1;
                    String totalPaBoxWhitoutCode = null;
                    String totalPaBox = null;
                    if (customer.equals("BHX")) {
                        if (strFlag.equals("1")) {
                            totalPaBoxWhitoutCode = iPallet + "-" + iSoThung + "\n(Thịt)" + "\n"+ reportDate2;
//                            totalPaBoxWhitoutCode = "1500" + "-" + iSoThung + "\n(Thịt)" + "\n"+ reportDate2;
                            totalPaBox = "PA" + iPallet + "-" + iSoThung + "-" + strFlag;
                        } else if (strFlag.equals("2")) {
                            totalPaBoxWhitoutCode = iPallet + "-" + iSoThung + "\n(Mát)" + "\n"+ reportDate2;
                            totalPaBox = "PA" + iPallet + "-" + iSoThung + "-" + strFlag;
                        } else {
                            totalPaBoxWhitoutCode = iPallet + "-" + iSoThung + "\n(Đông)" + "\n"+ reportDate2;
                            totalPaBox = "PA" + iPallet + "-" + iSoThung + "-" + strFlag;
                        }

                    } else {
                        totalPaBoxWhitoutCode = iPallet + "-" + iSoThung + "\n"+ reportDate2;
                        totalPaBox = "PA" + iPallet + "-" + iSoThung;
                    }
//                    Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
//                    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//                    Writer codeWriter;
//                    codeWriter = new Code39Writer();
//                    BitMatrix byteMatrix = codeWriter.encode(totalPaBox, BarcodeFormat.CODE_39, 540, 380, hintMap);
//                    int width = byteMatrix.getWidth();
//                    int height = byteMatrix.getHeight();
//                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//                    for (int i = 0; i < width; i++) {
//                        for (int j = 0; j < height; j++) {
//                            bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
//                        }
//                    }
//                    Bitmap.Config config = bitmap.getConfig();
//                    int width2 = bitmap.getWidth();
//                    int height2 = bitmap.getHeight();
//
//                    Bitmap newImage = Bitmap.createBitmap(width2, height2, config);
//                    Canvas canvas = new Canvas(newImage);
//                    canvas.drawBitmap(bitmap, 0, 0, null);
//
//                    Paint paint = new Paint();
//                    Paint.FontMetrics fm = new Paint.FontMetrics();
//                    paint.setColor(Color.WHITE);
//                    paint.getFontMetrics(fm);
//                    paint.setTextSize(18);
//                    paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                    paint.setTextAlign(Paint.Align.CENTER);
//                    int margin = 5;
//
//
//                    int xPos = (canvas.getWidth() / 2);
//                    int yPos = (int) (((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) * 0.25);
////                    float w = paint.measureText(totalPaBox);
//                    canvas.drawRect(width2 - margin, yPos / 0.50f,
//                            margin, 0, paint);
//                    paint.setColor(Color.BLACK);
//                    canvas.drawText(totalPaBox, xPos, yPos + (margin * 2), paint);

                    ///----------------------------------------------

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
//                    float w = paint.measureText(totalPaBox);
                    //        cv.drawRect(0, (int) (yPos / 0.50f), margin, 0, paint1);
                    cv.drawRect(0, 0, 540, 200, paint1);
                    paint1.setColor(Color.BLACK);
                    int y = yPos + (margin * 3);
                    int count = 0;
                    if (customer.equals("BHX")) {
                        for (String line : totalPaBoxWhitoutCode.split("\n")) {
//                            cv.drawText(line, xPos, y, paint1);
//                            y += paint1.descent() - paint1.ascent();

                            if (count == 2){
                                paint1.setTextSize(20);
                                y += paint1.descent() - paint1.ascent();
                                cv.drawText(line, xPos, 20, paint1);
                            }else {
                                cv.drawText(line, xPos, y, paint1);
                                y += paint1.descent() - paint1.ascent();
                            }

                            count++;
                        }
                    } else {
                        cv.drawText(totalPaBoxWhitoutCode, xPos, y, paint1);
                    }
                    List<ScanPalletCode> l = new ArrayList<>();
                    l.add(new ScanPalletCode(Integer.parseInt(totalPaBox.split("-")[0].replace("PA", "")), totalPaBox.trim(), reportDate2, userName, true, Integer.parseInt(nhom), customer, region, totalPaBox.split("-")[2]));


                    printPhotoFromExternal(bm, l);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            } else {
                Utilities.speakingSomeThingslow("Mã này đã hoàn thành không thể in tem. Hãy chọn mã hàng khác", Outbound2Activity.this);
            }
        } else {
            Utilities.speakingSomeThingslow("Vui lòng chọn mã hàng.", Outbound2Activity.this);

        }
    }

//    @OnLongClick(R.id.flButtonPrint)
//    public boolean printMultipleTem() {
//        final Dialog dialog = new Dialog(Outbound2Activity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_print_multiple_tem);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        TextView tvText = dialog.findViewById(R.id.tvText);
//        TextView tvText2 = dialog.findViewById(R.id.tvText2);
//        EditText edtFromBox = dialog.findViewById(R.id.edtFromBox);
//        EditText edtToBox = dialog.findViewById(R.id.edtToBox);
//        EditText edtPalletNumber = dialog.findViewById(R.id.edtPalletNumber);
//        Button btnHuy = dialog.findViewById(R.id.btnHuy);
//        Button btnPrint = dialog.findViewById(R.id.btnPrint);
//
//        if (tvPalletCurrent.getText().toString() != null && !tvPalletCurrent.getText().toString().equals("")) {
//            tvText.setText("PA");
//            tvText2.setText("-" + soThung);
//            edtFromBox.setText((soThung + 1) + "");
//            edtPalletNumber.setText(tvPalletCurrent.getText().toString());
//        }
//
//        edtToBox.setFilters(new InputFilter[]{new InputFilterMinMax("1", "10")});
//
//
//        btnHuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        btnPrint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                if (!edtToBox.getText().toString().equals("")) {
//                int iFromBox = Integer.parseInt(edtFromBox.getText().toString().equals("") ? String.valueOf(1) : edtFromBox.getText().toString());
//                int iToBox = Integer.parseInt(edtToBox.getText().toString().equals("") ? String.valueOf(0) : edtToBox.getText().toString());
//                List<Bitmap> lBitmap = new ArrayList<>();
//                try {
////                        int iPallet = Integer.parseInt(tvPalletCurrent.getText().toString());
//                    int iPallet = Integer.parseInt(edtPalletNumber.getText().toString());
//                    for (int i2 = iFromBox; i2 <= iToBox + iFromBox; i2++) {
//                        int iSoThung = i2;
//                        String totalPaBox = "PA" + iPallet + "-" + iSoThung;
//                        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
//                        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//                        Writer codeWriter;
//                        codeWriter = new Code39Writer();
//                        BitMatrix byteMatrix = codeWriter.encode(totalPaBox, BarcodeFormat.CODE_39, 500, 250, hintMap);
//                        int width = byteMatrix.getWidth();
//                        int height = byteMatrix.getHeight();
//                        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//                        for (int i = 0; i < width; i++) {
//                            for (int j = 0; j < height; j++) {
//                                bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
//                            }
//                        }
//                        Bitmap.Config config = bitmap.getConfig();
//                        int width2 = bitmap.getWidth();
//                        int height2 = bitmap.getHeight();
//
//                        Bitmap newImage = Bitmap.createBitmap(width2, height2, config);
//                        Canvas canvas = new Canvas(newImage);
//                        canvas.drawBitmap(bitmap, 0, 0, null);
//
//                        Paint paint = new Paint();
//                        Paint.FontMetrics fm = new Paint.FontMetrics();
//                        paint.setColor(Color.WHITE);
//                        paint.getFontMetrics(fm);
//                        paint.setTextSize(50);
//                        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                        paint.setTextAlign(Paint.Align.CENTER);
//                        int margin = 5;
//
//
//                        int xPos = (canvas.getWidth() / 2);
//                        int yPos = (int) (((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) * 0.25);
////                    float w = paint.measureText(totalPaBox);
//                        canvas.drawRect(width2 - margin, yPos / 0.50f,
//                                margin, 0, paint);
//                        paint.setColor(Color.BLACK);
//                        canvas.drawText(totalPaBox, xPos, yPos + (margin * 2), paint);
//
//                        lBitmap.add(newImage);
//                    }
//
//                    printMultiplePhotoFromExternal(lBitmap, dialog);
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                }
////                } else {
////                    Utilities.speakingSomeThingslow("Không được để trống", Outbound2Activity.this);
////                }
//
//            }
//        });
//
//        dialog.show();
//        dialog.getWindow().setAttributes(lp);
//
//
//        return true;
//
//    }


    private Connection getZebraPrinterConn() {
        return new BluetoothConnection(strMac);
    }


    private void printPhotoFromExternal(final Bitmap bitmap, List<ScanPalletCode> l) {


        MyRetrofit2.initRequest2().AddNewPalletCode(l).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().equals('"' + "Thành công" + '"')) {
                        l.clear();
                        Utilities.thongBaoDialog(Outbound2Activity.this, "Thêm khay thành công");
                        if (strMac != null && !strMac.equals("")) {
                            new Thread(new Runnable() {
                                public void run() {

                                    try {

                                        Looper.prepare();
                                        Toast.makeText(Outbound2Activity.this, "Sending image to printer", Toast.LENGTH_SHORT).show();
                                        Connection connection = getZebraPrinterConn();
                                        connection.open();
                                        ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);

                                        printer.printImage(ZebraImageFactory.getImage(bitmap), 0, 0, (int) (bitmap.getWidth() / 1.2f), bitmap.getHeight(), false);
                                        connection.close();

                                    } catch (ConnectionException e) {
                                        helper.showErrorDialogOnGuiThread(e.getMessage());
                                    } catch (ZebraPrinterLanguageUnknownException e) {
                                        helper.showErrorDialogOnGuiThread(e.getMessage());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        bitmap.recycle();
                                        helper.dismissLoadingDialog();
                                        Looper.myLooper().quit();
                                    }



                                }
                            }).start();
                        }
                    } else {
                        Utilities.thongBaoDialog(Outbound2Activity.this, "Hệ thống chưa lưu được khay này.Vui lòng in lại");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Utilities.thongBaoDialog(Outbound2Activity.this, "Kiểm tra kết nối Internet");
            }
        });
    }


    private void printMultiplePhotoFromExternal(final List<Bitmap> bitmap, Dialog dialog) {
        if (strMac != null && !strMac.equals("")) {
            new Thread(new Runnable() {
                public void run() {
                    try {

                        Looper.prepare();
                        Toast.makeText(Outbound2Activity.this, "Sending image to printer", Toast.LENGTH_SHORT).show();
                        Connection connection = getZebraPrinterConn();
                        connection.open();
                        ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);

                        for (Bitmap b : bitmap) {
                            printer.printImage(ZebraImageFactory.getImage(b), (int) (b.getWidth() / 3), b.getHeight(), b.getWidth(), b.getHeight(), false);
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
//                        bitmap.recycle();
                        helper.dismissLoadingDialog();
                        Looper.myLooper().quit();
                    }
                }
            }).start();
        }

    }

    @OnClick(R.id.btnAutoIn)
    public void AutoIn(View view){
        Intent intent = new Intent(Outbound2Activity.this, AutoInActivity.class);
        intent.putExtra("customer", customer);
        intent.putExtra("time", reportDate2);
        intent.putExtra("region",region);
        intent.putExtra("nhom",nhom);
        intent.putExtra("username",userName);
        startActivity(intent);
    }


}