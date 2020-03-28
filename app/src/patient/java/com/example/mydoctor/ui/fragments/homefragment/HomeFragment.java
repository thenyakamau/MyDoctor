package com.example.mydoctor.ui.fragments.homefragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.mydoctor.R;
import com.example.mydoctor.baseviews.BaseFragment;
import com.example.mydoctor.di.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment {

    private static final String TAG = "HomeFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private HomeFragmentViewModel homeFragmentViewModel;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_home_fragment, container, false);

        ButterKnife.bind(this, view);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Setting up");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        homeFragmentViewModel = ViewModelProviders.of(this, providerFactory). get(HomeFragmentViewModel.class);

        subscribeObservers();

        homeFragmentViewModel.fetchPatientRecords();

    }

    private void subscribeObservers() {

        homeFragmentViewModel.observePatientRecords().removeObservers(getViewLifecycleOwner());
        homeFragmentViewModel.observePatientRecords().observe(getViewLifecycleOwner(), dashBoardModelResource -> {

            if (dashBoardModelResource != null) {

                switch (dashBoardModelResource.status) {

                    case LOADING:
                        progressDialog.show();
                        break;

                    case SUCCESS:
                        if (progressDialog!=null){
                            progressDialog.dismiss();

                        }

                        if (dashBoardModelResource.data != null) {

                            runCharts();

                        }
                        break;


                    case ERROR:
                        if (progressDialog!=null){
                            progressDialog.dismiss();

                        }

                        Log.d(TAG, "subscribeObservers: " + dashBoardModelResource.message);

                }

            }

        });

    }

    private void runCharts() {



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
