package com.example.sportfashionstore.fragment.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragment;
import com.example.sportfashionstore.databinding.FragmentProfileBinding;

public class FragmentProfile extends BaseFragment<FragmentProfileBinding> {
    @Override
    protected FragmentProfileBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI() {

    }
}
