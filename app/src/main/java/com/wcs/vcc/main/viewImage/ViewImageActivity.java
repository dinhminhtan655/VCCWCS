package com.wcs.vcc.main.viewImage;

import android.os.Bundle;
import android.widget.ImageView;

import com.wcs.wcs.R;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewImageActivity extends BaseActivity {
    @BindView(R.id.iv_view_image_image)
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.black);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);

        String src = getIntent().getStringExtra("src");
        String url = Utilities.generateUrlImage(this, src);
        Utilities.getPicasso(this).load(url).into(ivImage);
    }

    @OnClick(R.id.iv_home)
    public void home() {
        onBackPressed();
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
