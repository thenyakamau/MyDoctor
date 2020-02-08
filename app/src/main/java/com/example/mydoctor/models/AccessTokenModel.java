
package com.example.mydoctor.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccessTokenModel implements Serializable, Parcelable
{

    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("error_code")
    @Expose
    private String error_code;
    @SerializedName("error_message")
    @Expose
    private String error_message;

    public final static Creator<AccessTokenModel> CREATOR = new Creator<AccessTokenModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AccessTokenModel createFromParcel(Parcel in) {
            return new AccessTokenModel(in);
        }

        public AccessTokenModel[] newArray(int size) {
            return (new AccessTokenModel[size]);
        }

    }
    ;
    private final static long serialVersionUID = 1385919991147658906L;

    protected AccessTokenModel(Parcel in) {
        this.tokenType = ((String) in.readValue((String.class.getClassLoader())));
        this.expiresIn = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.accessToken = ((String) in.readValue((String.class.getClassLoader())));
        this.refreshToken = ((String) in.readValue((String.class.getClassLoader())));
        this.error_code = ((String) in.readValue((String.class.getClassLoader())));
        this.error_message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AccessTokenModel() {
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(tokenType);
        dest.writeValue(expiresIn);
        dest.writeValue(accessToken);
        dest.writeValue(refreshToken);
    }

    public int describeContents() {
        return  0;
    }

}
