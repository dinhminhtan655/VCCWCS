package com.wcs.vcc.main.bigc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wcs.wcs.R;import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.api.BasketMovementParameter;
import com.wcs.vcc.api.InsertBasketMovementParameter;
import com.wcs.vcc.api.InsertBasketMovementReturnParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BasketMovementActivity extends BaseActivity {

    public static final String PURCHASING_ORDER_NUMBER = "purchasing_order_number";
    public static final String PURCHASING_ORDER_ID = "purchasing_order_id";
    public static final String BM_NUMBER = "bm_number";
    private String orderNumber;
    private View.OnClickListener action;
    private BasketMovementAdapter adapter;
    private ListView lv;
    private TextView tvBMNumberAndDate;
    private TextView tvCreatedByAndTime;
    private TextView tvRemark;
    private TextView tvTo;
    private TextView tvFrom;
    private int orderId;
    private String username;
    private MenuItem itemActionInsert;
    private ProgressDialog dialog;
    private String bmNumber;
    private boolean isInserted;
    private Button btReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_movement);
        Utilities.showBackIcon(getSupportActionBar());

        orderNumber = getIntent().getStringExtra(PURCHASING_ORDER_NUMBER);
        orderId = getIntent().getIntExtra(PURCHASING_ORDER_ID, 0);
        username = LoginPref.getUsername(this);
        bmNumber = getIntent().getStringExtra(BM_NUMBER);

        mapView();


        adapter = new BasketMovementAdapter(this, new ArrayList<BasketMovement>());
        lv.setAdapter(adapter);

        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBasketMovement();
            }
        };
        getBasketMovement();
    }

    private void mapView() {
        lv = (ListView) findViewById(R.id.lv_basket_movement);
        tvBMNumberAndDate = (TextView) findViewById(R.id.tv_bm_number_and_date);
        tvCreatedByAndTime = (TextView) findViewById(R.id.tv_bm_created_by_and_time);
        tvFrom = (TextView) findViewById(R.id.tv_bm_from);
        tvTo = (TextView) findViewById(R.id.tv_bm_to);
        tvRemark = (TextView) findViewById(R.id.tv_bm_remark);
        btReturn = (Button) findViewById(R.id.item_basket_movement_bt_return);
        snackBarView = lv;

        btReturn.setEnabled(bmNumber.trim().length() > 0);
        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void getBasketMovement() {
        if (dialog == null) {
            dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, action);
            return;
        }
        MyRetrofit.initRequest(this).getBasketMovement(new BasketMovementParameter(orderNumber)).enqueue(new Callback<List<BasketMovement>>() {
            @Override
            public void onResponse(Response<List<BasketMovement>> response, Retrofit retrofit) {
                List<BasketMovement> body = response.body();
                if (response.isSuccess() && body != null && body.size() > 0) {
                    updateUI(body.get(0));
                    setVisibleItemActionInsert(false);
                    adapter.clear();
                    adapter.addAll(body);
                } else {
                    setVisibleItemActionInsert(true);
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorWithAction(BasketMovementActivity.this, t, TAG, snackBarView, action);
            }
        });

    }

    private void updateUI(BasketMovement bm) {
        bmNumber = bm.getNumber();
        tvBMNumberAndDate.setText(getSpan("BM Number: ", bmNumber, "Date: ", bm.getDate()));

        tvCreatedByAndTime.setText(getSpan("CreatedBy: ", bm.getCreatedBy(), "Created: ", bm.getCreatedTime()));

        tvFrom.setText(getSpan("From: ", bm.getFromWhere()));

        tvTo.setText(getSpan("To: ", bm.getToWhere()));

        tvRemark.setText(getSpan("Remark: ", bm.getRemark()));
    }

    private void setVisibleItemActionInsert(boolean value) {
        if (itemActionInsert != null) {
            itemActionInsert.setVisible(value);
        }
    }

    public void insertBasketMovement() {
        if (dialog == null) {
            dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).insertBasketMovement(new InsertBasketMovementParameter(orderId, username)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                String body = response.body();
                if (response.isSuccess() && body != null) {
                    getBasketMovement();
                    isInserted = true;
                } else {
                    dismissDialog(dialog);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(BasketMovementActivity.this, t, TAG, snackBarView);
            }
        });
    }

    public void insertBasketMovementReturn() {
        if (dialog == null) {
            dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).insertBasketMovementReturn(new InsertBasketMovementReturnParameter(bmNumber, username)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                String body = response.body();
                if (response.isSuccess() && body != null) {
                    getBasketMovement();
                    isInserted = true;
                } else {
                    dismissDialog(dialog);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(BasketMovementActivity.this, t, TAG, snackBarView);
            }
        });
    }


    private SpannableStringBuilder getSpan(String label1, String text1, String label2, String text2) {
        SpannableStringBuilder span1 = new SpannableStringBuilder(text1);
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0, span1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder span2 = new SpannableStringBuilder(text2);
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0, span2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return new SpannableStringBuilder()
                .append(label1)
                .append(span1)
                .append("       ")
                .append(label2)
                .append(span2);
    }

    private SpannableStringBuilder getSpan(String label, String text) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return new SpannableStringBuilder(label).append(spannableStringBuilder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bigc_insert_basket_movement, menu);
        itemActionInsert = menu.findItem(R.id.action_insert);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_insert) {
            insertBasketMovement();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isInserted) {
            Intent data = new Intent();
            data.putExtra(SuppliersTodayActivity.POSITION, getIntent().getIntExtra(SuppliersTodayActivity.POSITION, 0));
            data.putExtra(BM_NUMBER, bmNumber);
            setResult(RESULT_OK, data);
        }
        super.onBackPressed();
    }
}
