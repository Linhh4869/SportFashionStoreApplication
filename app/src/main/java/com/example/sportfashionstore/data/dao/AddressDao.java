package com.example.sportfashionstore.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sportfashionstore.data.entity.AddressEntity;

import java.util.List;

@Dao
public interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAddress(AddressEntity address);

    @Update
    void updateAddress(AddressEntity address);

    @Delete
    void deleteAddress(AddressEntity address);

    @Query("DELETE FROM address WHERE id = :id")
    void deleteAddressById(long id);

    @Query("SELECT * FROM address WHERE id = :id")
    AddressEntity getAddressById(long id);

    @Query("SELECT * FROM address WHERE isSelected = 1")
    AddressEntity getSelectedAddress();

    @Query("SELECT * FROM address WHERE isDefaultAddress = 1")
    AddressEntity getDefaultAddress();

    @Query("SELECT * FROM address")
    LiveData<List<AddressEntity>> getAllAddress();

    @Query("SELECT * FROM address")
    List<AddressEntity> getAllAddressList();

    @Query("UPDATE address SET isSelected = 0 WHERE isSelected = 1")
    void updateSelectedRecord();

    @Query("UPDATE address SET isDefaultAddress = 0 WHERE isDefaultAddress = 1")
    void updateDefaultRecord();
}
