package com.example.sportfashionstore.di;

import android.content.Context;

import androidx.room.Room;

import com.example.sportfashionstore.data.AppDatabase;
import com.example.sportfashionstore.data.dao.AddressDao;
import com.example.sportfashionstore.data.dao.CartDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "app_database")
                .fallbackToDestructiveMigration(false)
                .build();
    }

    @Provides
    public CartDao provideCartDao(AppDatabase database) {
        return database.cartDao();
    }

    @Provides
    public AddressDao provideAddressDao(AppDatabase database) {
        return database.addressDao();
    }
}
