package com.example.mydoctor.ui.fragments.chatfragment;

import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.apierrorconverter.ApiErrorConverter;
import com.example.mydoctor.network.api.API;

import javax.inject.Inject;

public class ChatFragmentViewModel extends ViewModel {

    private API api;
    private ApiErrorConverter apiErrorConverter;

    @Inject
    public ChatFragmentViewModel(API api, ApiErrorConverter apiErrorConverter) {
        this.api = api;
        this.apiErrorConverter = apiErrorConverter;
    }
}
