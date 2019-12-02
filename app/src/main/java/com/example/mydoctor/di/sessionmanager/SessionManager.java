package com.example.mydoctor.di.sessionmanager;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.mydoctor.models.LoginModel;
import com.example.mydoctor.network.resource.AuthResource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {

    private static final String TAG = "SessionManager";
    private MediatorLiveData<AuthResource<LoginModel>> cachedUser = new MediatorLiveData<>();

    @Inject
    SessionManager() {
    }

    public void autheticateUser(final LiveData<AuthResource<LoginModel>> source){

        if (cachedUser != null){

            cachedUser.setValue(AuthResource.loading((LoginModel)null));
            cachedUser.addSource(source, loginModelAuthResource -> {

                cachedUser.setValue(loginModelAuthResource);
                cachedUser.removeSource(source);

            });
        }

    }
    public void logOut(){

        Log.d(TAG, "logOut: logging out");
        cachedUser.setValue(AuthResource.<LoginModel>logout());

    }

    public LiveData<AuthResource<LoginModel>> getAuthUser(){

        return cachedUser;

    }

}
