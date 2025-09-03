package com.wcs.vcc.main.newscanmasan.itemmasan;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickPackShipCartonInsertParameter;
import com.wcs.vcc.api.PickPackShipPackScanParameter;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.newscanmasan.APIGetNewLablePalletResponse;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.GroupSortingInfoResponse;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.PickingList;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.PickingListAdapter;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.PickingListParent;
import com.wcs.vcc.main.scanhang.ABAListIItemAllFragment;
import com.wcs.vcc.main.vo.PrinterZebraSupport;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.PalletFromTo;
import com.wcs.vcc.preferences.SpinCusIdPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.ZebraImageFactory;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class XdocPackingScanActivity extends RingScanActivity implements ABAListIItemAllFragment.ABAOnInputListener2 {
    @BindView(R.id.rvScanMasan)
    RecyclerView rvScanMasan;
    @BindView(R.id.tv_prev_barcode)
    TextView tv_prev_barcode;
    @BindView(R.id.et_target_scan)
    EditText et_target_scan;
    @BindView(R.id.tvSumSLOrder)
    TextView tvSumSLOrder;
    @BindView(R.id.tvSumTLOrder)
    TextView tvSumTLOrder;
    @BindView(R.id.tvSumSLScan)
    TextView tvSumSLScan;
    @BindView(R.id.tvSumTLScan)
    TextView tvSumTLScan;
    @BindView(R.id.tvPalletNum)
    TextView tvPalletNum;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvItemInfor)
    TextView tvItemInfor;
    @BindView(R.id.etPalletFrom)
    EditText etPalletFrom;
    @BindView(R.id.etPalletTo)
    EditText etPalletTo;
    TextView tvDiaPallet;
    TextView tvDiaItem;
    EditText edDiaCartonTotal;
    @BindView(R.id.edQty)
    EditText edQty;

    @BindView(R.id.etGroupSorting)
    EditText etGroupSorting;

    @BindView(R.id.btnSaveCarton)
    Button btnChangeType;

    @BindView(R.id.btnPrintLabel)
    Button btnPrintLabel;

    Button btnDiaOk;

    Button btnDiaTypeCarton;

    private static final String PREFS_NAME = "OurSavedAddress";
    private static final String ZEBRA_DEMO_BLUETOOTH_ADDRESS = "ZEBRA_DEMO_BLUETOOTH_ADDRESS";

    private Calendar calendar;
    private String  reportDate, strCustomerID,productionDate;
    private String username, androidId, barcode = "";
    private int isLoad = 1;

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd";

    private PickingListAdapter pickingListAdapter;

    public List<PickingList> pickingLists = new ArrayList<>();
    private List<PickingList> curentList = new ArrayList<>();

    private PickingList curentStore;

    public String curBarcode;
    private String Lot;

    private  String TypeScan = "+";
    private String PickingListOrder;

    private Integer palletNumber = 0;

    Boolean  IsWeightingRequire;
    String strMac;
    Integer palletID1 = 0;

    private  Integer QtyMiss = 0;

    GroupSortingInfoResponse groupSortingInfo;

    APIGetNewLablePalletResponse apiGetNewLablePalletResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xdoc_packing_scan);
        ButterKnife.bind(this);

        setUpScan();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        strMac = settings.getString(ZEBRA_DEMO_BLUETOOTH_ADDRESS, "");
        strCustomerID = String.valueOf(SpinCusIdPref.LoadCusID(XdocPackingScanActivity.this));
        reportDate = getIntent().getStringExtra("reportDate");
        PickingListOrder =getIntent().getStringExtra("pickingListOrder");
        productionDate =getIntent().getStringExtra("productionDate");
        Lot=getIntent().getStringExtra("Lot");
        tvPalletNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tvItemInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                if(etGroupSorting.getText()==null){
                    etGroupSorting.setText("0");
                }
                if(groupSortingInfo!=null) {
                    ABAListIItemAllFragment listItemFragment = ABAListIItemAllFragment.ABAnewInstance(reportDate, etGroupSorting.getText().toString(), groupSortingInfo.Region, groupSortingInfo.CustomerERP);
                    listItemFragment.show(fm, null);
                }
            }
        });

        btnPrintLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pickingLists.size()>0){
                    try {
                        getLabel(Integer.parseInt(pickingLists.get(0).StoreNumber),strCustomerID,pickingLists.get(0).CustomerRef,0);
                    }catch (Exception e){}

                }
                MyRetrofit.initRequest(XdocPackingScanActivity.this).insertPickPackShipCarton(
                        new PickPackShipCartonInsertParameter(username, androidId, pickingLists.get(0).DispatchingOrderNumber)).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            showMessage("Tạo mới thùng thành công!");
                            getPickingList(strCustomerID, reportDate, PickingListOrder,"");
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });

        curentList = new ArrayList<>();

        username = LoginPref.getUsername(this);
        androidId = Utilities.getAndroidID(getApplicationContext());
        etPalletFrom.setText(PalletFromTo.loadPalletFrom(XdocPackingScanActivity.this));
        etPalletTo.setText(PalletFromTo.loadPalletTo(XdocPackingScanActivity.this));

        getPickingList(strCustomerID, reportDate, PickingListOrder,"");


        tvPalletNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                Integer palletFrom = 0;
