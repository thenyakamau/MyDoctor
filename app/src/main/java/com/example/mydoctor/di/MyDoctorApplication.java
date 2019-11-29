package com.example.mydoctor.di;

import com.example.mydoctor.di.components.appcompoment.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class MyDoctorApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {

        return DaggerAppComponent.builder().application(this).build();

    }

}
