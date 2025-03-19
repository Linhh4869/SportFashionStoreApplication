package com.example.sportfashionstore.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StringUtil {
    public static String format(String number) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###");
        String formattedString = formatter.format(number);
        return formattedString.replace(",", ".");
    }

    public static boolean isNotNullAndEmpty(String string) {
        return string != null && !string.isEmpty();
    }
}
