package com.wcs.vcc.login;

import retrofit.http.GET;
import retrofit.http.Query;


public interface LoginService {
    @GET("api/values")
    LoginResult getvalues(@Query("id") String id);

}
