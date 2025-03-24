package com.example.sportfashionstore.ui.fragment.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.databinding.FragmentHomeBinding;
import com.example.sportfashionstore.viewmodel.HomeViewModel;

public class FragmentHome extends BaseFragmentViewModel<FragmentHomeBinding, HomeViewModel> {
    @Override
    protected FragmentHomeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {
        binding.setViewModel(viewModel);

    }
}