//                Integer palletTo = 0;
//                try {
//                    palletFrom = Integer.parseInt( etPalletFrom.getText().toString());
//                    palletTo = Integer.parseInt( etPalletTo.getText().toString());
//                }catch (Exception e){}
//
//                if(palletFrom>palletTo){
//                    Integer palletTemp = palletTo;
//                    palletTo = palletFrom;
//                    palletFrom = palletTemp;
//                }
//                for(Integer i = palletFrom;i <= palletTo; i++){
//                    getLabel(i,strCustomerID,pickingLists.get(0).CustomerRef,0);
//                }
                return false;
            }
        });
    }


    @Override
    public void onData(final String data) {
        super.onData(data);
        Log.d("DUONG", data);
        if(data.contains("PRINTER"))
        {
            LoginPref.setPrinter(XdocPackingScanActivity.this,data.replace("PRINTER",""));
            tvMessage.setText("Kết nối máy in " + data.replace("PRINTER",""));
            return;
        }

        QtyMiss = Integer.parseInt(edQty.getText().toString());
        tvMessage.setText(data);

        if(data.contains("PLO")) {
            String[] listCode = data.split("-");
            Log.d("DUONG", listCode.toString());
            if(listCode.length==4) {
                String ProductID = listCode[0].replaceAll("PLO","");
                String pickingListOrder = listCode[0].replaceAll("PLO","");
                Lot = listCode[1];
                String palletFrom = listCode[2];
                String palletTo = listCode[3];
                PickingListOrder =  pickingListOrder;

                PalletFromTo.savePalletFrom(XdocPackingScanActivity.this,palletFrom);
                PalletFromTo.savePalletTo(XdocPackingScanActivity.this,palletTo);

                etPalletFrom.setText( palletFrom);
                etPalletTo.setText( palletTo);
                getPickingList(strCustomerID, reportDate, PickingListOrder,PickingListOrder);
                return;
            }
            else {
                if(getPalletIDFromLabel(data)==0){
                    return;
                }
                else {
                    Integer qty = 1;

                    if(btnChangeType.getText().equals(TypeScan)){
                        qty=1*QtyMiss;
                    }else{
                        qty =-1*QtyMiss;
                    }

                    palletNumber = palletID1;

                    Scan(data,qty);
                }
            }
        }
        else if (data.contains("PA")) {
            if (getPalletIDFromLabel(data) == 0) {
                return;
            }
            else {
                Integer qty = 1;
                if(btnChangeType.getText().equals(TypeScan)) {
                    qty = 1 * QtyMiss;
                }else{
                    qty = -1 * QtyMiss;
                }

                palletNumber = palletID1;

                Log.d("DUONG", palletNumber.toString());

                Scan(data,qty);
            }
        }
        else {
            Integer qty = 1;

            if(btnChangeType.getText().equals(TypeScan)){
                qty = 1 * QtyMiss;
            }else{
                qty = -1 * QtyMiss;
            }

            PickPackShipPackScanParameter packShipPackScanParameter = new PickPackShipPackScanParameter(username, androidId, data,qty,PickingListOrder,pickingLists.get(0).XdocPickingListID);
            packShipPackScanParameter.CustomerRef2 = pickingLists.get(0).CustomerRef2;
            packShipPackScanParameter.CustomerID = strCustomerID;
            packShipPackScanParameter.ProductionDate = reportDate;
            packShipPackScanParameter.ExpDate = reportDate;
            packShipPackScanParameter.Lot = reportDate;
            packShipPackScanParameter.PalletNumber= Integer.valueOf(pickingLists.get(0).StoreNumber);
            MyRetrofit.initRequest(this).XdocPickingListScan(packShipPackScanParameter)
                    .enqueue(new Callback<XdocPackingPickingListOrderResponse>() {
                        @Override
                        public void onResponse(Response<XdocPackingPickingListOrderResponse> response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                if(response.body().getMessage().length()==0){
                                    getPickingList(strCustomerID, reportDate, PickingListOrder,PickingListOrder);
                                }else{
                                    tvMessage.setText(response.body().getMessage());
                                    tvMessage.setText(response.body().getMessage());

                                    //Log.d("DUONG", response.body().getMessage());

                                    Utilities.speakingSomeThing(response.body().getMessage(),XdocPackingScanActivity.this);
                                    getPickingList(strCustomerID, reportDate, PickingListOrder,"");
                                }
                            }
                        }
                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
        }
    }

    private int getPalletIDFromLabel(String data) {

        //Loại tem mới , dùng để quản lý khay rổ tự động
        if(ValidateTypeLabel(data) == 1) {
            palletID1 = Integer.parseInt(data.substring(0,data.indexOf("ID")).replace("PA",""));
        } else if(data.contains("PRINTLABEL")&&data.contains("PRG")) {
            String tempPallet = data.substring(0,data.indexOf("PRG")).replace("PRINTLABEL","");

            String productGroup = "0";

            if(data.contains("PRG")){
                productGroup = data.substring(data.indexOf("PRG"),data.length()).replaceAll("PRG","");
            }

            getLabel(Integer.parseInt(tempPallet),strCustomerID,productGroup,0);

            tvMessage.setText("Đang in tem  " + data);
            return 0;
        }

        else if(data.contains("ID")) {
            palletID1 = Integer.parseInt(data.substring(0,data.indexOf("ID")).replace("PA",""));
        }else {
            palletID1 = Integer.parseInt(data.replace("PA",""));
        }
        return palletID1;
    }

    private void Scan(String data, Integer qty) {

        if(pickingLists.size()>0 && palletNumber.toString().equals(pickingLists.get(0).StoreNumber)==false){
            tvMessage.setText("Vui Lòng scan palelt mới!");
            tvMessage.setText("Sai Pallet rồi!");
            Utilities.speakingSomeThing("Sai Pallet rồi!",XdocPackingScanActivity.this);
            return;
        }
        PickPackShipPackScanParameter packShipPackScanParameter = new PickPackShipPackScanParameter(username, androidId, data,qty,PickingListOrder,pickingLists.get(0).XdocPickingListID);
        packShipPackScanParameter.CustomerRef2 = pickingLists.get(0).CustomerRef2;
        packShipPackScanParameter.CustomerID = strCustomerID;
        packShipPackScanParameter.ProductionDate = reportDate;
        packShipPackScanParameter.ExpDate = reportDate;
        packShipPackScanParameter.Lot = reportDate;
        packShipPackScanParameter.PalletNumber=99999;
        MyRetrofit.initRequest(this).XdocPickingListScan(packShipPackScanParameter)
                .enqueue(new Callback<XdocPackingPickingListOrderResponse>() {
                    @Override
                    public void onResponse(Response<XdocPackingPickingListOrderResponse> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            Log.d("DUONG", response.body().getMessage());
                            if(response.body().getMessage().length()==0){
                                getPickingList(strCustomerID, reportDate, PickingListOrder,PickingListOrder);
                            }else{
                                tvMessage.setText(response.body().getMessage());
                                tvMessage.setText(response.body().getMessage());

                                Utilities.speakingSomeThing(response.body().getMessage(),XdocPackingScanActivity.this);
                                getPickingList(strCustomerID, reportDate, PickingListOrder,"");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }

    public  void onChangeType(View view)
    {
        if(btnChangeType.getText().equals(TypeScan))
        {
            btnChangeType.setText("-");
        }else{
            btnChangeType.setText("+");
        }
        getPickingList(strCustomerID, reportDate, PickingListOrder,PickingListOrder);
    }

    public void onXong(View view) {
        et_target_scan.requestFocus();
        //GroupSortingInfo
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_target_scan, InputMethodManager.SHOW_IMPLICIT);
        getPickingList(strCustomerID, reportDate, PickingListOrder,PickingListOrder);
    }

    public void onChange(View view){
        String palletTemp ="";
        if(etPalletFrom.getText()!=null && etPalletTo.getText()!=null){
            palletTemp = etPalletFrom.getText().toString();
            etPalletFrom.setText(etPalletTo.getText().toString());
            etPalletTo.setText(palletTemp);
        }
        et_target_scan.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_target_scan, InputMethodManager.SHOW_IMPLICIT);
        getPickingList(strCustomerID, reportDate, PickingListOrder,PickingListOrder);
    }

    private void getPickingList(String strCustomerID, String reportDate,String PickingListOrder, String data) {
        Integer palletFrom = 0;
        Integer palletTo = 0;
        try {
            palletFrom = Integer.parseInt( etPalletFrom.getText().toString());
            PalletFromTo.savePalletFrom(XdocPackingScanActivity.this,palletFrom.toString());
            palletTo = Integer.parseInt( etPalletTo.getText().toString());
            PalletFromTo.savePalletTo(XdocPackingScanActivity.this,palletTo.toString());
        }catch (Exception e){}

        MyRetrofit.initRequest(XdocPackingScanActivity.this).pickingListLoadPickingListDetails(strCustomerID, reportDate,PickingListOrder,data,palletFrom,palletTo,Lot).enqueue(new Callback<PickingListParent>() {
            @Override
            public void onResponse(Response<PickingListParent> response, Retrofit retrofit) {

                if (response.isSuccess() && response != null) {
                    groupSortingInfo = response.body().GroupSortingInfo;
                    if(response.body().GroupSortingInfo!=null){
                        etGroupSorting.setText(response.body().GroupSortingInfo.GroupSorting.toString());
                    }else{
                        etGroupSorting.setText("0");
                    }
                    if (response.body().PickingList.size() > 0) {

                        curBarcode = data;
                        pickingLists = response.body().PickingList;



                        if(pickingLists.size()==0){
                            return;
                        }

                        Integer qtyMiss = pickingLists.get(0).Quantity - pickingLists.get(0).NetQty;

                        Log.d("DUONG", String.valueOf(pickingLists.get(0).Quantity)+"-"+String.valueOf(pickingLists.get(0).NetQty));
                        Log.d("DUONG", String.valueOf(qtyMiss));

                        if(edQty.getText() != null && Integer.parseInt(edQty.getText().toString()) == 0){
                            QtyMiss = qtyMiss;
                            edQty.setText(qtyMiss.toString());
                        }
                        if ( qtyMiss > 0) {
                            edQty.setText(String.valueOf(qtyMiss));
                            Utilities.speakingSomeThing(pickingLists.get(0).StoreNumber + "!!!" + qtyMiss , XdocPackingScanActivity.this);
                            isLoad = 0;
                        }
                        curentList = pickingLists;
                        pickingListAdapter = new PickingListAdapter(new RecyclerViewItemOrderListener<PickingList>() {
                            @Override
                            public void onClick(PickingList item, int position, int order) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(XdocPackingScanActivity.this);
                                builder.setCancelable(false);
                                LayoutInflater inflater = getLayoutInflater();
                                @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.dialog_input_carton_sorting, (ViewGroup) findViewById(R.layout.activity_dispatching_order_packing));
                                tvDiaPallet = dialogView.findViewById(R.id.tvDiaPallet);
                                tvDiaItem = dialogView.findViewById(R.id.tvDiaItem);
                                edDiaCartonTotal = dialogView.findViewById(R.id.edDiaCartonTotal);
                                btnDiaOk = dialogView.findViewById(R.id.btnDiaOk);
                                btnDiaTypeCarton =  dialogView.findViewById(R.id.btnDiaTypeCarton);
                                btnDiaTypeCarton.setEnabled(false);


                                tvDiaPallet.setText(item.StoreNumber);
                                tvDiaItem.setText(item.ProductName);
                                builder.setView(dialogView);
                                final Dialog dialog2 = builder.create();
                                dialog2.show();

                                btnDiaOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(edDiaCartonTotal.getText()!=null &&edDiaCartonTotal.getText().length()> 0){
                                            Integer totalCarton = Integer.parseInt(edDiaCartonTotal.getText().toString());
                                            palletNumber = Integer.parseInt(item.StoreNumber);
                                            getLabel(Integer.parseInt(item.StoreNumber),strCustomerID,item.CustomerRef,totalCarton);
                                        }
                                        dialog2.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onLongClick(PickingList item, int position, int order) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(XdocPackingScanActivity.this);
                                builder.setCancelable(false);
                                LayoutInflater inflater = getLayoutInflater();
                                @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.dialog_input_carton_sorting, (ViewGroup) findViewById(R.layout.activity_dispatching_order_packing));
                                tvDiaPallet = dialogView.findViewById(R.id.tvDiaPallet);
                                tvDiaItem = dialogView.findViewById(R.id.tvDiaItem);
                                edDiaCartonTotal = dialogView.findViewById(R.id.edDiaCartonTotal);
                                btnDiaOk = dialogView.findViewById(R.id.btnDiaOk);
                                btnDiaTypeCarton =  dialogView.findViewById(R.id.btnDiaTypeCarton);
                                btnDiaOk.setEnabled(false);

                                tvDiaPallet.setText(item.StoreNumber);
                                tvDiaItem.setText(item.ProductName);
                                builder.setView(dialogView);
                                final Dialog dialog2 = builder.create();
                                dialog2.show();
                                btnDiaTypeCarton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(edDiaCartonTotal.getText()!=null &&edDiaCartonTotal.getText().length()> 0){
                                            Integer totalCarton = Integer.parseInt(edDiaCartonTotal.getText().toString());
                                            palletNumber = Integer.parseInt(item.StoreNumber);
                                            getLabel(Integer.parseInt(item.StoreNumber),strCustomerID,"0",totalCarton);
                                        }
                                        dialog2.dismiss();
                                    }
                                });


                            }
                        }, curentList);
                        pickingListAdapter.replace(pickingLists);
                        rvScanMasan.setAdapter(pickingListAdapter);
                        tvItemInfor.setText(pickingLists.get(0).ProductName);

                        int iSLO = 0, iSLScan = 0;
                        int dTLO = 0, dTLScan = 0;

                        for (PickingList p : pickingLists) {
                            iSLO += p.Quantity;
                            dTLO += p.Weights;
                            iSLScan += p.NetQty;
                            dTLScan += p.Quantity-p.NetQty;
                        }

                        NumberFormat formatter = new DecimalFormat("#0.00");

                        tvSumSLOrder.setText(String.valueOf(iSLO));
                        tvSumTLOrder.setText(String.valueOf(formatter.format(dTLO)));
                        tvSumSLScan.setText(String.valueOf(iSLScan));
                        tvSumTLScan.setText(String.valueOf(dTLScan));
                        if(qtyMiss==0){
                            Utilities.speakingSomeThing("Đã chia xong",XdocPackingScanActivity.this);
                            return;
                        }

                        tvPalletNum.setText(pickingLists.get(0).StoreNumber+ "-" +( pickingLists.get(0).Quantity- pickingLists.get(0).NetQty));

                    }

                } else {
                    rvScanMasan.setAdapter(new PickingListAdapter());
                    showMessage("Không thấy data");
                }


            }

            @Override
            public void onFailure(Throwable t) {
                showMessage("Kiểm tra kết nối mạng");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Const.isActivating = true;
        getPickingList(strCustomerID, reportDate, PickingListOrder,PickingListOrder);
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

    public void getLabel(int PalletID, String CusCode, String ProductGroup,Integer totalCarton){

        MyRetrofit.initRequest(XdocPackingScanActivity.this).AddNewPalletCode(PalletID, CusCode,ProductGroup ).enqueue(new Callback<List<APIGetNewLablePalletResponse>>() {
            @Override
            public void onResponse(Response<List<APIGetNewLablePalletResponse>> response, Retrofit retrofit) {
                if(response.body()==null) return;;
                apiGetNewLablePalletResponse = response.body().get(0);
                if (apiGetNewLablePalletResponse != null) {
                    String totalPaBoxWhitoutCode = apiGetNewLablePalletResponse.PalletID.replace("PA","") +"\n(" + apiGetNewLablePalletResponse.ProductGroup.replace("PRG","") +")\n" + reportDate;
                    String totalPaBox = apiGetNewLablePalletResponse.PalletID +"-"
                            + apiGetNewLablePalletResponse.ID.replace("ID","")+"-"
                            + apiGetNewLablePalletResponse.CustomerCode.replace("CUS","")
                            + apiGetNewLablePalletResponse.ProductGroup.replace("PRG","");

                    //Utilities.speakingSomeThing("Tem " + apiGetNewLablePalletResponse.PalletID + " Hàng " +apiGetNewLablePalletResponse.ProductGroup ,XdocPackingScanActivity.this);
                    Bitmap bm = Bitmap.createBitmap(540, 480, Bitmap.Config.ARGB_8888);

                    /*create a canvas object*/
                    Canvas cv = new Canvas(bm);
                    /*create a paint object*/
                    Paint paint = new Paint();


                    Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                    Writer codeWriter;
                    codeWriter = new Code39Writer();
                    BitMatrix byteMatrix = null;

                    Bitmap bitmap = FORMAT_CODE(apiGetNewLablePalletResponse.Label);


                    cv.drawBitmap(bitmap, 0f, 180f, paint);

                    Bitmap bitmap1 = Bitmap.createBitmap(540, 230, Bitmap.Config.ARGB_8888);

                    Canvas canvas = new Canvas(bitmap1);
                    canvas.drawBitmap(bm, 0, 0, null);
                    Paint paint1 = new Paint();

                    Paint.FontMetrics fm = new Paint.FontMetrics();
                    paint1.setColor(Color.WHITE);
                    paint1.getFontMetrics(fm);
                    paint1.setTextSize(100);
                    paint1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    paint1.setTextAlign(Paint.Align.CENTER);
                    int margin = 5;

                    int xPos = (cv.getWidth() / 2);
                    int yPos = (int) (((cv.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) * 0.25);
                    // float w = paint.measureText(totalPaBox);
                    //        cv.drawRect(0, (int) (yPos / 0.50f), margin, 0, paint1);
                    cv.drawRect(0, 0, 540, 200, paint1);
                    paint1.setColor(Color.BLACK);
                    int y = yPos + (margin * 3);
                    int count = 0;
                    for (String line : totalPaBoxWhitoutCode.split("\n")) {
                        if (count == 2){
                            paint1.setTextSize(20);
                            y += paint1.descent() - paint1.ascent();
                            cv.drawText(line, xPos, 30, paint1);
                        }else if(count==0) {
                            paint1.setTextSize(180);
                            // y += paint1.descent() - paint1.ascent();
                            cv.drawText(line, xPos, 160, paint1);


                        }else{
                            paint1.setTextSize(60);
                            cv.drawText(line, xPos, 200, paint1);
                            //  y += paint1.descent() - paint1.ascent();
                        }

                        count++;
                    }

                    cv.drawText(totalPaBoxWhitoutCode, xPos, y, paint1);
                    if(totalCarton>0){
                        Scan(apiGetNewLablePalletResponse.Label,totalCarton);
                    }
                    Connection connection = PrinterZebraSupport.getConnect(XdocPackingScanActivity.this);
                    if(connection==null){
                        return;
                    }
                    try {
                        connection.open();
                        ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
                        printer.printImage(ZebraImageFactory.getImage(bm), 200, 0, (int) (bitmap.getWidth() / 1.2f), bitmap.getHeight(), false);

                        connection.close();
                    } catch (ConnectionException | ZebraPrinterLanguageUnknownException | IOException e) { e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private Bitmap FORMAT_CODE(String text) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODE_128, 540, 450);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection getZebraPrinterConn() {
        return new BluetoothConnection(strMac);
    }

    @Override
    public void sendInput(String input, String itemName, String supplierID) {
        PickingListOrder = input;
        Lot = supplierID;
        et_target_scan.requestFocus();
        //GroupSortingInfo
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_target_scan, InputMethodManager.SHOW_IMPLICIT);
        getPickingList(strCustomerID, reportDate, PickingListOrder,PickingListOrder);
    }


}
