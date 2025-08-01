package com.example.sportfashionstore.app;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppContextProvider.initialize(this);
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        if (currentNightMode != AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
