package com.example.mydoctor.di.modules;

import com.example.mydoctor.network.api.API;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    @Provides
    static API provideAPI(Retrofit retrofit){

        return retrofit.create(API.class);

    }

}
