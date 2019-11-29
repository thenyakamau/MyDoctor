package com.example.mydoctor.di.modules.viewmodelmodule;

import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.customscopes.ViewModelKey;
import com.example.mydoctor.ui.loginacitivity.LoginViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class LoginViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    public abstract ViewModel bindLoginViewModel (LoginViewModel loginViewModel);

}
