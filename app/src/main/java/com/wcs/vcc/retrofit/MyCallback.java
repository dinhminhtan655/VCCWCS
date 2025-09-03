package com.wcs.vcc.retrofit;

import java.io.IOException;

import retrofit2.Response;

public abstract class MyCallback<T> {
    /**
     * Called for [200, 300) responses.
     */
    public abstract void success(Response<T> response);

    /**
     * Called for 401 responses.
     */
    public void unauthenticated(Response<?> response){}

    /**
     * Called for [400, 500) responses, except 401.
     */
    public  void clientError(Response<?> response){}

    /**
     * Called for [500, 600) response.
     */
    public  void serverError(Response<?> response){}

    /**
     * Called for network errors while making the call.
     */
    public abstract void networkError(IOException e);

    /**
     * Called for unexpected errors while making the call.
     */
    public void unexpectedError(Throwable t){}
}
