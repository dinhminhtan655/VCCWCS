package com.wcs.vcc.mvvm.data.services;

import static com.wcs.vcc.api.MyRetrofit.BASE_URL;

import com.wcs.vcc.mvvm.data.api.WCSApi;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WCSService {

//    private static final String URL = "http://103.147.122.55:810";
//    public static final String URL = "http://192.168.110.12:5555";

    private final WCSApi wcsApi;

    private static WCSService singleton;
    private static HttpLoggingInterceptor httpInstance;
    private static OkHttpClient okHttpInstance;

    private WCSService() {

        if (httpInstance == null) {
            httpInstance = new HttpLoggingInterceptor();
        }
        if (okHttpInstance == null) {
            okHttpInstance = new OkHttpClient.Builder().addInterceptor(httpInstance).connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        }

        httpInstance.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpInstance.sslSocketFactory();

        Retrofit mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpInstance)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(BASE_URL).build();

        wcsApi = mRetrofit.create(WCSApi.class);
    }

    public static WCSService getInstance() {
        if (singleton == null) {
            synchronized (WCSService.class) {
                if (singleton == null) {
                    singleton = new WCSService();
                }
            }
        }

        return singleton;
    }

    public WCSApi getWcsApi() {
        return wcsApi;
    }
}
