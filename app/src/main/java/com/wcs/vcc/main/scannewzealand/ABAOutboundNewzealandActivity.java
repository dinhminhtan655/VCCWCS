package com.wcs.vcc.main.scannewzealand;

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

public class ABAOutboundNewzealandActivity extends RingScanActivity implements ABAListItemNewzealandFragment.ABAOnInputListener2 {

    private static final String TAG = "ABAOutboundNewzealandAc";
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
    @BindView(R.id.rv_vinmartdetail)
    RecyclerView rv_vinmartdetail;
    @BindView(R.id.btnXong)
    Button btnXong;
    @BindView(R.id.toggleToiLui)
    ToggleButton toggleToiLui;
    @BindView(R.id.imgList)
    ImageView imgList;


    private Calendar calendar;
    private String reportDate, reportDate2, userName, strDocEntry, strDevice, strSLScan, strCode, strItemCode, strSupplierID;
    LinearLayoutManager layoutManager;
    Vibrator vibrator;
    MediaPlayer mediaPlayer, mediaPlayer2;

    TextToSpeech textToSpeech;

    String[] strP;

    int centerItemPosition;

    ABAOutboundVinmartAdapter adapter;

    int ii = 0, pos = 0;

    String currentItemCode = "";

    List<XDockVinOutboundPackingViewNewZealandABA> list;

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    String nhom;
    int iToggle = 0;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_b_a_outbound_newzealand);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        Log.e("name", LoginPref.getRealName(ABAOutboundNewzealandActivity.this));

        Bundle b = getIntent().getExtras();

        if (b != null) {
            nhom = b.getString("nhom");
        } else {
            nhom = "";
        }

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(ABAOutboundNewzealandActivity.this, R.raw.sairoi2);
        mediaPlayer2 = MediaPlayer.create(ABAOutboundNewzealandActivity.this, R.raw.nhammaroi);
        list = new ArrayList<XDockVinOutboundPackingViewNewZealandABA>();

        userName = LoginPref.getUsername(ABAOutboundNewzealandActivity.this);
        strDevice = Utilities.getAndroidID(ABAOutboundNewzealandActivity.this);
        tvUsername.setText(userName + " N" + nhom);

        setUpScan();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat dateformat2 = new SimpleDateFormat(DATE_FORMAT2);
        reportDate = dateformat.format(calendar.getTime());
        reportDate2 = dateformat2.format(calendar.getTime());
