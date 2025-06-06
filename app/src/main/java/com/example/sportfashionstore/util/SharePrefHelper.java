package com.example.sportfashionstore.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefHelper {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ROLE = "role";
    private static final String KEY_CATEGORY = "category";

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

    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public void setAddress(String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }

    public void setRole(String role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public void setCategory(String category) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CATEGORY, category);
        editor.apply();
    }

    public String getAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, "");
    }

    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, "");
    }

    public String getCategory() {
        return sharedPreferences.getString(KEY_CATEGORY, "");
    }


    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
