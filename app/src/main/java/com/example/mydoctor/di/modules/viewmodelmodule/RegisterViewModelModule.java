package com.example.mydoctor.di.modules.viewmodelmodule;

import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.modules.customscopes.ViewModelKey;
import com.example.mydoctor.ui.registeractivity.RegisterUserViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RegisterViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RegisterUserViewModel.class)
    public abstract ViewModel bindRegisterUserViewModel(RegisterUserViewModel registerUserViewModel);

}
