package com.example.mydoctor.network.api;

import com.example.mydoctor.models.LoginModel;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {


    @FormUrlEncoded
    @POST("login.php")
    Flowable<LoginModel> login(

            @Field("email") String email,
            @Field("password") String password

    );

}
