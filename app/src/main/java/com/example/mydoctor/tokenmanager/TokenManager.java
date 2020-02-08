package com.example.mydoctor.tokenmanager;

import android.content.SharedPreferences;

import com.example.mydoctor.models.AccessTokenModel;

public class TokenManager {

    private static String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String PREF_IS_LOGGED_IN = "isLoggedIn";
    private static final String PREF_VERIFIED = "Verified";


    private SharedPreferences preferences;
    private SharedPreferences.Editor editorPreferences;


    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences preferences) {
        this.preferences = preferences;
        this.editorPreferences = preferences.edit();
    }

    public static synchronized TokenManager getINSTANCE(SharedPreferences preferences){

        if (INSTANCE == null){

            INSTANCE = new TokenManager(preferences);

        }

        return INSTANCE;
    }

    public void saveAcessToken(AccessTokenModel accessTokenModel){

        editorPreferences.putString("ACCESS_TOKEN", accessTokenModel.getAccessToken()).commit();
        editorPreferences.putString("REFRESH_TOKEN", accessTokenModel.getRefreshToken()).commit();

    }

    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch){

        editorPreferences.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editorPreferences.commit();

    }

    public boolean isFirstTimeLaunch() {

        return preferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);

    }

    public void setIsLoggedIn(boolean isLoggedIn){

        editorPreferences.putBoolean(PREF_IS_LOGGED_IN, isLoggedIn).apply();

    }

    public boolean isLoggedIn(){

        return preferences.getBoolean(PREF_IS_LOGGED_IN, false);

    }

    public void setVerificationSatus(int verified){

        editorPreferences.putInt(PREF_VERIFIED, verified);

    }

    public int isVerified(){

        return preferences.getInt(PREF_VERIFIED, 0);

    }

    public void deleteToken(){

        editorPreferences.remove("ACCESS_TOKEN");
        editorPreferences.remove("REFRESH_TOKEN");

    }

    public AccessTokenModel getToken(){

        AccessTokenModel accessTokenModel = new AccessTokenModel();
        accessTokenModel.setAccessToken(preferences.getString("ACCESS_TOKEN", null));
        accessTokenModel.setRefreshToken(preferences.getString("REFRESH_TOKEN", null));


        return accessTokenModel;

    }

}
