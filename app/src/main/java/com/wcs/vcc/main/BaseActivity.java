package com.wcs.vcc.main;

import static com.wcs.vcc.main.containerandtruckinfor.dialog.CheckOutResultFragment.CODE_CAPTURE_IMAGE2;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.wcs.vcc.login.LoginActivity;
import com.wcs.vcc.preferences.LoginPref;
import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.LogoutService;
import com.wcs.wcs.BuildConfig;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.NoInternet;
import com.wcs.vcc.api.RetrofitError;
import com.wcs.vcc.api.SendMailParameter;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.vcc.utilities.WifiHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class BaseActivity extends AppCompatActivity {

    // Đếm ngược để logout khi không tương tác app;
//    private static final long LOGOUT_DELAY = 2 * 60 * 1000; // 15 minutes
//    private Handler handler;
//    private Runnable logoutRunnable;

    // Đếm ngược khi tắt màn hình nhưng còn ở trong App
    private BroadcastReceiver screenOffReceiver;
    private boolean isScreenOffReceiverRegistered = false;

//    private BroadcastReceiver userInteractionReceiver;
//    private boolean isInteractionReceiverRegistered = false;

    private LogoutService logoutService;


    public static final String TAG = BaseActivity.class.getSimpleName();
    public final int CODE_CAMERA = 123;
    public final int CODE_READ_EXTERNAL_STORAGE = 124;
    public static final int CODE_PICK_IMAGE = 101;
    public static final int CODE_CAPTURE_IMAGE = 102;

    public View snackBarView;
    public Uri imageCapturedUri;
    public ArrayList<File> files = new ArrayList<>();
    public static final String ORDER_NUMBER = "order_number";
    public File outputMediaFile;

    public Float parserFloat(String target) {
        try {
            return Float.parseFloat(target);
        } catch (NumberFormatException ex) {
            return 0f;
        }
    }

    public Integer parserInt(String target) {
        try {
            return Integer.parseInt(target);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public boolean isEmpty(EditText object) {
        return object.getText().toString().trim().length() == 0;
    }

    public boolean isEmpty(TextView object) {
        return object.getText().toString().trim().length() == 0;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        handler = new Handler();


        // Lấy tham chiếu đến dịch vụ LogoutService
//        logoutService = ((App) getApplication()).getLogoutService();

//        userInteractionReceiver = new UserInteractionReceiver();

//        logoutRunnable = new Runnable() {
//            @Override
//            public void run() {
//                logoutUser();
//            }
//        };

        screenOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_SHUTDOWN) ||
                        intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                        || Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                    startLogoutService();
                } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction()))
                    stopLogoutService();
            }
        };


