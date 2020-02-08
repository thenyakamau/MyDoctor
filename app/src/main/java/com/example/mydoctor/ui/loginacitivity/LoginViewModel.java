package com.example.mydoctor.ui.loginacitivity;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.apierrorconverter.ApiErrorConverter;
import com.example.mydoctor.di.sessionmanager.SessionManager;
import com.example.mydoctor.models.AccessTokenModel;
import com.example.mydoctor.models.ApiErrorsModel;
import com.example.mydoctor.network.api.API;
import com.example.mydoctor.network.resource.AuthResource;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";

    //Dagger injection via constructor
    private SessionManager sessionManager;
    private API api;
    private ApiErrorConverter apiErrorConverter;
    private ApiErrorsModel apiErrorsModel;

    @Inject
    LoginViewModel(API api, SessionManager sessionManager, ApiErrorConverter apiErrorConverter) {

        this.api =  api;
        this.sessionManager = sessionManager;
        this.apiErrorConverter = apiErrorConverter;

    }

    void authenticateUser(String email, String password){

        Log.d(TAG, "authenticateUser: Attempting to login");
       sessionManager.autheticateUser(queryUser(email, password));

    }
    private LiveData<AuthResource<AccessTokenModel>> queryUser(String email, String password){

        return LiveDataReactiveStreams.fromPublisher(

                api.login(email, password)
                        .onErrorReturn(throwable -> {

                            AccessTokenModel errorLoginModel = new AccessTokenModel();
                            errorLoginModel.setAccessToken("error");


                            try {
                                HttpException error = (HttpException) throwable;
                                Log.d(TAG, "queryUser: " + throwable.getMessage());


                                apiErrorsModel = apiErrorConverter.apiErrorsModel(error.response().errorBody());
                                String errors = apiErrorsModel.getMessage();
                                errorLoginModel.setError_message(errors);

                            } catch (Exception e) {
                                e.printStackTrace();
                                errorLoginModel.setError_message("Check credentials....");
                            }


                            return errorLoginModel;
                        })

                        .map(accessTokenModel -> {
                            if (accessTokenModel.getAccessToken().equals("error")){

                                return AuthResource.error(accessTokenModel.getError_message(), (AccessTokenModel)null);
                            }


                            return AuthResource.authenticated(accessTokenModel);
                        })
                        .subscribeOn(Schedulers.io())

        );
    }

    LiveData<AuthResource<AccessTokenModel>> observeAuthUser(){

        return sessionManager.getAuthUser();

    }

}
