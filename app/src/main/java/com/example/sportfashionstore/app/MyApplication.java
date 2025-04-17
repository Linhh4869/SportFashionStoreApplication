package com.example.sportfashionstore.app;

import android.app.Application;

import com.example.sportfashionstore.util.SharePrefHelper;

public class MyApplication extends Application {
    private static SharePrefHelper sharePrefHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        sharePrefHelper = SharePrefHelper.getInstance(this);
        AppContextProvider.initialize(this);
    }

    public static SharePrefHelper getSharePrefHelper() {
        return sharePrefHelper;
    }
}
