package com.wcs.vcc.main.detailphieu;

import static com.wcs.vcc.utilities.Utilities.removeDiacritics;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wcs.vcc.main.containerandtruckinfor.PdfDocumentAdapter;
import com.wcs.wcs.R;
import com.wcs.vcc.api.CustomerRequirementParam;
import com.wcs.vcc.api.DispatchingOrderScannedDeleteParameter;
import com.wcs.vcc.api.EmployeeWorkingISOCheckInputParams;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.OrdersInfo;
import com.wcs.vcc.api.PalletActualQuantityUpdateParam;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.UpdateDispatchingOrderDetailParameter;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.ChupHinhActivity;
import com.wcs.vcc.main.detailphieu.iso.IsoActivity;
import com.wcs.vcc.main.detailphieu.so_do_day.SoDoDayActivity;
import com.wcs.vcc.main.detailphieu.worker.WorkerActivity;
import com.wcs.vcc.main.vo.Group;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OrderDetailActivity extends EmdkActivity
        implements AdapterView.OnItemLongClickListener, View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = OrderDetailActivity.class.getSimpleName();
    public static final String PDF_FILE = "order_detail_pdf.pdf";
    public static final String DO_ID = "DO_ID";
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_SUB_TITLE = 1;
    public static final int TYPE_NORMAL_TEXT = 2;
    public ListView listView;
    private EditText etBarcode, etTargetScan;
    private TextView totalQuantity, tvQuantityCheck, tvTotalQuantityChecked;
    private TextView totalScanned;
    private View.OnClickListener action;
    private String orderNumber, orderNumberType;
    private String scanResult = "xx123456789", userName;
    private OrderDetailAdapter adapter;
    private MenuItem item_sort, itemFilter;
    private int filterResult = 0;
    private String deviceNumber;
    private List<Item> completedList = new LinkedList<>();
    private HashMap<String, List<OrderDetail>> groupDO = new LinkedHashMap<>();
    private List<String> keySetGroupDO = new LinkedList<>();

    private List<OrderDetail> list = new ArrayList<>();
    private int total = 0;
    private int scanned = 0;
    private int checkedQuantity = 0;
    private int quantityNormal = 0;
    private int barcodeNumber;
    private int positionJustScan = -1;
    private boolean isPallet, isManager, isGotoWorker;
    private OrderDetail palletInfo;
    private AlertDialog palletScanDialog;
    private String customerType, customerNumber, dispatchingID;
    private boolean isScanDK;
    private Calendar calendar, calendar2;
    private String reportDate, reportDate2;
    private boolean isScan = true;
    private String key = "";

    private List<OrderDetail> orderDetailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mapView();
        setListenerView();
        initial();
    }

    private void mapView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.lv_order_detail);
        etBarcode = findViewById(R.id.barcode);
        totalQuantity = findViewById(R.id.tv_total);
        tvQuantityCheck = findViewById(R.id.tv_QuantityCheck);
        totalScanned = findViewById(R.id.tv_scanned);
        tvTotalQuantityChecked = findViewById(R.id.tv_totalChecked);
        etTargetScan = findViewById(R.id.et_target_scan);

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
                    onData(contents.replaceAll("\t\t\n", ""));
                }
            }
        });
    }

    private void setListenerView() {
        findViewById(R.id.so_do_day).setOnClickListener(this);
        findViewById(R.id.take_picture).setOnClickListener(this);
        findViewById(R.id.worker).setOnClickListener(this);
        findViewById(R.id.btn_iso).setOnClickListener(this);
        findViewById(R.id.btnAccept).setOnClickListener(this);
        findViewById(R.id.print).setOnClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initial() {
        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        reportDate = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
        reportDate2 = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar2.getTimeInMillis()));
        deviceNumber = Utilities.getAndroidID(getApplicationContext());
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        orderNumber = getIntent().getStringExtra(ORDER_NUMBER);
        customerType = getIntent().getStringExtra("CUSTOMER_TYPE");
        isGotoWorker = getIntent().getBooleanExtra("GO_TO_WORKER", false);
        dispatchingID = getIntent().getStringExtra("DISPATCHING_ID");
        isGotoWorker = getIntent().getBooleanExtra("GO_TO_WORKER", false);
        String positionGroup = LoginPref.getPositionGroup(this);
        if (getIntent().hasExtra("BARCODE")) {
            scanResult = getIntent().getStringExtra("BARCODE");
        }
        isManager = Group.isEqualGroup(positionGroup, Group.MANAGER) || Group.isEqualGroup(positionGroup, Group.SUPERVISOR);

        getSupportActionBar().setTitle(orderNumber);
        Utilities.showBackIcon(getSupportActionBar());
        setUpScan();

        getRequest();

        if (orderNumber != null && !orderNumber.equals("")) {
            key = orderNumber.substring(0, 2);
            if (key.equalsIgnoreCase("DO")) {
                tvQuantityCheck.setText("TX");
            } else if (key.equalsIgnoreCase("RO")) {
                tvQuantityCheck.setText("TN");
            }
        }

        adapter = new OrderDetailAdapter(this, completedList, new IOrderDetailListener() {
            @Override
            public void onPalletNumberClick(Item item, int position) {

            }

            @Override
            public void onQuantityNumberClick(final Item item, int position) {
                OrderDetail items = (OrderDetail) item;
                String barcode = "PI";
                for (int i = 0; i < 11 - (items.PalletNumber.length() + 2); i++) {
                    barcode = barcode.concat("0");
                }
                onData(barcode + items.PalletNumber);


            }
        });

        listView.setAdapter(adapter);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderDetail(listView);
            }
        };
