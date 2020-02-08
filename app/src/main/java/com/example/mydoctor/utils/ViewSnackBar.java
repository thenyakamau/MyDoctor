package com.example.mydoctor.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.mydoctor.R;
import com.google.android.material.snackbar.Snackbar;

public class ViewSnackBar {

    private View view;
    private Context context;

    public ViewSnackBar(View view, Context context) {
        this.view = view;
        this.context = context;

    }

    public void viewMySnack(String message, int color){

        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(context, color));
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        snackbar.show();

    }

}
