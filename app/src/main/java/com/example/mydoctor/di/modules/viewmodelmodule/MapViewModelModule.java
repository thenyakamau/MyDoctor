package com.example.mydoctor.di.modules.viewmodelmodule;

import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.customscopes.ViewModelKey;
import com.example.mydoctor.ui.mapactivity.MapViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MapViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel.class)
    public abstract ViewModel bindMapViewModel (MapViewModel mapViewModel);

}
