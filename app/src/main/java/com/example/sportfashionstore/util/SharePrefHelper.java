package com.example.sportfashionstore.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefHelper {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_ADDRESS = "address";

    private static SharePrefHelper instance;
    private final SharedPreferences sharedPreferences;

    public static synchronized SharePrefHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharePrefHelper(context.getApplicationContext());
        }
        return instance;
    }

    public SharePrefHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public void setAddress(String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }

    public String getAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, "");
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
