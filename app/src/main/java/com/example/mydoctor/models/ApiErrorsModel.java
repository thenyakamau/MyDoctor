package com.example.mydoctor.models;

import java.util.List;
import java.util.Map;

public class ApiErrorsModel {

    String message;
    String error;

    Map<String, List<String>> errors;

    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
