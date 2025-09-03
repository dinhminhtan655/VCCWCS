package com.wcs.vcc.main.vesinhantoan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UpdateQHSEActivity extends BaseActivity {
    public static final String QHSEID = "QHSEID";
    public static final String QHSENUMBER = "QHSENumber";
    public static final String CreatedTime = "CreatedTime";
    public static final String Category = "Category";
    public static final String Comment = "Comment";
    public static final String Location = "Location";
    public static final String Subject = "Subject";
    public static final String PhotoAttachment = "PhotoAttachment";
    private static final String TAG = UpdateQHSEActivity.class.getSimpleName();
    @BindView(R.id.et_qhse_request_content)
    EditText etRequestContent;
    @BindView(R.id.et_qhse_area)
    EditText etArea;
    @BindView(R.id.et_qhse_subject)
    EditText etSubject;
    @BindView(R.id.tv_qhse_id)
    TextView tvID;
    @BindView(R.id.tv_qhse_create_time)
    TextView tvCreateTime;
    @BindView(R.id.acs_qhse_category)
    AppCompatSpinner acsCategory;
    @BindView(R.id.iv_qhse_image)
    ImageView ivImage;
    @BindView(R.id.grid_image)
    GridView gridImage;
    private String QHSERNumber;
    private ProgressDialog dialog;
    private ArrayList<File> files;
    private ThumbImageAdapter gridImageAdapter;
    private String userName;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_qhse);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        files = new ArrayList<>();
        userName = LoginPref.getUsername(this);
        storeId = LoginPref.getStoreId(this);

        initialUI();


    }

    private void initialUI() {
        QHSEActivity.isSuccess = false;
        Intent intent = getIntent();
        if (intent != null) {
            QHSERNumber = intent.getStringExtra(QHSENUMBER);
            tvID.setText(String.format(Locale.getDefault(), "%d", intent.getIntExtra(QHSEID, -1)));
            tvCreateTime.setText(intent.getStringExtra(CreatedTime));
            ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.qhse_type));
            adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            acsCategory.setAdapter(adapterType);
            acsCategory.setSelection(adapterType.getPosition(intent.getStringExtra(Category)));
            etSubject.setText(intent.getStringExtra(Subject));
            etRequestContent.setText(intent.getStringExtra(Comment));
            etArea.setText(intent.getStringExtra(Location));
            String imageName = intent.getStringExtra(PhotoAttachment);
            if (imageName.length() > 0) {
                ivImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utilities.getScreenWidth(this) / 2));
                Utilities.getPicasso(this).load(Utilities.generateUrlImage(this, imageName)).into(ivImage);
            }
        }
        gridImageAdapter = new ThumbImageAdapter(this, new ArrayList<Thumb>());
        gridImage.setAdapter(gridImageAdapter);
    }


    @OnClick(R.id.bt_qhse_create)
    public void updateQHSE(View view) {
        if (Utilities.isEmpty(etSubject))
            return;
        if (Utilities.isEmpty(etRequestContent))
            return;
        if (Utilities.isEmpty(etArea))
            return;
        Utilities.hideKeyboard(this);
        InsertQHSEParameter parameter = new InsertQHSEParameter(
                Integer.parseInt(tvID.getText().toString()),
                acsCategory.getSelectedItem().toString(),
                etRequestContent.getText().toString(),
                4,
                etArea.getText().toString(),
                etSubject.getText().toString(),
                userName,
                storeId
        );
        updateQHSE(view, parameter);
    }


    private void updateQHSE(final View view, InsertQHSEParameter parameter) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        dialog = Utilities.getProgressDialog(this, "Đang cập nhật bài viết...");
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
                    PostImage postImage = new PostImage(UpdateQHSEActivity.this, dialog, view, etRequestContent.getText().toString(), QHSERNumber, new UploadImageCallback() {
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
                        onBackPressed();
                    }
                }
                dismissDialog(dialog);
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(UpdateQHSEActivity.this, t, TAG, view);
                dismissDialog(dialog);
            }
        });
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
            onBackPressed();
        else if (id == R.id.action_camera) {
            imageChooser();
        }
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
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
