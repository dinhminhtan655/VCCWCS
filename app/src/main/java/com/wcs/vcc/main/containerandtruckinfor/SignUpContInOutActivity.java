package com.wcs.vcc.main.containerandtruckinfor;

import static com.wcs.vcc.main.containerandtruckinfor.dialog.CheckOutResultFragment.CODE_CAPTURE_IMAGE2;
import static com.wcs.vcc.utilities.Utilities.convertUsingIOUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wcs.vcc.api.ComboCustomerResult;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.xdoc.BookingInsertPictureParameter;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.containerandtruckinfor.dialog.CheckOutResultFragment;
import com.wcs.vcc.main.containerandtruckinfor.model.ContainerAndTruckInfo;
import com.wcs.vcc.main.containerandtruckinfor.model.Door;
import com.wcs.vcc.main.containerandtruckinfor.model.InfoSignedup;
import com.wcs.vcc.main.detailphieu.chuphinh.AttachmentInfo;
import com.wcs.vcc.main.detailphieu.chuphinh.ChupHinhActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.OrderNumber;
import com.wcs.vcc.main.detailphieu.chuphinh.ScanCameraPortrait;
import com.wcs.vcc.main.emdk.ScanListener;
import com.wcs.vcc.main.postiamge.GridImage;
import com.wcs.vcc.main.postiamge.ImageUtils;
import com.wcs.vcc.main.postiamge.Thumb;
import com.wcs.vcc.main.postiamge.ThumbImageAdapter;
import com.wcs.vcc.main.vesinhantoan.model.ImageThumb;
import com.wcs.vcc.main.vo.Group;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.ResizeImage;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;
import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ActivitySignUpContInOutBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignUpContInOutActivity extends BaseActivity implements CheckOutResultFragment.OnSendData, ScanListener, CheckOutResultFragment.onFragmentCloseListener {
    ActivitySignUpContInOutBinding binding;
    private int iDoor = 0;
    private List<String> carNumberList, containerNumList, sealNumList, driverNameList, identityList, phoneNumberList;
    private List<InfoSignedup> contList;
    private List<Door> carGateList;
    private String strReason, strCarNumber, strCustomerName;
    private List<ComboCustomerResult> customers;

    //Process Image
    private ContainerAndTruckInfo containerAndTruckInfo;
    private LinearLayoutManager layoutManager;
    private ThumbImageAdapter gridImageAdapter;
    private ArrayList<ImageThumb> stringList;
    //    private ArrayList<ImageThumb> stringListState;
    private ImageThumbAdapter2 imageThumbAdapter;
    private ProgressDialog dialog;
    private File file;
    private Bitmap bitmap;
    private ArrayList<File> files = new ArrayList<>();
    private ArrayList<File> filesUpdate = new ArrayList<>();
    private ArrayList<File> filesState = new ArrayList<>();
    private byte[] b;
    private static final String capturePhotoPath = "capture_photo_path";
    private static final String listFiles = "list_files";
    private static final String listImageThumb = "list_thumb";
    private static final String company_save = "company_save";
    private static final String vehicle_save = "vehicle_save";
    private static final String carnumber_save = "carnumber_save";
    private static final String gate_save = "gate_save";
    private static final String reason_save = "reason_save";
    private static final String reason_save2 = "reason_save2";
    private static final String NHAP = "Nhap";
    private static final String XUAT = "Xuat";
    private static final String KHONGXN = "Khong X-N";
//    private static final String KHAC = "Khac";
    private static final String idoor_save = "idoor_save";
    private String photoPath = null;
    private Boolean captureInProgress = false;
    private FragmentManager fm;
    private String saveCompanySpin, saveVehicleSpin, saveCarNumberSpin, saveGateSpin, saveReasonSpin;

    private String strCO;

    private String username;
    private ContainerAndTruckInfo info;
    private int iType;

    private String group;
    private String mac;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpContInOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle("Khai báo xe ra vào");
        setSupportActionBar(binding.toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        group = LoginPref.getInfoUser(this, LoginPref.POSITION_GROUP);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        fm = getSupportFragmentManager();
        username = LoginPref.getUsername(this);
        mac = Utilities.getWiFiMac();

        // Existed List
        carNumberList = new ArrayList<>();
        containerNumList = new ArrayList<>();
        sealNumList = new ArrayList<>();
        driverNameList = new ArrayList<>();
        identityList = new ArrayList<>();
        phoneNumberList = new ArrayList<>();
        contList = new ArrayList<>();

        carGateList = new ArrayList<>();
        customers = new ArrayList<>();

        stringList = new ArrayList<ImageThumb>();
//        stringListState = new ArrayList<ImageThumb>();

        gridImageAdapter = new ThumbImageAdapter(this, new ArrayList<Thumb>());
        dialog = Utilities.getProgressDialog(SignUpContInOutActivity.this, getString(R.string.upload));
        getCarNumberSignedList();
        getCustomer(1);
        setSearchLayoutVisibility(group);

        if (getIntent() != null) {
            iType = getIntent().getIntExtra("type", 3);
            Bundle bundle = getIntent().getBundleExtra("consetbun");
            if (iType == 0) {
                carNumberList = getIntent().getStringArrayListExtra("containerset");
                containerNumList = getIntent().getStringArrayListExtra("containernumset");
                sealNumList = getIntent().getStringArrayListExtra("sealnumset");
                driverNameList = getIntent().getStringArrayListExtra("drivernameset");
                identityList = getIntent().getStringArrayListExtra("identityset");
                phoneNumberList = getIntent().getStringArrayListExtra("phonenumberset");
                contList = (List<InfoSignedup>) bundle.getSerializable("conset");
                ArrayAdapter<String> containerNumAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, containerNumList);
                ArrayAdapter<String> sealNumAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sealNumList);
                ArrayAdapter<String> driverNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, driverNameList);
                ArrayAdapter<String> identityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, identityList);
                ArrayAdapter<String> phoneNumberAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, phoneNumberList);
                ContAutoCompleteAdapter contAdapter = new ContAutoCompleteAdapter(this, contList);
                binding.edtContainerNumber.setAdapter(containerNumAdapter);
                binding.edtSealNumber.setAdapter(sealNumAdapter);
                binding.edtNameDriver.setAdapter(driverNameAdapter);
                binding.edtIDCardDriver.setAdapter(identityAdapter);
                binding.edtPhoneNumberDriver.setAdapter(phoneNumberAdapter);
                binding.edtCarNumber.setAdapter(contAdapter);
                binding.btnSignOut.setVisibility(View.GONE);
                binding.btnXoa.setVisibility(View.GONE);
                binding.btnTimeIn.setVisibility(View.GONE);
                binding.btnStartTime.setVisibility(View.GONE);
                binding.btnEndTime.setVisibility(View.GONE);
                binding.btnStartTime1.setVisibility(View.GONE);
                binding.btnEndTime1.setVisibility(View.GONE);
                binding.tvStartTime.setVisibility(View.GONE);
                binding.tvTimeIn.setVisibility(View.GONE);
                binding.tvEndTime.setVisibility(View.GONE);
                binding.tvStartTime1.setVisibility(View.GONE);
                binding.tvEndTime1.setVisibility(View.GONE);
                binding.tvSignOut.setVisibility(View.GONE);

            } else if (iType == 1) {
                info = (ContainerAndTruckInfo) getIntent().getSerializableExtra("infocont");
                if (info != null) {
                    binding.tvTimeInOut.setText("Vào lúc: " + Utilities.formatDate_ddMMyyHHmm(info.TimeIn) + " Ra: " + Utilities.formatDate_ddMMyyHHmm(info.TimeOut));
                    binding.tvSpinCompany.setText(info.CustomerName);
                    binding.tvSpinCompany.setEnabled(true);
                    binding.tvSpinVehicle.setText(info.ContainerType);
                    binding.tvSpinVehicle.setEnabled(false);
                    binding.edtCarNumber.setText(info.ContainerNum);
                    binding.edtCarNumber.setEnabled(true);
                    binding.edtContainerNumber.setText(info.TruckIn);
                    binding.edtContainerNumber.setEnabled(true);
                    binding.edtSealNumber.setText(info.SealNumber);
                    binding.edtSealNumber.setEnabled(true);
                    binding.edtNameDriver.setText(info.DriverName);
                    binding.edtNameDriver.setEnabled(true);
                    binding.edtIDCardDriver.setText(info.DriverIDCardNo);
                    binding.edtIDCardDriver.setEnabled(true);
                    binding.edtPhoneNumberDriver.setText(String.valueOf(info.DriverMobilePhone));
                    binding.edtPhoneNumberDriver.setEnabled(true);
                    binding.edtRemark.setHint(info.Remarks);



                    if(!info.TimeIn.equalsIgnoreCase("1900-01-01T00:00:00")) {
                        binding.tvTimeIn.setText(Utilities.formatDate_ddMMyyHHmm(info.TimeIn));
                        binding.btnTimeIn.setEnabled(false);
                        binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnStartTime.setEnabled(true);
                        binding.btnStartTime.setBackgroundColor(Color.parseColor("#f57c00"));
                        binding.btnSignOut.setEnabled(true);
                        binding.btnSignOut.setBackgroundColor(Color.parseColor("#f57c00"));
                    }else{
                        binding.btnTimeIn.setBackgroundColor(Color.parseColor("#f57c00"));
                    }


                    if(!info.TimeIn.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.StartTime.equalsIgnoreCase("1900-01-01T00:00:00")){
                        binding.tvStartTime.setText(Utilities.formatDate_ddMMyyHHmm(info.StartTime));
                        binding.btnTimeIn.setEnabled(false);
                        binding.btnStartTime.setEnabled(false);
                        binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnStartTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnEndTime.setEnabled(true);
                        binding.btnEndTime.setBackgroundColor(Color.parseColor("#f57c00"));
                        binding.btnSignOut.setEnabled(false);
                        binding.btnSignOut.setBackgroundColor(getResources().getColor(R.color.colorGrayD));

                    }

                    if(!info.TimeIn.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.StartTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.EndTime.equalsIgnoreCase("1900-01-01T00:00:00")){
                        binding.tvEndTime.setText(Utilities.formatDate_ddMMyyHHmm(info.EndTime));
                        binding.btnTimeIn.setEnabled(false);
                        binding.btnStartTime.setEnabled(false);
                        binding.btnEndTime.setEnabled(false);
                        binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnStartTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnEndTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnStartTime1.setEnabled(true);
                        binding.btnStartTime1.setBackgroundColor(Color.parseColor("#f57c00"));
                        binding.btnSignOut.setEnabled(true);
                        binding.btnSignOut.setBackgroundColor(Color.parseColor("#f57c00"));
                    }

                    if(!info.TimeIn.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.StartTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.EndTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.StartTime1.equalsIgnoreCase("1900-01-01T00:00:00")){
                        binding.tvStartTime1.setText(Utilities.formatDate_ddMMyyHHmm(info.StartTime1));
                        binding.btnTimeIn.setEnabled(false);
                        binding.btnStartTime.setEnabled(false);
                        binding.btnEndTime.setEnabled(false);
                        binding.btnStartTime1.setEnabled(false);
                        binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnStartTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnEndTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnStartTime1.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnEndTime1.setEnabled(true);
                        binding.btnEndTime1.setBackgroundColor(Color.parseColor("#f57c00"));
                        binding.btnSignOut.setEnabled(false);
                        binding.btnSignOut.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                    }

                    if(!info.TimeIn.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.StartTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.EndTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.StartTime1.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.EndTime1.equalsIgnoreCase("1900-01-01T00:00:00")){
                        binding.tvEndTime1.setText(Utilities.formatDate_ddMMyyHHmm(info.EndTime1));
                        binding.btnTimeIn.setEnabled(false);
                        binding.btnStartTime.setEnabled(false);
                        binding.btnEndTime.setEnabled(false);
                        binding.btnStartTime1.setEnabled(false);
                        binding.btnEndTime1.setEnabled(false);
                        binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnStartTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnEndTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnStartTime1.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnEndTime1.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnSignOut.setEnabled(true);
                        binding.btnSignOut.setBackgroundColor(Color.parseColor("#f57c00"));
                    }

                    if(!info.TimeIn.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.StartTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.EndTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.StartTime1.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.EndTime1.equalsIgnoreCase("1900-01-01T00:00:00") &&
                            !info.TimeOut.equalsIgnoreCase("1900-01-01T00:00:00")){
                        binding.tvSignOut.setText(Utilities.formatDate_ddMMyyHHmm(info.TimeOut));
                        binding.btnTimeIn.setEnabled(false);
                        binding.btnStartTime.setEnabled(false);
                        binding.btnEndTime.setEnabled(false);
                        binding.btnStartTime1.setEnabled(false);
                        binding.btnEndTime1.setEnabled(false);
                        binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnStartTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnEndTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnStartTime1.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnEndTime1.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        binding.btnSignOut.setEnabled(true);
                        binding.btnSignOut.setBackgroundColor(Color.parseColor("#f57c00"));
                    }

                    iDoor = info.DockDoorID;
                    binding.tvGate.setEnabled(false);
                    binding.tvGateReason.setText(info.Reason);
                    binding.tvGateReason.setEnabled(true);

                    if(info.TimeIn != "1900-01-01T00:00:00"){
                        binding.tvTimeIn.setText(Utilities.formatDate_ddMMyyHHmm(info.TimeIn));
                    }else{
                        binding.tvTimeIn.setText("Chưa cập nhật");
                    }

                    if(info.StartTime != "1900-01-01T00:00:00"){
                        binding.tvStartTime.setText(Utilities.formatDate_ddMMyyHHmm(info.StartTime));
                    }else{
                        binding.tvStartTime.setText("Chưa cập nhật");
                    }

                    if(info.EndTime != "1900-01-01T00:00:00"){
                        binding.tvEndTime.setText(Utilities.formatDate_ddMMyyHHmm(info.EndTime));
                    }else{
                        binding.tvEndTime.setText("Chưa cập nhật");
                    }

                    if(info.StartTime1 != "1900-01-01T00:00:00"){
                        binding.tvStartTime1.setText(Utilities.formatDate_ddMMyyHHmm(info.StartTime1));
                    }else{
                        binding.tvStartTime1.setText("Chưa cập nhật");
                    }

                    if(info.EndTime1 != "1900-01-01T00:00:00"){
                        binding.tvEndTime1.setText(Utilities.formatDate_ddMMyyHHmm(info.EndTime1));
                    }else{
                        binding.tvEndTime1.setText("Chưa cập nhật");
                    }

                    if(info.TimeOut != "1900-01-01T00:00:00"){
                        binding.tvSignOut.setText(Utilities.formatDate_ddMMyyHHmm(info.TimeOut));
                    }else{
                        binding.tvSignOut.setText("Chưa cập nhật");
                    }

//                    binding.btnCapture.setVisibility(View.GONE);
                    binding.btnSignUp.setText("Cập nhật");
                    if (info.CheckOut) {
                        binding.btnSignUp.setVisibility(View.GONE);
                        binding.btnSignUp.setVisibility(View.GONE);
                        binding.btnXoa.setVisibility(View.GONE);
                    }
                    getAttachmentInfo(binding.tvGateReason, info.ContInOutNumber);
                }

            }
        }

        binding.etTargetScan.addTextChangedListener(new TextWatcher() {
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
                    onData(contents.replaceAll("\n", ""));
                }
            }
        });

        binding.edtCarNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.edtNameDriver.setText(contList.get(position).getDriverName());
                binding.edtIDCardDriver.setText(contList.get(position).getIDNumber());
                binding.edtPhoneNumberDriver.setText("0" + String.valueOf(contList.get(position).getDriverMobilePhone()));
            }
        });

        binding.ivCameraScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(SignUpContInOutActivity.this);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setCaptureActivity(ScanCameraPortrait.class);
                integrator.initiateScan();
            }
        });


        binding.tvGate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerSearchGateDialog(SignUpContInOutActivity.this, carGateList, binding.tvGate);
            }
        });


        binding.tvGateReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerSearchReasonDialog(SignUpContInOutActivity.this, binding.tvGateReason);
            }
        });

        binding.tvSpinCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerSearchCustomerDialog(SignUpContInOutActivity.this, binding.tvSpinCompany, customers);
            }
        });

        binding.tvSpinVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerSearchVehicleDialog(SignUpContInOutActivity.this, binding.tvSpinVehicle);
            }
        });

        imageThumbAdapter = new ImageThumbAdapter2(new RecyclerViewItemOrderListener<ImageThumb>() {
            @Override
            public void onClick(ImageThumb item, int position, int order) {
                int sizeListImageThumb = 0;
                int sizeListFile = 0;
                for (int i = 0; i < stringList.size(); i++){
                    if (!stringList.get(i).isUp()){
                        sizeListImageThumb++;
                    }else{
                        sizeListFile++;
                    }
                }

                int posImgThumb = stringList.size() - sizeListImageThumb;
                int posFile = posImgThumb - 1;


                switch (order) {
                    case 0:
                        if(iType == 1){
                            if(item.isUp()){
                                stringList.remove(position);
                                if(position > 0){
                                    files.remove(posFile);
                                }else {
                                    files.remove(posFile);
                                }

                                imageThumbAdapter.replace(stringList);
                                imageThumbAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(SignUpContInOutActivity.this, "Ảnh này đã up lên server không thể xoá", Toast.LENGTH_SHORT).show();
                            }
                        }else if(iType == 0){
                            stringList.remove(position);
                            files.remove(position);
                            imageThumbAdapter.replace(stringList);
                            imageThumbAdapter.notifyDataSetChanged();
                        }

//                        else{
//                            Toast.makeText(SignUpContInOutActivity.this, "Không thể xóa", Toast.LENGTH_SHORT).show();
//                        }
                        break;

                    case 1:
                        ViewPagerImageFragment.newInstance(stringList, position).show(fm, "viewpagerimage");
                        break;
                }

            }

            @Override
            public void onLongClick(ImageThumb item, int position, int order) {

            }
        }, stringList);

        binding.rvImage.setLayoutManager(layoutManager);
        binding.rvImage.setAdapter(imageThumbAdapter);

        binding.btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkCaptureImage();
                photoPicker();
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strSoXe = binding.edtCarNumber.getText().toString();
                String strSDT = binding.edtPhoneNumberDriver.getText().toString();
                String strIDCard = binding.edtIDCardDriver.getText().toString();
                String strContNumber = binding.edtContainerNumber.getText().toString();
                String strSeal = binding.edtSealNumber.getText().toString();
                String strDriverName = binding.edtNameDriver.getText().toString();
                String strCompany = binding.tvSpinCompany.getText().toString();
                String strVehicleType = binding.tvSpinVehicle.getText().toString();
                String strRemark = binding.edtRemark.getText().toString();
                int iDockID = iDoor;
                if (iType == 1) {
                    if (strSoXe.length() == 0) {
                        Toast.makeText(SignUpContInOutActivity.this, "Số xe không được để trống", Toast.LENGTH_SHORT).show();
                    } else if (strSDT.length() == 0) {
                        Toast.makeText(SignUpContInOutActivity.this, "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
                    } else if (strIDCard.length() == 0) {
                        Toast.makeText(SignUpContInOutActivity.this, "Số CMND/CCCD không được để trống", Toast.LENGTH_SHORT).show();
                    } else if (strDriverName.length() == 0) {
                        Toast.makeText(SignUpContInOutActivity.this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
                    } else if (binding.tvSpinCompany.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUpContInOutActivity.this, "Vui lòng chọn khách hàng", Toast.LENGTH_SHORT).show();
                    } else if (binding.tvSpinVehicle.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUpContInOutActivity.this, "Vui lòng chọn loại xe", Toast.LENGTH_SHORT).show();
                    } else if (binding.tvGateReason.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUpContInOutActivity.this, "Vui lòng chọn lý do cổng", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtSealNumber.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUpContInOutActivity.this, "Vui lòng nhập số Seal", Toast.LENGTH_SHORT).show();
                    }else if (binding.edtContainerNumber.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUpContInOutActivity.this, "Vui lòng nhập số Cont", Toast.LENGTH_SHORT).show();
                    }else {
                        UUID strConInOutID = info.ContInOutID;

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("Remark", strRemark);
                        jsonObject.addProperty("ContInOutID", String.valueOf(strConInOutID));
                        jsonObject.addProperty("CustomerName", strCompany);
                        jsonObject.addProperty("ContainerNum", strSoXe);
                        jsonObject.addProperty("DriverMobilePhone", strSDT);
                        jsonObject.addProperty("DriverIDCardNo", strIDCard);
                        jsonObject.addProperty("Reason", strReason);
                        jsonObject.addProperty("DriverName",strDriverName);
                        jsonObject.addProperty("TruckIn", strContNumber);
                        jsonObject.addProperty("SealNumber", strSeal);


                        MyRetrofit.initRequest(SignUpContInOutActivity.this).updateContInOutRemark(jsonObject).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Response<String> response, Retrofit retrofit) {
                                if (response.isSuccess()) {
                                    if (response.body().equalsIgnoreCase("OK")) {
                                        Toast.makeText(SignUpContInOutActivity.this, "Thành Công", Toast.LENGTH_SHORT).show();
                                        Utilities.speakingSomeThingslow("Cập nhật thành công", SignUpContInOutActivity.this);
                                        if (stringList.size() > 0) {
                                            insertBooking(info.ContInOutNumber, 0);
                                        }
                                    } else {
                                        Toast.makeText(SignUpContInOutActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                        Utilities.speakingSomeThingslow("Cập nhật thất bại", SignUpContInOutActivity.this);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(SignUpContInOutActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                Utilities.speakingSomeThingslow("Cập nhật thất bại", SignUpContInOutActivity.this);
                            }
                        });
                    }



                } else {


                    if (strSoXe.length() == 0) {
                        Toast.makeText(SignUpContInOutActivity.this, "Số xe không được để trống", Toast.LENGTH_SHORT).show();
                    } else if (strSDT.length() == 0) {
                        Toast.makeText(SignUpContInOutActivity.this, "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
                    } else if (strIDCard.length() == 0) {
                        Toast.makeText(SignUpContInOutActivity.this, "Số CMND/CCCD không được để trống", Toast.LENGTH_SHORT).show();
                    } else if (strDriverName.length() == 0) {
                        Toast.makeText(SignUpContInOutActivity.this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
                    } else if (binding.tvSpinCompany.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUpContInOutActivity.this, "Vui lòng chọn khách hàng", Toast.LENGTH_SHORT).show();
                    } else if (binding.tvSpinVehicle.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUpContInOutActivity.this, "Vui lòng chọn loại xe", Toast.LENGTH_SHORT).show();
                    } else if (binding.tvGateReason.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUpContInOutActivity.this, "Vui lòng chọn lý do cổng", Toast.LENGTH_SHORT).show();
                    }else if (binding.edtSealNumber.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUpContInOutActivity.this, "Vui lòng nhập số Seal", Toast.LENGTH_SHORT).show();
                    }else if (binding.edtContainerNumber.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUpContInOutActivity.this, "Vui lòng nhập số Cont", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("ScanResult", "");
                        jsonObject.addProperty("CustomerNumber", strCompany);
                        jsonObject.addProperty("ContainerNum", strSoXe);
                        jsonObject.addProperty("DriverName", strDriverName);
                        jsonObject.addProperty("DriverMobilePhone", strSDT);
                        jsonObject.addProperty("IDCard", strIDCard);
                        jsonObject.addProperty("Reason", strReason);
                        jsonObject.addProperty("DockDoorID", 0);
                        jsonObject.addProperty("SealNumber", strSeal);
                        jsonObject.addProperty("ContainerType", strVehicleType);
                        jsonObject.addProperty("TruckIn", strContNumber);
                        jsonObject.addProperty("Remarks", strRemark);

                        MyRetrofit.initRequest(SignUpContInOutActivity.this).gateContInOutInsert(jsonObject).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Response<String> response, Retrofit retrofit) {
                                if (response.isSuccess()) {
                                    if (response.body().startsWith("CO")) {
                                        Toast.makeText(SignUpContInOutActivity.this, "Thêm Thành công", Toast.LENGTH_SHORT).show();
                                        strCO = response.body();

                                        if (stringList.size() > 0) {
                                            insertBooking(strCO, 0);
                                        }

                                        if (stringList.size() == 0) {
                                            binding.edtCarNumber.requestFocus();
                                            clearText();
                                        }

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        },1000);

                                    } else {
                                        Toast.makeText(SignUpContInOutActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(SignUpContInOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        binding.btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckOutResultFragment.newInstance(new ArrayList<>(), info.ContainerNum, String.valueOf(info.ContInOutID), info.ContInOutNumber, info.OrderNumber).show(fm, "FMUPDATE");
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("ScanResult", info.OrderNumber);
//                MyRetrofit.initRequest(SignUpContInOutActivity.this).gateContInOutCheckOut(jsonObject).enqueue(new Callback<List<CarNumber>>() {
//                    @Override
//                    public void onResponse(Response<List<CarNumber>> response, Retrofit retrofit) {
//                        if (response.isSuccess()) {
//                            if (response.body().size() > 0) {
//                                CheckOutResultFragment.newInstance((ArrayList<CarNumber>) response.body(), info.ContainerNum, String.valueOf(info.ContInOutID), info.ContInOutNumber).show(fm, "FMUPDATE");
//                            } else {
//                                Utilities.speakingSomeThingslow("Phiếu này chưa được gán vào xe", SignUpContInOutActivity.this);
//                                Toast.makeText(SignUpContInOutActivity.this, "Phiếu này chưa được gán vào xe", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        Toast.makeText(SignUpContInOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });

        binding.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpContInOutActivity.this);
                builder.setMessage("Chú ý")
                        .setMessage("Bạn có muốn xóa phiếu này không?")
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                UUID strConInOutID = info.ContInOutID;
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("IPAddressDeleted",mac);
                                jsonObject.addProperty("UserName",username);
                                jsonObject.addProperty("ContInOutID",String.valueOf(strConInOutID));
                                MyRetrofit.initRequest(SignUpContInOutActivity.this).deleteContainerAndTruckDelete(jsonObject).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Response<String> response, Retrofit retrofit) {
                                        if (response.isSuccess()) {
                                            if (response.body().equalsIgnoreCase("OK")) {
                                                Toast.makeText(SignUpContInOutActivity.this, "Thành Công", Toast.LENGTH_SHORT).show();
                                                Utilities.speakingSomeThingslow("Xóa thành công", SignUpContInOutActivity.this);
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        finish();
                                                    }
                                                },1000);

                                            } else {
                                                Toast.makeText(SignUpContInOutActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                                Utilities.speakingSomeThingslow("Xóa thất bại", SignUpContInOutActivity.this);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Toast.makeText(SignUpContInOutActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }).create();
                builder.show();

            }
        });




        binding.btnTimeIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(SignUpContInOutActivity.this, "Thời gian vào", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpContInOutActivity.this);
                builder.setMessage("Bạn có chắc cập nhật thời gian vào").setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("IPAddressDeleted", mac);
                        jsonObject.addProperty("UserName", username);
                        jsonObject.addProperty("ContInOutID", info.ContInOutID.toString());
                        jsonObject.addProperty("Flag", 0);
                        MyRetrofit.initRequest(SignUpContInOutActivity.this).containerAndTruckUpdateTime(jsonObject).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Response<String> response, Retrofit retrofit) {
                                if(response.isSuccess() && response.body().equalsIgnoreCase("OK")){
                                    Toast.makeText(SignUpContInOutActivity.this, "Vào thành công!", Toast.LENGTH_SHORT).show();
                                    refreshInfoCont();
                                }else if(response.body().equalsIgnoreCase("KO")){
                                    Toast.makeText(SignUpContInOutActivity.this, "Vào thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(SignUpContInOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


                Dialog dialogTimeIn = builder.create();
                dialogTimeIn.show();


            }
        });

        binding.btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(SignUpContInOutActivity.this, "Thời gian cắm điện 1", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpContInOutActivity.this);
                builder.setMessage("Bạn có chắc cập nhật thời gian cắm điện 1").setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("IPAddressDeleted", mac);
                        jsonObject.addProperty("UserName", username);
                        jsonObject.addProperty("ContInOutID", info.ContInOutID.toString());
                        jsonObject.addProperty("Flag", 2);
                        MyRetrofit.initRequest(SignUpContInOutActivity.this).containerAndTruckUpdateTime(jsonObject).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Response<String> response, Retrofit retrofit) {
                                if(response.isSuccess() && response.body().equalsIgnoreCase("OK")){
                                    Toast.makeText(SignUpContInOutActivity.this, "Cắm điện lần 1 thành công!", Toast.LENGTH_SHORT).show();
                                    refreshInfoCont();
                                }else if(response.body().equalsIgnoreCase("KO")){
                                    Toast.makeText(SignUpContInOutActivity.this, "Cắm điện lần 1 thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(SignUpContInOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                });
                Dialog dialogCamDien1 = builder.create();
                dialogCamDien1.show();

            }
        });

        binding.btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpContInOutActivity.this);
                builder.setMessage("Bạn có chắc cập nhật thời gian rút điện 1").setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("IPAddressDeleted", mac);
                        jsonObject.addProperty("UserName", username);
                        jsonObject.addProperty("ContInOutID", info.ContInOutID.toString());
                        jsonObject.addProperty("Flag", 3);
                        MyRetrofit.initRequest(SignUpContInOutActivity.this).containerAndTruckUpdateTime(jsonObject).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Response<String> response, Retrofit retrofit) {
                                if(response.isSuccess() && response.body().equalsIgnoreCase("OK")){
                                    Toast.makeText(SignUpContInOutActivity.this, "Rút điện lần 1 thành công!", Toast.LENGTH_SHORT).show();
                                    refreshInfoCont();
                                }else if(response.body().equalsIgnoreCase("KO")){
                                    Toast.makeText(SignUpContInOutActivity.this, "Rút điện lần 1 thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(SignUpContInOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    Dialog dialogRutDien1 = builder.create();
                dialogRutDien1.show();

            }
        });

        binding.btnStartTime1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpContInOutActivity.this);
                builder.setMessage("Bạn có chắc cập nhật thời gian cắm điện 2").setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("IPAddressDeleted", mac);
                        jsonObject.addProperty("UserName", username);
                        jsonObject.addProperty("ContInOutID", info.ContInOutID.toString());
                        jsonObject.addProperty("Flag", 4);
                        MyRetrofit.initRequest(SignUpContInOutActivity.this).containerAndTruckUpdateTime(jsonObject).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Response<String> response, Retrofit retrofit) {
                                if(response.isSuccess() && response.body().equalsIgnoreCase("OK")){
                                    Toast.makeText(SignUpContInOutActivity.this, "Cắm điện lần 2 thành công!", Toast.LENGTH_SHORT).show();
                                    refreshInfoCont();
                                }else if(response.body().equalsIgnoreCase("KO")){
                                    Toast.makeText(SignUpContInOutActivity.this, "Cắm điện lần 2 thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(SignUpContInOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    Dialog dialogCamDien2 = builder.create();
                dialogCamDien2.show();

            }
        });


        binding.btnEndTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpContInOutActivity.this);
                builder.setMessage("Bạn có chắc cập nhật thời gian rút điện 2").setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("IPAddressDeleted", mac);
                        jsonObject.addProperty("UserName", username);
                        jsonObject.addProperty("ContInOutID", info.ContInOutID.toString());
                        jsonObject.addProperty("Flag", 5);
                        MyRetrofit.initRequest(SignUpContInOutActivity.this).containerAndTruckUpdateTime(jsonObject).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Response<String> response, Retrofit retrofit) {
                                if(response.isSuccess() && response.body().equalsIgnoreCase("OK")){
                                    Toast.makeText(SignUpContInOutActivity.this, "Rút điện lần 2 thành công!", Toast.LENGTH_SHORT).show();
                                    refreshInfoCont();
                                }else if(response.body().equalsIgnoreCase("KO")){
                                    Toast.makeText(SignUpContInOutActivity.this, "Rút điện lần 2 thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(SignUpContInOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    Dialog dialogRutDien2 = builder.create();
                dialogRutDien2.show();

            }
        });



        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                binding.btnPrint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strSoXe = binding.edtCarNumber.getText().toString();
                        String strSDT = binding.edtPhoneNumberDriver.getText().toString();
                        String strDriverName = binding.edtNameDriver.getText().toString();
                        String strGateReason = binding.tvGateReason.getText().toString();
                        String strGate = binding.tvGate.getText().toString();
                        if (strGateReason.trim().equalsIgnoreCase("N"))
                            strGateReason = "Nhập";
                        else if (strGateReason.trim().equalsIgnoreCase("X"))
                            strGateReason = "Xuất";
                        else if (strGateReason.trim().equalsIgnoreCase("K"))
                            strGateReason = "Không X/N";
