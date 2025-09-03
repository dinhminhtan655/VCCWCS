package com.wcs.vcc.main.nhapngoaigio;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wcs.wcs.R;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.OverTimeOrderDetailsParameter;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OverTimeOrderDetailsActivity extends BaseActivity {
    private final String TAG = OverTimeOrderDetailsActivity.class.getSimpleName();
    @BindView(R.id.lvOrderDetail)
    ListView listView;
    @BindView(R.id.tv_over_time_details_total)
    TextView tvTotal;
    private UUID employeeID;
    private float totalWeight;
    private String orderDate = "";
    private View.OnClickListener action;
    private OverTimeOrderDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_time_order_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        initial();

    }

    private void initial() {
        if (getIntent() != null) {
            employeeID = UUID.fromString(getIntent().getStringExtra("EMPLOYEE_ID"));
            orderDate = getIntent().getStringExtra("order_date");
        }
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmployeeID(listView);
            }
        };
        adapter = new OverTimeOrderDetailsAdapter(this, new ArrayList<OverTimeOrderDetailsInfo>());
        listView.setAdapter(adapter);
        getEmployeeID(listView);
    }

    public void getEmployeeID(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this)
                .getOverTimeOrderDetails(new OverTimeOrderDetailsParameter(employeeID, orderDate))
                .enqueue(new Callback<List<OverTimeOrderDetailsInfo>>() {
                    @Override
                    public void onResponse(Response<List<OverTimeOrderDetailsInfo>> response, Retrofit retrofit) {
                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        totalWeight = 0;
                        if (response.isSuccess() && response.body() != null) {
                            List<OverTimeOrderDetailsInfo> body = response.body();
                            adapter.clear();
                            adapter.addAll(body);
                            for (OverTimeOrderDetailsInfo info : body)
                                totalWeight += info.getTotalWeight();

                        }
                        tvTotal.setText(NumberFormat.getInstance().format(totalWeight));
                        dismissDialog(dialog);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissDialog(dialog);
                        RetrofitError.errorWithAction(getApplicationContext(), t, TAG, view, action);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        Const.isActivating = true;
        super.onResume();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }
}
