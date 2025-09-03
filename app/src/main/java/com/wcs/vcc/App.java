package com.wcs.vcc;

import android.app.Activity;
import android.app.Application;

import com.wcs.vcc.di.AppInjector;
import com.wcs.wcs.BuildConfig;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

public class App extends Application implements HasActivityInjector {

    private static App sInstance;
//    private LogoutService logoutService;

    @Inject
    DispatchingAndroidInjector<Activity> androidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

//        // Khởi tạo LogoutService
//        logoutService = new LogoutService();
//        // ... Cấu hình các thiết lập khác cho dịch vụ
//
//        // Bắt đầu dịch vụ LogoutService
//        startService(new Intent(this, LogoutService.class));

        AppInjector.init(this);
    }

//    public LogoutService getLogoutService() {
//        return logoutService;
//    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return androidInjector;
    }
    public static App getInstance() {
        return sInstance;
    }

}
