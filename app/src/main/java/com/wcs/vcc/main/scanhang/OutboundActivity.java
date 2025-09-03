package com.wcs.vcc.main.scanhang;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit2;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.scannewzealand.adapter.ABAOutboundVinmartAdapter;
import com.wcs.vcc.main.scannewzealand.model.XDockVinOutboundPackingViewNewZealandABA;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutboundActivity extends RingScanActivity implements ABAListIItemAllFragment.ABAOnInputListener2 {

    private static final String TAG = "OutboundActivity";

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
    @BindView(R.id.toggleToiLui)
    ToggleButton toggleToiLui;
    @BindView(R.id.imgList)
    ImageView imgList;
    @BindView(R.id.tvRegion)
    TextView tvRegion;


    private Calendar calendar;
    private String reportDate, reportDate2, userName, strDocEntry, strDevice, strSLScan, strCode, strItemCode, strSupplierID;
    LinearLayoutManager layoutManager;
    Vibrator vibrator;
    MediaPlayer mediaPlayer, mediaPlayer2;

    TextToSpeech textToSpeech;

    String[] strP, strT;

    int centerItemPosition;

    ABAOutboundVinmartAdapter adapter;

//    private ItemScanViewModel itemScanViewModel;

    int ii = 0, pos = 0, soThung;

    String currentItemCode = "";

    List<XDockVinOutboundPackingViewNewZealandABA> list;
    //    ArrayList<Integer> listSoThung;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    int iToggle = 0;
    String region, nhom, customer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbound);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        Log.e("name", LoginPref.getRealName(OutboundActivity.this));


        Bundle b = getIntent().getExtras();

        if (b != null) {
            customer = b.getString("customer");
            region = b.getString("region");
            nhom = b.getString("nhom");
            tvRegion.setText(region);


        } else {
            region = "";
            tvRegion.setText("Null");
            nhom = "";
        }

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(OutboundActivity.this, R.raw.sairoi2);
        mediaPlayer2 = MediaPlayer.create(OutboundActivity.this, R.raw.nhammaroi);
        list = new ArrayList<XDockVinOutboundPackingViewNewZealandABA>();

        userName = LoginPref.getUsername(OutboundActivity.this);
        strDevice = Utilities.getAndroidID(OutboundActivity.this);
        tvUsername.setText(userName + "/" + customer + "/N" + nhom);

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
                        Toast.makeText(OutboundActivity.this, "" + iToggle, Toast.LENGTH_SHORT).show();
                        loadPackingView(currentItemCode);
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


        btnXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(OutboundActivity.this);
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
                            adapter.notifyDataSetChanged();
                        }

