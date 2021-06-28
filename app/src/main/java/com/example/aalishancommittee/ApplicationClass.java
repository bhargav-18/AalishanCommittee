package com.example.aalishancommittee;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends Application
{
    public static final String APPLICATION_ID = "C893C06A-3D61-2D7F-FF3A-9121DB132900";
    public static final String API_KEY = "256CC407-3944-45B4-AA42-CDC9D995506D";
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
