package com.example.sportfashionstore.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sportfashionstore.data.entity.CartEntity;

import java.util.List;

@Dao
public interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCartItem(CartEntity cart);

    @Update
    void updateCartItem(CartEntity cart);

    @Delete
    void delete(CartEntity cart);

    @Delete
    void deleteCartItem(CartEntity cartItem);

    @Query("SELECT * FROM carts WHERE isShowCart = 1")
    LiveData<List<CartEntity>> getAllCartItems();

    @Query("DELETE FROM carts WHERE isShowCart = 0")
    void deleteItemNotCart();

    @Query("SELECT * FROM carts WHERE product_variant_id = :productVariantId AND color = :color AND size = :size")
    CartEntity getCartItemByProductInfo(String productVariantId, String color, String size);

    @Query("SELECT * FROM carts WHERE id = :id")
    CartEntity getCartItemById(long id);
}