//                        SettingPref.removeCodeItem(OutboundVinmartActivity.this);
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


    private void loadPackingView(final String strPreCode) {

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ItemCode", tv_prev_barcode.getText().toString());
        jsonObject.addProperty("Delivery_Date", reportDate2);
        jsonObject.addProperty("User_Chia_Item", nhom);
        jsonObject.addProperty("CustomerID", customer);
        jsonObject.addProperty("SupplierID", strSupplierID);
        jsonObject.addProperty("Region", region);
        jsonObject.addProperty("sort", iToggle);

        MyRetrofit2.initRequest2().loadXDockAllOutboundPackingView(jsonObject).enqueue(new Callback<List<XDockVinOutboundPackingViewNewZealandABA>>() {
            @Override
            public void onResponse(Call<List<XDockVinOutboundPackingViewNewZealandABA>> call, Response<List<XDockVinOutboundPackingViewNewZealandABA>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // Lưu lại mã hiện tại đang scan
                    if (list.size() > 0) {
                        list.clear();
                    } else if (list.size() == 0) {
                        currentItemCode = tv_prev_barcode.getText().toString();
                    }

                    list.addAll(response.body());


                    if (response.body().size() > 0) {
                        final JsonObject jsonObject3 = new JsonObject();
                        jsonObject3.addProperty("PalletId", response.body().get(0).Pallet_ID);
                        jsonObject3.addProperty("Delivery_Date", reportDate2);
                        jsonObject3.addProperty("Region", region);
                        jsonObject3.addProperty("CustomerCode", customer);

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
                                Toast.makeText(OutboundActivity.this, "Không lấy được số thùng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    if (list.size() > 0) {

                        tvItemName.setText(list.get(0).Item_Name);

                        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int i) {
                                if (i == TextToSpeech.SUCCESS) {
                                    String content = response.body().get(0).Pallet_ID + " !! " + (int) response.body().get(0).SoBich;
                                    String content2 = "Đã hoàn thành";
                                    textToSpeech.setLanguage(new Locale("vi_VN"));
                                    textToSpeech.setSpeechRate(2.5f);
                                    if (response.body().get(0).XacNhan.equals("0")) {
                                        textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
                                    } else if (response.body().get(0).XacNhan.equals("2")) {
                                        textToSpeech.speak(content2, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                }
                            }
                        });
                    } else {
                        tvItemName.setText("NaN");
                    }

                    adapter = new ABAOutboundVinmartAdapter(new RecyclerViewItemOrderListener<XDockVinOutboundPackingViewNewZealandABA>() {
                        @Override
                        public void onClick(final XDockVinOutboundPackingViewNewZealandABA item, int position, int order) {
                            switch (order) {
                                case 0:
                                    Log.e("aaa", item.Doc_Entry);
                                    strDocEntry = item.Doc_Entry;
                                    strSLScan = item.SLScan;
                                    pos = position;
                                    break;

                                case 2:
                                    final EditText edtSLMove, edtDiaSLThung;
                                    final TextView tvDiaSoPallet;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(OutboundActivity.this);
                                    LayoutInflater inflater = getLayoutInflater();
                                    @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.dialog_update_slmove, (ViewGroup) findViewById(R.layout.activity_outbound_vinmart));
                                    edtSLMove = dialogView.findViewById(R.id.edDiaCartonTotal);
                                    edtDiaSLThung = dialogView.findViewById(R.id.edtDiaSLThung);
                                    tvDiaSoPallet = dialogView.findViewById(R.id.tvDiaSoPallet);

                                    tvDiaSoPallet.setText(item.Pallet_ID);

                                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            int slThung = Integer.parseInt(edtDiaSLThung.getText().toString().equals("") ? String.valueOf(0) : edtDiaSLThung.getText().toString());
                                            if (slThung >= soThung) {
                                                JsonObject jsonObject2 = new JsonObject();

                                                int sLMove2 = Integer.parseInt(edtSLMove.getText().toString().equals("") ? String.valueOf(0) : edtSLMove.getText().toString());
                                                if (sLMove2 + Integer.parseInt(item.SLScan) > item.SoBich) {
                                                    Toast.makeText(OutboundActivity.this, "Số move không được lớn hơn SL Chia", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    jsonObject2.addProperty("Doc_Entry", strDocEntry);
                                                    jsonObject2.addProperty("SLMove", sLMove2);
                                                    jsonObject2.addProperty("UserName", nhom);
                                                    jsonObject2.addProperty("DeviceNumber", userName);
                                                    jsonObject2.addProperty("Region", region);
                                                    jsonObject2.addProperty("CustomerCode", customer);
                                                    jsonObject2.addProperty("SoThung", slThung);

                                                    MyRetrofit2.initRequest2().updateXDockAllOutboundPackingDetailMove(jsonObject2).enqueue(new Callback<String>() {
                                                        @Override
                                                        public void onResponse(Call<String> call, Response<String> response) {
                                                            if (response.isSuccessful() && response.body() != null) {
                                                                if (response.body().equals("OK")) {
                                                                    Toast.makeText(OutboundActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                                                    loadPackingView(tv_prev_barcode.getText().toString());
                                                                } else {
                                                                    loadPackingView(tv_prev_barcode.getText().toString());
                                                                }

                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<String> call, Throwable t) {
                                                            Toast.makeText(OutboundActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }

                                            } else {
                                                Utilities.speakingSomeThingslow("Số thùng cao nhất hiện tại là " + soThung, OutboundActivity.this);
                                            }
                                        }
                                    });
                                    builder.setView(dialogView);
                                    Dialog dialog = builder.create();
                                    dialog.show();


                                    break;
                            }
                        }

                        @Override
                        public void onLongClick(XDockVinOutboundPackingViewNewZealandABA item, int position, int order) {

                        }
                    }, list);
                    adapter.replace(list);
                    rv_vinmartdetail.setLayoutManager(layoutManager);
                    rv_vinmartdetail.setAdapter(adapter);

                    int sumSLChia = 0, sumSLScan = 0, sumMove = 0, sumDieuChinh = 0, sumConfirm = 0, sumUpDown = 0;

                    rv_vinmartdetail.smoothScrollToPosition(pos);
                    rv_vinmartdetail.setScrollY(centerItemPosition);

                    if (list.size() > 0) {
                        tvPalletCurrent.setText(list.get(0).Pallet_ID);
                    } else {
                        tvPalletCurrent.setText("");
                        tvSTCurrent.setText("");
                    }


                    for (XDockVinOutboundPackingViewNewZealandABA s : list) {
                        sumSLChia += (int) s.SoBich;
                        sumSLScan += Integer.parseInt(s.SLScan);
                        sumMove += Integer.parseInt(s.SLMove);
                        sumDieuChinh += Integer.parseInt(s.SLDieuChinh);
                        sumConfirm += Integer.parseInt(s.getConfirmQuantity());
                        sumUpDown += Integer.parseInt(s.getThieuDu());
                        ii++;
                    }

                    tvSumSLChia.setText(String.valueOf(sumSLChia));
                    tvSumMove.setText(String.valueOf(sumMove));
                    tvSumDieuChinh.setText(String.valueOf(sumDieuChinh));
                    tvSumConfirm.setText(String.valueOf(sumConfirm));
                    tvSumUpDown.setText(String.valueOf(sumUpDown));


                }
            }

            @Override
            public void onFailure(Call<List<XDockVinOutboundPackingViewNewZealandABA>> call, Throwable t) {
                Toast.makeText(OutboundActivity.this, "Vui lòng kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
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
                loadPackingView(currentItemCode);

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
        loadPackingView(currentItemCode);
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        reportDate2 = Utilities.formatDate_yyyyMMdd(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        loadPackingView(currentItemCode);
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
                            if (Integer.parseInt(strT[1]) >= soThung) {
                                if (list.get(pos).XacNhan.equals("0")) {
                                    int sLMove = (int) list.get(pos).SoBich - Integer.parseInt(list.get(pos).SLDieuChinh);
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
                                                if (response.body().equals('"' + "OK" + '"')) {
                                                    Toast.makeText(OutboundActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                                    tv_prev_barcode.setText(currentItemCode);
                                                    if (list.size() > 0) {
                                                        list.clear();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                    loadPackingView(tv_prev_barcode.getText().toString());
                                                } else {
                                                    Toast.makeText(OutboundActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(OutboundActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Utilities.speakingSomeThingslow("Số thùng/Khay của Pallet " + tvPalletCurrent.getText().toString() + " cao nhất hiện tại là " + soThung, OutboundActivity.this);
                            }

                        }

                    } catch (Exception e) {
                        Log.e("er", e + "");
                    }

                }
            } else {
                if (list.size() == 0) {
                    Utilities.speakingSomeThingslow(itemName, OutboundActivity.this);
                    loadPackingView(tv_prev_barcode.getText().toString());
                } else if (list.size() > 0) {

                    if (tv_prev_barcode.getText().toString().equals(currentItemCode)) {
                        loadPackingView(currentItemCode);
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
}