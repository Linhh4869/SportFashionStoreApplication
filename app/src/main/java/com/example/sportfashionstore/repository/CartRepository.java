package com.example.sportfashionstore.repository;

import androidx.lifecycle.LiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.data.AppDatabase;
import com.example.sportfashionstore.data.dao.CartDao;
import com.example.sportfashionstore.data.entity.CartEntity;
import com.example.sportfashionstore.model.Order;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.util.Constants;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {
    private final FirebaseFirestore db;
    private final CartDao cartDao;
    private final LiveData<List<CartEntity>> allCartItems;
    private final ExecutorService executorService;
    private final FirebaseAuth firebaseAuth;

    public CartRepository() {
        this.db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        AppDatabase database = MyApplication.getAppDatabase();
        cartDao = database.cartDao();
        allCartItems = cartDao.getAllCartItems();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CartEntity>> getAllCartItems() {
        return allCartItems;
    }

    public long insertCartItem(final CartEntity cartItem, String size) {
        try {
            return executorService.submit(() -> {
                // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
                CartEntity existingItem = cartDao.getCartItemByProductInfo(
                        cartItem.getProductId(),
                        cartItem.getProductVariantId(),
                        size
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
            }).get();

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

    public void addNewOrder(Order order, int updateQuantity, String variantId, DataStateCallback<String> callback) {
        order.setOrderId(UUID.randomUUID().toString());
        order.setCreatedAt(Timestamp.now());
        order.setUpdateAt(Timestamp.now());
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            order.setUserId(user.getUid());
        }

        updateOrder(variantId, updateQuantity, order, callback);
    }

    public void updateOrder(String variantId, int updateQuantity, Order order, DataStateCallback<String> callback) {
        WriteBatch batch = db.batch();
        batch.update(db.collection(Constants.Collection.PRODUCT_VARIANTS).document(variantId), "quantity", updateQuantity);
        batch.set(db.collection(Constants.Collection.ORDERS).document(order.getOrderId()), order);
        batch.commit()
                .addOnSuccessListener(aVoid -> callback.onSuccess(""))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}
