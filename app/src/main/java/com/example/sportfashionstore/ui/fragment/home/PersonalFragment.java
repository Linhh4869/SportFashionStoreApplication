package com.example.sportfashionstore.ui.fragment.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.databinding.FragmentProfileBinding;
import com.example.sportfashionstore.ui.adapter.UtilityAdapter;
import com.example.sportfashionstore.viewmodel.PersonalViewModel;

public class PersonalFragment extends BaseFragmentViewModel<FragmentProfileBinding, PersonalViewModel> {
    private UtilityAdapter accountSettingAdapter;
    private UtilityAdapter commonSettingAdapter;

    @Override
    protected FragmentProfileBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUi() {
        accountSettingAdapter = new UtilityAdapter();
        commonSettingAdapter = new UtilityAdapter();

        binding.setViewModel(viewModel);
        binding.rcvAccountSetting.setAdapter(accountSettingAdapter);
        binding.rcvCommonSetting.setAdapter(commonSettingAdapter);

        viewModel.getAccFeatureList().observe(getViewLifecycleOwner(), list -> {
            accountSettingAdapter.setData(list);
        });

        viewModel.getCommonFeatureList().observe(getViewLifecycleOwner(), list -> {
            commonSettingAdapter.setData(list);
        });
    }
}
