package com.example.sportfashionstore.app;

import android.app.Application;
import android.content.Context;

public class AppContextProvider {
    private static Application application;

    public static void initialize(Application app) {
        application = app;
    }

    public static Context getContext() {
        if (application == null) {
            throw new IllegalStateException("AppContextProvider must be initialized in Application class");
        }
        return application.getApplicationContext();
    }
}
