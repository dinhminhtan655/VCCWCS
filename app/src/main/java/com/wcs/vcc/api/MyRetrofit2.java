package com.wcs.vcc.api;

import static com.wcs.vcc.api.MyRetrofit.BASE_URL;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MyRetrofit2 {

//    public static String BASE_URL = "http://103.147.122.55:810";//API test
//    public static String BASE_URL = "http://192.168.110.12:5555";//API test

    private static MyRequests ourInstance;
    private static HttpLoggingInterceptor httpInstance;
    private static OkHttpClient okHttpInstance;

    public static MyRequests initRequest2() {



        if (httpInstance == null) {
            httpInstance = new HttpLoggingInterceptor();
        }
        if (okHttpInstance == null) {
            okHttpInstance = new OkHttpClient.Builder().addInterceptor(httpInstance).connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        }
        httpInstance.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpInstance.sslSocketFactory();

        if (ourInstance == null) {
            ourInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpInstance)
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build().create(MyRequests.class);
        }

        return ourInstance;
    }
}
