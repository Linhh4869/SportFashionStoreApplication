package com.example.sportfashionstore.fragment.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragment;
import com.example.sportfashionstore.databinding.FragmentHomeBinding;

public class FragmentHome extends BaseFragment<FragmentHomeBinding> {
    @Override
    protected FragmentHomeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI() {

    }
}
