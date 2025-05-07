package com.example.sportfashionstore.util;

import androidx.room.TypeConverter;

import com.google.firebase.Timestamp;

public class TimeStampConverter {

    @TypeConverter
    public static Long fromTimestamp(Timestamp timestamp) {
        return timestamp != null ? timestamp.toDate().getTime() : null;
    }

    @TypeConverter
    public static Timestamp toTimestamp(Long millis) {
        return millis != null ? new Timestamp(new java.util.Date(millis)) : null;
    }
}
