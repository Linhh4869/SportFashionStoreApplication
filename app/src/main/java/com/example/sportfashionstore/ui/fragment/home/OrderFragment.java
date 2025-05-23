package com.example.sportfashionstore.ui.fragment.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.callback.OnItemClickListener;
import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.databinding.FragmentOrderBinding;
import com.example.sportfashionstore.model.Order;
import com.example.sportfashionstore.ui.adapter.OrderAdapter;
import com.example.sportfashionstore.viewmodel.OrderViewModel;

import java.util.ArrayList;

public class OrderFragment extends BaseFragmentViewModel<FragmentOrderBinding, OrderViewModel> {
    private OrderAdapter orderAdapter;

    @Override
    protected FragmentOrderBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentOrderBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {
        viewModel.getOrderList();
        orderAdapter = new OrderAdapter(new OnItemClickListener<Order>() {
            @Override
            public void onItemClicked(Order item) {
                viewModel.handleButton(item);
            }
        });
        binding.rcvOrder.setAdapter(orderAdapter);
        viewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                viewModel.setUpAction(resource.data);
                orderAdapter.setData(viewModel.setContentStatus(resource.data));
            } else {
                orderAdapter.setData(new ArrayList<>());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.removeListener();
    }
}
