package com.wcs.vcc.main.containerandtruckinfor.dialog;

import static android.app.Activity.RESULT_OK;
import static com.wcs.vcc.main.BaseActivity.TAG;
import static com.wcs.vcc.utilities.Utilities.convertUsingIOUtils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.xdoc.BookingInsertPictureParameter;
import com.wcs.vcc.main.containerandtruckinfor.CarCheckOutAdapter;
import com.wcs.vcc.main.containerandtruckinfor.SignUpContInOutActivity;
import com.wcs.vcc.main.containerandtruckinfor.model.CarNumber;
import com.wcs.vcc.main.containerandtruckinfor.model.UserCheckOut;
import com.wcs.vcc.main.postiamge.GridImage;
import com.wcs.vcc.main.vo.Group;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.ResizeImage;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.wcs.databinding.FragmentCheckOutResultBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class CheckOutResultFragment extends DialogFragment {

    private FragmentCheckOutResultBinding binding;
    public static final int CODE_CAPTURE_IMAGE2 = 113;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_CARNUMBER = "carnum";
    private static final String ARG_CONTINOUT = "continoutid";
    private static final String ARG_CONTINOUTNUMBER = "continoutnumber";
    private static final String ARG_ORDERNUMBER = "ordernumber";


    private List<CarNumber> mParam1 = new ArrayList<>();
    private String carNum, contInOut, contInOutNumber, orderNumber;
    private byte[] b;
    private File file = null;
    private String username;
    private OnSendData sendData;
    private String group, strUserOut;
    private boolean bUserCheckOut;
//    private String mParam2;

    public CheckOutResultFragment() {
        // Required empty public constructor
    }

    public interface OnSendData {
        void sendInput(boolean success);
    }

    public interface onFragmentCloseListener{
        void onFragmentClosed();
    }

    private onFragmentCloseListener listener;

    @Override
    public void onStart() {
        super.onStart();
        Utilities.DialogFullscreen(CheckOutResultFragment.this, true);
    }


    public static CheckOutResultFragment newInstance(ArrayList<CarNumber> list, String strCarNumber, String strContInOutID, String strContInOutNumber, String strOrderNumber) {
        CheckOutResultFragment fragment = new CheckOutResultFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, list);
        args.putString(ARG_CARNUMBER, strCarNumber);
        args.putString(ARG_CONTINOUT, strContInOutID);
        args.putString(ARG_CONTINOUTNUMBER, strContInOutNumber);
        args.putString(ARG_ORDERNUMBER, strOrderNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (List<CarNumber>) getArguments().getSerializable(ARG_PARAM1);
            carNum = getArguments().getString(ARG_CARNUMBER);
            contInOut = getArguments().getString(ARG_CONTINOUT);
            contInOutNumber = getArguments().getString(ARG_CONTINOUTNUMBER);
            orderNumber = getArguments().getString(ARG_ORDERNUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCheckOutResultBinding.inflate(LayoutInflater.from(getContext()));
        View view = binding.getRoot();
        group = LoginPref.getInfoUser(getContext(), LoginPref.POSITION_GROUP);
        username = LoginPref.getUsername(getContext());
        CarCheckOutAdapter adapter = new CarCheckOutAdapter(getContext(), mParam1);
        binding.lvCheckOutDialog.setAdapter(adapter);

        getUserCheckOut(contInOut);

        if (mParam1.size() > 0) {
            binding.tvResultDialog.setText("Danh Sách Xe Ra \n" + mParam1.get(0).getOrderNumber());
            binding.edtRemark.setVisibility(View.GONE);
            binding.imgCheckOut.setVisibility(View.GONE);
            binding.btnCaptureCheckOut.setVisibility(View.GONE);
            binding.btnSignOut.setVisibility(View.GONE);
        } else {
            binding.tvResultDialog.setText("Đăng Ký Xe Ra");
            binding.lvCheckOutDialog.setVisibility(View.GONE);
        }

        setSearchLayoutVisibility(group);

        binding.btnCaptureCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCaptureImage();
            }
        });

        binding.btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgCheckOut.getDrawable() != null) {

                    String strRemark = binding.edtRemark.getText().toString();
                    String strConInOutID = contInOut;

                    JsonObject jsonObjectRemark = new JsonObject();
                    jsonObjectRemark.addProperty("Remark", strRemark);
                    jsonObjectRemark.addProperty("ContInOutID", strConInOutID);

                    MyRetrofit.initRequest(getActivity()).updateContInOutRemark(jsonObjectRemark).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Response<String> response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                if (response.body().equalsIgnoreCase("OK")) {
                                } else {
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("ScanResult", orderNumber);

                    MyRetrofit.initRequest(getActivity()).gateContInOutCheckOut(jsonObject).enqueue(new Callback<List<CarNumber>>() {
                        @Override
                        public void onResponse(Response<List<CarNumber>> response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                if (response.body().size() > 0) {
                                    binding.lvCheckOutDialog.setVisibility(View.VISIBLE);
                                    binding.btnSignOut.setVisibility(View.GONE);
                                    binding.btnCaptureCheckOut.setVisibility(View.GONE);
                                    CarCheckOutAdapter adapter = new CarCheckOutAdapter(getContext(), response.body());
                                    binding.lvCheckOutDialog.setAdapter(adapter);
                                    sendData.sendInput(true);
                                    insertBooking(contInOutNumber);
                                } else {
                                    Utilities.speakingSomeThingslow("Phiếu này chưa được gán vào xe", getContext());
                                    Toast.makeText(getContext(), "Phiếu này chưa được gán vào xe", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Vui lòng chụp ảnh xe ra đính kèm", Toast.LENGTH_SHORT).show();
                    Utilities.speakingSomeThingslow("Vui lòng chụp hình", getActivity());
                }
            }
        });


        binding.btnBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().finish();
                getActivity().getSupportFragmentManager().beginTransaction().remove(CheckOutResultFragment.this).commit();
            }
        });

        return view;
    }

    public void checkCaptureImage() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, ((SignUpContInOutActivity) getActivity()).CODE_CAMERA);
        } else
            ((SignUpContInOutActivity) getActivity()).intentCaptureImage2();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CAPTURE_IMAGE2 && resultCode == RESULT_OK) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                file = GridImage.updateGridImage2(((SignUpContInOutActivity) getActivity()).outputMediaFile.getPath(), null);
            } else {
                file = GridImage.updateGridImage2(((SignUpContInOutActivity) getActivity()).imageCapturedUri.getPath(), null);
            }
            file = ResizeImage.getCompressedImageFile(file, getContext());
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            bitmap = ResizeImage.getOrientation(bitmap, file.getAbsolutePath());
            binding.imgCheckOut.setImageBitmap(bitmap);
        }
    }


    private void insertBooking(String strCO) {
        File files = ResizeImage.getCompressedImageFile(file, getContext());
        b = convertUsingIOUtils(files);

        String strImage = null;
        if (b != null)
            strImage = Base64.encodeToString(b, Base64.DEFAULT);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("OrderNumber", strCO);
        jsonObject.addProperty("UserName", username);
        jsonObject.addProperty("FileData", strImage);

        MyRetrofit.initRequest(getContext()).bookingInsertPicture(new BookingInsertPictureParameter(username, strImage, strCO)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.code() == 200) {
                        binding.btnBackMain.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sendData = (CheckOutResultFragment.OnSendData) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }

        if(context instanceof onFragmentCloseListener){
            listener = (onFragmentCloseListener) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listener != null){
            listener.onFragmentClosed();
        }
    }

    private void setSearchLayoutVisibility(String group) {

        int groupInt = Group.convertGroupStringToInt(group);

        if (groupInt == Group.TRANSPORTATION || groupInt == Group.MANAGER) {
            binding.btnSignOut.setVisibility(View.VISIBLE);
            binding.btnCaptureCheckOut.setVisibility(View.VISIBLE);
        } else {
            binding.btnSignOut.setVisibility(View.GONE);
            binding.btnCaptureCheckOut.setVisibility(View.GONE);
        }
    }

    private void getUserCheckOut(String strContInOutID) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ContInOutID", strContInOutID);
        MyRetrofit.initRequest(getContext()).getUserCheckOut(jsonObject).enqueue(new Callback<UserCheckOut>() {
            @Override
            public void onResponse(Response<UserCheckOut> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    strUserOut = response.body().UserOut;
                    bUserCheckOut = response.body().UserCheckOut;
                    if (!bUserCheckOut) {
                        Utilities.setUnderLine(binding.tvUserCheckOut, "Chưa thể cho xe ra");
                        binding.btnCaptureCheckOut.setVisibility(View.GONE);
                        binding.btnSignOut.setVisibility(View.GONE);
                    } else
                        Utilities.setUnderLine(binding.tvUserCheckOut, strUserOut);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}