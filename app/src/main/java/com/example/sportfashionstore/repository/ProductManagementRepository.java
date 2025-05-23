package com.example.sportfashionstore.repository;

import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.model.Category;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.util.Constants;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementRepository {
    private final WriteBatch batch;
    private final CollectionReference productRef;
    private final CollectionReference variantRef;
    private final CollectionReference categoryRef;

    public ProductManagementRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        batch = db.batch();
        productRef = db.collection(Constants.Collection.PRODUCTS);
        variantRef = db.collection(Constants.Collection.PRODUCT_VARIANTS);
        categoryRef = db.collection(Constants.Collection.CATEGORIES);
    }

    public void addProduct(Product product, DataStateCallback<String> callback) {
        DocumentReference productDocRef = productRef.document();
        product.setId(productDocRef.getId());
        batch.set(productDocRef, product);

        for (ProductVariant variant : product.getProductVariants()) {
            DocumentReference variantDocRef = variantRef.document();
            variant.setProductVariantId(variantDocRef.getId());
            variant.setProductId(productDocRef.getId());
            batch.set(variantDocRef, variant);
        }

        batch.commit()
                .addOnSuccessListener(aVoid -> callback.onSuccess("Thanh cong"))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    private void updateProduct(Product product, DataStateCallback<String> callback) {
        DocumentReference productDocRef = productRef.document(product.getId());
        batch.set(productDocRef, product);

        for (ProductVariant variant : product.getProductVariants()) {
            DocumentReference variantDocRef = variantRef.document(variant.getId());
            batch.set(variantDocRef, variant);
        }

        batch.commit()
                .addOnSuccessListener(aVoid -> callback.onSuccess("Thanh cong"))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    private void deleteProduct(Product product, DataStateCallback<String> callback) {
        DocumentReference productDocRef = productRef.document(product.getId());
        batch.delete(productDocRef);

        for (ProductVariant variant : product.getProductVariants()) {
            DocumentReference variantDocRef = variantRef.document(variant.getId());
            batch.delete(variantDocRef);
        }

        batch.commit()
                .addOnSuccessListener(aVoid -> callback.onSuccess("Thanh cong"))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getProductToCURD(String productId, DataStateCallback<Product> callback) {
        Task<DocumentSnapshot> productTask = productRef.document(productId).get();
        Task<QuerySnapshot> variantsTask =
                variantRef.whereEqualTo("productId", productId)
                        .whereEqualTo("status", "active")
                        .orderBy("createdAt", Query.Direction.DESCENDING)
                        .get();

        Task<Void> allTasks = Tasks.whenAll(productTask, variantsTask);
        allTasks.addOnSuccessListener(result -> {
                    DocumentSnapshot productSnapshot = productTask.getResult();
                    if (!productSnapshot.exists()) {
                        callback.onError("Khong co ban ghi");
                        return;
                    }

                    Product product = productSnapshot.toObject(Product.class);

                    if (product == null) {
                        callback.onError("Khong co ban ghi");
                        return;
                    }

                    QuerySnapshot variantsSnapshot = variantsTask.getResult();
                    List<ProductVariant> variantList = new ArrayList<>();
                    try {
                        for (DocumentSnapshot documentSnapshot : variantsSnapshot) {
                            ProductVariant variant = documentSnapshot.toObject(ProductVariant.class);
                            variantList.add(variant);
                        }
                    } catch (Exception e) {
                        callback.onError(e.getMessage());
                    }

                    product.setProductVariants(variantList);
                    callback.onSuccess(product);
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getProductListByCategory(String categoryId, DataStateCallback<List<Product>> callback) {
        Query query = !categoryId.isEmpty()
                ? productRef.whereEqualTo("categoryId", categoryId)
                : productRef;

        query.whereEqualTo("status", "active")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Product> products = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Product product = doc.toObject(Product.class);
                        if (product != null) products.add(product);
                    }

                    if (products.isEmpty()) {
                        callback.onError("Khong co ban ghi");
                    } else {
                        callback.onSuccess(products);
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getCategoryList(DataStateCallback<ArrayList<Category>> callback) {
        categoryRef.orderBy("index", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    ArrayList<Category> categories = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Category category = doc.toObject(Category.class);
                        if (category != null) {
                            category.setId(doc.getId());
                            categories.add(category);
                        }
                    }

                    if (categories.isEmpty()) {
                        callback.onError("Khong co danh muc");
                    } else {
                        callback.onSuccess(categories);
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}
