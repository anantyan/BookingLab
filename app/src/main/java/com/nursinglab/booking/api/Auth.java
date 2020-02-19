package com.nursinglab.booking.api;

import com.nursinglab.booking.component.ResponseComponent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Auth {

    @FormUrlEncoded
    @POST("account_api/login")
    Call<ResponseComponent> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("account_api/profile")
    Call<ResponseComponent> profile(
            @Field("id") String id
    );
}
