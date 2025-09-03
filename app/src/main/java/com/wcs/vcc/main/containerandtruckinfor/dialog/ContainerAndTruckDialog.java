package com.wcs.vcc.main.containerandtruckinfor.dialog;

import static android.app.Activity.RESULT_OK;
import static com.wcs.vcc.main.BaseActivity.CODE_CAPTURE_IMAGE;
import static com.wcs.vcc.main.BaseActivity.TAG;
import static com.wcs.vcc.utilities.Utilities.convertUsingIOUtils;
import static com.wcs.vcc.utilities.Utilities.dismissDialog;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.wcs.vcc.api.ComboCustomerResult;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.xdoc.BookingInsertPictureParameter;
import com.wcs.vcc.main.containerandtruckinfor.ContainerAndTruckInfoActivity;
import com.wcs.vcc.main.containerandtruckinfor.ImageThumbAdapter2;
import com.wcs.vcc.main.containerandtruckinfor.model.ContainerAndTruckInfo;
import com.wcs.vcc.main.detailphieu.chuphinh.AttachmentInfo;
import com.wcs.vcc.main.detailphieu.chuphinh.AttachmentInfoRecAdapter;
import com.wcs.vcc.main.detailphieu.chuphinh.ChupHinhActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.OrderNumber;
import com.wcs.vcc.main.postiamge.Thumb;
import com.wcs.vcc.main.postiamge.ThumbImageAdapter;
import com.wcs.vcc.main.vesinhantoan.model.ImageThumb;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.ResizeImage;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;
import com.wcs.wcs.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ContainerAndTruckDialog extends BottomSheetDialogFragment {

    private static final String KEY_BUNDLE = "KEY_OBJECT";
    private static final String KEY_INOUT = "KEY_INOUT";
    private static final String NHAP = "Nhap";
    private static final String KHONGXN = "Khong X-N";

    private TextView tvTitleRaVao, tvCustomerName;
    private EditText edtContainerNum, edtPhoneNumber, edtIDCard;
    private Button btnDK;
    private ImageButton btnCapture;
    private SearchableSpinner spinCustomer;
    private RadioGroup radiInOut, radiSwitchWH;
    private RadioButton radiContIn, radiContOut, radiLanh, radiKho;
    private String strContInOut, strCustomer, strCO;
    private int checkedStore;

    private RecyclerView rv_image;
    private LinearLayoutManager layoutManager;
    private ThumbImageAdapter gridImageAdapter;
    private List<ImageThumb> stringList;
    private ImageThumbAdapter2 imageThumbAdapter;
    private File file;
    private Bitmap bitmap;
    private ArrayList<File> files = new ArrayList<>();
    private ProgressDialog dialog;

    private List<ComboCustomerResult> customers;
    private ArrayAdapter<ComboCustomerResult> spineradapter;

    private ContainerAndTruckInfo containerAndTruckInfo;
    private boolean bInOut;
    private String username;
    private byte[] b;
    private AttachmentInfoRecAdapter adapterRec;
    private List<AttachmentInfo> list;
//    private OnSendData sendData;
//
//    public interface OnSendData {
//        void sendInput(boolean success);
//    }

    public static ContainerAndTruckDialog newInstance(ContainerAndTruckInfo containerAndTruckInfo, boolean bInOut) {
        ContainerAndTruckDialog dialog = new ContainerAndTruckDialog();
        Bundle bundle = new Bundle();
        if (containerAndTruckInfo != null)
            bundle.putSerializable(KEY_BUNDLE, containerAndTruckInfo);
        bundle.putBoolean(KEY_INOUT, bInOut);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_container_truck, container, false);
        initLayout(v);

        containerAndTruckInfo = (ContainerAndTruckInfo) this.getArguments().get(KEY_BUNDLE);
        bInOut = this.getArguments().getBoolean(KEY_INOUT);
        list = new ArrayList<AttachmentInfo>();
        username = LoginPref.getUsername(getContext());
        stringList = new ArrayList<ImageThumb>();

        gridImageAdapter = new ThumbImageAdapter(getContext(), new ArrayList<Thumb>());

        imageThumbAdapter = new ImageThumbAdapter2(new RecyclerViewItemOrderListener<ImageThumb>() {
            @Override
            public void onClick(ImageThumb item, int position, int order) {
                switch (order) {
                    case 0:
                        stringList.remove(position);
                        imageThumbAdapter.replace(stringList);
                        imageThumbAdapter.notifyDataSetChanged();
                        break;

                    case 1:
                        Dialog builder = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        builder.getWindow().setBackgroundDrawable(
                                new ColorDrawable(Color.BLACK));
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                //nothing;
                            }
                        });

                        ImageView imageView = new ImageView(getContext());
                        imageView.setImageBitmap(item.getbImageThumb());
                        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {

                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        if (report.areAllPermissionsGranted()) {
                                            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                            Utilities.saveBitmapToMedia(getContext(), bitmap);
                                        } else {
                                            Utilities.showSettingsDialog(getActivity());
                                        }
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).check();
                                return false;
                            }
                        });
                        builder.show();
                        break;
                }

            }

            @Override
            public void onLongClick(ImageThumb item, int position, int order) {

            }
        }, stringList);

        rv_image.setLayoutManager(layoutManager);
        rv_image.setAdapter(imageThumbAdapter);

        radiContIn.setChecked(true);
        radiLanh.setChecked(true);


        if (radiContIn.isChecked()) {
            strContInOut = NHAP;
        }

        if (radiLanh.isChecked()) {
            checkedStore = 1;
            getCustomer(checkedStore);
        }


        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strSoXe = edtContainerNum.getText().toString();
                String strSDT = edtPhoneNumber.getText().toString();
                String strIDCard = edtIDCard.getText().toString();
                String strCustomerNum = containerAndTruckInfo != null ? containerAndTruckInfo.CustomerNumber : strCustomer;
                String strContInOut2;
                if (containerAndTruckInfo != null) {
                    strContInOut2 = KHONGXN;
                } else
                    strContInOut2 = strContInOut;
                if (strSoXe.length() == 0) {
                    Toast.makeText(getActivity(), "Số xe không được để trống", Toast.LENGTH_SHORT).show();
                } else if (!Utilities.validateSoXe(strSoXe)) {
                    Toast.makeText(getActivity(), "Số xe Không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (edtPhoneNumber.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
                } else if (!Utilities.validateSDT(strSDT)) {
                    Toast.makeText(getActivity(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (strIDCard.length() == 0) {
                    Toast.makeText(getActivity(), "Số CMND/CCCD không được để trống", Toast.LENGTH_SHORT).show();
                } else if (!Utilities.validateCMNDCCCD(strIDCard) || strIDCard.length() == 10 || strIDCard.length() == 11) {
                    Toast.makeText(getActivity(), "Số CMND/CCCD không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("CustomerNumber", strCustomerNum);
                    jsonObject.addProperty("ContainerNum", strSoXe);
                    jsonObject.addProperty("DriverMobilePhone", strSDT);
                    jsonObject.addProperty("IDCard", strIDCard);
                    jsonObject.addProperty("Reason", strContInOut2);

                    MyRetrofit.initRequest(getContext()).gateContInOutInsert(jsonObject).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Response<String> response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                if (response.body().startsWith("CO")) {
                                    Toast.makeText(getActivity(), "Thêm Thành công", Toast.LENGTH_SHORT).show();
                                    edtContainerNum.getText().clear();
                                    edtPhoneNumber.getText().clear();
                                    edtIDCard.getText().clear();
//                                    sendData.sendInput(true);
//                                    ((ContainerAndTruckInfoActivity) getActivity()).getContainerAndTruckInfo(v,);
//                                    if (containerAndTruckInfo == null)
                                    strCO = response.body();
//                                    else
//                                        strCO = containerAndTruckInfo.ContInOutNumber;
                                    for (int i = 0; i < stringList.size(); i++) {
                                        insertBooking(strCO, i);
                                    }

                                    dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCaptureImage();
            }
        });


        radiInOut.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiContIn:
                        strContInOut = NHAP;
                        break;
                    case R.id.radiContOut:
                        strContInOut = KHONGXN;
                        break;

                }
            }
        });

        radiSwitchWH.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiLanh:
                        checkedStore = 1;
                        getCustomer(checkedStore);
                        break;

                    case R.id.radiKho:
                        checkedStore = 2;
                        getCustomer(checkedStore);
                        break;
                }
            }
        });

        if (containerAndTruckInfo != null) {
            if (!bInOut) {
                if (containerAndTruckInfo.Reason.equalsIgnoreCase("N"))
                    tvTitleRaVao.setText("Đăng ký xe container ra");
                else if (containerAndTruckInfo.Reason.equalsIgnoreCase("K")) {
                    tvTitleRaVao.setText("Phiếu xe container ra");
                }
                radiSwitchWH.setVisibility(View.GONE);
                radiInOut.setVisibility(View.GONE);
                spinCustomer.setVisibility(View.GONE);
                edtContainerNum.setText(containerAndTruckInfo.ContainerNum);
                tvCustomerName.setText(containerAndTruckInfo.CustomerName);
                edtPhoneNumber.setText("0" + containerAndTruckInfo.DriverMobilePhone);
                edtIDCard.setText(containerAndTruckInfo.DriverIDCardNo);
                edtContainerNum.setEnabled(false);
                edtPhoneNumber.setEnabled(false);
                edtIDCard.setEnabled(false);
                btnCapture.setVisibility(View.GONE);
                if (containerAndTruckInfo.Reason.equalsIgnoreCase("K") || containerAndTruckInfo.Reason.equalsIgnoreCase("N")) {

                    tvCustomerName.setVisibility(View.VISIBLE);
                }
                if (containerAndTruckInfo.Reason.equalsIgnoreCase("K"))
                    btnDK.setVisibility(View.GONE);
                getAttachmentInfo(v);
            }
        } else
            tvTitleRaVao.setText("Đăng ký xe container vào");

        return v;
    }

    private void initLayout(View v) {
        tvTitleRaVao = v.findViewById(R.id.tvTitleRaVao);
        tvCustomerName = v.findViewById(R.id.tvCustomerName);
        rv_image = v.findViewById(R.id.rv_image);
        edtContainerNum = v.findViewById(R.id.edtContainerNum);
        edtPhoneNumber = v.findViewById(R.id.edtPhoneNumber);
        edtIDCard = v.findViewById(R.id.edtIDCard);
        btnDK = v.findViewById(R.id.btnSignUp);
        btnCapture = v.findViewById(R.id.btnCapture);
        spinCustomer = v.findViewById(R.id.spinCustomerContInOut);
        radiInOut = v.findViewById(R.id.radiInOut);
        radiContIn = v.findViewById(R.id.radiContIn);
        radiContOut = v.findViewById(R.id.radiContOut);
        radiSwitchWH = v.findViewById(R.id.radiSwitchWH);
        radiLanh = v.findViewById(R.id.radiLanh);
        radiKho = v.findViewById(R.id.radiKho);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    private void getCustomer(int iStore) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("StoreID", iStore);
        jsonObject.addProperty("IsIntegrated", false);
        MyRetrofit.initRequest(getContext()).loadListCustomer(jsonObject).enqueue(new Callback<List<ComboCustomerResult>>() {
            @Override
            public void onResponse(Response<List<ComboCustomerResult>> response, Retrofit retrofit) {
                customers = response.body();
                spineradapter = new ArrayAdapter<ComboCustomerResult>(getContext(), android.R.layout.simple_spinner_dropdown_item, customers);
                spinCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        strCustomer = customers.get(position).getCustomerNumber();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spinCustomer.setAdapter(spineradapter);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (stringList.size() > 2) {
            Toast.makeText(getContext(), "Số lượng hình không lớn hơn 3", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (requestCode == CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    files.add(GridImage.updateGridImage2(((ContainerAndTruckInfoActivity) getActivity()).outputMediaFile.getPath(), gridImageAdapter));
                } else {
//                    files.add(GridImage.updateGridImage2(((ContainerAndTruckInfoActivity) getActivity()).imageCapturedUri.getPath(), gridImageAdapter));
                }

                for (int i = 0; i < files.size(); i++) {
                    file = ResizeImage.getCompressedImageFile(files.get(i), getContext());
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    bitmap = ResizeImage.getOrientation(bitmap, file.getAbsolutePath());

                }
                stringList.add(new ImageThumb(bitmap));

                imageThumbAdapter.replace(stringList);
                imageThumbAdapter.notifyDataSetChanged();
            }


        }
    }

    public void checkCaptureImage() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, ((ContainerAndTruckInfoActivity) getActivity()).CODE_CAMERA);
        } else
            ((ContainerAndTruckInfoActivity) getActivity()).intentCaptureImage();
    }

    private void insertBooking(String strCO, int i) {
        if (files.size() > 0) {
            File file = files.get(i);
            file = ResizeImage.getCompressedImageFile(file, getContext());
            b = convertUsingIOUtils(file);
        } else return;

        String strImage = null;
        if (b != null)
            strImage = Base64.encodeToString(b, Base64.DEFAULT);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("OrderNumber", strCO);
        jsonObject.addProperty("UserName", username);
        jsonObject.addProperty("FileData", strImage);

        ProgressDialog dialog = Utilities.getProgressDialog(getContext(), getString(R.string.upload));
        dialog.show();
        MyRetrofit.initRequest(getContext()).bookingInsertPicture(new BookingInsertPictureParameter(username, strImage, strCO)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.code() == 200) {
                    Log.d("hinhanh", i + " && " + files.size());
                    dialog.dismiss();
                    ChupHinhActivity.isUpdate = true;
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getAttachmentInfo(final View view) {

        ProgressDialog dialog = Utilities.getProgressDialog(getContext(), getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(getContext())) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(getContext(), new NoInternet(), TAG, view, null);
            return;
        }


        MyRetrofit.initRequest(getContext()).getAttachmentInfoV2(new OrderNumber(containerAndTruckInfo.ContInOutNumber)).enqueue(new Callback<List<AttachmentInfo>>() {
            @Override
            public void onResponse(Response<List<AttachmentInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    dismissDialog(dialog);
                    if (stringList.size() > 0) {
                        stringList.clear();
                    }

                    for (int i = 0; i < response.body().size(); i++) {
                        stringList.add(new ImageThumb(response.body().get(i).getBitmap()));
                    }
                    imageThumbAdapter.replace(stringList);
                    rv_image.setLayoutManager(layoutManager);
                    rv_image.setAdapter(imageThumbAdapter);
                } else {
                    dismissDialog(dialog);
                    Toast.makeText(getContext(), response.message() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                Toast.makeText(getContext(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
//            sendData = (ContainerAndTruckDialog.OnSendData) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}


