package com.example.mydoctor.ui.loginacitivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mydoctor.models.LoginModel;
import com.example.mydoctor.network.api.API;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";
    private MediatorLiveData<LoginModel> authUser = new MediatorLiveData<>();
    private API api;

    @Inject
    LoginViewModel(API api) {

        this.api =  api;

    }

    public void authenticateUser(String email, String password){
        final LiveData<LoginModel> source = LiveDataReactiveStreams.fromPublisher(

                api.login(email, password)
                .subscribeOn(Schedulers.io())

        );

        authUser.addSource(source, loginModel -> {

            authUser.setValue(loginModel);
            authUser.removeSource(source);

        });

    }

    public LiveData<LoginModel> observeUser(){

        return authUser;

    }

}
