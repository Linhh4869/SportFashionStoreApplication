package com.example.sportfashionstore.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Helper {
    private static final String NUMBER_FORMAT = "#,###,###";
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

    public static String formatPrice(int price) {
        try {
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
            otherSymbols.setGroupingSeparator('.');
            return new DecimalFormat(NUMBER_FORMAT).format(price).replace(",", ".");
        } catch (Exception ex) {
            return String.valueOf(price);
        }
    }

    public static void showMyToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
