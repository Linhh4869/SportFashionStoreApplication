package com.example.sportfashionstore.ui.fragment.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragment;
import com.example.sportfashionstore.databinding.FragmentResultOrderBinding;
import com.example.sportfashionstore.ui.HomeActivity;

public class ResultOrderFragment extends BaseFragment<FragmentResultOrderBinding> {
    @Override
    protected FragmentResultOrderBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentResultOrderBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        binding.btnViewOrder.setOnClickListener(v -> {
            if (getActivity() == null)
                return;

            intent.putExtra(HomeActivity.KEY_SCREEN, "orders");
            getActivity().startActivity(intent);
            getActivity().finish();
        });

        binding.btnBackHome.setOnClickListener(v -> {
            if (getActivity() == null)
                return;

            intent.putExtra(HomeActivity.KEY_SCREEN, "home");
            getActivity().startActivity(intent);
            getActivity().finish();
        });
    }
}
