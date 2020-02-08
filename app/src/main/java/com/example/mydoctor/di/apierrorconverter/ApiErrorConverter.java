package com.example.mydoctor.di.apierrorconverter;

import com.example.mydoctor.models.ApiErrorsModel;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

@Singleton
public class ApiErrorConverter {

    private static final String TAG = "ApiErrorConverter";
    private Retrofit retrofit;

    @Inject
    public ApiErrorConverter(Retrofit retrofit) {

        this.retrofit = retrofit;

    }

    public ApiErrorsModel apiErrorsModel(ResponseBody responseBody){

        Converter<ResponseBody, ApiErrorsModel> converter = retrofit.responseBodyConverter(ApiErrorsModel.class, new Annotation[0]);
        ApiErrorsModel apiErrorsModel = null;

        try {
            apiErrorsModel = converter.convert(responseBody);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return apiErrorsModel;
    }

}
