package com.example.sportfashionstore.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragment;
import com.example.sportfashionstore.databinding.FragmentAuthBinding;

public class FragmentAuth extends BaseFragment<FragmentAuthBinding> {

    @Override
    protected FragmentAuthBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentAuthBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI() {
    }
}
