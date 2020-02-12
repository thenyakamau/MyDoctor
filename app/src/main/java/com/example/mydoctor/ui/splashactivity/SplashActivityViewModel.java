package com.example.mydoctor.ui.splashactivity;

import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.apierrorconverter.ApiErrorConverter;
import com.example.mydoctor.network.api.API;

import javax.inject.Inject;

public class SplashActivityViewModel extends ViewModel {

    private API api;
    private ApiErrorConverter apiErrorConverter;

    @Inject
    public SplashActivityViewModel(API api, ApiErrorConverter apiErrorConverter) {
        this.api = api;
        this.apiErrorConverter = apiErrorConverter;
    }
}
