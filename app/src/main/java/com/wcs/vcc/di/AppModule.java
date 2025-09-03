package com.wcs.vcc.di;

import android.app.Application;

import com.wcs.vcc.preferences.SettingPref;
import com.wcs.vcc.retrofit.IService;
import com.wcs.vcc.retrofit.ErrorHandlingCallAdapterFactory;
import com.wcs.vcc.retrofit.MainThreadExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class AppModule {

    @Provides
    IService provideWhcService(String baseUrl, OkHttpClient okHttpClient, CallAdapter.Factory callAdapterFactory, Executor mainThreadExecutor) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(callAdapterFactory)
                .client(okHttpClient)
                .callbackExecutor(mainThreadExecutor)
                .build()
                .create(IService.class);
    }

    @Provides
    @Singleton
    OkHttpClient providerOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor).build();

    }

    @Provides
    String providerBaseUrl(Application context) {
        return String.format("http://%s", SettingPref.getInfoNetwork(context)[0]);
    }

    @Provides
    @Singleton
    CallAdapter.Factory providerRetrofitCallAdapterFactory() {
        return new ErrorHandlingCallAdapterFactory();
    }

    @Provides
    @Singleton
    Executor providerMainThreadExecutor() {
        return new MainThreadExecutor();
    }

//    @Singleton @Provides
//    GithubDb provideDb(Application app) {
//        return Room.databaseBuilder(app, GithubDb.class,"github.db").build();
//    }

//    @Singleton @Provides
//    UserDao provideUserDao(GithubDb db) {
//        return db.userDao();
//    }

}
