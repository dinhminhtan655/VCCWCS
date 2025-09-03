package com.wcs.vcc.main.scanbhx;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wcs.wcs.R;import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.scanhang.adapter.ItemScanAdapter;
import com.wcs.vcc.main.scanhang.model.ItemScan;
import com.wcs.vcc.main.scanhang.viewmodel.ItemScanViewModel;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class ABAOutboundBHX2Activity extends RingScanActivity implements ABAListItemBHXFragment.ABAOnInputListener2 {


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

    private Calendar calendar;
    private String reportDate, reportDate2, userName, strDocEntry, strDevice, strSLScan, strCode, strItemCode, strSupplierID, strMac;
    LinearLayoutManager layoutManager;
    Vibrator vibrator;
    MediaPlayer mediaPlayer, mediaPlayer2;

    TextToSpeech textToSpeech;

    String[] strP, strT;

    int centerItemPosition;

    ItemScanAdapter adapter;

    private ItemScanViewModel itemScanViewModel;

    int ii = 0, pos = 0, soThung;

    String currentItemCode = "";

    List<ItemScan> list;
    //    ArrayList<Integer> listSoThung;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    int iToggle = 0;
    String region, nhom, customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_b_a_outbound_b_h_x2);
    }

    @Override
    public void sendInput(String input, String itemName, String supplierID) {

    }



}