package com.example.sportfashionstore.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportfashionstore.R;

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

    public static String formatPriceNumber(long price) {
        try {
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
            otherSymbols.setGroupingSeparator('.');
            return new DecimalFormat(NUMBER_FORMAT).format(price).replace(",", ".");
        } catch (Exception ex) {
            return String.valueOf(price);
        }
    }

    public static void showMyToast(Activity activity, String content) {
        if (activity == null || content == null || content.isEmpty()) {
            return;
        }
        try {
            LayoutInflater inflater = activity.getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast_full_content, null);

            TextView textView = layout.findViewById(R.id.textView);
            textView.setText(content);

            android.widget.Toast toast = new android.widget.Toast(activity);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (Exception ex) {
            // Do nothing
        }
    }
}
