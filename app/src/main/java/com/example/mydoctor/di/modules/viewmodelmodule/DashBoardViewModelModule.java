package com.example.mydoctor.di.modules.viewmodelmodule;

import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.customscopes.ViewModelKey;
import com.example.mydoctor.ui.dashboardactivity.DashBoardViewModel;
import com.example.mydoctor.ui.fragments.chatfragment.ChatFragmentViewModel;
import com.example.mydoctor.ui.fragments.homefragment.HomeFragmentViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class DashBoardViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DashBoardViewModel.class)
    public abstract ViewModel bindDashBoardViewModel (DashBoardViewModel dashBoardViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentViewModel.class)
    public abstract ViewModel bindHomeFragmentViewModel (HomeFragmentViewModel homeFragmentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatFragmentViewModel.class)
    public abstract ViewModel bindChatFragmentViewModel (ChatFragmentViewModel chatFragmentViewModel);

}
