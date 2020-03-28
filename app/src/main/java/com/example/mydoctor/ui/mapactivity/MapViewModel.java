package com.example.mydoctor.ui.mapactivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.apierrorconverter.ApiErrorConverter;
import com.example.mydoctor.models.ApiErrorsModel;
import com.example.mydoctor.models.ApiSuccessModel;
import com.example.mydoctor.models.DashBoardModel;
import com.example.mydoctor.network.api.API;
import com.example.mydoctor.network.resource.Resource;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class MapViewModel extends ViewModel {

    private API api;
    private ApiErrorConverter apiErrorConverter;
    private ApiErrorsModel apiErrorsModel;
    private MediatorLiveData<Resource<ApiSuccessModel>> userLocation = new MediatorLiveData<>();

    @Inject
    MapViewModel(API api, ApiErrorConverter apiErrorConverter) {
        this.api = api;
        this.apiErrorConverter = apiErrorConverter;
    }

    private void setUserLocation(LiveData<Resource<ApiSuccessModel>>source) {
        userLocation.setValue(Resource.loading((ApiSuccessModel)null));
        userLocation.addSource(source, dashBoardModelResource -> {

            userLocation.setValue(dashBoardModelResource);
            userLocation.removeSource(source);

        });
    }

    private LiveData<Resource<ApiSuccessModel>> addUserLocation(double latitude, double longitude) {

        return LiveDataReactiveStreams.fromPublisher(
                api.addMuserLocation(latitude, longitude)
                        .onErrorReturn(throwable -> {
                            ApiSuccessModel errorApiSuccess = new ApiSuccessModel();
                            errorApiSuccess.setError_message("-1");

                            try {
                                HttpException error = (HttpException) throwable;

                                apiErrorsModel = apiErrorConverter.apiErrorsModel(error.response().errorBody());
                                String errors = apiErrorsModel.getError();

                                errorApiSuccess.setError_message(errors);

                            }catch (Exception e){

                                e.printStackTrace();
                                errorApiSuccess.setError_message("Check your internet connection...");

                            }
                            return errorApiSuccess;
                        })

                        .map(apiSuccessModel -> {
                            if (apiSuccessModel.getError_message().equals("-1")){

                                return Resource.error(apiSuccessModel.getError_message(), (ApiSuccessModel)null);
                            }
                            return Resource.success(apiSuccessModel);
                        })
                        .subscribeOn(Schedulers.io())
        );

    }

    LiveData<Resource<ApiSuccessModel>> observeUserLocation(){
        return userLocation;
    }

    void saveUserLocation(double latitude, double longitude){

        setUserLocation(addUserLocation(latitude, longitude));

    }



}
