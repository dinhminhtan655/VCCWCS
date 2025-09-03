package com.wcs.vcc.main.bigcqa;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.GridView;

import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.main.detailphieu.chuphinh.AttachmentInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class QaPoProductPictureActivity extends AppCompatActivity {

    @BindView(R.id.gridView)
    GridView gridView;
    String strOrderNumber;
    QaPoProductPictureAdapter adapter;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_po_product_picture);
        ButterKnife.bind(this);
        view = getWindow().getDecorView().getRootView();
        if (getIntent() != null)
            strOrderNumber = getIntent().getStringExtra("ordernumber");


        getAttachmentInfo(view);

    }


    public void getAttachmentInfo(final View view) {
        MyRetrofit.initRequest(this).getAttachmentInfo(strOrderNumber).enqueue(new Callback<List<AttachmentInfo>>() {
            @Override
            public void onResponse(Response<List<AttachmentInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter = new QaPoProductPictureAdapter(response.body(), QaPoProductPictureActivity.this);
                    gridView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
}
