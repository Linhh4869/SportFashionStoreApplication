package com.example.sportfashionstore.app;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.sportfashionstore.data.AppDatabase;
import com.example.sportfashionstore.util.SharePrefHelper;

public class MyApplication extends Application {
    private static SharePrefHelper sharePrefHelper;
    private static AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        sharePrefHelper = SharePrefHelper.getInstance(this);
        appDatabase = AppDatabase.getDatabase(this);
        AppContextProvider.initialize(this);
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        if (currentNightMode != AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static SharePrefHelper getSharePrefHelper() {
        return sharePrefHelper;
    }

    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
