package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.model.Order;
import com.example.sportfashionstore.model.OrderStatus;
import com.example.sportfashionstore.repository.OrderRepository;

import java.util.List;

public class OrderViewModel extends BaseViewModel {
    private final OrderRepository orderRepository;
    private final MutableLiveData<Resource<List<Order>>> ordersLiveData = new MutableLiveData<>();

    public OrderViewModel() {
        orderRepository = new OrderRepository();
    }

    public List<Order> setContentStatus(List<Order> orders) {
        for (Order order : orders) {
            order.setContentStatus(orderRepository.getStatusInfo(order.getStatus()));
        }
        return orders;
    }

    public void getOrderList() {
        setLoadingState(ordersLiveData);
        orderRepository.getOrderList(new DataStateCallback<>() {
            @Override
            public void onSuccess(List<Order> data) {
                if (data != null && !data.isEmpty()) {
                    setUpAction(data);
                    setSuccessState(ordersLiveData, data);
                } else {
                    setErrorState(ordersLiveData, "Chưa có đơn hàng nào");
                }
            }

            @Override
            public void onError(String message) {
                setErrorState(ordersLiveData, message);
            }
        });
    }

    public void handleButton(Order order) {
        orderRepository.updateOrder(order);
    }

    public void setUpAction(List<Order> list) {
        String role = MyApplication.getSharePrefHelper().getRole();
        if (list == null || list.isEmpty()) return;
        for (Order order : list) {
            switch (role) {
                case "BUYER":
                    setForBuyer(order);
                    break;
                case "OWNER":
                    setForOwner(order);
                    break;
                case "SHIPPER":
                    setForShipper(order);
                    break;
            }
        }
    }

    private void setForBuyer(Order order) {
        switch (order.getStatus()) {
            case 0:
                order.setMessage("Hủy đơn hàng");
                order.setAvailable(true);
                break;
            case 1:
                order.setAvailable(false);
                order.setMessage("Đang chờ vận chuyển");
                break;
            case 2:
            case 3:
                order.setAvailable(false);
                order.setMessage("Đang vận chuyển");
                break;
            case 4:
                order.setAvailable(false);
                order.setMessage("Đã giao hàng thành công");
                break;
        }
    }

    private void setForOwner(Order order) {
        switch (order.getStatus()) {
            case 0:
                order.setMessage("Xác nhận đơn");
                order.setAvailable(true);
                break;
            case 1:
                order.setAvailable(true);
                order.setMessage("Chuyển cho shipper");
                break;
            case 2:
                order.setAvailable(false);
                order.setMessage("Đang chờ shipper nhận đơn");
                break;
            case 3:
                order.setAvailable(false);
                order.setMessage("Đang giao hàng");
                break;
            case 4:
                order.setAvailable(false);
                order.setMessage("Đơn hàng giao thành công");
                break;
            case 5:
                order.setAvailable(false);
                order.setMessage("Đơn hàng bị hủy");
                break;
        }
    }

    private void setForShipper(Order order) {
        switch (order.getStatus()) {
            case 2:
                order.setAvailable(true);
                order.setMessage("Nhận đơn hàng");
                break;
            case 3:
                order.setAvailable(true);
                order.setMessage("Xác nhận giao thành công");
                break;
        }
    }

    public MutableLiveData<Resource<List<Order>>> getOrdersLiveData() {
        return ordersLiveData;
    }

    public void removeListener() {
        orderRepository.removeListener();
    }
}