//        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        btChooseDate.setText(reportDate);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        int totalVisibleItems = layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition();
        centerItemPosition = totalVisibleItems / 2;

        toggleToiLui.setChecked(false);

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()){
                    case R.id.toggleToiLui:
                        if (b){
                            iToggle = 1;
                        }else {
                            iToggle = 0;
                        }
                        Toast.makeText(ABAOutboundNewzealandActivity.this, "" + iToggle, Toast.LENGTH_SHORT).show();
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
                ABAListItemNewzealandFragment listItemFragment = ABAListItemNewzealandFragment.ABAnewInstance(reportDate2, nhom);
                listItemFragment.show(fm,null);
            }
        });




        btnXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ABAOutboundNewzealandActivity.this);
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
                    et_target_scan.setText(result.get(0).trim().replace(" ",""));
                }
                break;
            }

        }
    }


    private void loadPackingView(final String strPreCode) {
        final JsonObject jsonObject = new JsonObject();

//        if (strPreCode.length() >= 13) {
        jsonObject.addProperty("ItemCode", tv_prev_barcode.getText().toString());
//            jsonObject.addProperty("Barcode", tv_prev_barcode.getText().toString());
        jsonObject.addProperty("Delivery_Date", reportDate2);
        jsonObject.addProperty("User_Chia_Item", nhom);
        jsonObject.addProperty("CustomerID", "NEWZEALAND");
        jsonObject.addProperty("SupplierID", strSupplierID);
//            jsonObject.addProperty("strItemBarCode", "BCODE");
        jsonObject.addProperty("sort", iToggle);
//        } else {
//            jsonObject.addProperty("ItemCode", tv_prev_barcode.getText().toString());
//            jsonObject.addProperty("Barcode", "1");
//            jsonObject.addProperty("Delivery_Date", reportDate2);
//            jsonObject.addProperty("User_Chia_Item", nhom);
//            jsonObject.addProperty("strItemBarCode", "ICODE");

//            jsonObject.addProperty("sort", iToggle);
//        }

        MyRetrofit2.initRequest2().loadXDockNEWZEALANDOutboundPackingView(jsonObject).enqueue(new retrofit2.Callback<List<XDockVinOutboundPackingViewNewZealandABA>>() {
            @Override
            public void onResponse(Call<List<XDockVinOutboundPackingViewNewZealandABA>> call, retrofit2.Response<List<XDockVinOutboundPackingViewNewZealandABA>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // Lưu lại mã hiện tại đang scan
                    if (list.size() > 0) {
                        list.clear();
                    } else if (list.size() == 0) {
//                        SettingPref.setCodeItem(OutboundVinmartActivity.this, tv_prev_barcode.getText().toString());
                        currentItemCode = tv_prev_barcode.getText().toString();
                    }

                    list.addAll(response.body());

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
                                    final EditText edtSLMove;
                                    final TextView tvDiaSoPallet;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ABAOutboundNewzealandActivity.this);
                                    LayoutInflater inflater = getLayoutInflater();
                                    @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.dialog_update_slmove, (ViewGroup) findViewById(R.layout.activity_outbound_vinmart));
                                    edtSLMove = dialogView.findViewById(R.id.edDiaCartonTotal);
                                    tvDiaSoPallet = dialogView.findViewById(R.id.tvDiaSoPallet);

                                    tvDiaSoPallet.setText(item.Pallet_ID);

                                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            JsonObject jsonObject2 = new JsonObject();
                                            int sLMove2 = Integer.parseInt(edtSLMove.getText().toString().equals("") ? String.valueOf(0) : edtSLMove.getText().toString());
                                            if (sLMove2 + Integer.parseInt(item.SLScan) > item.SoBich) {
                                                Toast.makeText(ABAOutboundNewzealandActivity.this, "Số move không được lớn hơn SL Chia", Toast.LENGTH_SHORT).show();
                                            } else {
                                                jsonObject2.addProperty("Doc_Entry", strDocEntry);
                                                jsonObject2.addProperty("SLMove", sLMove2);
                                                jsonObject2.addProperty("UserName", nhom);
                                                jsonObject2.addProperty("DeviceNumber", userName);



                                                MyRetrofit2.initRequest2().updateXDockNEWZEALANDOutboundPackingDetailMove(jsonObject2).enqueue(new retrofit2.Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                                        if (response.isSuccessful() && response.body() != null) {
                                                            if (response.body().equals("OK")) {
                                                                Toast.makeText(ABAOutboundNewzealandActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                                                loadPackingView(tv_prev_barcode.getText().toString());
                                                            } else {
                                                                loadPackingView(tv_prev_barcode.getText().toString());
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<String> call, Throwable t) {
                                                        Toast.makeText(ABAOutboundNewzealandActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
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
                Toast.makeText(ABAOutboundNewzealandActivity.this, "Vui lòng kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
//        MyRetrofit2.initRequest2().loadXDockNEWZEALANDOutboundPackingView(jsonObject).enqueue(new Callback<List<XDockVinOutboundPackingViewABA>>() {
//            @Override
//            public void onResponse(final Response<List<XDockVinOutboundPackingViewABA>> response, Retrofit retrofit) {
//                if (response.isSuccess() && response.body() != null) {
//
//                    // Lưu lại mã hiện tại đang scan
//                    if (list.size() > 0) {
//                        list.clear();
//                    } else if (list.size() == 0) {
////                        SettingPref.setCodeItem(OutboundVinmartActivity.this, tv_prev_barcode.getText().toString());
//                        currentItemCode = tv_prev_barcode.getText().toString();
//                    }
//
//                    list.addAll(response.body());
//
//                    if (list.size() > 0) {
//
//                        tvItemName.setText(list.get(0).Item_Name);
//
//                        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//                            @Override
//                            public void onInit(int i) {
//                                if (i == TextToSpeech.SUCCESS) {
//                                    String content = response.body().get(0).Pallet_ID + " !! " + (int) response.body().get(0).SoBich;
//                                    String content2 = "Đã hoàn thành";
//                                    textToSpeech.setLanguage(new Locale("vi_VN"));
//                                    textToSpeech.setSpeechRate(2.5f);
//                                    if (response.body().get(0).XacNhan.equals("0")) {
//                                        textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
//                                    } else if (response.body().get(0).XacNhan.equals("2")) {
//                                        textToSpeech.speak(content2, TextToSpeech.QUEUE_FLUSH, null);
//                                    }
//                                }
//                            }
//                        });
//                    } else {
//                        tvItemName.setText("NaN");
//                    }
//
//                    adapter = new ABAOutboundVinmartAdapter(new RecyclerViewItemOrderListener<XDockVinOutboundPackingViewABA>() {
//                        @Override
//                        public void onClick(final XDockVinOutboundPackingViewABA item, int position, int order) {
//                            switch (order) {
//                                case 0:
//                                    Log.e("aaa", item.Doc_Entry);
//                                    strDocEntry = item.Doc_Entry;
//                                    strSLScan = item.SLScan;
//                                    pos = position;
//                                    break;
//
//                                case 2:
//                                    final EditText edtSLMove;
//                                    final TextView tvDiaSoPallet;
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(ABAOutboundNewzealandActivity.this);
//                                    LayoutInflater inflater = getLayoutInflater();
//                                    @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.dialog_update_slmove, (ViewGroup) findViewById(R.layout.activity_outbound_vinmart));
//                                    edtSLMove = dialogView.findViewById(R.id.edtDiaSLMove);
//                                    tvDiaSoPallet = dialogView.findViewById(R.id.tvDiaSoPallet);
//
//                                    tvDiaSoPallet.setText(item.Pallet_ID);
//
//                                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                                        }
//                                    }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            JsonObject jsonObject2 = new JsonObject();
//                                            int sLMove2 = Integer.parseInt(edtSLMove.getText().toString().equals("") ? String.valueOf(0) : edtSLMove.getText().toString());
//                                            if (sLMove2 + Integer.parseInt(item.SLScan) > item.SoBich) {
//                                                Toast.makeText(ABAOutboundNewzealandActivity.this, "Số move không được lớn hơn SL Chia", Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                jsonObject2.addProperty("Doc_Entry", strDocEntry);
//                                                jsonObject2.addProperty("SLMove", sLMove2);
//                                                jsonObject2.addProperty("UserName", nhom);
//                                                jsonObject2.addProperty("DeviceNumber", userName);
//
//
//
//                                                MyRetrofit2.initRequest2().updateXDockNEWZEALANDOutboundPackingDetailMove(jsonObject2).enqueue(new retrofit2.Callback<String>() {
//                                                    @Override
//                                                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                                                        if (response.isSuccessful() && response.body() != null) {
//                                                            if (response.body().equals("OK")) {
//                                                                Toast.makeText(ABAOutboundNewzealandActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
//                                                                loadPackingView(tv_prev_barcode.getText().toString());
//                                                            } else {
//                                                                loadPackingView(tv_prev_barcode.getText().toString());
//                                                            }
//
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(Call<String> call, Throwable t) {
//                                                        Toast.makeText(ABAOutboundNewzealandActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    });
//                                    builder.setView(dialogView);
//                                    Dialog dialog = builder.create();
//                                    dialog.show();
//
//
//                                    break;
//                            }
//                        }
//
//                        @Override
//                        public void onLongClick(XDockVinOutboundPackingViewABA item, int position, int order) {
//
//                        }
//                    }, list);
//                    adapter.replace(list);
//                    rv_vinmartdetail.setLayoutManager(layoutManager);
//                    rv_vinmartdetail.setAdapter(adapter);
//
//                    int sumSLChia = 0, sumSLScan = 0, sumMove = 0, sumDieuChinh = 0, sumConfirm = 0, sumUpDown = 0;
//
//                    rv_vinmartdetail.smoothScrollToPosition(pos);
//                    rv_vinmartdetail.setScrollY(centerItemPosition);
//
//                    if (list.size() > 0) {
//                        tvPalletCurrent.setText(list.get(0).Pallet_ID);
//                    } else {
//                        tvPalletCurrent.setText("");
//                    }
//
//
//                    for (XDockVinOutboundPackingViewABA s : list) {
//                        sumSLChia += (int) s.SoBich;
//                        sumSLScan += Integer.parseInt(s.SLScan);
//                        sumMove += Integer.parseInt(s.SLMove);
//                        sumDieuChinh += Integer.parseInt(s.SLDieuChinh);
//                        sumConfirm += Integer.parseInt(s.getConfirmQuantity());
//                        sumUpDown += Integer.parseInt(s.getThieuDu());
//                        ii++;
//                    }
//
//                    tvSumSLChia.setText(String.valueOf(sumSLChia));
//                    tvSumMove.setText(String.valueOf(sumMove));
//                    tvSumDieuChinh.setText(String.valueOf(sumDieuChinh));
//                    tvSumConfirm.setText(String.valueOf(sumConfirm));
//                    tvSumUpDown.setText(String.valueOf(sumUpDown));
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Toast.makeText(ABAOutboundNewzealandActivity.this, "Vui lòng kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
//            }
//        });
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
        loadDataByBarCodeAndItemCode(data,"");
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
                } catch (Exception e) {
                    Log.e("er", e + "");
                }
                if (list.size() > 0) {
                    try {
                        if (Integer.parseInt(strP[1]) != Integer.parseInt(tvPalletCurrent.getText().toString())) {
                            Toast.makeText(this, "Bạn chưa thêm hàng xong ở Pallet thứ: " + tvPalletCurrent.getText().toString(), Toast.LENGTH_SHORT).show();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                vibrator.vibrate(500);
                            }
                            mediaPlayer.start();
                        } else {
                            if (list.get(pos).XacNhan.equals("0")) {
                                int sLMove = (int) list.get(pos).SoBich - Integer.parseInt(list.get(pos).SLDieuChinh);
                                JsonObject jsonObject1 = new JsonObject();
                                jsonObject1.addProperty("Doc_Entry", strDocEntry);
                                jsonObject1.addProperty("SLMove", sLMove);
                                jsonObject1.addProperty("UserName", nhom);
                                jsonObject1.addProperty("DeviceNumber", userName);

                                Log.e(TAG, strDocEntry + " : " + userName + " : " + strDevice + " : " + sLMove);
                                MyRetrofit2.initRequest2().updateXDockNEWZEALANDOutboundPackingDetailMove(jsonObject1).enqueue(new retrofit2.Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            if (response.body().equals('"' + "OK" + '"')) {
                                                Toast.makeText(ABAOutboundNewzealandActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                                tv_prev_barcode.setText(currentItemCode);
                                                if (list.size() > 0) {
                                                    list.clear();
                                                    adapter.notifyDataSetChanged();
                                                }
                                                loadPackingView(tv_prev_barcode.getText().toString());
                                            } else {
                                                Toast.makeText(ABAOutboundNewzealandActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(ABAOutboundNewzealandActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                    } catch (Exception e) {
                        Log.e("er", e + "");
                    }

                }
            } else {
                if (list.size() == 0) {
                    Utilities.speakingSomeThingslow(itemName,ABAOutboundNewzealandActivity.this);
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
//        soundPool.release();
//        soundPool = null;
    }

    @Override
    public void sendInput(String input, String itemName, String supplierID) {
        strItemCode = input;
        strSupplierID = supplierID;
        tv_prev_barcode.setText(strItemCode);
        loadDataByBarCodeAndItemCode(strItemCode,itemName);
    }
}