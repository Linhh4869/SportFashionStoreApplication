package com.example.sportfashionstore.ui.fragment.authentication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragment;
import com.example.sportfashionstore.databinding.FragmentAuthBinding;

public class AuthFragment extends BaseFragment<FragmentAuthBinding> {

    @Override
    protected FragmentAuthBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentAuthBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI() {
    }
}
