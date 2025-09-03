package com.wcs.vcc.utilities;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.wcs.vcc.login.LoginActivity;
import com.wcs.vcc.main.MainActivity;
import com.wcs.vcc.preferences.LoginPref;

public class LogoutService extends Service {

    private static final int NOTIFICATION_ID = 1997;
    private static final String CHANNEL_ID = "LogoutServiceChannel";

    private static final long LOGOUT_DELAY = 15 * 60 * 1000;

    private Handler handler;
    private Runnable logoutRunnable;

    private boolean isUserActive;


    public class LocalBinder extends Binder {
        LogoutService getService() {
            return LogoutService.this;
        }
    }

    private final IBinder binder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        logoutRunnable = new Runnable() {
            @Override
            public void run() {

                //Thực thi đăng xuất người dùng;
//                    logoutUser();
//                updateSignout();
                logoutUser();
                Log.d("testlogout", "hoàn thành");
                stopForeground(true);
                stopSelf();


            }
        };

//        startLogoutTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(logoutRunnable);
//        stopLogoutTimer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        startLogoutTimer();

        startForeground(NOTIFICATION_ID, Utilities.createNotification(this, MainActivity.class,
                        CHANNEL_ID, "Viet Cold Chain", "Đang chờ để đăng xuất...", NOTIFICATION_ID, NotificationManager.IMPORTANCE_LOW)
                .setDefaults(0).setSound(null).setPriority(NotificationCompat.PRIORITY_LOW).build());

        handler.removeCallbacks(logoutRunnable);

        handler.postDelayed(logoutRunnable, LOGOUT_DELAY);

        return START_STICKY;
    }

//    public void resetLogoutTimer() {
//        handler.removeCallbacks(logoutRunnable);
//        handler.postDelayed(logoutRunnable, LOGOUT_DELAY);
//    }

    public void stopLogoutTimer() {
        handler.removeCallbacks(logoutRunnable);
    }


//    private void startLogoutTimer() {
//        Log.d("testlogout", "start logout bên service");
//        handler.postDelayed(logoutRunnable, LOGOUT_DELAY);
//    }

    private void logoutUser() {
        Log.d("testlogout", "OK logout bên service");
//        stopLogoutTimer();
        LoginPref.resetInfoUserByUser(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
