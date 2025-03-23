package com.example.sportfashionstore.fragment;

import android.content.Intent;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.activity.ActivityHome;
import com.example.sportfashionstore.commonbase.BaseFragmentViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.databinding.FragmentLoginBinding;
import com.example.sportfashionstore.util.SharePrefHelper;
import com.example.sportfashionstore.viewmodel.AuthViewModel;

public class FragmentLogin extends BaseFragmentViewModel<FragmentLoginBinding, AuthViewModel> {

    private SharePrefHelper sharePrefHelper;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void setupUi() {
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.btnLogin.setOnClickListener(v -> {
            viewModel.onLogin();
        });

        binding.btnChangeToRegister.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.go_to_register);
        });

        viewModel.getHideErrorEmail().observe(getViewLifecycleOwner(), hideError -> {
            binding.tvErrorEmail.setVisibility(hideError ? View.GONE : View.VISIBLE);
        });

        viewModel.getHideErrorPassword().observe(getViewLifecycleOwner(), hideError -> {
            binding.tvErrorPass.setVisibility(hideError ? View.GONE : View.VISIBLE);
        });

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.state.equals(Resource.State.SUCCESS) && resource.data != null) {
                sharePrefHelper = new SharePrefHelper(requireContext());
                sharePrefHelper.setLoggedIn(true);
                Intent intent = new Intent(requireActivity(), ActivityHome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                requireActivity().startActivity(intent);
                requireActivity().finish();
            }
        });
    }
}
