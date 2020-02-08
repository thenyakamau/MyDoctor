package com.example.mydoctor.ui.registeractivity;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.apierrorconverter.ApiErrorConverter;
import com.example.mydoctor.di.createpartfromstring.CreatePartFromString;
import com.example.mydoctor.models.AccessTokenModel;
import com.example.mydoctor.models.ApiErrorsModel;
import com.example.mydoctor.network.api.API;
import com.example.mydoctor.network.resource.AuthResource;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;

public class RegisterUserViewModel extends ViewModel {

    private static final String TAG = "RegisterUserViewModel";

    private MediatorLiveData<AuthResource<AccessTokenModel>> newUser = new MediatorLiveData<>();

    private API api;
    private ApiErrorConverter apiErrorConverter;
    private ApiErrorsModel apiErrorsModel;
    private CreatePartFromString createPartFromString;

    @Inject
    RegisterUserViewModel(API api, ApiErrorConverter apiErrorConverter, CreatePartFromString createPartFromString) {
        this.api = api;
        this.apiErrorConverter = apiErrorConverter;
        this.createPartFromString = createPartFromString;

    }


    private void addUser(final LiveData<AuthResource<AccessTokenModel>> source){

        newUser.setValue(AuthResource.loading((AccessTokenModel)null));
        newUser.addSource(source, accessTokenModelAuthResource -> {

            newUser.setValue(accessTokenModelAuthResource);
            newUser.removeSource(source);

        });


    }

    private LiveData<AuthResource<AccessTokenModel>> registerUser(RequestBody name, RequestBody email, RequestBody number, RequestBody password, RequestBody confirm_password, MultipartBody.Part image){

        return LiveDataReactiveStreams.fromPublisher(


                api.registerUser(name, email, number,password, confirm_password, image)
                        .onErrorReturn(throwable -> {

                            AccessTokenModel errorAccessTokenModel = new AccessTokenModel();
                            errorAccessTokenModel.setAccessToken("-1");
                            try {

                                HttpException error = (HttpException) throwable;


                                apiErrorsModel = apiErrorConverter.apiErrorsModel(error.response().errorBody());



                                for (Map.Entry<String, List<String>> errors : apiErrorsModel.getErrors().entrySet()) {


                                    if (errors.getKey().equals("name")) {

                                        String dname = errors.getValue().get(0);
                                        errorAccessTokenModel.setError_message(dname);
                                    }
                                    if (errors.getKey().equals("email")) {

                                        errorAccessTokenModel.setError_message("Email already exists...");

                                    }

                                    if (errors.getKey().equals("password")) {

                                        String dname = errors.getValue().get(0);
                                        errorAccessTokenModel.setError_message(dname);

                                    }

                                }

                            }catch (Exception e){

                                e.printStackTrace();
                                errorAccessTokenModel.setError_message("Check your internet connection...");
                            }



                            return errorAccessTokenModel;
                        })

                        .map(accessTokenModel -> {
                            if (accessTokenModel.getAccessToken().equals("-1")){

                                return AuthResource.error(accessTokenModel.getError_message(), (AccessTokenModel)null);
                            }


                            return AuthResource.authenticated(accessTokenModel);
                        })
                        .subscribeOn(Schedulers.io())

        );

    }

    void registration(String name, String email, String number, String password, String confirm_password, Uri imagePath){


        MultipartBody.Part image = createPartFromString.createImage("image", imagePath);
        RequestBody mname = createPartFromString.createPartFromString(name);
        RequestBody memail = createPartFromString.createPartFromString(email);
        RequestBody mnumber = createPartFromString.createPartFromString(number);
        RequestBody mpassword = createPartFromString.createPartFromString(password);
        RequestBody mconfirm_password = createPartFromString.createPartFromString(confirm_password);

        Log.d(TAG, "registration: attempting registration");
        addUser(registerUser(mname, memail, mnumber, mpassword, mconfirm_password, image));

    }

    LiveData<AuthResource<AccessTokenModel>> observeNewUser(){

        return newUser;

    }

}
