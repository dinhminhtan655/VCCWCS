package com.wcs.vcc.main.detailphieu;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.ProductViewParam;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.wcs.databinding.ActivityProductDetailBinding;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.main.detailphieu.chuphinh.AttachmentInfo;
import com.wcs.vcc.main.detailphieu.chuphinh.AttachmentInfoAdapter;
import com.wcs.vcc.main.detailphieu.chuphinh.UploadFileActivity;
import com.wcs.vcc.main.detailphieu.worker.ProductView;
import com.wcs.vcc.main.viewImage.ViewImageActivity;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.wcs.vcc.main.detailphieu.chuphinh.ChupHinhActivity.CAPTURE_IMAGE;
import static com.wcs.vcc.main.detailphieu.chuphinh.ChupHinhActivity.PICK_IMAGE;
import static com.wcs.vcc.main.detailphieu.chuphinh.ChupHinhActivity.TYPE;

public class ProductDetailActivity extends ShowHomeButtonActivity {
    private static final String TAG = "ProductDetailActivity";
    public static final String SCAN_RESULT = "scan_result";
    public static final int REQUEST_CODE = 1000;
    private ActivityProductDetailBinding binding;
    private View.OnClickListener action;
    private View root;
    private String scanResult;
    private AttachmentInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        root = binding.getRoot();
        ButterKnife.bind(this);
        scanResult = getIntent().getStringExtra(SCAN_RESULT);

        adapter = new AttachmentInfoAdapter(this, new ArrayList<AttachmentInfo>());
        binding.lvProductView.setAdapter(adapter);
        binding.lvProductView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AttachmentInfo item = adapter.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(getApplicationContext(), ViewImageActivity.class);
                    intent.putExtra("src", item.getAttachmentFile());
                    startActivity(intent);
                }
            }
        });

        loadProductView(scanResult);
        getAttachmentInfo();

        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProductView(scanResult);
            }
        };
    }

    private void loadProductView(String scanResult) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, root, action);
            return;
        }
        MyRetrofit.initRequest(this)
                .loadProductView(new ProductViewParam(scanResult))
                .enqueue(new Callback<List<ProductView>>() {
                    @Override
                    public void onResponse(Response<List<ProductView>> response, Retrofit retrofit) {
                        if (response != null) {
                            List<ProductView> body = response.body();
                            if (body != null && body.size() > 0) {
                                ProductView productView = body.get(0);
                                binding.setProduct(productView);
                            }
                        }
                        dismissDialog(dialog);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissDialog(dialog);
                        RetrofitError.errorNoAction(ProductDetailActivity.this, t, TAG, root);
                    }
                });
    }

    public void getAttachmentInfo() {
        MyRetrofit.initRequest(this).getAttachmentInfo(scanResult).enqueue(new Callback<List<AttachmentInfo>>() {
            @Override
            public void onResponse(Response<List<AttachmentInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            getAttachmentInfo();
        }
    }

    @OnClick(R.id.bt_take_picture)
    public void takePicture() {
        intentUpload(CAPTURE_IMAGE);
    }

    @OnClick(R.id.bt_browser_gallery)
    public void browserGallery() {
        intentUpload(PICK_IMAGE);
    }

    private void intentUpload(int type) {
        Intent intent = new Intent(this, UploadFileActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(ORDER_NUMBER, scanResult);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
