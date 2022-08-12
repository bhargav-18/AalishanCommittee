package com.example.aalishancommittee;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends Application
{
    public static final String APPLICATION_ID = "F36E41D1-988F-7CBE-FFA4-AD0768896800";
    public static final String API_KEY = "076C8BDD-12EA-41B7-9B78-37793EFA44D2";
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
