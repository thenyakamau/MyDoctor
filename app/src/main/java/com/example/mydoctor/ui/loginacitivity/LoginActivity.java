package com.example.mydoctor.ui.loginacitivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.example.mydoctor.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        setLogo();

    }

    public void setLogo(){

        requestManager
                .load(logo)
                .into(image_login);

    }

}
