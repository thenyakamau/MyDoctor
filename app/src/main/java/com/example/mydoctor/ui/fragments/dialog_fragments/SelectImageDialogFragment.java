package com.example.mydoctor.ui.fragments.dialog_fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mydoctor.R;
import com.example.mydoctor.ui.inputlisteners.SelectCameraDialogInputListner;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectImageDialogFragment extends DialogFragment {

    private static final String TAG = "SelectImageDialogFragme";
    private SelectCameraDialogInputListner selectCameraDialogInputListner;

    public SelectImageDialogFragment(SelectCameraDialogInputListner selectCameraDialogInputListner) {
        this.selectCameraDialogInputListner = selectCameraDialogInputListner;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_item_select_image, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            selectCameraDialogInputListner =(SelectCameraDialogInputListner) getActivity();
        }catch (ClassCastException e){

            Log.d(TAG, "onAttach: "+e.getMessage());

        }


    }

    @OnClick(R.id.dialog_item_select_camera)
    void selectCamera(){

        selectCameraDialogInputListner.cameraInput();
        Objects.requireNonNull(getDialog()).dismiss();

    }

    @OnClick(R.id.dialog_item_select_gallery)
    void selectGallery(){

        selectCameraDialogInputListner.galleryInput();
        Objects.requireNonNull(getDialog()).dismiss();

    }

}
