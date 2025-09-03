package com.wcs.vcc.retrofit;

import androidx.annotation.NonNull;

import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.CallAdapter;


class ErrorHandlingCallAdapter<R> implements CallAdapter<R, MyCall<R>> {
    private final Type responseType;
    private final Executor callbackExecutor;

    ErrorHandlingCallAdapter(Type responseType, Executor callbackExecutor) {
        this.responseType = responseType;
        this.callbackExecutor = callbackExecutor;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public MyCall<R> adapt(@NonNull Call<R> call) {
        return new MyCallAdapter<>(call, callbackExecutor);
    }
}