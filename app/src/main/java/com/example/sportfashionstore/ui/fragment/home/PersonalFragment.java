package com.example.sportfashionstore.ui.fragment.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sportfashionstore.callback.OnItemClickListener;
import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.databinding.FragmentProfileBinding;
import com.example.sportfashionstore.model.FeatureModel;
import com.example.sportfashionstore.ui.HomeActivity;
import com.example.sportfashionstore.ui.MainActivity;
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
        accountSettingAdapter = new UtilityAdapter(item -> {
            viewModel.handleAccountSettingFeature(item);
        });

        commonSettingAdapter = new UtilityAdapter(item -> {
            viewModel.handleAccountSettingFeature(item);
        });

        binding.setViewModel(viewModel);
        binding.rcvAccountSetting.setAdapter(accountSettingAdapter);
        binding.rcvCommonSetting.setAdapter(commonSettingAdapter);

        viewModel.getAccFeatureList().observe(getViewLifecycleOwner(), list -> {
            accountSettingAdapter.setData(list);
        });

        viewModel.getCommonFeatureList().observe(getViewLifecycleOwner(), list -> {
            commonSettingAdapter.setData(list);
        });

        viewModel.getProcessLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.state.equals(Resource.State.SUCCESS)) {
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                intent.putExtra(MainActivity.SHOW_STANDBY, false);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                requireActivity().startActivity(intent);
                requireActivity().finish();
            }
        });
    }
}
