package com.example.sportfashionstore.ui.fragment.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragment;
import com.example.sportfashionstore.databinding.FragmentOrderBinding;

public class OrderFragment extends BaseFragment<FragmentOrderBinding> {
    @Override
    protected FragmentOrderBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentOrderBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI() {

    }
}
