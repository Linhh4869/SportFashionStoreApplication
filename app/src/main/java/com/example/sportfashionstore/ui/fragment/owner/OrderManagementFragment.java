package com.example.sportfashionstore.ui.fragment.owner;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.databinding.FragmentOrderManagementBinding;
import com.example.sportfashionstore.viewmodel.OrderManagementViewModel;

public class OrderManagementFragment extends BaseFragmentViewModel<FragmentOrderManagementBinding, OrderManagementViewModel> {
    @Override
    protected FragmentOrderManagementBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentOrderManagementBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {

    }
}
