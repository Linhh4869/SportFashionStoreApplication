package com.example.sportfashionstore.ui.fragment.shipper;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.ui.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.databinding.FragmentShipperOrderBinding;
import com.example.sportfashionstore.viewmodel.ShipperOrderViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ShipperOrderFragment extends BaseFragmentViewModel<FragmentShipperOrderBinding, ShipperOrderViewModel> {
    @Override
    protected FragmentShipperOrderBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentShipperOrderBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {

    }
}
