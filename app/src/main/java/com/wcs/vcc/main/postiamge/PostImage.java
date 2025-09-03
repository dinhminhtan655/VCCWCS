package com.wcs.vcc.main.postiamge;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wcs.vcc.main.UploadImageCallback;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.api.AttachmentParameter;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class PostImage {
    private static final String TAG = PostImage.class.getSimpleName();
    private final UploadImageCallback callback;
    private ProgressDialog dialog;
    private AppCompatActivity context;
    private View view;
    private String username;
    private Date dateCreate;
    private String originalFileName;
    private String md5FileName;
    private int fileSize;
    private String description;
    private String orderNumber;
    private boolean result = true;

    public PostImage(AppCompatActivity context, ProgressDialog dialog, View view, String description, String orderNumber, UploadImageCallback callback) {
        this.dialog = dialog;
        this.context = context;
        this.view = view;
        this.description = description;
        this.orderNumber = orderNumber;
        this.callback = callback;
        username = LoginPref.getUsername(context);
    }

    public boolean uploadImage(final ArrayList<File> files, final int lastIndex) {
        int size = files.size();
        dialog.setMessage(String.format(Locale.getDefault(), "Đang tải lên %d/%d ...", size - lastIndex, size));
        if (lastIndex >= 0 && size > 0 && lastIndex < size) {
            Log.d("stt", lastIndex + "");
            File fileUpload = files.get(lastIndex);
            dateCreate = new Date(fileUpload.lastModified());
            originalFileName = fileUpload.getName();
            md5FileName = Utilities.md5(originalFileName) + ".jpg";
            fileSize = (int) fileUpload.length() / 1024;

            RequestBody requestBodyFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), fileUpload);
            RequestBody requestBodyFileName =
                    RequestBody.create(MediaType.parse("multipart/form-data"), md5FileName);
            RequestBody requestBodyDescription =
                    RequestBody.create(MediaType.parse("multipart/form-data"), description);

            Call<String> call = MyRetrofit.initRequest(context)
                    .uploadFile(requestBodyFile, requestBodyFileName, requestBodyDescription);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    updateAttachment(files, lastIndex);
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(context, "Upload thất bại", Toast.LENGTH_LONG).show();
                    result = false;
                    dismissDialog(dialog);
                }
            });
        }
        return result;
    }

    private void updateAttachment(final ArrayList<File> files, final int n) {
        result = false;
        if (!WifiHelper.isConnected(context)) {
            dismissDialog(dialog);
            return;
        }
        final AttachmentParameter parameter = new AttachmentParameter(
                Utilities.formatDateTime_yyyyMMddHHmmssFromMili(dateCreate.getTime()),
                description,
                md5FileName,
                fileSize,
                Utilities.getDefaultUUID(),
                username,
                0,
                3,
                orderNumber,
                originalFileName);
        MyRetrofit.initRequest(context)
                .setAttachment(parameter).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            if (n > 0) {
                                uploadImage(files, n - 1);
                            } else {
                                Toast.makeText(context, "Thành công", Toast.LENGTH_LONG).show();
                                result = true;
                                dismissDialog(dialog);
                                callback.uploadDone(parameter);
                            }
                        } else {
                            Toast.makeText(context, "Upload thất bại", Toast.LENGTH_LONG).show();
                            result = false;
                            dismissDialog(dialog);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        result = false;
                        dismissDialog(dialog);
                        RetrofitError.errorNoAction(context, t, TAG, view);
                    }
                });
    }

    private void dismissDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
