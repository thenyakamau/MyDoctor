package com.example.mydoctor.di.modules.activitybuildersmodule;

import com.example.mydoctor.ui.loginacitivity.LoginActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract LoginActivity contributeLoginActivity();

}
