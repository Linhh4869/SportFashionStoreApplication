package com.example.sportfashionstore.ui.fragment.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragment;
import com.example.sportfashionstore.databinding.FragmentCartBinding;

public class FragmentCart extends BaseFragment<FragmentCartBinding> {
    @Override
    protected FragmentCartBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentCartBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI() {

    }
}
