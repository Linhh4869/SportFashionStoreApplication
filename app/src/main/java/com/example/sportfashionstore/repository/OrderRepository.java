package com.example.sportfashionstore.repository;

import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.model.Order;
import com.example.sportfashionstore.model.OrderStatus;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {
    private final FirebaseFirestore db;
    private final FirebaseAuth firebaseAuth;
    private final Map<Integer, OrderStatus> statusMap;

    public OrderRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        statusMap = new HashMap<>();
        statusMap.put(0, new OrderStatus("Đang chờ xử lý", android.R.color.holo_orange_light));
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

        Query query = db.collection(Constants.Collection.ORDERS)
                .whereLessThan("status", 5)
                .whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid())
                .orderBy("createdAt", Query.Direction.DESCENDING);

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Order> orders = new ArrayList<>();
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    Order order = document.toObject(Order.class);
                    if (order != null) {
                        order.setOrderId(document.getId());
                        orders.add(order);
                    }
                }
            }

            callback.onSuccess(orders);
        }).addOnFailureListener(e -> {
            callback.onError(e.getMessage());
        });
    }
}
