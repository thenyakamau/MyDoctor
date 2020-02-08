package com.example.mydoctor.di;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.mydoctor.di.components.appcompoment.DaggerAppComponent;
import com.example.mydoctor.tokenmanager.TokenManager;
import com.facebook.stetho.Stetho;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class MyDoctorApplication extends DaggerApplication {

    public static Context context;
    public static MyDoctorApplication instance;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {

        return DaggerAppComponent.builder().application(this).build();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        instance = this;

        Stetho.initializeWithDefaults(this);
    }

    public static MyDoctorApplication getMyDoctorApplication(Context context){
        return (MyDoctorApplication) context.getApplicationContext();
    }

    public static synchronized MyDoctorApplication getInstance(){
        return instance;
    }
    public static Context getContext(){
        return context;
    }


    public static boolean hasNetwork(){

        return instance.isConnected();

    }

    public boolean isConnected(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

    }

    public TokenManager tokenManager(){

        return TokenManager.getINSTANCE(getSharedPreferences("prefs",MODE_PRIVATE));

    }

}
