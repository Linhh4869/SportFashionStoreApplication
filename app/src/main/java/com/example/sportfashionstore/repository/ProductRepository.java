package com.example.sportfashionstore.repository;

import com.example.sportfashionstore.commonbase.DataStateCallback;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.util.Constants;
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

    public void getProducts(DataStateCallback<List<Product>> callback) {
        Query query = db.collection(Constants.Collection.PRODUCTS)
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
                    if (product != null) {
                        product.setId(document.getId());
                        products.add(product);
                    }
                }
            } else {
                hasMoreData.set(false);
            }

            callback.onSuccess(products);
        }).addOnFailureListener(e -> {
            callback.onError(e.getMessage());
        });
    }

    public void resetPagination() {
        lastVisible = null;
        hasMoreData.set(true);
        isLoading.set(false);
    }

    public void getProductById(String productId, DataStateCallback<Product> callback) {
        db.collection(Constants.Collection.PRODUCTS).document(productId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Product product = documentSnapshot.toObject(Product.class);
                        if (product != null) {
                            product.setId(documentSnapshot.getId());
                            callback.onSuccess(product);
                        }
                    } else {
                        callback.onError("Product not found");
                    }
                })
                .addOnFailureListener( e -> {
                    callback.onError(e.getMessage());
                });
    }

//    public void getProductVariants(String productId, VariantCallback callback) {
//        db.collection("productVariants")
//                .whereEqualTo("productId", productId)
//                .whereEqualTo("status", "active")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<ProductVariant> variants = new ArrayList<>();
//                    for (DocumentSnapshot document : queryDocumentSnapshots) {
//                        ProductVariant variant = document.toObject(ProductVariant.class);
//                        variants.add(variant);
//                    }
//                    callback.onSuccess(variants);
//                })
//                .addOnFailureListener(callback::onFailure);
//    }

}
