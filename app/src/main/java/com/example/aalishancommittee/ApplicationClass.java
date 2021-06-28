package com.example.aalishancommittee;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends Application
{
    public static final String APPLICATION_ID = "<Application ID>";
    public static final String API_KEY = "<API Key>";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static List<Flats> flats;
    public static List<Rules> rules;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.initApp( this, APPLICATION_ID, API_KEY );

        Backendless.setUrl( SERVER_URL );

    }
}
