package com.example.sportfashionstore.repository;

import androidx.lifecycle.LiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.data.AppDatabase;
import com.example.sportfashionstore.data.dao.CartDao;
import com.example.sportfashionstore.data.entity.CartEntity;
import com.google.firebase.Timestamp;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {
    private final CartDao cartDao;
    private final LiveData<List<CartEntity>> allCartItems;
    private final ExecutorService executorService;

    public CartRepository() {
        AppDatabase database = MyApplication.getAppDatabase();
        cartDao = database.cartDao();
        allCartItems = cartDao.getAllCartItems();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CartEntity>> getAllCartItems() {
        return allCartItems;
    }

    public long insertCartItem(final CartEntity cartItem) {
        try {
            return executorService.submit(() -> {
                // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
                CartEntity existingItem = cartDao.getCartItemByProductInfo(
                        cartItem.getProductId(),
                        cartItem.getProductVariantId()
                );

                if (existingItem != null && cartItem.isShowCart() == 1) {
                    // Nếu đã tồn tại, cập nhật số lượng
                    CartEntity updatedItem = existingItem.copy();
                    updatedItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
                    updatedItem.setUpdatedAt(Timestamp.now());
                    cartDao.updateCartItem(updatedItem);
                    return existingItem.getId();  // Trả về ID của sản phẩm đã tồn tại
                } else {
                    // Nếu chưa tồn tại, thêm mới
                    return cartDao.insertCartItem(cartItem);  // Trả về ID của sản phẩm mới
                }
            }).get();  // Đợi kết quả từ executorService

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public interface CartItemCallback {
        void onCartItemLoaded(CartEntity cartItem);
    }

    public void getCartItemById(final long id, final CartItemCallback callback) {
        executorService.execute(() -> {
            CartEntity cartItem = cartDao.getCartItemById(id);
            callback.onCartItemLoaded(cartItem);
        });
    }

    public void updateCartItem(final CartEntity cartItem) {
        executorService.execute(() -> {
            CartEntity updatedItem = cartItem.copy();
            updatedItem.setUpdatedAt(Timestamp.now());
            cartDao.updateCartItem(updatedItem);
        });
    }

    public void deleteCartItem(final CartEntity cartItem) {
        executorService.execute(() -> cartDao.deleteCartItem(cartItem));
    }

    public void deleteCartItemById(final long id) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cartDao.deleteCartItemById(id);
            }
        });
    }
}
