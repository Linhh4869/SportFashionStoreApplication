package com.example.sportfashionstore.util;

import android.annotation.SuppressLint;
import android.content.Context;

public class Util {
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        String deviceId = "";
        try {
            deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            return deviceId;
        } catch (Exception e) {
            e.printStackTrace();
            return deviceId;
        }
    }
}
