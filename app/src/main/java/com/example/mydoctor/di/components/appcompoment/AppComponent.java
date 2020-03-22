package com.example.mydoctor.di.components.appcompoment;

import android.app.Application;

import com.example.mydoctor.di.MyDoctorApplication;
import com.example.mydoctor.di.apierrorconverter.ApiErrorConverter;
import com.example.mydoctor.di.createpartfromstring.CreatePartFromString;
import com.example.mydoctor.di.firebaseservice.FireBaseService;
import com.example.mydoctor.di.modules.activitybuildersmodule.ActivityBuildersModule;
import com.example.mydoctor.di.modules.appmodule.AppModule;
import com.example.mydoctor.di.modules.viewmodelmodule.ViewModelFactoryModule;
import com.example.mydoctor.di.sessionmanager.SessionManager;

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
                ViewModelFactoryModule.class,
        }
)
public interface AppComponent extends AndroidInjector<MyDoctorApplication> {
    
    SessionManager sessionManager();
    FireBaseService fireBaseService();
    ApiErrorConverter apiErrorConverter();
    CreatePartFromString createPartFromString();

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();

    }

}
