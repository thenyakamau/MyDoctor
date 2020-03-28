package com.example.mydoctor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiSuccessModel {

    @Expose
    @SerializedName("message")
    String message;
    @SerializedName("error_message")
    @Expose
    private String error_message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
