package com.example.sportfashionstore.repository;

import com.example.sportfashionstore.model.OrderStatus;

import java.util.HashMap;
import java.util.Map;

public class OrderStatusRepository {
    private final Map<Integer, OrderStatus> statusMap;

    public OrderStatusRepository() {
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
}
