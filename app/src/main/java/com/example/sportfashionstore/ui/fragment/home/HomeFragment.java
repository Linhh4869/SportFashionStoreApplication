package com.example.sportfashionstore.ui.fragment.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.databinding.FragmentHomeBinding;
import com.example.sportfashionstore.ui.adapter.ProductHomeAdapter;
import com.example.sportfashionstore.viewmodel.HomeViewModel;

import java.util.ArrayList;

public class HomeFragment extends BaseFragmentViewModel<FragmentHomeBinding, HomeViewModel> {
    private ProductHomeAdapter productHomeAdapter;

    @Override
    protected FragmentHomeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {
        binding.setViewModel(viewModel);
        if (viewModel.getProductList().getValue() == null) {
            viewModel.getProductListHome();
        }

        productHomeAdapter = new ProductHomeAdapter();
        binding.rcvHomeProduct.setAdapter(productHomeAdapter);

        viewModel.getProductLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                productHomeAdapter.setData(resource.data);
            } else {
                productHomeAdapter.setData(new ArrayList<>());
            }
        });
    }
}
