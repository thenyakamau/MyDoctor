package com.example.mydoctor.ui.loginacitivity;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";

    @Inject
    public LoginViewModel() {

        Log.d(TAG, "LoginViewModel: viewModel is working...");
    }
}
