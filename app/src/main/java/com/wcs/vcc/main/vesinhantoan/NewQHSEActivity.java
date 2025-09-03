package com.wcs.vcc.main.vesinhantoan;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wcs.wcs.R;
import com.wcs.vcc.api.AttachmentParameter;
import com.wcs.vcc.api.InsertQHSEParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.main.UploadImageCallback;
import com.wcs.vcc.main.postiamge.GridImage;
import com.wcs.vcc.main.postiamge.PostImage;
import com.wcs.vcc.main.postiamge.Thumb;
import com.wcs.vcc.main.postiamge.ThumbImageAdapter;
import com.wcs.vcc.main.vesinhantoan.model.ImageThumb;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.ResizeImage;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NewQHSEActivity extends BaseActivity {
    private static final String TAG = NewQHSEActivity.class.getSimpleName();
    @BindView(R.id.et_qhse_request_content)
    EditText etRequestContent;
    @BindView(R.id.et_qhse_area)
    EditText etArea;
    @BindView(R.id.et_qhse_subject)
    EditText etSubject;
    @BindView(R.id.tv_qhse_create_time)
    TextView tvCreateTime;
    @BindView(R.id.acs_qhse_category)
    AppCompatSpinner acsCategory;
    //    @BindView(R.id.grid_image)
//    GridView gridImage;
    @BindView(R.id.rv_image)
    RecyclerView rv_image;
    private String QHSERNumber;
    private ProgressDialog dialog;
    private ThumbImageAdapter gridImageAdapter;
    private String userName;
    private int storeId;
    private ArrayList<File> files = new ArrayList<>();
//    private File outputMediaFile;

    private List<ImageThumb> stringList;

    private ImageThumbAdapter imageThumbAdapter;

    private LinearLayoutManager layoutManager;

    private File file;

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ve_sinh_an_toan);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        userName = LoginPref.getUsername(this);
        storeId = LoginPref.getStoreId(this);

        initialUI();
    }

    private void initialUI() {

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        QHSEActivity.isSuccess = false;
        tvCreateTime.setText(Utilities.getCurrentTime());
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.qhse_type));
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acsCategory.setAdapter(adapterType);

        stringList = new ArrayList<ImageThumb>();

        gridImageAdapter = new ThumbImageAdapter(this, new ArrayList<Thumb>());
//        gridImage.setAdapter(gridImageAdapter);
    }


    private void insertQHSE(final View view, InsertQHSEParameter parameter) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        dialog = Utilities.getProgressDialog(this, getString(R.string.creating_new_article));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dismissDialog(dialog);
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).insertQHSE(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    QHSERNumber = response.body();
                    PostImage postImage = new PostImage(NewQHSEActivity.this, dialog, view, etRequestContent.getText().toString(), QHSERNumber, new UploadImageCallback() {
                        @Override
                        public void uploadDone(AttachmentParameter params) {

                        }
                    });

                    if (files.size() > 0) {
                        postImage.uploadImage(files, files.size() - 1);
                        QHSEActivity.isSuccess = true;
                    } else {
                        QHSEActivity.isSuccess = true;
                        dismissDialog(dialog);
                        finish();
                    }

                } else {
                    Snackbar.make(view, getString(R.string.error_system), Snackbar.LENGTH_LONG).show();
                    dismissDialog(dialog);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(NewQHSEActivity.this, t, TAG, view);
                dismissDialog(dialog);
            }
        });
    }


    @OnClick(R.id.bt_qhse_create)
    public void createQHSE(View view) {
        if (Utilities.isEmpty(etSubject))
            return;
        if (Utilities.isEmpty(etRequestContent))
            return;
        if (Utilities.isEmpty(etArea))
            return;
        Utilities.hideKeyboard(this);
        InsertQHSEParameter parameter = new InsertQHSEParameter(
                acsCategory.getSelectedItem().toString(),
                etRequestContent.getText().toString(),
                0,
                etArea.getText().toString(),
                etSubject.getText().toString(),
                userName,
                storeId
        );
        insertQHSE(view, parameter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qhse_item, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utilities.hideKeyboard(this);
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_camera)
            imageChooser();

        return true;
    }

    private void imageChooser() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.chon_nguon_anh).setItems(new CharSequence[]{getString(R.string.chon_hinh_tu_may_anh), getString(R.string.chon_hinh_tu_bo_suu_tap)},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        checkCaptureImage();
                                        break;
                                    case 1:
                                        checkPickImage();
                                        break;
                                }
                            }
                        })
                .create();
        dialog.show();

//        checkCaptureImage();
    }

    public void checkCaptureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CODE_CAMERA);
        } else
            intentCaptureImage();
    }

    public void checkPickImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_READ_EXTERNAL_STORAGE);
        } else
            intentPickImage();
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CODE_CAMERA) {
//            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                intentCaptureImage();
//            }
//        } else if (requestCode == CODE_READ_EXTERNAL_STORAGE) {
//            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                intentPickImage();
//            }
//        }
//
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("jiji", resultCode + "");

        if (stringList.size() > 2) {
            Toast.makeText(this, "Số lượng hình không lớn hơn 3", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (requestCode == CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    files = GridImage.updateGridImage( outputMediaFile.getPath(), gridImageAdapter);
                } else {
                    files = GridImage.updateGridImage( imageCapturedUri.getPath(), gridImageAdapter);
                }
            } else if (requestCode == CODE_PICK_IMAGE && resultCode == RESULT_OK) {
                files = GridImage.updateGridImage(this, data, gridImageAdapter, stringList.size());
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "CANCELED ", Toast.LENGTH_LONG).show();
            }

            for (int i = 0; i < files.size(); i++) {
                file = ResizeImage.getCompressedImageFile(files.get(i), NewQHSEActivity.this);
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                bitmap = ResizeImage.getOrientation(bitmap, file.getAbsolutePath());
                stringList.add(new ImageThumb(bitmap));
            }


            imageThumbAdapter = new ImageThumbAdapter(new RecyclerViewItemOrderListener<ImageThumb>() {
                @Override
                public void onClick(ImageThumb item, int position, int order) {
                    stringList.remove(position);
                    imageThumbAdapter.notifyDataSetChanged();
                }

                @Override
                public void onLongClick(ImageThumb item, int position, int order) {

                }
            }, stringList);

            imageThumbAdapter.replace(stringList);
            rv_image.setLayoutManager(layoutManager);
            rv_image.setAdapter(imageThumbAdapter);
        }


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
