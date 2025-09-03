package com.wcs.vcc.main.detailphieu.chuphinh;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ChupHinhActivity extends BaseActivity {
    public static final String TYPE = "type";
    public static boolean isUpdate;
    public static int PICK_IMAGE = 0;
    public static int CAPTURE_IMAGE = 1;
    private final String TAG = ChupHinhActivity.class.getSimpleName();
    //    @BindView(R.id.lvOrderDetail)
//    ListView listView;
    @BindView(R.id.tv_order_id)
    TextView tvOrderID;
    @BindView(R.id.tv_order_dock)
    TextView tvOrderDock;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;
    @BindView(R.id.tv_order_customer_number)
    TextView tvCusNumber;
    @BindView(R.id.tv_order_customer_name)
    TextView tvCusName;
    @BindView(R.id.tv_order_special_requirement)
    TextView tvSpecialRequirement;
    private String orderNumber;
    private View.OnClickListener action;
    private AttachmentInfoAdapter adapter;
    private Bitmap bitmap;

    private AttachmentInfoRecAdapter adapterRec;

    private LinearLayoutManager layoutManager;

    @BindView(R.id.rvOrderDetail)
    RecyclerView rvOrderDetail;

    private List<AttachmentInfo> list;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chup_hinh);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        if (getIntent() != null)
            orderNumber = getIntent().getStringExtra(ORDER_NUMBER);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderInfo(rvOrderDetail);
            }
        };

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        list = new ArrayList<AttachmentInfo>();


//        adapter = new AttachmentInfoAdapter(ChupHinhActivity.this, new ArrayList<AttachmentInfo>());
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Intent intent = new Intent(getApplicationContext(), ViewImageActivity.class);
////                ByteArrayOutputStream stream = new ByteArrayOutputStream();
////                adapter.getItem(position).getBitmap().compress(Bitmap.CompressFormat.PNG, 50, stream);
////                byte[] byteArray = stream.toByteArray();
//////                intent.putExtra("src",byteArray);
////                Bundle b = new Bundle();
////                b.putByteArray("Image", byteArray);
////                intent.putExtras(b);
////                startActivity(intent);
//
//                Dialog builder = new Dialog(ChupHinhActivity.this, android.R.style.Theme_Light);
//                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                builder.getWindow().setBackgroundDrawable(
//                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        //nothing;
//                    }
//                });
//
//                ImageView imageView = new ImageView(ChupHinhActivity.this);
//                imageView.setImageBitmap(adapter.getItem(position).getBitmap());
//                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));
//                builder.show();
//            }
//        });
        getAttachmentInfo(rvOrderDetail);
        getOrderInfo(rvOrderDetail);
    }

    @Override
    protected void onResume() {
        Const.isActivating = true;
        if (isUpdate)
            getAttachmentInfo(rvOrderDetail);
        super.onResume();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    public void getOrderInfo(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getOrderInfo(orderNumber).enqueue(new Callback<List<OrderInfo>>() {
            @Override
            public void onResponse(Response<List<OrderInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    OrderInfo orderInfo = response.body().get(0);
                    tvOrderID.setText(orderInfo.OrderID.toString());
                    tvOrderNumber.setText(orderInfo.getOrderNumber());
                    tvOrderDate.setText(orderInfo.getOrderDate());
                    tvCusNumber.setText(orderInfo.getCustomerNumber());
                    tvCusName.setText(orderInfo.getCustomerName());
                    tvOrderDock.setText(orderInfo.getDockNumber());
                    tvSpecialRequirement.setText(orderInfo.getSpecialRequirement());
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(ChupHinhActivity.this, t, TAG, view, action);
            }
        });
    }

    public void getAttachmentInfo(final View view) {

        ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }


        MyRetrofit.initRequest(ChupHinhActivity.this).getAttachmentInfoV2(new OrderNumber(orderNumber)).enqueue(new Callback<List<AttachmentInfo>>() {
            @Override
            public void onResponse(Response<List<AttachmentInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    dismissDialog(dialog);
                    if (list.size() > 0) {
                        list.clear();
                    }
                    list.addAll(response.body());
                    adapterRec = new AttachmentInfoRecAdapter(ChupHinhActivity.this, new RecyclerViewItemOrderListener<AttachmentInfo>() {
                        @Override
                        public void onClick(AttachmentInfo item, int position, int order) {
                            Dialog builder = new Dialog(ChupHinhActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            builder.getWindow().setBackgroundDrawable(
                                    new ColorDrawable(Color.BLACK));
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    //nothing;
                                }
                            });

                            ImageView imageView = new ImageView(ChupHinhActivity.this);
                            imageView.setImageBitmap(item.getBitmap());
                            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
                            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {

                                    Dexter.withActivity(ChupHinhActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {

                                        @Override
                                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                                            if (report.areAllPermissionsGranted()) {
                                                Toast.makeText(ChupHinhActivity.this, "Tải về", Toast.LENGTH_SHORT).show();

                                                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                                                Utilities.saveBitmapToMedia(ChupHinhActivity.this, bitmap);
                                            }else{
                                                Utilities.showSettingsDialog(ChupHinhActivity.this);
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
                        }

                        @Override
                        public void onLongClick(AttachmentInfo item, int position, int order) {

                        }
                    }, list);

                    adapterRec.replace(list);
                    rvOrderDetail.setLayoutManager(layoutManager);
                    rvOrderDetail.setAdapter(adapterRec);

                } else {
                    dismissDialog(dialog);
                    Toast.makeText(ChupHinhActivity.this, response.message() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                Toast.makeText(ChupHinhActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.bt_take_picture)
    public void takePicture() {
        intentUpload(CAPTURE_IMAGE);
    }

    @OnClick(R.id.bt_browser_gallery)
    public void browserGallery() {
        intentUpload(PICK_IMAGE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void intentUpload(int type) {
        Intent intent = new Intent(this, UploadFileActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(ORDER_NUMBER, orderNumber);
        startActivity(intent);
    }


}
