package com.example.mydoctor.ui.loginacitivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.RequestManager;
import com.example.mydoctor.R;
import com.example.mydoctor.di.viewmodels.ViewModelProviderFactory;
import com.example.mydoctor.models.LoginModel;
import com.example.mydoctor.ui.registeractivity.RegisterActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class LoginActivity extends DaggerAppCompatActivity {

    private static final String TAG = "LoginActivity";

    //binds instance helps with avoiding repetitive code and keeps code clean
    @BindView(R.id.image_login)
    ImageView image_login;
    @BindView(R.id.input_email)
    EditText input_email;
    @BindView(R.id.input_password)
    EditText input_password;

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

        subscribeObservers();

    }

    private void subscribeObservers(){

        loginViewModel.observeUser().observe(this, loginModel -> {

            if (loginModel!=null){

                Log.d(TAG, "subscribeObservers: "+ loginModel.getLogin().get(0).getEmail());

            }

        });

    }

    public void setLogo(){

        // It is to ease the use for glide by making it a dagger instance
        requestManager
                .load(logo)
                .into(image_login);

    }

    //binds instance helps with avoiding repetitive code and keeps code clean
    @OnClick(R.id.btn_login)
    void login(){

        if (!validate()){
            Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = input_email.getText().toString();
        String password = input_password.getText().toString();

        loginViewModel.authenticateUser(email, password);

    }

    @OnClick(R.id.link_register)
    void register(){

        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

    }


    private boolean validate() {
        boolean valid = true;

        String email = input_email.getText().toString();
        String password = input_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("enter a valid email address");
            valid = false;
        } else {
            input_email.setError(null);
        }

        if ( password.length() < 3 || password.length() > 20) {
            input_password.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            input_password.setError(null);
        }

        return valid;

    }

}
