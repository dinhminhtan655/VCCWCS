package com.wcs.vcc.main.newscanmasan.itemmasan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.PickPackShipPackScanParameter;
import com.wcs.vcc.api.xdoc.response.PaletInfor;
import com.wcs.vcc.main.RingScanActivity;
import com.wcs.vcc.main.newscanmasan.APIGetNewLablePalletResponse;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.GroupSortingInfoResponse;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.PickingList;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.PickingListAdapter;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.PickingListParent;
import com.wcs.vcc.main.scanhang.ABAListIItemAllFragment;
import com.wcs.vcc.preferences.DatePref;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.preferences.PalletFromTo;
import com.wcs.vcc.preferences.SpinCusIdPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class XdocPackingScanByLotActivity  extends RingScanActivity {
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

    private  String TypeScan = "+1";
    private String PickingListOrder;

    private Integer palletNumber = 0;

    Boolean  IsWeightingRequire;
    String strMac;
    Integer palletID1 = 0;

    Integer PalletIDWH;
    String ProductionDate,ExpDate,WH;
    GroupSortingInfoResponse groupSortingInfo;

    PaletInfor paletInfor;

    APIGetNewLablePalletResponse apiGetNewLablePalletResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xdoc_packing_scan_by_lot);
        ButterKnife.bind(this);

        setUpScan();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        strMac = settings.getString(ZEBRA_DEMO_BLUETOOTH_ADDRESS, "");
        strCustomerID = String.valueOf(SpinCusIdPref.LoadCusID(XdocPackingScanByLotActivity.this));
        reportDate = DatePref.LoadOrderDate(XdocPackingScanByLotActivity.this);
        PalletIDWH =getIntent().getIntExtra("PalletIDWH",0);

        MyRetrofit.initRequest(this).APIGetPalletInfor(PalletIDWH)
                .enqueue(new Callback<PaletInfor>() {
                    @Override
                    public void onResponse(Response<PaletInfor> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            paletInfor = response.body();
                            if(paletInfor!=null){
                                tvPalletNum.setText(paletInfor.getPalletNumber().toString());
                                tvMessage.setText(paletInfor.getLocationNumber());
                                PickingListOrder = paletInfor.getProductNumber();
                                Lot=paletInfor.getCustomerRef();
                                getPickingList(strCustomerID, reportDate, PickingListOrder,PickingListOrder);
                            }

                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {

                    }
                });





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
                if(groupSortingInfo!=null){
                    ABAListIItemAllFragment listItemFragment = ABAListIItemAllFragment.ABAnewInstance(reportDate, etGroupSorting.getText().toString(), groupSortingInfo.Region, groupSortingInfo.CustomerERP);
                    listItemFragment.show(fm, null);
                }


            }
        });


        curentList = new ArrayList<>();

        username = LoginPref.getUsername(this);
        androidId = Utilities.getAndroidID(getApplicationContext());
        etPalletFrom.setText(PalletFromTo.loadPalletFrom(XdocPackingScanByLotActivity.this));
        etPalletTo.setText(PalletFromTo.loadPalletTo(XdocPackingScanByLotActivity.this));



        tvPalletNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }


    @Override
    public void onData(final String data) {
        super.onData(data);
        if(paletInfor==null) {
            tvMessage.setText("Vui lòng scan label chia hàng!");
            Utilities.speakingSomeThing("Vui lòng scan label chia hàng!",XdocPackingScanByLotActivity.this);
            return;
        }
        if(data.contains("PRINTER"))
        {
            LoginPref.setPrinter(XdocPackingScanByLotActivity.this,data.replace("PRINTER",""));
            tvMessage.setText("Kết nối máy in " + data.replace("PRINTER",""));
            return;
        }




        tvMessage.setText(data);




        if(getPalletIDFromLabel(data)==0){
            return;
        }

        Integer qty = 1;

        if(btnChangeType.getText().equals(TypeScan)){
            qty=1*pickingLists.get(0).Quantity;
        }else{
            qty =-1*pickingLists.get(0).Quantity;
        }

        palletNumber = palletID1;

        Scan(data,qty);

    }

    private int getPalletIDFromLabel(String data){

        //Loại tem mới , dùng để quản lý khay rổ tự động
        if(ValidateTypeLabel(data)==1){
            palletID1 = Integer.parseInt(data.substring(0,data.indexOf("ID")).replace("PA",""));
        } else if(data.contains("PRINTLABEL")&&data.contains("PRG")){
            String tempPallet = data.substring(0,data.indexOf("PRG")).replace("PRINTLABEL","");

            String productGroup = "0";

            if(data.contains("PRG")){
                productGroup = data.substring(data.indexOf("PRG"),data.length()).replaceAll("PRG","");
            }
            tvMessage.setText("Đang in tem  " + data);
            return 0;
        }

        else if(data.contains("ID")) {
            palletID1 = Integer.parseInt(data.substring(0,data.indexOf("ID")).replace("PA",""));
        }else{
            palletID1 = Integer.parseInt(data.replace("PA",""));
        }
        return palletID1;
    }

    private void Scan(String data, Integer qty){
        if(pickingLists.size()>0&&palletNumber.toString().equals(pickingLists.get(0).StoreNumber)==false){
            tvMessage.setText("Vui Lòng scan palelt mới!");
            tvMessage.setText("Sai Pallet rồi!");
            Utilities.speakingSomeThing("Sai Pallet rồi!",XdocPackingScanByLotActivity.this);
            return;
        }
        PickPackShipPackScanParameter packShipPackScanParameter = new PickPackShipPackScanParameter(username, androidId, data,qty,PickingListOrder,pickingLists.get(0).XdocPickingListID);
        packShipPackScanParameter.CustomerRef2 = pickingLists.get(0).CustomerRef2;
        packShipPackScanParameter.CustomerID = strCustomerID;
        packShipPackScanParameter.PalletNumber = paletInfor.getPalletNumber();
        packShipPackScanParameter.ProductionDate = paletInfor.getProductionDate();
        packShipPackScanParameter.ExpDate = paletInfor.getUseByDate();
        packShipPackScanParameter.Lot = paletInfor.getCustomerRef();

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

                                Utilities.speakingSomeThing(response.body().getMessage(),XdocPackingScanByLotActivity.this);
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
            btnChangeType.setText("-1");
        }else{
            btnChangeType.setText("+1");
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

    private void getPickingList(String strCustomerID, String reportDate, String PickingListOrder, String data) {
        if(paletInfor==null){
           return;
        }


        Integer palletFrom = 0;
        Integer palletTo = 0;
        try {
            palletFrom = Integer.parseInt( etPalletFrom.getText().toString());
            PalletFromTo.savePalletFrom(XdocPackingScanByLotActivity.this,palletFrom.toString());
            palletTo = Integer.parseInt( etPalletTo.getText().toString());
            PalletFromTo.savePalletTo(XdocPackingScanByLotActivity.this,palletTo.toString());
        }catch (Exception e){}

        MyRetrofit.initRequest(XdocPackingScanByLotActivity.this).pickingListLoadPickingListDetails(strCustomerID, reportDate,PickingListOrder,data,palletFrom,palletTo,strCustomerID).enqueue(new Callback<PickingListParent>() {
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
                        pickingLists = response.body().PickingList;
                        if(pickingLists.size()==0){
                            return;
                        }
                        Integer qtyMiss = pickingLists.get(0).Quantity - pickingLists.get(0).NetQty;

                        if ( qtyMiss>0){
                            Utilities.speakingSomeThing(pickingLists.get(0).StoreNumber + "!!!" + qtyMiss , XdocPackingScanByLotActivity.this);
                            isLoad = 0;
                        }
                        curentList = pickingLists;
                        pickingListAdapter = new PickingListAdapter(new RecyclerViewItemOrderListener<PickingList>() {
                            @Override
                            public void onClick(PickingList item, int position, int order) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(XdocPackingScanByLotActivity.this);
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
                                            Scan(PickingListOrder, totalCarton);

                                        }
                                        dialog2.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onLongClick(PickingList item, int position, int order) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(XdocPackingScanByLotActivity.this);
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
                            Utilities.speakingSomeThing("Đã chia xong",XdocPackingScanByLotActivity.this);
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
}
