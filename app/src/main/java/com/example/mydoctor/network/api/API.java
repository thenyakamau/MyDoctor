package com.example.mydoctor.network.api;

import com.example.mydoctor.models.AccessTokenModel;
import com.example.mydoctor.models.LoginModel;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API {


    @FormUrlEncoded
    @POST("api/login.php")
    Flowable<AccessTokenModel> login(

            @Field("email") String email,
            @Field("password") String password

    );

    @POST("api/refresh")
    @FormUrlEncoded
    Call<AccessTokenModel> refresh(
            @Field("refresh_token") String refreshToken
    );


    @Multipart
    @POST("api/registerPatient")
    Flowable<AccessTokenModel> registerUser(

            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("phone_number") RequestBody number,
            @Part("password") RequestBody password,
            @Part("password confirmation") RequestBody confirmPassword,
            @Part MultipartBody.Part image
    );
}
