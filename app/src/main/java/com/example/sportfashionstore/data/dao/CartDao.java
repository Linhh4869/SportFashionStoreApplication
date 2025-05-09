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
    void insertCartItem(CartEntity cart);

    @Update
    void updateCartItem(CartEntity cart);

    @Delete
    void delete(CartEntity cart);

    @Delete
    void deleteCartItem(CartEntity cartItem);

    @Query("SELECT * FROM carts WHERE isShowCart = 1")
    LiveData<List<CartEntity>> getAllCartItems();

    @Query("DELETE FROM carts WHERE id = :id")
    void deleteCartItemById(long id);

    @Query("SELECT * FROM carts WHERE product_id = :productId AND product_variant_id = :productVariantId")
    CartEntity getCartItemByProductInfo(String productId, String productVariantId);

    @Query("SELECT * FROM carts WHERE id = :id")
    CartEntity getCartItemById(long id);
}
