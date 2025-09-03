package com.wcs.vcc.main.bigc;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.IdRes;
import com.google.android.material.textfield.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wcs.wcs.R;import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.ProductUpdateBigCParameter;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.SCMSalesOrderProductParameter;
import com.wcs.vcc.api.SCMSalesOrderProductsParams;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ProductUpdateActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, TextWatcher {

    private TextView tvHeader;
    private View.OnClickListener action;
    private int salesOrderProductId;
    private String username;
    private float dispatchedGrossWeight;
    private int dispatchedActualCarton;
    private int basketQuantity;
    private EditText etWeight;
    private EditText etCarton;
    private MySpinner spinnerBasketList;
    private BasketSpinnerAdapter adapter;
    private RadioGroup rgBasketType;
    private RadioButton rbBasketType;
    private RadioButton rbBasketQuantity;
    private int basketID;
    private EditText etBasketQuantity;
    private RadioButton rbBasket7;
    private RadioButton rbBasket6;
    private RadioButton rbBasket5;
    private RadioButton rbBasket4;
    private RadioButton rbBasket3;
    private RadioButton rbBasket2;
    private RadioButton rbBasket1;
    private Basket basket;
    private boolean isAutoChoose;
    private RadioButton rbBasketQuantity1;
    private RadioButton rbBasketQuantity2;
    private RadioButton rbBasketQuantity3;
    private RadioButton rbBasketQuantity4;
    private RadioButton rbBasketQuantity5;
    private RadioGroup rgBasketQuantity;
    private int currentPosition;
    private List<Basket> basketList;
    private ProgressDialog dialog;
    private boolean isShouldRefresh;
    private boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);
        Utilities.showBackIcon(getSupportActionBar());

        mapViewAndSetListener();

        currentPosition = getIntent().getIntExtra(ProductsOfStoreActivity.CURRENT_POSITION, 0);

        updateUI();

        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBasketList();
            }
        };
        username = LoginPref.getUsername(this);

        adapter = new BasketSpinnerAdapter(ProductUpdateActivity.this, new ArrayList<Basket>(), this);
        spinnerBasketList.setAdapter(adapter);
        getBasketList();
    }

    private void updateUI() {
        Product product = ProductsOfStoreActivity.products.get(currentPosition);
        if (product != null) {
            @SuppressLint("StringFormatMatches") String header = String.format(Locale.getDefault(), getString(R.string.bigc_title_product_update),
                    getIntent().getStringExtra(SuppliersTodayActivity.NEXT_ACTIVITY_HEADER),
                    product.getName(),
                    product.getStoreBookingWeight()
            );
            tvHeader.setText(header);
            salesOrderProductId = product.getId();
        }
    }

    private void mapViewAndSetListener() {
        tvHeader = (TextView) findViewById(R.id.tv_product_update_header);
        etWeight = (TextInputEditText) findViewById(R.id.et_product_update_weight);
        etCarton = (TextInputEditText) findViewById(R.id.et_product_update_carton);
        etBasketQuantity = (EditText) findViewById(R.id.et_update_product_basket_quantity);
        spinnerBasketList = (MySpinner) findViewById(R.id.spinner_product_update_basket_list);
        rbBasket1 = (RadioButton) findViewById(R.id.rb_update_product_1);
        rbBasket2 = (RadioButton) findViewById(R.id.rb_update_product_2);
        rbBasket3 = (RadioButton) findViewById(R.id.rb_update_product_3);
        rbBasket4 = (RadioButton) findViewById(R.id.rb_update_product_4);
        rbBasket5 = (RadioButton) findViewById(R.id.rb_update_product_5);
        rbBasket6 = (RadioButton) findViewById(R.id.rb_update_product_6);
        rbBasket7 = (RadioButton) findViewById(R.id.rb_update_product_7);
        rbBasketQuantity1 = (RadioButton) findViewById(R.id.rb_update_product_basket_quantity_1);
        rbBasketQuantity2 = (RadioButton) findViewById(R.id.rb_update_product_basket_quantity_2);
        rbBasketQuantity3 = (RadioButton) findViewById(R.id.rb_update_product_basket_quantity_3);
        rbBasketQuantity4 = (RadioButton) findViewById(R.id.rb_update_product_basket_quantity_4);
        rbBasketQuantity5 = (RadioButton) findViewById(R.id.rb_update_product_basket_quantity_5);
        rgBasketQuantity = (RadioGroup) findViewById(R.id.rg_product_update_basket_quantity);
        rgBasketType = (RadioGroup) findViewById(R.id.rg_product_update_basket_type);
        rgBasketQuantity.setOnCheckedChangeListener(this);
        rgBasketType.setOnCheckedChangeListener(this);
        etBasketQuantity.addTextChangedListener(this);
        snackBarView = tvHeader;
    }

    public void getBasketList() {
        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, action);
            return;
        }
        MyRetrofit.initRequest(this).getBasketList().enqueue(new Callback<List<Basket>>() {
            @Override
            public void onResponse(Response<List<Basket>> response, Retrofit retrofit) {
                basketList = response.body();
                if (response.isSuccess() && basketList != null) {
                    Basket basket = new Basket(0, "", "");
                    basketList.add(0, basket);
                    adapter.clear();
                    adapter.addAll(basketList);
                    getSCMSalesOrderProducts();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
            }
        });
    }

    public void getSCMSalesOrderProducts() {

        MyRetrofit.initRequest(this).getSCMSalesOrderProducts(new SCMSalesOrderProductsParams(salesOrderProductId)).enqueue(new Callback<List<ProductUpdate>>() {
            @Override
            public void onResponse(Response<List<ProductUpdate>> response, Retrofit retrofit) {
                List<ProductUpdate> listResult = response.body();
                if (response.isSuccess() && listResult != null) {
                    ProductUpdate productUpdate = listResult.get(0);
                    if (productUpdate != null) {

                        if (!isFirstTime) {
                            etWeight.setText(String.valueOf(productUpdate.DispatchedGrossWeight));
                        } else {
                            isFirstTime = false;
                        }
                        etCarton.setText(String.valueOf(productUpdate.DispatchedActualCarton));
                        etBasketQuantity.setText(String.valueOf(productUpdate.BasketQuantity));

                        String basketNumber = productUpdate.BasketNumber;
                        chooseBasketNumberFromList(basketNumber);
                    }
                    dismissDialog(dialog);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(ProductUpdateActivity.this, t, TAG, snackBarView, action);
            }
        });
    }

    private void chooseBasketNumberFromList(String basketNumber) {
        int position = basketList.indexOf(new Basket(-1, basketNumber, ""));
        if (position >= 0 && position < basketList.size()) {
            isAutoChoose = true;
            basket = adapter.getItem(position);
            spinnerBasketList.setSelection(position);
            autoChooseRadioButtonBasketType(position);
            isAutoChoose = false;
        }
    }

    public void update(View view) {
        String sWeight = etWeight.getText().toString();
        String sCarton = etCarton.getText().toString();
        if (sWeight.trim().length() == 0) {
            Toast.makeText(this, "Bạn hãy nhập ký giao", Toast.LENGTH_LONG).show();
            etWeight.requestFocus();
            return;
        }

        if (basket != null && basket.getId() != 0 && basketQuantity == 0) {
            Toast.makeText(this, "Bạn hãy chọn số lượng khay", Toast.LENGTH_LONG).show();
            return;
        }

        calculateBasket();

        dispatchedGrossWeight = Float.parseFloat(sWeight);
        dispatchedActualCarton = Integer.parseInt(sCarton);
        updateProduct();
    }

    private void calculateBasket() {
        basketID = basket == null ? 0 : basket.getId();
        if (basketID == 0) {
            basketQuantity = 0;
        }
    }

    public void updateProduct() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.updating));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, action);
            return;
        }
        ProductUpdateBigCParameter param = new ProductUpdateBigCParameter(salesOrderProductId,
                dispatchedGrossWeight, dispatchedActualCarton, basketID, basketQuantity, username);
        dismissDialog(dialog);

        MyRetrofit.initRequest(this).updateProductBigC(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                dismissDialog(dialog);
                if (response.isSuccess()) {
                    isShouldRefresh = true;
                    Toast.makeText(ProductUpdateActivity.this, R.string.update_successful, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    Toast.makeText(ProductUpdateActivity.this, R.string.update_failed, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(ProductUpdateActivity.this, t, TAG, snackBarView, action);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (group == rgBasketType) {
            rbBasketType = (RadioButton) findViewById(checkedId);
            if (rbBasketType != null && !isAutoChoose) {
                int position = Integer.parseInt(rbBasketType.getTag().toString());
                basket = adapter.getItem(position);
                spinnerBasketList.setSelection(position);
            }
        } else {
            rbBasketQuantity = (RadioButton) findViewById(checkedId);
            if (rbBasketQuantity != null && !isAutoChooseQuantity) {
                basketQuantity = Integer.parseInt(rbBasketQuantity.getText().toString());
                etBasketQuantity.setText(String.valueOf(basketQuantity));
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v != null) {
            isAutoChoose = true;
            basket = (Basket) v.getTag();
            int position = adapter.getPosition(basket);
            spinnerBasketList.setSelection(position);
            spinnerBasketList.onDetachedFromWindow();
            autoChooseRadioButtonBasketType(position);
            isAutoChoose = false;
        }
    }

    private void autoChooseRadioButtonBasketType(int position) {
        switch (position) {
            case 1:
                rbBasket1.setChecked(true);
                rbBasketType = rbBasket1;
                break;
            case 2:
                rbBasket2.setChecked(true);
                rbBasketType = rbBasket2;
                break;
            case 3:
                rbBasket3.setChecked(true);
                rbBasketType = rbBasket3;
                break;
            case 4:
                rbBasket4.setChecked(true);
                rbBasketType = rbBasket4;
                break;
            case 5:
                rbBasket5.setChecked(true);
                rbBasketType = rbBasket5;
                break;
            case 6:
                rbBasket6.setChecked(true);
                rbBasketType = rbBasket6;
                break;
            case 7:
                rbBasket7.setChecked(true);
                rbBasketType = rbBasket7;
                break;
            default:
                if (rbBasketType != null) {
                    rbBasketType.setChecked(false);
                    rgBasketType.clearCheck();
                }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private boolean isAutoChooseQuantity;

    @Override
    public void afterTextChanged(Editable s) {
        isAutoChooseQuantity = true;
        if (etBasketQuantity.getText().length() != 0) {
            basketQuantity = Integer.parseInt(etBasketQuantity.getText().toString());
        } else basketQuantity = 0;
        autoChooseRadioButtonBasketQuantity(basketQuantity);
        isAutoChooseQuantity = false;
    }


    private void autoChooseRadioButtonBasketQuantity(int quantity) {
        switch (quantity) {
            case 1:
                rbBasketQuantity1.setChecked(true);
                rbBasketQuantity = rbBasketQuantity1;
                break;
            case 2:
                rbBasketQuantity2.setChecked(true);
                rbBasketQuantity = rbBasketQuantity2;
                break;
            case 3:
                rbBasketQuantity3.setChecked(true);
                rbBasketQuantity = rbBasketQuantity3;
                break;
            case 4:
                rbBasketQuantity4.setChecked(true);
                rbBasketQuantity = rbBasketQuantity4;
                break;
            case 5:
                rbBasketQuantity5.setChecked(true);
                rbBasketQuantity = rbBasketQuantity5;
                break;
            default:
                if (rbBasketQuantity != null) {
                    rbBasketQuantity.setChecked(false);
                    rgBasketQuantity.clearCheck();
                }
        }
    }


    public void updateKilo(View view) {
        updateSalesOrderProduct();
    }

    public void add(View view) {
        calculateBasket();
        insertSalesOrderProduct();
    }

    public void delete(View view) {
        deleteSalesOrderProduct();
    }

    private void updateSalesOrderProduct() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.updating));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).updateSalesOrderProduct(new SCMSalesOrderProductParameter(salesOrderProductId, username)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                dialog.dismiss();
                if (response.isSuccess()) {
                    isShouldRefresh = true;
                    getSCMSalesOrderProducts();
                    Toast.makeText(ProductUpdateActivity.this, R.string.update_successful, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ProductUpdateActivity.this, R.string.update_failed, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(ProductUpdateActivity.this, new NoInternet(), TAG, snackBarView);
            }
        });
    }

    private void insertSalesOrderProduct() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.inserting_new));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).insertSalesOrderProduct(new SCMSalesOrderProductParameter(salesOrderProductId, username, basketID, basketQuantity)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                dialog.dismiss();
                if (response.isSuccess()) {
                    isShouldRefresh = true;
                    Toast.makeText(ProductUpdateActivity.this, "Thêm mới thành công", Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else {
                    Toast.makeText(ProductUpdateActivity.this, "Thêm mới thất bại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(ProductUpdateActivity.this, new NoInternet(), TAG, snackBarView);
            }
        });
    }

    private void deleteSalesOrderProduct() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.deleting));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).deleteSalesOrderProduct(new SCMSalesOrderProductParameter(salesOrderProductId, username)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                dialog.dismiss();
                if (response.isSuccess()) {
                    isShouldRefresh = true;
                    Toast.makeText(ProductUpdateActivity.this, "Xóa thành công", Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else {
                    Toast.makeText(ProductUpdateActivity.this, "Xóa thất bại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(ProductUpdateActivity.this, new NoInternet(), TAG, snackBarView);
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(isShouldRefresh ? RESULT_OK : RESULT_CANCELED);
        super.onBackPressed();
    }
}
