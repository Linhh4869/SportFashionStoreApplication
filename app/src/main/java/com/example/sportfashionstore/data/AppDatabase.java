package com.example.sportfashionstore.data;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.sportfashionstore.data.dao.AddressDao;
import com.example.sportfashionstore.data.dao.CartDao;
import com.example.sportfashionstore.data.entity.AddressEntity;
import com.example.sportfashionstore.data.entity.CartEntity;
import com.example.sportfashionstore.util.TimeStampConverter;

@Database(
        entities = {CartEntity.class, AddressEntity.class},
        version = 2,
        exportSchema = false
)
@TypeConverters({TimeStampConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract CartDao cartDao();
    public abstract AddressDao addressDao();
}
