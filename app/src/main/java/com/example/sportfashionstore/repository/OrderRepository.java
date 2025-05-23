package com.example.sportfashionstore.repository;

import androidx.annotation.Nullable;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.model.Order;
import com.example.sportfashionstore.model.OrderStatus;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {
    private final FirebaseFirestore db;
    private final FirebaseAuth firebaseAuth;
    private final Map<Integer, OrderStatus> statusMap;
    private ListenerRegistration listenerRegistration;
    String currentUser;

    public OrderRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        statusMap = new HashMap<>();
        currentUser = MyApplication.getSharePrefHelper().getRole();
        statusMap.put(0, new OrderStatus("Đang chờ xử lý", R.color.my_orange));
        statusMap.put(1, new OrderStatus("Đơn hàng đang chờ vận chuyển", android.R.color.holo_blue_light));
        statusMap.put(2, new OrderStatus("Đơn hàng đang được vận chuyển", android.R.color.holo_green_light));
        statusMap.put(3, new OrderStatus("Đã nhận hàng", android.R.color.holo_green_dark));
        statusMap.put(4, new OrderStatus("Đơn hàng đã bị hủy", android.R.color.holo_red_light));
    }

    public OrderStatus getStatusInfo(int code) {
        return statusMap.getOrDefault(code, new OrderStatus("Trạng thái không xác định", android.R.color.black));
    }

    public void getOrderList(DataStateCallback<List<Order>> callback) {
        if (firebaseAuth.getCurrentUser() == null)
            return;

        Query query;
        switch (currentUser) {
            case Constants.Role.OWNER:
                query = db.collection(Constants.Collection.ORDERS);
                break;
            case Constants.Role.SHIPPER:
                query = db.collection(Constants.Collection.ORDERS)
                        .whereGreaterThan("staus", 1)
                        .whereLessThan("status", 4);
                break;
            default:
                query = db.collection(Constants.Collection.ORDERS)
                        .whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid())
                       .whereLessThan("status", 5);
                break;
        }

        listenerRegistration = query.orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        callback.onError(e.getMessage());
                        return;
                    }

                    List<Order> orders = new ArrayList<>();
                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Order order = document.toObject(Order.class);
                            if (order != null) {
                                order.setOrderId(document.getId());
                                orders.add(order);
                            }
                        }
                    }
                    callback.onSuccess(orders);
                });

    }

    public void updateOrder(Order order) {
        int nextStatus;
        switch (currentUser) {
            case Constants.Role.OWNER:
            case Constants.Role.SHIPPER:
                nextStatus = order.getStatus() + 1;
                break;
            default:
                nextStatus = 5;
                break;
        }
        db.collection(Constants.Collection.ORDERS)
                .document(order.getOrderId())
                .update("status", nextStatus) // Cập nhật trường status thành 2 (ví dụ)
                .addOnSuccessListener(aVoid -> {
                    // Cập nhật thành công
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                });

    }

    public void removeListener() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }
}