//                        else if (strGateReason.trim().equalsIgnoreCase("O"))
//                            strGateReason = "Khác";khác
                        int iDockID = iDoor;

                        String strCO = "";
                        if (info != null) {
                            strCO = info.ContInOutNumber;
                        }
                        createPDFFile(Utilities.getAppPatch(SignUpContInOutActivity.this)
                                + "test_pdf.pdf", strSoXe, strSDT, strDriverName, strGate, strGateReason, strCO);


                    }
                });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                   permission, PermissionToken token) {

            }
        }).check();


    }


    private void createPDFFile(String path, String strSoXe, String strSDT, String strName, String strGate, String strReason, String strCO) {
        if (new File(path).exists())
            new File(path).delete();
        try {
            Document document = new Document();
            //Save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //Open to write
            document.open();

            //Setting
            document.setPageSize(PageSize.A6);
            document.setMargins(0, 0, 0, 0);
            document.addCreationDate();
            document.addAuthor("QMWH");

            //Font setting
            BaseFont baseFont = BaseFont.createFont("assets/fonts/TimesNewRoman400.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
            float fontSize = 32.0f;
            float fontSizeGate = 35.0f;
            float valueFontSize = 26.0f;


            //Create Image
            Drawable d = getDrawable(R.drawable.vanhologo);
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapData = stream.toByteArray();

            Image image = Image.getInstance(bitmapData);
//            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
//                    - document.rightMargin() - 0) / image.getWidth()) * 35;

            image.scaleAbsolute(200, 100);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);

            //Create QRCODE of Document
            BarcodeQRCode qrCode = new BarcodeQRCode(strCO, 250, 250, null);
            Image pdfFormXObject = qrCode.getImage();
            pdfFormXObject.scaleAbsolute(200, 200);

            pdfFormXObject.setAlignment(Element.ALIGN_CENTER);
            document.add(pdfFormXObject);

            Font font = new Font(baseFont, fontSize, Font.BOLD, BaseColor.BLACK);
            Font fontGATE = new Font(baseFont, fontSizeGate, Font.BOLD, BaseColor.BLACK);
            addNewItem(document, strSoXe, Element.ALIGN_CENTER, font);
            addNewItem(document, "Cửa: " + strGate + "    " + strReason, Element.ALIGN_CENTER, fontGATE);
            addNewItem(document, strName + "    " + strSDT, Element.ALIGN_CENTER, font);
            addNewItem(document, "................................................................", Element.ALIGN_CENTER, font);
            addNewItem(document, "................................................................", Element.ALIGN_CENTER, font);
            addNewItem(document, "................................................................", Element.ALIGN_CENTER, font);
            addNewItem(document, "................................................................", Element.ALIGN_CENTER, font);
            addNewItem(document, "................................................................", Element.ALIGN_CENTER, font);
            addNewItem(document, "................................................................", Element.ALIGN_CENTER, font);

            document.close();
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

            printPDF();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewItem(Document document, String strReason, int alignCenter, Font font) throws DocumentException {
        Chunk chunk = new Chunk(strReason, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(alignCenter);
        document.add(paragraph);
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(SignUpContInOutActivity.this, Utilities.getAppPatch(SignUpContInOutActivity.this) + "test_pdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
    }

    private void getCarNumberSignedList() {
        MyRetrofit.initRequest(this).getComboDockDoorID().enqueue(new Callback<List<Door>>() {
            @Override
            public void onResponse(Response<List<Door>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    if (response.body() != null) {
                        carGateList.addAll(response.body());
                        if (iType == 1) {
                            for (Door d : carGateList) {
                                if (d.dockDoorID == iDoor)
                                    binding.tvGate.setText(d.toString());
                            }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void refreshInfoCont(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ContInOutID", info.ContInOutID.toString());
        MyRetrofit.initRequest(SignUpContInOutActivity.this).containerAndTruckInforDetails(jsonObject).enqueue(new Callback<List<ContainerAndTruckInfo>>() {
            @Override
            public void onResponse(Response<List<ContainerAndTruckInfo>> response, Retrofit retrofit) {

                    if(response.isSuccess() && response.body() != null && response.body().size() > 0){

                        if(!response.body().get(0).TimeIn.equalsIgnoreCase("1900-01-01T00:00:00")){
                            binding.tvTimeIn.setText(Utilities.formatDate_ddMMyyHHmm(response.body().get(0).TimeIn));
                            binding.btnTimeIn.setEnabled(false);
                            binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnStartTime.setEnabled(true);
                            binding.btnStartTime.setBackgroundColor(Color.parseColor("#f57c00"));
                            binding.btnSignOut.setEnabled(true);
                            binding.btnSignOut.setBackgroundColor(Color.parseColor("#f57c00"));
                        }else{
                            binding.btnTimeIn.setBackgroundColor(Color.parseColor("#f57c00"));
                        }

                        if(!response.body().get(0).TimeIn.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).StartTime.equalsIgnoreCase("1900-01-01T00:00:00")){
                            binding.tvStartTime.setText(Utilities.formatDate_ddMMyyHHmm(response.body().get(0).StartTime));
                            binding.btnTimeIn.setEnabled(false);
                            binding.btnStartTime.setEnabled(false);
                            binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnStartTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnEndTime.setEnabled(true);
                            binding.btnEndTime.setBackgroundColor(Color.parseColor("#f57c00"));
                            binding.btnSignOut.setEnabled(false);
                            binding.btnSignOut.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        }

                        if(!response.body().get(0).TimeIn.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).StartTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).EndTime.equalsIgnoreCase("1900-01-01T00:00:00")){
                            binding.tvEndTime.setText(Utilities.formatDate_ddMMyyHHmm(response.body().get(0).EndTime));
                            binding.btnTimeIn.setEnabled(false);
                            binding.btnStartTime.setEnabled(false);
                            binding.btnEndTime.setEnabled(false);
                            binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnStartTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnEndTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnStartTime1.setEnabled(true);
                            binding.btnStartTime1.setBackgroundColor(Color.parseColor("#f57c00"));
                            binding.btnSignOut.setEnabled(true);
                            binding.btnSignOut.setBackgroundColor(Color.parseColor("#f57c00"));
                        }

                        if(!response.body().get(0).TimeIn.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).StartTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).EndTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).StartTime1.equalsIgnoreCase("1900-01-01T00:00:00")){
                            binding.tvStartTime1.setText(Utilities.formatDate_ddMMyyHHmm(response.body().get(0).StartTime1));
                            binding.btnTimeIn.setEnabled(false);
                            binding.btnStartTime.setEnabled(false);
                            binding.btnEndTime.setEnabled(false);
                            binding.btnStartTime1.setEnabled(false);
                            binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnStartTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnEndTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnStartTime1.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnEndTime1.setEnabled(true);
                            binding.btnEndTime1.setBackgroundColor(Color.parseColor("#f57c00"));
                            binding.btnSignOut.setEnabled(false);
                            binding.btnSignOut.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                        }

                        if(!response.body().get(0).TimeIn.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).StartTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).EndTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).StartTime1.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).EndTime1.equalsIgnoreCase("1900-01-01T00:00:00")){
                            binding.tvEndTime1.setText(Utilities.formatDate_ddMMyyHHmm(response.body().get(0).EndTime1));
                            binding.btnTimeIn.setEnabled(false);
                            binding.btnStartTime.setEnabled(false);
                            binding.btnEndTime.setEnabled(false);
                            binding.btnStartTime1.setEnabled(false);
                            binding.btnEndTime1.setEnabled(false);
                            binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnStartTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnEndTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnStartTime1.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnEndTime1.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnSignOut.setEnabled(true);
                            binding.btnSignOut.setBackgroundColor(Color.parseColor("#f57c00"));
                        }

                        if(!response.body().get(0).TimeIn.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).StartTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).EndTime.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).StartTime1.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).EndTime1.equalsIgnoreCase("1900-01-01T00:00:00") &&
                                !response.body().get(0).TimeOut.equalsIgnoreCase("1900-01-01T00:00:00")){
                            binding.tvSignOut.setText(Utilities.formatDate_ddMMyyHHmm(response.body().get(0).TimeOut));
                            binding.btnTimeIn.setEnabled(false);
                            binding.btnStartTime.setEnabled(false);
                            binding.btnEndTime.setEnabled(false);
                            binding.btnStartTime1.setEnabled(false);
                            binding.btnEndTime1.setEnabled(false);
                            binding.btnTimeIn.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnStartTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnEndTime.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnStartTime1.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnEndTime1.setBackgroundColor(getResources().getColor(R.color.colorGrayD));
                            binding.btnSignOut.setEnabled(true);
                            binding.btnSignOut.setBackgroundColor(Color.parseColor("#f57c00"));
                        }


                    }

                else{
                    Toast.makeText(SignUpContInOutActivity.this, "Lấy thông tin vừa cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(SignUpContInOutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void SpinnerSearchCarNumberDialog(Activity activity, List<String> list, TextView textView) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, list);
        EditText edtSearch = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        listView.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(adapter.getItem(position).toString());
                strCarNumber = list.get(position);
                binding.edtCarNumber.setText(strCarNumber);
                dialog.dismiss();
            }
        });
    }

    private void SpinnerSearchGateDialog(Activity activity, List<Door> list, TextView textView) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ArrayAdapter<Door> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, list);
        EditText edtSearch = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        listView.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(adapter.getItem(position).toString());
                iDoor = list.get(position).dockDoorID;
                dialog.dismiss();
            }
        });
    }

    private void SpinnerSearchReasonDialog(Activity activity, TextView textView) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, new String[]{"Nhập", "Xuất","Không X/N"});
        EditText edtSearch = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        listView.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(adapter.getItem(position));
                if (position == 0) {
                    strReason = NHAP;
                } else if (position == 1)
                    strReason = XUAT;
                else if(position == 2)
                    strReason = KHONGXN;
