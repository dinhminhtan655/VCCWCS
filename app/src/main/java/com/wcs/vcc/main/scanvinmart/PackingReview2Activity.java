package com.wcs.vcc.main.scanvinmart;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.XDockVinOutboundPackingByStoreView;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PackingReview2Activity extends AppCompatActivity {
    public static final String TAG = "PackingReviewActivity";

    @BindView(R.id.tvPalletID)
    TextView tvPalletID;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvSumScan)
    TextView tvSumScan;
    @BindView(R.id.tvSumMove)
    TextView tvSumMove;
    @BindView(R.id.tvSumDieuChinh)
    TextView tvSumDieuChinh;
    @BindView(R.id.rvPackingReview2)
    RecyclerView rvPackingReview2;
    private Calendar calendar;
    String strPalletID, reportDate, reportDate2, strStoreCode;
    PackingReview2Adapter adapter;

    int ii = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_review2);
        ButterKnife.bind(this);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            Utilities.showBackIcon(supportActionBar);
        }

        calendar = Calendar.getInstance();
        Intent i = getIntent();
        if (i != null) {
            strPalletID = i.getStringExtra("palletid");
            reportDate = i.getStringExtra("reportdate");
            reportDate2 = i.getStringExtra("reportdate2");
            strStoreCode = i.getStringExtra("storecode");
            reportDate = Utilities.formatDate_ddMMyyyy(reportDate);
            reportDate2 = Utilities.formatDate_yyyyMMdd(reportDate2);
            tvPalletID.setText(strPalletID);
            tvDate.setText(reportDate);
        } else {
            tvPalletID.setText("NaN");
            tvDate.setText("NaN");
        }

        loadPackingReview2();


    }

    private void loadPackingReview2() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("Delivery_Date",reportDate2);
        jsonObject.addProperty("Store_Code",strStoreCode);

        MyRetrofit.initRequest(PackingReview2Activity.this).loadXDockVinOutboundPackingByStoreView(jsonObject).enqueue(new Callback<List<XDockVinOutboundPackingByStoreView>>() {
            @Override
            public void onResponse(Response<List<XDockVinOutboundPackingByStoreView>> response, Retrofit retrofit) {
                if (response.isSuccess() && response != null){
                    adapter = new PackingReview2Adapter();
                    adapter.replace(response.body());
                    rvPackingReview2.setAdapter(adapter);
                }
                int sumScan = 0;
                int sumMove = 0;
                int sumDieuChinh = 0;

                for (XDockVinOutboundPackingByStoreView x : response.body()){
                     sumScan += Integer.parseInt(x.SLScan);
                     sumMove += Integer.parseInt(x.SLMove);
                     sumDieuChinh += Integer.parseInt(x.SLDieuChinh);

                     tvSumScan.setText(String.valueOf(sumScan));
                     tvSumMove.setText(String.valueOf(sumMove));
                     tvSumDieuChinh.setText(String.valueOf(sumDieuChinh));
                    ii++;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(PackingReview2Activity.this, "Kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

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
