package com.example.sportfashionstore.fragment.authentication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.navigation.Navigation;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.commonbase.BaseFragment;
import com.example.sportfashionstore.databinding.FragmentStandbyBinding;

public class FragmentStandby extends BaseFragment<FragmentStandbyBinding> {
    @Override
    protected FragmentStandbyBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentStandbyBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI() {
        binding.btnStart.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.go_to_auth);
        });
    }
}