//                else
//                    strReason = KHAC;

                dialog.dismiss();
            }
        });
    }

    private void SpinnerSearchCustomerDialog(Activity activity, TextView textView, List<ComboCustomerResult> list) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ArrayAdapter<ComboCustomerResult> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, list);
        EditText edtSearch = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        listView.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(adapter.getItem(position).getCustomerName());
                strCustomerName = adapter.getItem(position).getCustomerName();
                dialog.dismiss();
            }
        });
    }

    private void SpinnerSearchVehicleDialog(Activity activity, TextView textView) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, new String[]{"Cont lạnh 40", "Cont lạnh 20",
                "Cont khô 40", "Cont khô 20", "Xe tải khô thùng kín", "Xe tải bạt", "Xe tải lạnh", "Khác"});
        EditText edtSearch = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        listView.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(adapter.getItem(position).toString());
                strCustomerName = adapter.getItem(position).toString();
                dialog.dismiss();
            }
        });
    }


    private void getCustomer(int iStore) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("StoreID", iStore);
        jsonObject.addProperty("IsIntegrated", false);
        MyRetrofit.initRequest(this).loadListCustomer(jsonObject).enqueue(new Callback<List<ComboCustomerResult>>() {
            @Override
            public void onResponse(Response<List<ComboCustomerResult>> response, Retrofit retrofit) {
                if (response.body() != null) {
                    customers = response.body();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void checkCaptureImage() {
        if (ContextCompat.checkSelfPermission(SignUpContInOutActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SignUpContInOutActivity.this, new String[]{Manifest.permission.CAMERA}, (SignUpContInOutActivity.this).CODE_CAMERA);
        } else
            (SignUpContInOutActivity.this).intentCaptureImage();
    }

    public void checkPickImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_READ_EXTERNAL_STORAGE);
        } else
            (SignUpContInOutActivity.this).intentPickImage();
    }

    public void photoPicker() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.chon_nguon_anh).setItems(
                        new CharSequence[]{getString(R.string.chon_hinh_tu_may_anh), getString(R.string.chon_hinh_tu_bo_suu_tap)},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        checkCaptureImage();
                                        break;
                                    case 1:
                                        checkPickImage();
                                        break;
                                }
                            }
                        })
                .create();
        dialog.show();
    }



    private void insertBooking(String strCO, int i) {

        if (files.size() > 0) {
            File file = files.get(i);
            file = ResizeImage.getCompressedImageFile(file, SignUpContInOutActivity.this);
            b = convertUsingIOUtils(file);
        } else return;

        String strImage = null;
        if (b != null)
            strImage = Base64.encodeToString(b, Base64.DEFAULT);
        if (!dialog.isShowing())
            dialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("OrderNumber", strCO);
        jsonObject.addProperty("UserName", username);
        jsonObject.addProperty("FileData", strImage);

        MyRetrofit.initRequest(SignUpContInOutActivity.this).bookingInsertPicture(new BookingInsertPictureParameter(username, strImage, strCO)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.code() == 200) {
                    Log.d("hinhanh", i + " && " + files.size());
                    ChupHinhActivity.isUpdate = true;

                    if (i == files.size() - 1) {
                        if(iType == 0){
                            clearText();
                            files.clear();
                            stringList.clear();
                            binding.edtCarNumber.requestFocus();
                            imageThumbAdapter.notifyDataSetChanged();
                        }

                    } else {
                        insertBooking(strCO, i + 1);
                    }
                    if (dialog.isShowing())
                        dialog.dismiss();

                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(SignUpContInOutActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getAttachmentInfo(final View view, String strCO) {

        ProgressDialog dialog = Utilities.getProgressDialog(SignUpContInOutActivity.this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(SignUpContInOutActivity.this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(SignUpContInOutActivity.this, new NoInternet(), TAG, view, null);
            return;
        }


        MyRetrofit.initRequest(SignUpContInOutActivity.this).getAttachmentInfoV2(new OrderNumber(strCO)).enqueue(new Callback<List<AttachmentInfo>>() {
            @Override
            public void onResponse(Response<List<AttachmentInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    dismissDialog(dialog);
                    if (stringList.size() > 0) {
                        stringList.clear();
                    }

                    for (int i = 0; i < response.body().size(); i++) {
                        stringList.add(new ImageThumb(response.body().get(i).getBitmap(), false));
                    }
                    imageThumbAdapter.replace(stringList);
                    binding.rvImage.setAdapter(imageThumbAdapter);
                } else {
                    dismissDialog(dialog);
                    Toast.makeText(SignUpContInOutActivity.this, response.message() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                Toast.makeText(SignUpContInOutActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outputMediaFile != null)
            outState.putString(capturePhotoPath, outputMediaFile.getPath());
        outState.putSerializable(listFiles, files);
        outState.putSerializable(listImageThumb, stringList);
        outState.putString(company_save, binding.tvSpinCompany.getText().toString());
        outState.putString(vehicle_save, binding.tvSpinVehicle.getText().toString());
        outState.putString(gate_save, binding.tvGate.getText().toString());
        outState.putString(reason_save, binding.tvGateReason.getText().toString());
        outState.putString(reason_save2, strReason);
        outState.putInt(idoor_save, iDoor);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        photoPath = savedInstanceState.getString(capturePhotoPath);
//        files.clear();
        files.addAll((ArrayList<File>) savedInstanceState.getSerializable(listFiles));
        stringList.addAll((ArrayList<ImageThumb>) savedInstanceState.getSerializable(listImageThumb));
        imageThumbAdapter.replace(stringList);
        imageThumbAdapter.notifyDataSetChanged();
        binding.tvSpinCompany.setText(savedInstanceState.getString(company_save));
        binding.tvSpinVehicle.setText(savedInstanceState.getString(vehicle_save));
        binding.tvGate.setText(savedInstanceState.getString(gate_save));
        binding.tvGateReason.setText(savedInstanceState.getString(reason_save));
        strReason = savedInstanceState.getString(reason_save2);
        iDoor = savedInstanceState.getInt(idoor_save);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (stringList.size() > 2) {
//            Toast.makeText(SignUpContInOutActivity.this, "Số lượng hình không lớn hơn 3", Toast.LENGTH_SHORT).show();
//            return;
//        } else {
        if (resultCode != RESULT_CANCELED) {
            if (requestCode != IntentIntegrator.REQUEST_CODE){
                if (requestCode == CODE_CAPTURE_IMAGE && resultCode == RESULT_OK ) {
                    outputMediaFile = new File(photoPath == null ? outputMediaFile.getPath() : photoPath);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        files.add(GridImage.updateGridImage2((SignUpContInOutActivity.this).outputMediaFile.getPath(), gridImageAdapter));
                    } else {
                        files.add(GridImage.updateGridImage2((SignUpContInOutActivity.this).imageCapturedUri.getPath(), gridImageAdapter));
                    }
                }else if(requestCode == CODE_PICK_IMAGE && resultCode == RESULT_OK){
                    Uri uriImage = data.getData();
                    if(uriImage != null){
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inSampleSize = Const.SAMPLE_SIZE;
//                        String path = ImageUtils.getPath(this, uriImage);
//                        if (path == null || path.length() == 0) {
//                            Toast.makeText(this, "Không thể tìm đúng đường dẫn của hình ảnh đã chọn", Toast.LENGTH_LONG).show();
//                        }else {
                            try {
                                String path = ResizeImage.resizeImageFromUri(this, uriImage, Const.IMAGE_UPLOAD_WIDTH);
                                files.add(GridImage.updateGridImage2(path, gridImageAdapter));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
//                            files.add(new File(path));

//                        }
                    }
//
                }

                else if (requestCode == CODE_CAPTURE_IMAGE2 && resultCode == RESULT_OK) {
                    for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }



                for (int i = 0; i < files.size(); i++) {
                    file = ResizeImage.getCompressedImageFile(files.get(i), SignUpContInOutActivity.this);
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    bitmap = ResizeImage.getOrientation(bitmap, file.getAbsolutePath());
                }

                stringList.add(new ImageThumb(bitmap, true));
                onSaveInstanceState(new Bundle());
//                onRestoreInstanceState(new Bundle());
                imageThumbAdapter.replace(stringList);
                imageThumbAdapter.notifyDataSetChanged();

                photoPath = null;
            }

            else   {
                if (resultCode == RESULT_OK) {
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    String dataScan = result.getContents();
                    onData(dataScan);
                }
            }
        }
//        }
    }

    private void clearText() {
        binding.tvSpinCompany.setText("");
        binding.tvSpinVehicle.setText("");
        binding.edtCarNumber.getText().clear();
        binding.edtContainerNumber.getText().clear();
        binding.edtSealNumber.getText().clear();
        binding.edtNameDriver.getText().clear();
        binding.edtIDCardDriver.getText().clear();
        binding.edtPhoneNumberDriver.getText().clear();
        binding.tvGate.setText("");
        binding.tvGateReason.setText("");
        binding.edtRemark.getText().clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AcPause", "Pause");
        contList.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("AcStop", "Stop");
    }

    private void setSearchLayoutVisibility(String group) {

        int groupInt = Group.convertGroupStringToInt(group);

        if (groupInt == Group.TRANSPORTATION || groupInt == Group.MANAGER) {
            binding.btnSignUp.setVisibility(View.VISIBLE);
            binding.btnCapture.setVisibility(View.VISIBLE);
        } else {
            binding.btnSignUp.setVisibility(View.GONE);
            binding.btnCapture.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    public void sendInput(boolean success) {
//        if (success) {
//            checkSwithRadio();
//        }
    }



    @Override
    public void onData(String data) {
        Const.timePauseActive = 0;
        data = data.trim();
        if (!data.equals("") && !data.equals("\t\t")) {
            binding.etTargetScan.setText("");
            binding.tvPrevBarcode.setText(data);

            String[] a = data.split("\\|");

            if(a.length > 4){
                String strName = "";
                String strCCCDNumber = "";
                for(int i = 0; i < a.length; i++){
                    if(i == 0){
                        strCCCDNumber = a[0];
                    }else if (i == 2){
                        strName = a[2];
                    }
                }

                //            Gson gson = new Gson();
//            QrCodeTruck qrCodeTruck = gson.fromJson(data, QrCodeTruck.class);
//            binding.edtCarNumber.setText("");
                binding.edtNameDriver.setText("");
                binding.edtIDCardDriver.setText("");
//            binding.edtPhoneNumberDriver.setText("");
//            binding.edtCarNumber.setText();
                binding.edtNameDriver.setText(strName);
                binding.edtIDCardDriver.setText(strCCCDNumber);
//            binding.edtPhoneNumberDriver.setText(qrCodeTruck.getPhonenum());
            }else{
                Toast.makeText(this, "Không hợp lệ", Toast.LENGTH_SHORT).show();
            }
    }
    }

    @Override
    public void onFragmentClosed() {
        refreshInfoCont();
    }
}