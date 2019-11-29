package com.example.mydoctor.di.components.appcompoment;

import android.app.Application;

import com.example.mydoctor.di.MyDoctorApplication;
import com.example.mydoctor.di.modules.activitybuildersmodule.ActivityBuildersModule;
import com.example.mydoctor.di.modules.appmodule.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                AppModule.class,
        }
)
public interface AppComponent extends AndroidInjector<MyDoctorApplication> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();

    }

}
