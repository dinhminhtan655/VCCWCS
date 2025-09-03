package com.wcs.vcc.main.bigcqa;

import android.app.ProgressDialog;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleRegistryOwner;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import android.view.View;
import android.widget.Toast;

import com.wcs.wcs.R;import com.wcs.vcc.api.AttachmentParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.QaPoProductParam;
import com.wcs.vcc.api.QaPoProductUpdateParam;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.wcs.databinding.ActivityQaPoProductBinding;
import com.wcs.vcc.main.ShowHomeButtonActivity;
import com.wcs.vcc.main.UploadImageCallback;
import com.wcs.vcc.main.postiamge.PostImage;
import com.wcs.vcc.main.vo.QaPoProduct;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.ParseNumberHelper;
import com.wcs.vcc.utilities.Utilities;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.wcs.vcc.main.bigcqa.QaPoProductUpdateDialogFragment.filess;

public class QaPoProductActivity extends ShowHomeButtonActivity implements LifecycleRegistryOwner {
    public static final String PO_ID = "PurchasingOrderID";
    public static final String PO_NUMBER = "po_number";
    public static final String PO_DATE = "PurchasingDate";
    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private ActivityQaPoProductBinding binding;
    private QaPoProductAdapter adapter;
    private QaPoProductViewModel viewModel;
    private String username;
    private int poId;
    private ProgressDialog dialog;
    View view;

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.white);
        setTitle(getIntent().getStringExtra(PO_NUMBER) + " - " + getIntent().getStringExtra(PO_DATE));
        view = getWindow().getDecorView().getRootView();
        username = LoginPref.getUsername(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_qa_po_product);
        viewModel = ViewModelProviders.of(this).get(QaPoProductViewModel.class);

        adapter = new QaPoProductAdapter(callback);
        binding.recycleView.setAdapter(adapter);
        binding.recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        poId = getIntent().getIntExtra(PO_ID, 0);
        loadPurchasingOrderProduct(poId);
    }

    QaPoProductCallback callback = new QaPoProductCallback() {
        @Override
        public void onClick(QaPoProduct product) {
            viewModel.setQaPoProduct(product);
            QaPoProductUpdateDialogFragment dialog = new QaPoProductUpdateDialogFragment();
            dialog.show(getSupportFragmentManager(), null);
        }

        @Override
        public void onLongClick(QaPoProduct poProduct) {
            viewModel.setQaPoProduct(poProduct);
            Intent i = new Intent(QaPoProductActivity.this, QaPoProductPictureActivity.class);
            i.putExtra("ordernumber", poProduct.getPurchasingOrderProductNumber());
            startActivity(i);
        }
    };


    private void loadPurchasingOrderProduct(int poId) {
        MyRetrofit.initRequest(this).loadQaPurchasingOrderProduct(new QaPoProductParam(poId)).enqueue(new Callback<List<QaPoProduct>>() {
            @Override
            public void onResponse(Response<List<QaPoProduct>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<QaPoProduct> qaPoProductList = response.body();
                    if (qaPoProductList != null) {
                        adapter.replace(qaPoProductList);
                        if (qaPoProductList.size() > 0) {
                            QaPoProduct qaPoProduct = qaPoProductList.get(0);
                            binding.editRemark.setText(qaPoProduct.getPurchasingOrderRemark());
                            binding.editPoDamage.setText(String.valueOf(qaPoProduct.getPoDamage()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    void updateProduct(QaPoProduct product) {
        product.setPoDamage(ParseNumberHelper.parseDouble(binding.editPoDamage.getText().toString()));
        product.setPurchasingOrderRemark(binding.editRemark.getText().toString());
        updatePoProduct(view,product);
    }

    private void updatePoProduct(View view, final QaPoProduct product) {

        dialog = Utilities.getProgressDialog(this, getString(R.string.creating_new_article));
        dialog.show();

        QaPoProductUpdateParam param = new QaPoProductUpdateParam();
        param.UserName = username;
        param.PODamage = product.getPoDamage();
        param.PODetailDamage = product.getPoDetailDamage();
        param.PODetailDamageError = product.getPoDetailDamageError();
        param.PurchasingOrderID = poId;
        param.PurchasingOrderProductID = product.getId();
        param.PurchasingOrderProductRemark = product.getRemark();
        param.PurchasingOrderRemark = product.getPurchasingOrderRemark();
        param.QABy = product.getQaBy();
        param.QtyQADamageH = product.getQtyQADamageH();
        param.QtyQADamageL = product.getQtyQADamageL();
        param.QtyQADamageVH = product.getQtyQADamageVH();
        param.QtyQA = product.getQtyQA();
        param.UpdateTime = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(System.currentTimeMillis());

        String pur = product.getPurchasingOrderProductNumber();

        MyRetrofit.initRequest(this).updateQaPoProduct(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    PostImage postImage = new PostImage(QaPoProductActivity.this, dialog, view, "123", pur, new UploadImageCallback() {
                        @Override
                        public void uploadDone(AttachmentParameter params) {

                        }
                    });
                    if (filess.size() > 0) {
                        postImage.uploadImage(filess, filess.size() - 1);
                    } else {
                        dismissDialog(dialog);
                    }
                    adapter.notifyItemChanged(product.getPositionInList());
                } else {
                    Toast.makeText(QaPoProductActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(QaPoProductActivity.this, t, "", binding.getRoot());
            }
        });
    }
}
