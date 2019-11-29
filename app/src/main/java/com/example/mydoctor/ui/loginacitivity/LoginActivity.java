package com.example.mydoctor.ui.loginacitivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.RequestManager;
import com.example.mydoctor.R;
import com.example.mydoctor.di.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class LoginActivity extends DaggerAppCompatActivity {

    @BindView(R.id.image_login)
    ImageView image_login;

    @Inject
    Drawable logo;
    @Inject
    RequestManager requestManager;
    @Inject
    ViewModelProviderFactory providerFactory;

    private  LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        setLogo();

        loginViewModel = ViewModelProviders.of(this, providerFactory).get(LoginViewModel.class);

    }

    public void setLogo(){

        requestManager
                .load(logo)
                .into(image_login);

    }

}
