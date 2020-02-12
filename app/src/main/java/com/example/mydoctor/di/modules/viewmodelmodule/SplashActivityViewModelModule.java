package com.example.mydoctor.di.modules.viewmodelmodule;

import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.customscopes.ViewModelKey;
import com.example.mydoctor.ui.splashactivity.SplashActivityViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SplashActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashActivityViewModel.class)
    public abstract ViewModel bindSplashActivityViewModel(SplashActivityViewModel splashActivityViewModel);

}
