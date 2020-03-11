package com.example.mydoctor.ui.fragments.homefragment;

import android.os.Bundle;
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

    @Inject
    ViewModelProviderFactory providerFactory;

    private HomeFragmentViewModel homeFragmentViewModel;

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

        homeFragmentViewModel = ViewModelProviders.of(this, providerFactory). get(HomeFragmentViewModel.class);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
