package com.example.sportfashionstore.viewmodel;

import androidx.lifecycle.MutableLiveData;

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

    public MutableLiveData<Resource<List<Order>>> getOrdersLiveData() {
        return ordersLiveData;
    }
}
