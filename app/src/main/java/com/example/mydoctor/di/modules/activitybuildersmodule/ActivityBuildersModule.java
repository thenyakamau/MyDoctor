package com.example.mydoctor.di.modules.activitybuildersmodule;

import com.example.mydoctor.di.modules.ApiModule;
import com.example.mydoctor.di.modules.activitybuildersmodule.fragmentbuildermodules.DashBoardFragmentsBuilderModule;
import com.example.mydoctor.di.modules.apimodule.AuthApiModule;
import com.example.mydoctor.di.modules.viewmodelmodule.DashBoardViewModelModule;
import com.example.mydoctor.di.modules.viewmodelmodule.LoginViewModelModule;
import com.example.mydoctor.di.modules.viewmodelmodule.MapViewModelModule;
import com.example.mydoctor.di.modules.viewmodelmodule.RegisterViewModelModule;
import com.example.mydoctor.di.modules.viewmodelmodule.SplashActivityViewModelModule;
import com.example.mydoctor.ui.dashboardactivity.DashBoardActivity;
import com.example.mydoctor.ui.loginacitivity.LoginActivity;
import com.example.mydoctor.ui.mapactivity.MapActivity;
import com.example.mydoctor.ui.registeractivity.RegisterActivity;
import com.example.mydoctor.ui.splashactivity.SplashActivity;

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

    @ContributesAndroidInjector(
            modules = {
                    SplashActivityViewModelModule.class,
                    ApiModule.class,
            }
    )
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector(
            modules = {
                    DashBoardViewModelModule.class,
                    AuthApiModule.class,
                    DashBoardFragmentsBuilderModule.class
            }
    )
    abstract DashBoardActivity contributeDashBoardActivity();

    @ContributesAndroidInjector(
            modules = {
                    MapViewModelModule.class,
                    AuthApiModule.class,
            }
    )
    abstract MapActivity contributeMapActivity();


}
