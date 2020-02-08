package com.example.mydoctor.network.customauthenticator;

import androidx.annotation.Nullable;

import com.example.mydoctor.models.AccessTokenModel;
import com.example.mydoctor.network.api.API;
import com.example.mydoctor.tokenmanager.TokenManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;

public class CustomAuthenticator implements Authenticator {

    private TokenManager tokenManager;
    private static CustomAuthenticator INSTANCE;
    Retrofit retrofit;


    private CustomAuthenticator(TokenManager tokenManager, Retrofit retrofit){
        this.tokenManager = tokenManager;
        this.retrofit = retrofit;

    }

    public static synchronized CustomAuthenticator getInstance(TokenManager tokenManager, Retrofit retrofit){
        if(INSTANCE == null){
            INSTANCE = new CustomAuthenticator(tokenManager, retrofit);
        }

        return INSTANCE;
    }


    @Nullable
    @Override
    public Request authenticate(Route route, @NotNull Response response) throws IOException {

        if(responseCount(response) >= 3){
            return null;
        }

        AccessTokenModel token = tokenManager.getToken();


        API api = retrofit.create(API.class);
        Call<AccessTokenModel> call = api.refresh(token.getRefreshToken());
        retrofit2.Response<AccessTokenModel> res = call.execute();

        if(res.isSuccessful()){
            AccessTokenModel newToken = res.body();
            tokenManager.saveAcessToken(newToken);

            return response.request().newBuilder().header("Authorization", "Bearer " + res.body().getAccessToken()).build();
        }else{
            return null;
        }
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}