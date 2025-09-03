package com.wcs.vcc.main.detailphieu.chuphinh;

import static com.wcs.vcc.utilities.Utilities.convertUsingIOUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.wcs.wcs.R;
import com.wcs.vcc.api.AttachmentParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.xdoc.BookingInsertPictureParameter;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.UploadImageCallback;
import com.wcs.vcc.main.postiamge.GridImage;
import com.wcs.vcc.main.postiamge.PostImage;
import com.wcs.vcc.main.postiamge.Thumb;
import com.wcs.vcc.main.postiamge.ThumbImageAdapter;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.ResizeImage;
import com.wcs.vcc.utilities.Utilities;

import java.io.File;
import java.util.ArrayList;

import android.util.Base64;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UploadFileActivity extends BaseActivity {

    private final String TAG = UploadFileActivity.class.getSimpleName();

    @BindView(R.id.et_description_file_upload)
    EditText etDesc;
    @BindView(R.id.grid_image)
    GridView gridImage;
    @BindView(R.id.btnTakePhoto)
    AppCompatButton btnTakePhoto;
    private String orderNumber;
    private ThumbImageAdapter gridImageAdapter;

    private String username;

    private byte[] b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        username = LoginPref.getUsername(this);
        snackBarView = gridImage;

        Intent intent = getIntent();
        orderNumber = intent.getStringExtra(ORDER_NUMBER);
        int type = intent.getIntExtra(ChupHinhActivity.TYPE, 0);
        gridImageAdapter = new ThumbImageAdapter(this, new ArrayList<Thumb>());
        gridImage.setAdapter(gridImageAdapter);

        if (type == ChupHinhActivity.PICK_IMAGE)
            checkPickImage();
        else
            checkCaptureImage();

        ChupHinhActivity.isUpdate = false;

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentCaptureImage();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentPickImage();
            }
        } else if (requestCode == CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentCaptureImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                files = GridImage.updateGridImage(outputMediaFile.getPath(), gridImageAdapter);
            } else {
                files = GridImage.updateGridImage(imageCapturedUri.getPath(), gridImageAdapter);
            }
        } else if (requestCode == CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            files = GridImage.updateGridImage(this, data, gridImageAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attachment, menu);
        return true;
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

    public UploadFileActivity getInstance() {
        return UploadFileActivity.this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        else if (itemId == R.id.action_post) {
//            uploadingImage();
            insertBooking();
        }

        return true;
    }

    private void uploadingImage() {
        ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.upload));
        dialog.show();
        PostImage postImage = new PostImage(this, dialog, snackBarView, etDesc.getText().toString(), orderNumber, new UploadImageCallback() {
            @Override
            public void uploadDone(AttachmentParameter params) {
                setResult(RESULT_OK);
                onBackPressed();
            }
        });
        postImage.uploadImage(files, files.size() - 1);
    }


    private void insertBooking() {
        if (files.size() > 0) {
            File file = files.get(0);
            file = ResizeImage.getCompressedImageFile(file, UploadFileActivity.this);
            b = convertUsingIOUtils(file);
        } else return;

        String strImage = null;
        if (b != null)
            strImage = Base64.encodeToString(b, Base64.DEFAULT);

        ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.upload));
        dialog.show();
        MyRetrofit.initRequest(UploadFileActivity.this).bookingInsertPicture(new BookingInsertPictureParameter(username, strImage, orderNumber)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.code() == 200) {
                    Toast.makeText(UploadFileActivity.this, response.body() + " Thành công 1", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    ChupHinhActivity.isUpdate = true;
                    finish();
                } else {
                    Toast.makeText(UploadFileActivity.this, response.body() + " Thất bại", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UploadFileActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        ChupHinhActivity.isUpdate = true;
        super.onBackPressed();
    }
}
