package com.example.sportfashionstore.ui.fragment.owner;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.databinding.FragmentSalesRevenueBinding;
import com.example.sportfashionstore.viewmodel.SaleRevenueViewModel;

public class SaleRevenueFragment extends BaseFragmentViewModel<FragmentSalesRevenueBinding, SaleRevenueViewModel> {
    @Override
    protected FragmentSalesRevenueBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSalesRevenueBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {

    }
}
