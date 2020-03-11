package com.example.mydoctor.di.modules.activitybuildersmodule.fragmentbuildermodules;

import com.example.mydoctor.ui.fragments.chatfragment.ChatFragment;
import com.example.mydoctor.ui.fragments.homefragment.HomeFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DashBoardFragmentsBuilderModule {

    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract ChatFragment contributeChatFragment();

}
