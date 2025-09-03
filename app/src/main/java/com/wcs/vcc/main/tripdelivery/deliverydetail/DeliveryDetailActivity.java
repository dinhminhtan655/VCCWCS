package com.wcs.vcc.main.tripdelivery.deliverydetail;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.SendMailParameter;
import com.wcs.vcc.api.TripDeliveryDetailStatusUpdateParameter;
import com.wcs.wcs.databinding.ActivityDeliveryDetailBinding;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.ChupHinhActivity;
import com.wcs.vcc.main.giaonhanhoso.SignActivity;
import com.wcs.vcc.main.tripdelivery.productdetails.ProductDetailsActivity;
import com.wcs.vcc.main.tripdelivery.statuslist.TripDeliveryStatusAdapter;
import com.wcs.vcc.main.tripdelivery.statuslist.TripDeliveryStatusList;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DeliveryDetailActivity extends EmdkActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final int SIGN_ACTIVITY_CODE = 100;
    private ActivityDeliveryDetailBinding binding;
    private ProgressDialog progressDialog;
    private String orderNumber;
    private String username;
    private String androidID;
    private TripDeliveryOrderDetail data;
    private TripDeliveryStatusAdapter statusAdapter;
    private TripDeliveryStatusList currentTripStatus;
//    private Button btn_DanhXemSachPhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.showBackIcon(getSupportActionBar());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_detail);

        setUpScan();
        binding.btDeliveryDetailUpdateStatus.setOnClickListener(this);
        binding.btDeliveryDetailCapturePhoto.setOnClickListener(this);
        binding.btDeliveryDetailSign.setOnClickListener(this);
        binding.btDeliveryDetailItemDetail.setOnClickListener(this);

        binding.spDeliveryDetailStatusList.setOnItemSelectedListener(this);

        orderNumber = getIntent().getStringExtra("ORDER_NUMBER");
        username = LoginPref.getUsername(this);
        androidID = Utilities.getAndroidID(this);

        tripDeliveryOrderDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delivery_detail_update_status:
                boolean isRejected = binding.cbDeliveryDetailRejected.isChecked();
                String reason = binding.etDeliveryDetailRejectedReason.getText().toString();
                int tripStatus = currentTripStatus != null ? currentTripStatus.TripStatus : -1;
                String tripDetailID = data != null ? data.TripDetailID.toString() : "";
                tripDeliveryDetailStatusUpdate(tripDetailID, tripStatus, isRejected, reason);
                break;
            case R.id.bt_delivery_detail_capture_photo:

                if (data != null) {
                    Intent intent = new Intent(this, ChupHinhActivity.class);
                    intent.putExtra(ORDER_NUMBER, data.getTripDetailNumber());
                    startActivity(intent);
                }
                break;
            case R.id.bt_delivery_detail_sign:
                if (data != null) {
                    Intent intent = new Intent(this, SignActivity.class);
                    intent.putExtra(ORDER_NUMBER, data.getTripDetailNumber());
                    startActivityForResult(intent, SIGN_ACTIVITY_CODE);
                }
                break;
            case R.id.bt_delivery_detail_item_detail:
                Intent intent = new Intent(this, ProductDetailsActivity.class);
                intent.putExtra("ORDER_NUMBER", orderNumber);
                startActivity(intent);
                break;
        }
    }

    private void tripDeliveryOrderDetail() {

        progressDialog = Utilities.getProgressDialog(this, "Loading...");
        progressDialog.show();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("ScanResult=%s", orderNumber));

        MyRetrofit.initRequest(this).tripDeliveryOrderDetail(body).enqueue(new Callback<List<TripDeliveryOrderDetail>>() {
            @Override
            public void onResponse(Response<List<TripDeliveryOrderDetail>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<TripDeliveryOrderDetail> list = response.body();
                    if (list.size() > 0) {
                        data = list.get(0);
                        binding.setData(data);
                        orderNumber = data.OrderNumber;

                        tripDeliveryStatusList();
                    } else {
                        progressDialog.dismiss();
                    }
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void tripDeliveryStatusList() {

        MyRetrofit.initRequest(this).tripDeliveryStatusList().enqueue(new Callback<List<TripDeliveryStatusList>>() {
            @Override
            public void onResponse(Response<List<TripDeliveryStatusList>> response, Retrofit retrofit) {
                if (response.isSuccess()) {

                    List<TripDeliveryStatusList> body = response.body();
                    if (body != null) {
                        statusAdapter = new TripDeliveryStatusAdapter(DeliveryDetailActivity.this, body);
                        binding.spDeliveryDetailStatusList.setAdapter(statusAdapter);

                        for (int i = 0, n = body.size(); i < n; i++) {
                            TripDeliveryStatusList item = body.get(i);
                            if (item != null) {
                                if (item.TripStatus == data.TripStatus) {
                                    binding.spDeliveryDetailStatusList.setSelection(i, true);
                                    currentTripStatus = item;
                                }
                            }
                        }
                    }
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void tripDeliveryDetailStatusUpdate(String tripDetailID, int tripStatus, boolean isRejected, String reason) {
        progressDialog = Utilities.getProgressDialog(this, "Updating...");
        progressDialog.show();

        TripDeliveryDetailStatusUpdateParameter parameter = new TripDeliveryDetailStatusUpdateParameter(
                username, androidID, tripDetailID, tripStatus, isRejected, reason
        );

        MyRetrofit.initRequest(this).tripDeliveryDetailStatusUpdate(parameter).enqueue(new Callback<List<TripDeliveryOrderDetail>>() {
            @Override
            public void onResponse(Response<List<TripDeliveryOrderDetail>> response, Retrofit retrofit) {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentTripStatus = statusAdapter.getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_ACTIVITY_CODE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra("filePath");
            if (filePath != null) {
                SendMailParameter parameter = new SendMailParameter(orderNumber, username);
                sendMail(binding.getRoot(), parameter);
//                try {
//                    filePath = ResizeImage.resizeImageFromFile(filePath, Const.IMAGE_UPLOAD_WIDTH);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = Const.SAMPLE_SIZE;
//                Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
//                ivThumbSignature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utilities.getScreenWidth(this) / 2));
//                ivThumbSignature.setImageBitmap(bitmap);
//                btGhsSign.setEnabled(false);
            }
        }
    }

//    private  void XemDanhSachPhi() {
//
//        btn_DanhXemSachPhi = (Button)findViewById(R.id.btn_DS_Phi_Da_Gui);
//
//
//        btn_DanhXemSachPhi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DeliveryDetailActivity.this, DanhSachPhiACtivity.class);
//                startActivity(intent);
//            }
//        });
//    }

    @Override
    public void onData(String data) {
        super.onData(data);

        if (data.contains("do") || data.contains("DO")) {

            orderNumber = data;
            tripDeliveryOrderDetail();
        }
    }
}
