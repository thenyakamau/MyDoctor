package com.example.mydoctor.ui.registeractivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mydoctor.R;
import com.example.mydoctor.baseviews.BaseActivity;
import com.example.mydoctor.di.viewmodels.ViewModelProviderFactory;
import com.example.mydoctor.tokenmanager.TokenManager;
import com.example.mydoctor.ui.cameraactivity.CameraActivity;
import com.example.mydoctor.ui.fragments.dialog_fragments.SelectImageDialogFragment;
import com.example.mydoctor.ui.inputlisteners.SelectCameraDialogInputListner;
import com.example.mydoctor.utils.ViewSnackBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements SelectCameraDialogInputListner {

    private static final String TAG = "RegisterActivity";
    private static final int UPLOAD_FROM_GALLERY = 100;
    private static final int UPLOAD_FROM_CAMERA = 200;

    @BindView(R.id.register_name)
    EditText register_name;
    @BindView(R.id.register_email)
    EditText register_email;
    @BindView(R.id.register_number)
    EditText register_number;
    @BindView(R.id.register_password)
    EditText register_password;
    @BindView(R.id.register_confirm_password)
    EditText register_confirm_password;
    @BindView(R.id.register_logo)
    ImageView register_logo;

    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    TokenManager tokenManager;

    private ViewSnackBar viewSnackBar;
    private RegisterUserViewModel registerUserViewModel;
    private Bitmap bitmap;
    private Uri imagePath;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        View myView = findViewById(android.R.id.content);

        registerUserViewModel = ViewModelProviders.of(this, providerFactory).get(RegisterUserViewModel.class);

        viewSnackBar = new ViewSnackBar(myView, this);

        subscribeObservers();
    }

        private void subscribeObservers() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering");
        progressDialog.setMessage("Loading...");

        //correct method of passing viewModel data to the activity
        //Do not pass any context or views to the viewModel treat the activity as a view
        registerUserViewModel.observeNewUser().observe(this, accessTokenModelAuthResource -> {
            if (accessTokenModelAuthResource != null) {

                switch (accessTokenModelAuthResource.status) {

                    case LOADING:
                        progressDialog.show();
                        break;

                    case AUTHENTICATED:
                        progressDialog.dismiss();
                        assert accessTokenModelAuthResource.data != null;
                        if (accessTokenModelAuthResource.data.getTokenType().equals("Bearer")) {

                            tokenManager.saveAcessToken(accessTokenModelAuthResource.data);

                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                                    .setIcon(R.drawable.ic_check_black)
                                    .setTitle("Success")
                                    .setMessage(accessTokenModelAuthResource.message)
                                    .setCancelable(false)
                                    .setPositiveButton("Next", (dialogInterface, i) -> dialogInterface.dismiss());


                            builder.show();

                        }
                        break;


                    case ERROR:
                        progressDialog.dismiss();

                        Log.d(TAG, "subscribeObservers: " + accessTokenModelAuthResource.message);


                       viewSnackBar.viewMySnack(accessTokenModelAuthResource.message, R.color.primarycolor);


                        break;

                    case NOT_AUTHENTICATED:
                        progressDialog.dismiss();
                        break;

                }

            }
        });

    }


    @OnClick(R.id.register_logo)
    void selectDialog(){

        SelectImageDialogFragment selectImageDialogFragment = new SelectImageDialogFragment(this);
        selectImageDialogFragment.show(getSupportFragmentManager(), "SelectImageDialogFragme");

    }

    @Override
    public void cameraInput() {

        if (checkCameraHardware(this)) {

            Intent intent = new Intent(this, CameraActivity.class);
            startActivityForResult(intent, UPLOAD_FROM_CAMERA);


        } else {

            viewSnackBar.viewMySnack("No Capable of taking pictures", R.color.primarycolor);

        }


    }

    @Override
    public void galleryInput() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Logo"), UPLOAD_FROM_GALLERY);

    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            Toast.makeText(context, "You phone seems to have no camera...", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == UPLOAD_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                imagePath = filePath;


                register_logo.setImageDrawable(new BitmapDrawable(bitmap));


            } catch (IOException e) {
                e.printStackTrace();
                viewSnackBar.viewMySnack( "Something went wrong with the image...", R.color.primarycolor);

            }

        }

        if (requestCode == UPLOAD_FROM_CAMERA) {

            Log.d(TAG, "onActivityResult: resultcode okay");

            if (resultCode == RESULT_OK && data != null) {

                String fileName = data.getStringExtra("result");
                Log.d(TAG, "onActivityResult: " + fileName);

                assert fileName != null;
                File imageFile = new File(fileName);
                if (imageFile.exists()) {

                    bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    imagePath = Uri.fromFile(imageFile);

                    Log.d(TAG, "onActivityResult: " + bitmap);


                    register_logo.setImageDrawable(new BitmapDrawable(bitmap));


                }


            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();

            }
        }

    }


    private boolean validate() {
        boolean valid = true;

        String email = register_email.getText().toString();
        String number = register_number.getText().toString();
        String password = register_password.getText().toString();
        String confirmPassword = register_confirm_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            register_email.setError("enter a valid email address");
            valid = false;
        } else {
            register_email.setError(null);
        }

        if (number.isEmpty() || !Patterns.PHONE.matcher(number).matches()) {
            register_number.setError("enter a valid email address");
            valid = false;
        } else {
            register_number.setError(null);
        }


        if (password.length() < 6 || password.length() > 20) {
            register_password.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            register_password.setError(null);
        }

        if (!password.equals(confirmPassword)) {

            register_password.setError("Passwords do not match");
            register_confirm_password.setError("Passwords do not match");
            valid = false;

        } else {

            register_password.setError(null);
            register_confirm_password.setError(null);

        }

        return valid;

    }

    @OnClick(R.id.btn_register)
    void register() {

        if (!validate()) {

            viewSnackBar.viewMySnack("Enter all fields", R.color.primarycolor);
            return;

        }

        String name = register_name.getText().toString();
        String email = register_email.getText().toString().toLowerCase();
        String number = register_number.getText().toString().toLowerCase();
        String password = register_password.getText().toString();
        String confirmPassword = register_confirm_password.getText().toString();

        registerUserViewModel.registration(name, email, number, password, confirmPassword, imagePath);


    }



}
