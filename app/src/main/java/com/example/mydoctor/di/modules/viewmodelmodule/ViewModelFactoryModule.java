package com.example.mydoctor.di.modules.viewmodelmodule;

import androidx.lifecycle.ViewModelProvider;

import com.example.mydoctor.di.viewmodels.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    //JUST AN EFFICIENT WAY OF PROVIDING AM INJECTION WITHOUT A METHOD

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);

}