//        getOrderDetail(listView);
    }

    public void getOrderDetail(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getDetailPhieu(new OrdersInfo(scanResult, orderNumber, userName, deviceNumber, customerType)).enqueue(new Callback<List<OrderDetail>>() {
            @Override
            public void onResponse(Response<List<OrderDetail>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    orderDetailList.clear();
                    orderDetailList.addAll(response.body());

                    resetUI();

                    groupDO(response.body());

                    merge();

                    updateUI();

                    checkAutoScan();

                    dismissDialog(dialog);

                    if (!isPallet && isManager && isGotoWorker) {
                        gotoWorker();
                        isGotoWorker = false;
                    }
                } else {
                    dismissDialog(dialog);
                    if (isPallet) {
                        showPalletScanDialog(false, scanResult);
                        scanResult = "xx123456789";
                        isPallet = false;
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(OrderDetailActivity.this, t, TAG, view, action);
            }
        });
    }


    private void resetUI() {
        keySetGroupDO.clear();
        groupDO.clear();
        completedList.clear();
        positionJustScan = -1;
        total = 0;
        scanned = 0;
        checkedQuantity = 0;
        quantityNormal = 0;
        palletInfo = null;
    }

    public HashMap<String, List<OrderDetail>> groupDO(List<OrderDetail> listOrderDetail) {

        for (OrderDetail orderDetail : listOrderDetail) {

            String result = orderDetail.getResult();
//            int iActualQuantity = Integer.parseInt(orderDetail.getActualQuantity());
            int quantity = Integer.parseInt(orderDetail.getQuantityOfPackages());
            int quantityScan = Integer.parseInt(orderDetail.getScanQty());
            int quantityChecked = Integer.parseInt(orderDetail.getActualQuantity());
            total += quantity;
            scanned += quantityScan;
            checkedQuantity += quantityChecked;
            if (String.valueOf(barcodeNumber).equals(orderDetail.PalletNumber) || scanResult.equalsIgnoreCase(orderDetail.BarcodeString)) {
                palletInfo = orderDetail;
            }
            if (isResultScanValidWithCaseFilter(result)) {
                if (result.trim().length() == 0)
                    quantityNormal++;
                if (result.equalsIgnoreCase("OK") || result.equalsIgnoreCase("XX")) {
//                    scanned += quantity;
                }

                String labelDO = orderDetail.getDO();
                if (groupDO.containsKey(labelDO)) {
                    groupDO.get(labelDO).add(orderDetail);
                } else {
                    keySetGroupDO.add(labelDO);
                    List<OrderDetail> items = new LinkedList<>();
                    items.add(orderDetail);
                    groupDO.put(labelDO, items);
                }
            }
        }
        return groupDO;
    }

    private boolean isResultScanValidWithCaseFilter(String orderResult) {
        orderResult = orderResult.trim();
        return filterResult == 0
                || (filterResult == 1 && (orderResult.equals("") || orderResult.equals("NO")))
                || (filterResult == 2 && (orderResult.equals("OK") || orderResult.equals("XX")));
    }

    public List<Item> merge() {
        for (String labelDO : keySetGroupDO) {
            list.clear();
            list = groupDO.get(labelDO);
            List<OrderDetail> items = groupDO.get(labelDO);

            DO DO = new DO(items.get(0).getDO() + " ~ " + items.get(0).getSpecialRequirement());
            completedList.add(DO);

            int sizeGroupDO = items.size();
            String tmpProductName = "";
            Product product = null;

            for (int i = 0; i < sizeGroupDO; i++) {
                OrderDetail orderDetail = items.get(i);
                orderDetail.setCustomerNumber(customerNumber);
                String productNumber = orderDetail.getProductNumber();
                String productName = orderDetail.getProductName();
                int quantity = Integer.parseInt(orderDetail.getQuantityOfPackages());

                if (notSameProductNameInGroupDO(tmpProductName, productName)) {

                    product = new Product(productNumber + "~" + orderDetail.getProductName(), quantity, orderDetail.PalletNumber, orderDetail.ProductNumber);
                    completedList.add(product);
                    tmpProductName = productName;

                } else {
                    if (product != null)
                        product.setTotal(product.getTotal() + quantity);
                }
                if (barcodeNumber != 0 && orderDetail.PalletNumber.contains(barcodeNumber + ""))
                    positionJustScan = completedList.size();

                completedList.add(orderDetail);
            }
        }
        return completedList;
    }


    private boolean notSameProductNameInGroupDO(String tmpProductName, String productName) {
        return !tmpProductName.equals(productName);
    }

    public void getRequest() {
        MyRetrofit.initRequest(this).getRequestPhieu(new CustomerRequirementParam(orderNumber)).enqueue(new Callback<List<RequirementInfo>>() {
            @Override
            public void onResponse(Response<List<RequirementInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    StringBuilder message = new StringBuilder();
                    for (RequirementInfo info : response.body())
                        message.append(info.getRequirement()).append("\n");
                    if (message.toString().length() > 0)
                        Utilities.basicDialog(OrderDetailActivity.this, message.toString(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void checkAutoScan() {
        if (isClickedFromCamera && isPallet && quantityNormal > 0)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btCameraScan.performClick();
                }
            }, 1000);
    }

    private void updateUI() {
        totalScanned.setText(String.format(Locale.US, "%d", scanned));
        totalQuantity.setText(String.format(Locale.US, "%d", total));
        tvTotalQuantityChecked.setText(String.format(Locale.US, "%d", checkedQuantity));
        adapter.notifyDataSetChanged();
        if (positionJustScan != -1)
            listView.smoothScrollToPosition(positionJustScan);
        if (isPallet) {
            showPalletScanDialog(true, scanResult);
            scanResult = "xx123456789";
            isPallet = false;
        }
    }

    private void showPalletScanDialog(boolean ok, String strScanResult) {
        View scanDialogView = LayoutInflater.from(this).inflate(R.layout.order_detail_scan_dialog, null);
        palletScanDialog = new AlertDialog.Builder(OrderDetailActivity.this)
                .setView(scanDialogView)
                .setCancelable(true)
                .create();

        TextView palletIDTV = (TextView) scanDialogView.findViewById(R.id.scan_dialog_palletId);
        TextView hsdTV = (TextView) scanDialogView.findViewById(R.id.scan_dialog_hsd);
        TextView nsxTV = (TextView) scanDialogView.findViewById(R.id.scan_dialog_nsx);
        TextView remainTV = (TextView) scanDialogView.findViewById(R.id.scan_dialog_remain);
//        TextView remarkTV = (TextView) scanDialogView.findViewById(R.id.scan_dialog_remark);
        TextView txTV = (TextView) scanDialogView.findViewById(R.id.scan_dialog_tx);
        TextView slTV = (TextView) scanDialogView.findViewById(R.id.scan_dialog_sl);
        LinearLayout gridLayout = scanDialogView.findViewById(R.id.scan_dialog_grid);
        LinearLayout lnSL = scanDialogView.findViewById(R.id.lnSL);
        LinearLayout lnCusRef = scanDialogView.findViewById(R.id.lnCusRef);
        LinearLayout lnCusRef2 = scanDialogView.findViewById(R.id.lnCusRef2);
        LinearLayout lnNSXHSD = scanDialogView.findViewById(R.id.lnNSXHSD);
        LinearLayout lnSLChecked = scanDialogView.findViewById(R.id.lnSLChecked);
        LinearLayout lnNSXHSDChecked = scanDialogView.findViewById(R.id.lnNSXHSDChecked);
        LinearLayout lnDialogTon = scanDialogView.findViewById(R.id.lnDialogTon);
        EditText edtSL = scanDialogView.findViewById(R.id.edtSL);
        EditText edtSLChecked = scanDialogView.findViewById(R.id.edtSLChecked);
        EditText edtCusRef = scanDialogView.findViewById(R.id.edtCusRef);
        EditText edtCusRef2 = scanDialogView.findViewById(R.id.edtCusRef2);
        EditText edtRemark = scanDialogView.findViewById(R.id.edtRemark);
        Button btnNSX = scanDialogView.findViewById(R.id.btnNSX);
        Button btnNSXChecked = scanDialogView.findViewById(R.id.btnNSXChecked);
        Button btnHSD = scanDialogView.findViewById(R.id.btnHSD);
        Button btnHSDChecked = scanDialogView.findViewById(R.id.btnHSDChecked);
        Button btnUpdate = scanDialogView.findViewById(R.id.btnUpdate);
        Button btnClose = scanDialogView.findViewById(R.id.btnClose);
        Button btnClose2 = scanDialogView.findViewById(R.id.btnClose2);
        TextView notScanTV = (TextView) scanDialogView.findViewById(R.id.scan_dialog_not_scan);
        TextView tvNotFoundPalletInfo = (TextView) scanDialogView.findViewById(R.id.scan_dialog_not_found_pallet_info);
        CheckBox cbChecked = scanDialogView.findViewById(R.id.cb_detail_phieu_checked);
        TextView tvNoData = scanDialogView.findViewById(R.id.tvNoData);
        edtSL.requestFocus();
        edtSL.setSelection(edtSL.getText().length());
        btnNSX.setText(reportDate);
        btnHSD.setText(reportDate2);



        if (ok) {
            if (palletInfo != null) {
                notScanTV.setVisibility(View.GONE);
                gridLayout.setVisibility(View.VISIBLE);
                palletIDTV.setText(palletInfo.PalletNumber + "\n" + palletInfo.getLabel());
                hsdTV.setText(palletInfo.getUseByDate());
                nsxTV.setText(palletInfo.getProductionDate());
                btnNSX.setText(Utilities.formatDate_ddMMyyyy(palletInfo.getProductionDate()));
                btnHSD.setText(Utilities.formatDate_ddMMyyyy(palletInfo.getUseByDate()));
                edtSLChecked.setText(palletInfo.getActualQuantity());
                edtSL.setText(palletInfo.getQuantityOfPackages());
//                remarkTV.setText(palletInfo.getRemark());
                remainTV.setText(String.format(Locale.getDefault(), "%d", palletInfo.getRemainByProductAtLocation()));
                edtCusRef.setText(palletInfo.getActualCustomerRef());
                edtCusRef2.setText(palletInfo.getActualCustomerRef2());
                edtRemark.setText(palletInfo.getRemark());
                btnNSXChecked.setText(palletInfo.getActualProductionDate());
                btnHSDChecked.setText(palletInfo.getActualUseByDate());

                edtSL.requestFocus();
                edtSL.setSelection(edtSL.getText().length());

                String result = palletInfo.getResult();
                txTV.setText(palletInfo.getActualQuantity());
                if (result.equalsIgnoreCase(getString(R.string.ok)))
                    cbChecked.setChecked(true);
                slTV.setText(palletInfo.getQuantityOfPackages());
                if (result.equalsIgnoreCase("NO")) {
                    txTV.setTextColor(Color.RED);
                    slTV.setTextColor(Color.RED);
                    btnUpdate.setVisibility(View.GONE);
                }
                if (palletInfo.getStatus() == 2) {
                    tvNoData.setVisibility(View.VISIBLE);
                    if (key.equalsIgnoreCase("RO"))
                        tvNoData.setText("Số thùng: " + palletInfo.getQuantityOfPackages() + "\nPhiếu đã confirm");
                    else
                        tvNoData.setText("Tồn: " + palletInfo.getRemainByProductAtLocation()
                                + "\n TX: " + palletInfo.getActualQuantity() + "\n Còn lại: "
                                + (Integer.parseInt(palletInfo.getQuantityOfPackages())
                                - Integer.parseInt(palletInfo.getActualQuantity())) + "\nPhiếu đã confirm");
                    Utilities.speakingSomeThingslow("Phiếu đã confirm", OrderDetailActivity.this);
                    btnUpdate.setVisibility(View.GONE);
                    lnSL.setVisibility(View.GONE);
                    edtCusRef.setEnabled(false);
                    edtCusRef2.setEnabled(false);
                    lnNSXHSD.setVisibility(View.GONE);
                    edtRemark.setEnabled(false);
                }

                if (Integer.parseInt(palletInfo.getActualQuantity()) == 0) {
                    lnSLChecked.setVisibility(View.GONE);
                    lnNSXHSDChecked.setVisibility(View.GONE);
                }
                if (isScan) {
                    gridLayout.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    Utilities.speakingSomeThingslow("OK", OrderDetailActivity.this);
                    boolean checked = cbChecked.isChecked();
                    int iSL;
                    String remark = edtRemark.getText().toString();
                    String strCusRef = edtCusRef.getText().toString();
                    String strCusRef2 = edtCusRef2.getText().toString();
                    String strNSX = Utilities.formatDate_MMddyyyy4(reportDate);
                    String strHSD = Utilities.formatDate_MMddyyyy4(reportDate2);
//                    if (edtSL.getText().length() != 0) {
                    iSL = Integer.valueOf(edtSL.getText().toString());
                    if (key.equalsIgnoreCase("RO"))
                        tvNoData.setText("Số thùng: " + palletInfo.getQuantityOfPackages() + "\nOK");
                    else
                        tvNoData.setText("Tồn: " + palletInfo.getRemainByProductAtLocation()
                                + "\n TX: " + palletInfo.getActualQuantity() + "\n Còn lại: "
                                + (Integer.parseInt(palletInfo.getQuantityOfPackages())
                                - Integer.parseInt(palletInfo.getActualQuantity())) + "\nOK");

                    if (palletInfo.getStatus() == 2) {
                        tvNoData.setVisibility(View.VISIBLE);
                        if (key.equalsIgnoreCase("RO"))
                            tvNoData.setText("Số thùng: " + palletInfo.getQuantityOfPackages() + "\nPhiếu đã confirm");
                        else
                            tvNoData.setText("Tồn: " + palletInfo.getRemainByProductAtLocation()
                                    + "\n TX: " + palletInfo.getActualQuantity() + "\n Còn lại: "
                                    + (Integer.parseInt(palletInfo.getQuantityOfPackages())
                                    - Integer.parseInt(palletInfo.getActualQuantity())) + "\nPhiếu đã confirm");
                        Utilities.speakingSomeThingslow("Phiếu đã confirm", OrderDetailActivity.this);
                        btnUpdate.setVisibility(View.GONE);
                        lnSL.setVisibility(View.GONE);
                        edtCusRef.setEnabled(false);
                        edtCusRef2.setEnabled(false);
                        lnNSXHSD.setVisibility(View.GONE);
                        edtRemark.setEnabled(false);
                    } else {
                        if (palletInfo.getResult().equalsIgnoreCase("OK")) {
                            if (key.equalsIgnoreCase("RO"))
                                tvNoData.setText("Số thùng: " + palletInfo.getQuantityOfPackages() + "\n Đã Scan Rồi");
                            else
                                tvNoData.setText("Tồn: " + palletInfo.getRemainByProductAtLocation()
                                        + "\n TX: " + palletInfo.getActualQuantity() + "\n Còn lại: "
                                        + (Integer.parseInt(palletInfo.getQuantityOfPackages())
                                        - Integer.parseInt(palletInfo.getActualQuantity())) + "\n Đã Scan Rồi");
                            Utilities.speakingSomeThingslow("Đã Scan Rồi", OrderDetailActivity.this);
                            if (isScan) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (palletScanDialog.isShowing())
                                            palletScanDialog.dismiss();
                                    }
                                }, 1500);
                            } else {
                                if (palletScanDialog.isShowing())
                                    palletScanDialog.dismiss();
                            }
                        } else {
                            updatePalletID(listView, palletScanDialog, checked, palletInfo.getDispatchingOrderDetailID(), palletInfo.getPalletID(), remark, iSL, strCusRef, strCusRef2, strNSX, strHSD, strScanResult);
                        }
                    }
                }

            } else {
                tvNotFoundPalletInfo.setVisibility(View.VISIBLE);
                btnClose2.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
                tvNoData.setText("Không có dữ liệu");
                Utilities.speakingSomeThingslow("Không có dữ liệu", OrderDetailActivity.this);
            }
        } else {
            notScanTV.setVisibility(View.VISIBLE);
            btnClose2.setVisibility(View.VISIBLE);
            gridLayout.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText("Không có dữ liệu");
            Utilities.speakingSomeThingslow("Không có dữ liệu", OrderDetailActivity.this);
        }

        if (orderNumber != null && !orderNumber.equals("")) {
            if (orderNumber.substring(0, 2).equalsIgnoreCase("DO")) {
                lnDialogTon.setVisibility(View.VISIBLE);
                lnSL.setVisibility(View.VISIBLE);

            } else if (orderNumber.substring(0, 2).equalsIgnoreCase("RO")) {
                lnDialogTon.setVisibility(View.GONE);
                lnSL.setVisibility(View.GONE);
            }
        }


//        palletScanDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        palletScanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        palletScanDialog.show();


        edtSL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtSL.setSelection(edtSL.getText().length());
                if (s.toString().length() >= 1 && s.toString().startsWith("0")) {
                    if (s.toString().length() >= 2) {
                        edtSL.setText(String.valueOf(Integer.valueOf(s.toString())));
                    }
                }
            }
        });

        btnHSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialogHSD = new DatePickerDialog(OrderDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar2.set(year, monthOfYear, dayOfMonth);
                        reportDate2 = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar2.getTimeInMillis()));
                        btnHSD.setText(reportDate2);
                    }
                }, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH));
                dialogHSD.setCanceledOnTouchOutside(false);
                dialogHSD.show();
            }
        });

        btnNSX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialogNSX = new DatePickerDialog(OrderDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        reportDate = Utilities.formatDate_ddMMyyyy(Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis()));
                        btnNSX.setText(reportDate);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialogNSX.setCanceledOnTouchOutside(false);
                dialogNSX.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialogNSX.show();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palletScanDialog.dismiss();
            }
        });

        btnClose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palletScanDialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = cbChecked.isChecked();
                int iSL;
                String remark = edtRemark.getText().toString();
                String strCusRef = edtCusRef.getText().toString();
                String strCusRef2 = edtCusRef2.getText().toString();
                String strNSX = Utilities.formatDate_MMddyyyy4(reportDate);
                String strHSD = Utilities.formatDate_MMddyyyy4(reportDate2);
                if (edtSL.getText().length() != 0) {
                    iSL = Integer.valueOf(edtSL.getText().toString());
                    updatePalletID(listView, palletScanDialog, checked, palletInfo.getDispatchingOrderDetailID(), palletInfo.getPalletID(), remark, iSL, strCusRef, strCusRef2, strNSX, strHSD, strScanResult);
                } else {
                    Utilities.speakingSomeThingslow("Số lượng không được để trống", OrderDetailActivity.this);
                    Toast.makeText(OrderDetailActivity.this, "Số lượng không được để trống", Toast.LENGTH_SHORT).show();
                }
            }

        });

        isScan = true;

    }


    @Override
    protected void onResume() {
        Const.isActivating = true;
        getOrderDetail(listView);

        super.onResume();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    @Override
    protected void onPause() {
        Const.isActivating = false;
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_phieu, menu);
        item_sort = menu.findItem(R.id.action_menu);
        itemFilter = menu.findItem(R.id.action_filter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_detail_no:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.product_no));
                }
                break;
            case R.id.action_detail_name:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.product_name));
                }
                break;
            case R.id.action_detail_remark:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.remark));
                }
                break;
            case R.id.action_detail_nxs:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.nxs));
                }
                break;
            case R.id.action_detail_hsd:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.hsd));
                }
                break;
            case R.id.action_detail_vi_tri:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item_sort.setTitle(getResources().getString(R.string.vi_tri));
                }
                break;
            case R.id.action_filter_all:
                filterResult = 0;
                if (!item.isChecked()) {
                    item.setChecked(true);
                    itemFilter.setTitle(getResources().getString(R.string.all));
                }
                getOrderDetail(listView);
                break;
            case R.id.action_filter_yet_scan:
                filterResult = 1;
                if (!item.isChecked()) {
                    item.setChecked(true);
                    itemFilter.setTitle(getResources().getString(R.string.not_yet_scan));
                }
                getOrderDetail(listView);
                break;
            case R.id.action_filter_scanned:
                filterResult = 2;
                if (!item.isChecked()) {
                    item.setChecked(true);
                    itemFilter.setTitle(getResources().getString(R.string.scanned));
                }
                getOrderDetail(listView);
                break;
        }
        return true;
    }

    @Override
    public void onData(String result) {
        super.onData(result);
        Const.timePauseActive = 0;
        result = result.trim();
        if (!result.equals("") && !result.contains("\t\t")) {
            isPallet = false;
            if (palletScanDialog != null) {
                palletScanDialog.dismiss();
                palletScanDialog = null;
            }
            etBarcode.setText(result);
            String type = "";
            try {
                if (Pattern.compile("[a-zA-Z]{2}\\d+").matcher(result).matches()) {
                    type = result.substring(0, 2);
                    barcodeNumber = Integer.parseInt(result.substring(2));
                } else if (Pattern.compile("[a-zA-Z]{3}\\d+").matcher(result).matches()) {
                    type = result.substring(0, 3);
                    barcodeNumber = Integer.parseInt(result.substring(3));
                }
            } catch (IndexOutOfBoundsException ex) {
                Toast.makeText(OrderDetailActivity.this, "Chuỗi barcode không hợp lệ.", Toast.LENGTH_LONG).show();
                return;
            } catch (Exception ex) {
                Log.e(TAG, "", ex);
            }

            scanResult = result;
            if (type.equalsIgnoreCase("pi") ||
                    type.equalsIgnoreCase("cw") ||
                    result.length() == 20) {
                isPallet = true;
                if (isScanDK) {
                    getOrderDetail(listView);
                } else {
//                employeeWorkingISOCheckInput(orderNumber, true);
                    getOrderDetail(listView);
                }
            } else if (type.equalsIgnoreCase("do") ||
                    type.equalsIgnoreCase("ro") ||
                    type.equalsIgnoreCase("tw") ||
                    type.equalsIgnoreCase("tm")) {
                isGotoWorker = true;
                isScanDK = false;
                orderNumber = String.format(Locale.getDefault(), "%s-%d", type, barcodeNumber);
                updateActionBarTitle(orderNumber);
                getRequest();
                getOrderDetail(listView);
            } else if (type.equalsIgnoreCase("dk")) {
                getOrderDetail(listView);
            } else {
                isScanDK = true;
            }
        }

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.so_do_day) {
            Intent intent = new Intent(getApplicationContext(), SoDoDayActivity.class);
            intent.putExtra(DO_ID, Integer.parseInt(orderNumber.split("-")[1]));
            startActivity(intent);
        } else if (id == R.id.take_picture) {
            if (orderNumber.length() > 0) {
                Intent intent = new Intent(this, ChupHinhActivity.class);
                intent.putExtra(ORDER_NUMBER, orderNumber);
                startActivity(intent);
            } else
                Snackbar.make(v, "Vui lòng Scan đơn hàng trước khi chụp hình", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.worker) {
            gotoWorker();
        } else if (id == R.id.btn_iso) {
            gotoIso();
        } else if (id == R.id.btnAccept) {
            boolean isEnough = true;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getResult().equalsIgnoreCase("")) {
                    isEnough = false;
                    break;
                }
            }
            boolean finalIsEnough = isEnough;
            Utilities.basicDialog(OrderDetailActivity.this, "Bạn có muốn xác nhận không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (finalIsEnough) {
                        acceptCheo(dispatchingID, orderNumber, userName, customerNumber);
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "Các Pallet chưa bắn đủ", Toast.LENGTH_SHORT).show();
                        Utilities.speakingSomeThingslow("Các Pallet chưa bắn đủ", OrderDetailActivity.this);
                    }
                }
            });

        }else if (id == R.id.print){
            Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {
                    createPDFFile(Utilities.getAppPatch(OrderDetailActivity.this)+PDF_FILE, orderDetailList);
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {

                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                }
            }).check();

        }
    }

    private void acceptCheo(String varOrderID, String varOrderNumber, String varUserID, String varCustomerNumber) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("varOrderID", varOrderID);
        jsonObject.addProperty("varOrderNumber", varOrderNumber);
        jsonObject.addProperty("varUserID", varUserID);
        jsonObject.addProperty("varCustomerNumber", varCustomerNumber);

        MyRetrofit.initRequest(OrderDetailActivity.this).updateSTTransactionInsertAll(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Toast.makeText(OrderDetailActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Lỗi từ hệ thống", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Lỗi từ hệ thống", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void gotoWorker() {
        Intent intent = new Intent(this, WorkerActivity.class);
        intent.putExtra(ORDER_NUMBER, orderNumber);
        startActivity(intent);
    }

    private void gotoIso() {
        Intent intent = new Intent(this, IsoActivity.class);
        intent.putExtra("ORDER_NUMBER", orderNumber);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    private void updateActualPalletQuantity(OrderDetail orderDetail, int actualNumber) {
        PalletActualQuantityUpdateParam parameter = new PalletActualQuantityUpdateParam(
                userName, deviceNumber, orderNumber,
                Integer.parseInt(orderDetail.PalletNumber),
                actualNumber, orderDetail.getDispatchingOrderDetailID()
        );
        MyRetrofit.initRequest(this).updateActualPalletQuantity(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                String body = response.body();
                if (response.isSuccess() && body != null) {
                    boolean isShowed = showMessage(body);
                    if (!isShowed) {
                        getOrderDetail(listView);
                    }
                    Utilities.hideKeyboard(OrderDetailActivity.this);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


    private void updatePalletID(final View view, AlertDialog palletScanDialog, boolean checked, UUID dispatchingOrderDetailID, UUID palletID, String remark, int iSL,
                                String strCus, String strCus2, String strNSX, String strHSD, String strScanResult) {
        UpdateDispatchingOrderDetailParameter parameter = new UpdateDispatchingOrderDetailParameter(checked, dispatchingOrderDetailID, palletID, remark, userName, orderNumber,
                deviceNumber, iSL, strCus, strCus2, strNSX, strHSD, strScanResult);
        MyRetrofit.initRequest(this).updateDispatchingOrderDetail(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {

                if (response.isSuccess() && response.body() != null) {
                    getOrderDetail(listView);

                    if (isScan) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (palletScanDialog.isShowing())
                                    palletScanDialog.dismiss();
                            }
                        }, 1500);
                    } else {
                        if (palletScanDialog.isShowing())
                            palletScanDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(OrderDetailActivity.this, t, TAG, view);
            }
        });
    }

    private void executeDispatchingOrderScannedDelete(final View view, DispatchingOrderScannedDeleteParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.deleting));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).executeDispatchingOrderScannedDelete(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    dismissDialog(dialog);
                    getOrderDetail(view);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(OrderDetailActivity.this, t, TAG, view);
            }
        });
    }

    private void employeeWorkingISOCheckInput(String orderNumber, final boolean isScanPallet) {
        EmployeeWorkingISOCheckInputParams params = new EmployeeWorkingISOCheckInputParams(orderNumber);
        MyRetrofit.initRequest(this).employeeWorkingISOCheckInput(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    String body = response.body();
                    if (body != null && body.trim().equalsIgnoreCase("no")) {
                        gotoIso();
                    } else {
                        isScanDK = true;
                        if (isScanPallet) {
                            getOrderDetail(listView);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = completedList.get(position);
        if (item instanceof Product) {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.SCAN_RESULT, ((Product) item).getProductNumber());
            startActivity(intent);
        }
    }

    private void updateActionBarTitle(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }
    private void createPDFFile(String path ,List<OrderDetail> orderDetailList) {
        if (new File(path).exists())
            new File(path).delete();

        try{
            Document document = new Document();
            //Save

            PdfWriter pdfWriter =  PdfWriter.getInstance(document, new FileOutputStream(path));


            //Setting
            document.setPageSize(PageSize.A5.rotate());
            document.setMargins(10, 0, 0, 0);
            document.addCreationDate();
            document.addAuthor("Cholimex");

            //Open to write
            document.open();

            //Font setting
            BaseFont baseFont = BaseFont.createFont("assets/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            float fontSizeTitle = 30.0f;
            float fontSizeSubTitle = 25.0f;
            float fontSizeNormal = 20.0f;

            Font font = new Font(baseFont, fontSizeTitle, Font.BOLD, BaseColor.BLACK);
            Font font2 = new Font(baseFont, fontSizeNormal, Font.BOLD, BaseColor.BLACK);
            Font font3 = new Font(baseFont, fontSizeSubTitle, Font.BOLD, BaseColor.BLACK);

            for(int i = 0; i < orderDetailList.size(); i++){

                //Nội dụng dòng 1
                List<String> listRow1 = new ArrayList<>();
                listRow1.add(0,"Loai hang: " + orderDetailList.get(i).getProductCategoryDescription());
                listRow1.add(1,"TPH-1: " + orderDetailList.get(i).getCustomerName());

                //Nội dụng dòng 2
                List<String> listRow2 = new ArrayList<>();
                listRow2.add(0,"Ma hang: " + orderDetailList.get(i).ProductNumber);
                listRow2.add(1,"Nhiet do: " + orderDetailList.get(i).getTemperatureRequire());

                //Nội dụng dòng 3
                List<String> listRow3 = new ArrayList<>();
                listRow3.add(0,"Ten hang: " + removeDiacritics(orderDetailList.get(i).getProductName()));

                //Nội dụng dòng 4
                List<String> listRow4 = new ArrayList<>();
                listRow4.add(0,"NSX: " + orderDetailList.get(i).getProductionDate());
                listRow4.add(1,"HSD: " + orderDetailList.get(i).getUseByDate());
                listRow4.add(2,"Phieu nhap: " + orderDetailList.get(i).getDO());

                //Nội dụng dòng 5
                List<String> listRow5 = new ArrayList<>();
                listRow5.add(0,"Ma lo 1: " + orderDetailList.get(i).getActualCustomerRef());
                listRow5.add(1,"Ngay nhap: " + orderDetailList.get(i).getReceivingOrderDate());

                //Nội dụng dòng 6
                List<String> listRow6 = new ArrayList<>();
                listRow6.add(0,"Ma lo 2: " + orderDetailList.get(i).getActualCustomerRef2());
                listRow6.add(1,"Ghi chu: " + orderDetailList.get(i).getRemark());

                //Nội dụng dòng 7
                List<String> listRow7 = new ArrayList<>();
                listRow7.add(0,"Xe/cont: " + orderDetailList.get(i).getVehicleNumber());
//                listRow7.add(1,"Dien giai: " + orderDetailList.get(i).getReceivingOrderRemark());

                //Nội dung dòng 8
                List<String> listRow8 = new ArrayList<>();
                listRow8.add(0,"Kg/thung: " + orderDetailList.get(i).getWeightPerPackage());
                listRow8.add(1,orderDetailList.get(i).PalletNumber + "");

                //Nội dung dòng 9
                List<String> listRow9 = new ArrayList<>();
                listRow9.add(0,"Gross: " + orderDetailList.get(i).getGrossWeight());
                listRow9.add(1, orderDetailList.get(i).getLabel());

                //Nội dung dòng 10
                List<String> listRow10 = new ArrayList<>();
                listRow10.add(0,"SL: " + orderDetailList.get(i).getQuantityOfPackages());
                listRow10.add(1, "Tui (Goi): " + orderDetailList.get(i).getUnitQuantity());
                listRow10.add(2, "Tong: " + orderDetailList.get(i).getSumQuantity());



                document.newPage();
                // Dòng 1
                addNewItemHaveColumnTitle(document,listRow1, Element.ALIGN_LEFT, font3, TYPE_TITLE);
                // Dòng 2
                addNewItemHaveColumnTitle(document,listRow2,Element.ALIGN_LEFT, font, TYPE_TITLE);
                // Dòng3
                addNewItemHaveColumnTitle(document,listRow3,Element.ALIGN_LEFT, font,TYPE_TITLE);
                // Dòng 4
                addNewItemHaveColumnTitle(document,listRow4,Element.ALIGN_LEFT, font2, TYPE_NORMAL_TEXT);
                // Dòng 5
                addNewItemHaveColumnTitle(document,listRow5,Element.ALIGN_LEFT, font2,TYPE_NORMAL_TEXT);
                // Dòng 6
                addNewItemHaveColumnTitle(document,listRow6,Element.ALIGN_LEFT, font2,TYPE_NORMAL_TEXT);
                // Dòng 7
                addNewItemHaveColumnTitle(document,listRow7,Element.ALIGN_LEFT, font2,TYPE_NORMAL_TEXT);
                //Create QRCODE of Document
                BarcodeQRCode qrCode = new BarcodeQRCode(orderDetailList.get(i).getPalletID_Barcode(), 120, 120, null);
                Image pdfFormXObjectQR = qrCode.getImage();
                pdfFormXObjectQR.scaleAbsolute(120, 120);
                pdfFormXObjectQR.setAlignment(Element.ALIGN_LEFT);
//                document.add(pdfFormXObjectQR);

                //Create BARCODE of Document
                PdfContentByte pdfContentByte = pdfWriter.getDirectContent();
                Barcode128 barCode = new Barcode128();
                barCode.setCode(orderDetailList.get(i).getPalletID_Barcode());
                barCode.setCodeType(Barcode128.CODE128);
                Image pdfFormXObjectBarcode = barCode.createImageWithBarcode(pdfContentByte, null,null);
                pdfFormXObjectBarcode.scaleAbsolute(200, 80);
                pdfFormXObjectBarcode.setAlignment(Element.ALIGN_CENTER);

                PdfPTable table = new PdfPTable(2);

                // Set Cell QRCode
                PdfPCell qrCodeCell = new PdfPCell(pdfFormXObjectQR);
                qrCodeCell.setBorder(Rectangle.NO_BORDER);
                qrCodeCell.setFixedHeight(100);
                table.addCell(qrCodeCell);

                // Set Cell Barcode
                PdfPCell barcodeCell = new PdfPCell(pdfFormXObjectBarcode);
                barcodeCell.setBorder(Rectangle.NO_BORDER);
                barcodeCell.setFixedHeight(100);
                barcodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                barcodeCell.setVerticalAlignment(Element.ALIGN_CENTER);

                table.addCell(barcodeCell);

                document.add(table);

                addNewItemHaveColumnTitle(document,listRow8,Element.ALIGN_LEFT, font2,TYPE_NORMAL_TEXT);
                addNewItemHaveColumnTitle(document,listRow9,Element.ALIGN_LEFT, font2,TYPE_NORMAL_TEXT);
                addNewItemHaveColumnTitle(document,listRow10,Element.ALIGN_LEFT, font2,TYPE_NORMAL_TEXT);

            }

            document.close();

            printPDF();


        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5);

        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(OrderDetailActivity.this, Utilities.getAppPatch(OrderDetailActivity.this) + PDF_FILE);
            printManager.print("Document", printDocumentAdapter, builder.build());
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
    }

    private void addNewItemHaveColumnTitle(Document document,List<String> listContent, int alignCenter, Font font, int typeText) throws DocumentException {
        PdfPTable table = new PdfPTable(listContent.size());
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);

        switch (typeText){
            case TYPE_TITLE:
                cell.setFixedHeight(35);
                break;
            case TYPE_SUB_TITLE:
                cell.setFixedHeight(30);
                break;
            case  TYPE_NORMAL_TEXT:
                cell.setFixedHeight(25);
                break;
            default:
                break;
        }

        for(int i = 0; i < listContent.size(); i++){
//            cell.setFixedHeight(35);
            cell.setPhrase(new Phrase(listContent.get(i),font));
            table.addCell(cell);
        }
        document.add(table);
    }
}
