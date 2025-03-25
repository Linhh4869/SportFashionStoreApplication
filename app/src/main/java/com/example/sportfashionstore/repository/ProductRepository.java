package com.example.sportfashionstore.repository;

import com.example.sportfashionstore.model.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProductRepository {
    private final FirebaseFirestore db;
    private DocumentSnapshot lastVisible;
    private final AtomicBoolean hasMoreData = new AtomicBoolean(true);
    private final AtomicBoolean isLoading = new AtomicBoolean(false);

    public ProductRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    // Callback interface để trả về kết quả
    public interface ProductCallback {
        void onSuccess(List<Product> products);
        void onFailure(Exception e);
    }

    public interface ProductDetailCallback {
        void onSuccess(Product product);
        void onFailure(Exception e);
    }

    public void getProducts(ProductCallback callback) {
        if (isLoading.get() || !hasMoreData.get()) {
            return;
        }
        isLoading.set(true);

        Query query = db.collection("products")
                .whereEqualTo("status", "active")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(10);

        if (lastVisible != null) {
            query = query.startAfter(lastVisible);
        }

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Product> products = new ArrayList<>();
            if (!queryDocumentSnapshots.isEmpty()) {
                lastVisible = queryDocumentSnapshots.getDocuments()
                        .get(queryDocumentSnapshots.size() - 1);
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    Product product = document.toObject(Product.class);
                    product.setId(document.getId());
                    products.add(product);
                }
            } else {
                hasMoreData.set(false);
            }

            isLoading.set(false);
            callback.onSuccess(products);
        }).addOnFailureListener(e -> {
            isLoading.set(false);
            callback.onFailure(e);
        });
    }

    // Reset phân trang
    public void resetPagination() {
        lastVisible = null;
        hasMoreData.set(true);
        isLoading.set(false);
    }

    // Truy vấn chi tiết sản phẩm
    public void getProductById(String productId, ProductDetailCallback callback) {
        db.collection("products").document(productId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Product product = documentSnapshot.toObject(Product.class);
                        product.setId(documentSnapshot.getId());
                        callback.onSuccess(product);
                    } else {
                        callback.onFailure(new Exception("Product not found"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getProductVariants(String productId, VariantCallback callback) {
        db.collection("productVariants")
                .whereEqualTo("productId", productId)
                .whereEqualTo("status", "active")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ProductVariant> variants = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        ProductVariant variant = document.toObject(ProductVariant.class);
                        variants.add(variant);
                    }
                    callback.onSuccess(variants);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getProductReviews(String productId, ReviewCallback callback) {
        db.collection("reviews")
                .whereEqualTo("productId", productId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Review> reviews = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Review review = document.toObject(Review.class);
                        reviews.add(review);
                    }
                    callback.onSuccess(reviews);
                })
                .addOnFailureListener(callback::onFailure);
    }
}
