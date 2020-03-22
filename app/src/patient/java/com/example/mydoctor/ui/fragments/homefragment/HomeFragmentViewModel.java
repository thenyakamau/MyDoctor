package com.example.mydoctor.ui.fragments.homefragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mydoctor.di.apierrorconverter.ApiErrorConverter;
import com.example.mydoctor.models.ApiErrorsModel;
import com.example.mydoctor.models.DashBoardModel;
import com.example.mydoctor.network.api.API;
import com.example.mydoctor.network.resource.Resource;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class HomeFragmentViewModel extends ViewModel {

    private API api;
    private ApiErrorConverter apiErrorConverter;
    private MediatorLiveData<Resource<DashBoardModel>> patientRecords = new MediatorLiveData<>();
    private ApiErrorsModel apiErrorsModel;

    @Inject
    HomeFragmentViewModel(API api, ApiErrorConverter apiErrorConverter) {
        this.api = api;
        this.apiErrorConverter = apiErrorConverter;
    }


    private void fetchPatientRecords(LiveData<Resource<DashBoardModel>>source){

        patientRecords.setValue(Resource.loading((DashBoardModel)null));
        patientRecords.addSource(source, dashBoardModelResource -> {

            patientRecords.setValue(dashBoardModelResource);
            patientRecords.removeSource(source);

        });

    }

    private LiveData<Resource<DashBoardModel>> mPatientRecords() {

        return LiveDataReactiveStreams.fromPublisher(
                api.fetchMyPatientRecord()
                        .onErrorReturn(throwable -> {
                            DashBoardModel errorDashBoard = new DashBoardModel();
                            errorDashBoard.setCountHealthRate(null);

                            try {
                                HttpException error = (HttpException) throwable;

                                apiErrorsModel = apiErrorConverter.apiErrorsModel(error.response().errorBody());
                                String errors = apiErrorsModel.getError();

                                errorDashBoard.setError_message(errors);

                            }catch (Exception e){

                                e.printStackTrace();
                                errorDashBoard.setError_message("Check your internet connection...");

                            }
                            return errorDashBoard;
                        })

                        .map(policiesModel -> {
                            if (policiesModel.getCountHealthRate() == null){

                                return Resource.error(policiesModel.getError_message(), (DashBoardModel)null);
                            }
                            return Resource.success(policiesModel);
                        })
                        .subscribeOn(Schedulers.io())
        );

    }

    LiveData<Resource<DashBoardModel>> observePatientRecords(){
        return patientRecords;
    }

    void fetchPatientRecords(){

        fetchPatientRecords(mPatientRecords());

    }



}
