package com.wcs.vcc.retrofit;


public interface MyCall<T> {
    void cancel();

    void enqueue(MyCallback<T> callback);

    MyCall<T> clone();
}
