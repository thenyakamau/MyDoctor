package com.example.mydoctor.di.modules.activitybuildersmodule;

import com.example.mydoctor.di.modules.ApiModule;
import com.example.mydoctor.di.modules.viewmodelmodule.LoginViewModelModule;
import com.example.mydoctor.di.modules.viewmodelmodule.RegisterViewModelModule;
import com.example.mydoctor.ui.loginacitivity.LoginActivity;
import com.example.mydoctor.ui.registeractivity.RegisterActivity;
import com.example.mydoctor.ui.registeractivity.RegisterUserViewModel;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
            modules = {
                    LoginViewModelModule.class,
                    ApiModule.class,
            }
    )
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector(
            modules = {
                    RegisterViewModelModule.class,
                    ApiModule.class,
            }
    )
    abstract RegisterActivity contributeRegisterActivity();

}
