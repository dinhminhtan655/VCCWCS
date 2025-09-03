package com.wcs.vcc.main.capnhatphienban;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.crash.FirebaseCrash;
import com.wcs.wcs.BuildConfig;
import com.wcs.wcs.R;
import com.wcs.vcc.main.BaseActivity;
import com.wcs.vcc.preferences.SettingPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CapNhatUngDungActivity extends BaseActivity {
    public static final int REQUEST_CODE = 124;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ll_view)
    LinearLayout llView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String urlApk;
    private File fileApk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_ung_dung);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        urlApk = String.format("http://%s/File_Download/vcc.apk", SettingPref.getInfoNetwork(this)[0]);
        Log.d("CapNhatUngDungActivity", urlApk);

//        fileApk = new File(Environment.getExternalStorageDirectory(), "vhl.apk");
        fileApk = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "vcc.apk");


        Log.d("CapNhatUngDungActivity", fileApk.toString());
    }

    @OnClick(R.id.bt_tai_pbm)
    public void taiPbm() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            downloadApkTask();
        }
    }

    private void downloadApkTask() {
        if (fileApk.exists()) {
            fileApk.delete();
        }

//        if (!fileApk.exists()) {
//            fileApk.getParentFile().mkdirs();
//        }
        new DownloadFileAsync().execute(urlApk);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadApkTask();
            }
    }

    private class DownloadFileAsync extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            llView.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            int count;
            String result = "";
            if (WifiHelper.isConnected(CapNhatUngDungActivity.this)) {
                try {
                    URL url = new URL(urls[0]);
                    HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                    connect.connect();

                    int lengthOfFile = connect.getContentLength();

                    InputStream input = new BufferedInputStream(url.openStream());
//                    OutputStream output = new FileOutputStream(fileApk);
                    OutputStream output = new FileOutputStream(fileApk);

                    byte data[] = new byte[lengthOfFile];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress((int) ((total * 100) / lengthOfFile));
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                } catch (IOException e) {
                    Log.e("DownloadApk", "", e);
//                    FirebaseCrash.report(e);
                }
            } else {
                result = getString(R.string.no_internet);
            }

            return result;

        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                llView.setVisibility(View.GONE);
                if (result.equalsIgnoreCase("")) {
                    if (fileApk.exists())
                        installApk(fileApk);
                } else {
                    Snackbar.make(llView, result, Snackbar.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                FirebaseCrash.report(ex);
            }
        }
    }

    private void installApk(File file) {
        Uri uriFileApk;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uriFileApk = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uriFileApk = Uri.fromFile(fileApk);
        }
        intent.setDataAndType(uriFileApk, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Const.isActivating = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Const.isActivating = false;
    }

}