//        startLogoutTimer();


    }

    private boolean isAppRunningInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
        if (runningProcesses != null && runningProcesses.size() > 0) {
            String packageName = getPackageName();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.processName.equals(packageName) && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onStop() {

        startLogoutService();

        super.onStop();

//        stopLogoutTimer();
    }

    protected void startLogoutService() {
        Intent serviceIntent = new Intent(BaseActivity.this, LogoutService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        stopLogoutService();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        if (!isScreenOffReceiverRegistered) {
            registerReceiver(screenOffReceiver, filter);
            isScreenOffReceiverRegistered = true;
        }


//
//        if (!isInteractionReceiverRegistered) {
//            registerReceiver(userInteractionReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
//            isInteractionReceiverRegistered = true;
//        }

//        startLogoutTimer();
//        logoutService.setUserActive(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopService(new Intent(this, LogoutService.class));

//        logoutService.setUserActive(false);
//        unregisterReceiver(userInteractionReceiver);
//        isInteractionReceiverRegistered = false;

    }

    @Override
    protected void onDestroy() {

//        if (isInteractionReceiverRegistered)
//            unregisterReceiver(userInteractionReceiver);

//        if (isScreenOffReceiverRegistered)
//            unregisterReceiver(screenOffReceiver);
//        startLogoutService();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        resetLogoutTimer();
        stopLogoutService();
        return super.dispatchTouchEvent(ev);
    }

    protected void stopLogoutService() {
        Intent intentService = new Intent(this, LogoutService.class);
        stopService(intentService);
    }


//    protected void startLogoutTimer() {
//        Log.d("testlogout", "start logout");
//        handler.postDelayed(logoutRunnable, LOGOUT_DELAY);
//    }

//    protected void resetLogoutTimer() {
//        Log.d("testlogout", "reset logout");
//        if (logoutRunnable != null)
//            handler.removeCallbacks(logoutRunnable);
//        handler.postDelayed(logoutRunnable, LOGOUT_DELAY);
//    }

//    protected void stopLogoutTimer() {
//        Log.d("testlogout", "stop logout");
//        handler.removeCallbacks(logoutRunnable);
//    }


    protected void logoutUser() {
        Log.d("testlogout", "OK logout");
//        stopLogoutTimer();
        LoginPref.resetInfoUserByUser(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public void photoPicker() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.chon_nguon_anh).setItems(
                        new CharSequence[]{getString(R.string.chon_hinh_tu_may_anh), getString(R.string.chon_hinh_tu_bo_suu_tap)},
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentCaptureImage();
            }
        } else if (requestCode == CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentPickImage();
            }
        }

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

    public void intentPickImage() {
        Intent getImageIntent = new Intent(Intent.ACTION_PICK);
        getImageIntent.setType("image/*");
//        getImageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(getImageIntent, CODE_PICK_IMAGE);
    }

    private File createImageFile() throws IOException {
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }

    public void intentCaptureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                outputMediaFile = createImageFile();
            } catch (IOException ex) {
                Log.d("errorPic", ex.getMessage() + "");
            }

            if (outputMediaFile != null) {
                imageCapturedUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", outputMediaFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCapturedUri);
                startActivityForResult(intent, CODE_CAPTURE_IMAGE);
            }
        } else {
            Toast.makeText(this, "chưa cấp quyền!", Toast.LENGTH_SHORT).show();
        }

    }

    public void intentCaptureImage2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                outputMediaFile = createImageFile();
            } catch (IOException ex) {
                Log.d("errorPic", ex.getMessage() + "");
            }

            if (outputMediaFile != null) {
                imageCapturedUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", outputMediaFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCapturedUri);
                startActivityForResult(intent, CODE_CAPTURE_IMAGE2);
            }
        } else {
            Toast.makeText(this, "null rồi!", Toast.LENGTH_SHORT).show();
        }

    }

    public void dismissDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public void sendMail(final View view, SendMailParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang gửi email...");
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dismissDialog(dialog);
            return;
        }
        MyRetrofit.initRequest(this).sendMail(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                dismissDialog(dialog);
                String body = response.body();
                if (response.isSuccess() && body != null) {
                    showMessage(body);
                } else {
                    showMessage("Gửi email không thành công.");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dismissDialog(dialog);
                RetrofitError.errorNoAction(BaseActivity.this, t, TAG, view);
            }
        });
    }

    public void setupActionSearch(Menu menu, final Filterable adapter) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    doSearch(adapter, newText);
                    return true;
                }
            });
        }
    }

    private void doSearch(Filterable adapter, String keyword) {
        adapter.getFilter().filter(keyword);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean showMessage(String message) {
        message = message.trim();
        if (!(message.equals("") || message.equals("OK") || message.equals("Thành công!"))) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .create();
            dialog.show();
            return true;
        }
        return false;
    }


    public interface IShowMessage {
        void callback(boolean isShow);
    }

    public void showMessage(String message, IShowMessage callback) {
        callback.callback(showMessage(message));
    }

    @Override
    public void onUserInteraction() {
        Const.timePauseActive = 0;
        super.onUserInteraction();
    }

}
