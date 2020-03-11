package com.example.mydoctor.ui.loginacitivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.RequestManager;
import com.example.mydoctor.BuildConfig;
import com.example.mydoctor.R;
import com.example.mydoctor.baseviews.BaseActivity;
import com.example.mydoctor.di.viewmodels.ViewModelProviderFactory;
import com.example.mydoctor.tokenmanager.TokenManager;
import com.example.mydoctor.ui.registeractivity.RegisterActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

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
    @Inject
    TokenManager tokenManager;

    private  LoginViewModel loginViewModel;
    private ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Loading...");

        //correct method of passing viewModel data to the activity
        //Do not pass any context or views to the viewModel treat the activity as a view
        loginViewModel.observeAuthUser().observe(this, loginModelAuthResource -> {

            if (loginModelAuthResource!=null){

                switch (loginModelAuthResource.status){

                    case LOADING:
                        progressDialog.show();
                        break;

                    case AUTHENTICATED:
                        progressDialog.dismiss();
                        if (loginModelAuthResource.data != null) {

                            tokenManager.saveAcessToken(loginModelAuthResource.data);

                            if (BuildConfig.FLAVOR.equals("patient")) {

                                try {
                                    Intent intent = new Intent(LoginActivity.this, Class.forName("com.example.mydoctor.ui.dashboardactivity.DashBoardActivity"));
                                    startActivity(intent);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        break;


                    case ERROR:
                        progressDialog.dismiss();

                        Log.d(TAG, "subscribeObservers: "+loginModelAuthResource.message);

                        break;

                    case NOT_AUTHENTICATED:
                        progressDialog.dismiss();
                        break;

                }

            }

        });

    }

    public void setLogo(){

        // It is to ease the use for glide by making it a dagger instance
        //this is a temporary display
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
