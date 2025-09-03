package com.wcs.vcc.main.giaonhanhoso;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.wcs.wcs.R;
import com.wcs.vcc.api.AttachmentParameter;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.UploadImageCallback;
import com.wcs.vcc.main.giaonhanhosooffline.DispatchingOrderDetailActivity;
import com.wcs.vcc.main.postiamge.GridImage;
import com.wcs.vcc.main.postiamge.PostImage;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignActivity extends BaseActivity {
    @BindView(R.id.gestureOverlayView)
    GestureOverlayView gestureOverlayView;

    private final String TAG = SignActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 124;
    private String orderNumber;
    private File fileSignature;
    private boolean isSigned;
    private boolean isOffline;
    private AttachmentParameter lastPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        orderNumber = getIntent().getStringExtra(DispatchingOrderDetailActivity.ORDER_NUMBER);
        isOffline = getIntent().getBooleanExtra(DispatchingOrderDetailActivity.IS_OFFLINE, false);

        gestureOverlayView.setDrawingCacheEnabled(true);

    }

    @OnClick(R.id.fab)
    public void saveSign(View view) {
        if (gestureOverlayView.getGesture() != null && gestureOverlayView.getGesture().getLength() > 500) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            } else
                saveSign();
        } else
            Snackbar.make(view, "Chữ ký của bạn quá ngắn", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE)
            if (grantResults.length != 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveSign();
                Log.e(TAG, "onRequestPermissionsResult: allow");
            } else
                Log.e(TAG, "onRequestPermissionsResult: denied");
    }

    private void saveSign() {
        Bitmap drawingCache = gestureOverlayView.getDrawingCache(true);
        File makeDirectorySign = new File(Environment.getExternalStorageDirectory() + "/Sign/");
        makeDirectorySign.mkdir();
        String fileName = String.format("signature_%s.png", orderNumber);
        fileSignature = new File(makeDirectorySign, fileName);
        try {
            isSigned = true;
            FileOutputStream os = new FileOutputStream(fileSignature);
            drawingCache.compress(Bitmap.CompressFormat.PNG, 10, os);
            os.close();

            if (!isOffline) {
                ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang lưu chữ ký...");
                dialog.show();

                PostImage postImage = new PostImage(this, dialog, gestureOverlayView, "", orderNumber, new UploadImageCallback() {
                    @Override
                    public void uploadDone(AttachmentParameter params) {
                        lastPhoto = params;
                        onBackPressed();
                    }
                });
                postImage.uploadImage(GridImage.updateGridImage(fileSignature.getPath(), null), 0);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isSigned) {
            Intent intent = new Intent();
            intent.putExtra("filePath", fileSignature.getPath());
            if (lastPhoto != null) {
                intent.putExtra("FILE_NAME", lastPhoto.AttachmentFile);
            }
            setResult(RESULT_OK, intent);
        } else {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
        }
        super.onBackPressed();
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
