package com.example.mydoctor.ui.loginacitivity;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.sessionmanager.SessionManager;
import com.example.mydoctor.models.LoginModel;
import com.example.mydoctor.network.api.API;
import com.example.mydoctor.network.resource.AuthResource;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";

    //Dagger injection via constructor
    private SessionManager sessionManager;
    private API api;

    @Inject
    LoginViewModel(API api, SessionManager sessionManager) {

        this.api =  api;
        this.sessionManager = sessionManager;

    }

    void authenticateUser(String email, String password){

        Log.d(TAG, "authenticateUser: Attempting to login");
       sessionManager.autheticateUser(queryUser(email, password));

    }
    private LiveData<AuthResource<LoginModel>> queryUser(String email, String password){

        return LiveDataReactiveStreams.fromPublisher(

                api.login(email, password)
                        .onErrorReturn(throwable -> {

                            LoginModel errorLoginModel = new LoginModel();
                            errorLoginModel.setMessage("error");

                            return errorLoginModel;
                        })

                        .map(loginModel -> {
                            if (loginModel.getMessage().equals("error")){

                                return AuthResource.error("Could not authenticate", (LoginModel)null);
                            }


                            return AuthResource.authenticated(loginModel);
                        })
                        .subscribeOn(Schedulers.io())

        );


    }

    LiveData<AuthResource<LoginModel>> observeAuthUser(){

        return sessionManager.getAuthUser();

    }

}
