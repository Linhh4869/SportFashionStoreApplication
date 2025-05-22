package com.example.sportfashionstore.ui.fragment.owner;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.databinding.FragmentProductManagementBinding;
import com.example.sportfashionstore.viewmodel.ProductManagementViewModel;

public class ProductManagementFragment extends BaseFragmentViewModel<FragmentProductManagementBinding, ProductManagementViewModel> {
    @Override
    protected FragmentProductManagementBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentProductManagementBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {

    }
}
