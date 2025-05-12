package com.example.sportfashionstore.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.sportfashionstore.data.dao.CartDao;
import com.example.sportfashionstore.data.entity.CartEntity;
import com.example.sportfashionstore.util.TimeStampConverter;

@Database(
        entities = {CartEntity.class},
        version = 2,
        exportSchema = false
)
@TypeConverters({TimeStampConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "app_database";

    private static AppDatabase INSTANCE;

    public abstract CartDao cartDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
