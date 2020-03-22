package com.example.mydoctor.network.firebaseservice;

import android.util.Log;

import com.example.mydoctor.network.api.API;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FireBaseServiceInstance {

    private static final String TAG = "FireBaseServiceInstance";

    private   Retrofit retrofit;


    public FireBaseServiceInstance(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public  void saveFireBaseInstance(String token){

        API api = retrofit.create(API.class);
        Call<ResponseBody> call = api.saveFirebaseToken(token);
        try {
            Response<ResponseBody> res = call.execute();

            if (res.isSuccessful()){

                Log.d(TAG, "saveFireBaseInstance: ");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
